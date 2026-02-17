package openai

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"log"
	"text/template"

	goopenai "github.com/sashabaranov/go-openai"
)

// Service handles OpenAI API interactions.
type Service struct {
	client *goopenai.Client
}

// Result represents the expected JSON response from OpenAI.
type Result struct {
	Decision string  `json:"decision"`
	Reason   *string `json:"reason"`
}

// NewService creates a new OpenAI service with the given API key.
func NewService(apiKey string) *Service {
	return &Service{client: goopenai.NewClient(apiKey)}
}

// ProcessWithTemplate applies variables to a prompt template and sends it to OpenAI.
func (s *Service) ProcessWithTemplate(ctx context.Context, promptTemplate string, variables map[string]any) (*Result, error) {
	tmpl, err := template.New("prompt").Parse(promptTemplate)
	if err != nil {
		return nil, fmt.Errorf("failed to parse template: %w", err)
	}

	var buf bytes.Buffer
	if err := tmpl.Execute(&buf, variables); err != nil {
		return nil, fmt.Errorf("failed to execute template: %w", err)
	}

	prompt := buf.String()
	log.Printf("[openai-service] Generated prompt: %s", prompt)

	resp, err := s.client.CreateChatCompletion(ctx, goopenai.ChatCompletionRequest{
		Model: "gpt-4o-mini",
		Messages: []goopenai.ChatCompletionMessage{
			{
				Role:    goopenai.ChatMessageRoleSystem,
				Content: "You are a business process analyst. Always respond with valid JSON in the specified format only.",
			},
			{
				Role:    goopenai.ChatMessageRoleUser,
				Content: prompt,
			},
		},
		MaxTokens:   500,
		Temperature: 0.7,
	})
	if err != nil {
		return nil, fmt.Errorf("OpenAI API call failed: %w", err)
	}

	if len(resp.Choices) == 0 {
		return nil, fmt.Errorf("no response from OpenAI API")
	}

	var result Result
	if err := json.Unmarshal([]byte(resp.Choices[0].Message.Content), &result); err != nil {
		return nil, fmt.Errorf("failed to parse OpenAI response as JSON: %w", err)
	}

	if result.Decision == "" {
		return nil, fmt.Errorf("OpenAI response missing 'decision' field")
	}

	return &result, nil
}
