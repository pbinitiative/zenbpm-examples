package main

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"log"
	"text/template"

	"github.com/sashabaranov/go-openai"
)

// AIResult represents the structured JSON response from OpenAI.
type AIResult struct {
	Decision string  `json:"decision"`
	Reason   *string `json:"reason"`
}

// OpenAIService wraps the OpenAI client for business-process validation.
type OpenAIService struct {
	client *openai.Client
}

func NewOpenAIService(apiKey string) *OpenAIService {
	return &OpenAIService{client: openai.NewClient(apiKey)}
}

// ProcessWithTemplate renders the prompt template with process variables,
// sends it to GPT-4o-mini, and parses the JSON response.
func (s *OpenAIService) ProcessWithTemplate(ctx context.Context, promptTemplate string, variables map[string]any) (*AIResult, error) {
	tmpl, err := template.New("prompt").Parse(promptTemplate)
	if err != nil {
		return nil, fmt.Errorf("failed to parse template: %w", err)
	}

	var buf bytes.Buffer
	if err := tmpl.Execute(&buf, variables); err != nil {
		return nil, fmt.Errorf("failed to execute template: %w", err)
	}

	prompt := buf.String()
	log.Printf("[openai] Prompt: %s", prompt)

	resp, err := s.client.CreateChatCompletion(ctx, openai.ChatCompletionRequest{
		Model: "gpt-4o-mini",
		Messages: []openai.ChatCompletionMessage{
			{
				Role:    openai.ChatMessageRoleSystem,
				Content: "You are a business process analyst. Always respond with valid JSON in the specified format only.",
			},
			{
				Role:    openai.ChatMessageRoleUser,
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

	var result AIResult
	if err := json.Unmarshal([]byte(resp.Choices[0].Message.Content), &result); err != nil {
		return nil, fmt.Errorf("failed to parse OpenAI response as JSON: %w", err)
	}

	if result.Decision == "" {
		return nil, fmt.Errorf("OpenAI response missing 'decision' field")
	}

	return &result, nil
}
