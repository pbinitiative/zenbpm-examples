package openai

import (
	"context"
	"encoding/json"
	"fmt"
	"log"

	"github.com/pbinitiative/zenbpm/pkg/zenclient"
	"github.com/pbinitiative/zenbpm/pkg/zenclient/proto"
)

// Worker handles openai-connector jobs.
type Worker struct {
	service *Service
}

// NewWorker creates a new OpenAI worker with the given API key.
func NewWorker(apiKey string) *Worker {
	return &Worker{service: NewService(apiKey)}
}

// Handler processes a job by validating data via OpenAI.
func (w *Worker) Handler(ctx context.Context, job *proto.WaitingJob) (map[string]any, *zenclient.WorkerError) {
	jobKey := job.GetKey()
	log.Printf("[openai-connector] Received job %d", jobKey)

	variables := map[string]any{}
	if err := json.Unmarshal(job.Variables, &variables); err != nil {
		return nil, &zenclient.WorkerError{
			Err:       fmt.Errorf("failed to unmarshal variables: %w", err),
			ErrorCode: "PARSE_ERROR",
		}
	}

	okConditionsVar, exists := variables["okConditions"]
	if !exists {
		return nil, &zenclient.WorkerError{
			Err:       fmt.Errorf("no 'okConditions' variable found"),
			ErrorCode: "MISSING_VARIABLE",
		}
	}

	okConditions, ok := okConditionsVar.(string)
	if !ok || okConditions == "" {
		return nil, &zenclient.WorkerError{
			Err:       fmt.Errorf("'okConditions' must be a non-empty string"),
			ErrorCode: "INVALID_VARIABLE",
		}
	}

	prompt := "Podmínky pro ok:\n" + okConditions +
		". Zkontroluj vše co zadal uživatel a pokud není jakákoliv z podmínek splněna pak NOT_OK." +
		" Vždy vracej pouze validní JSON v následujícím formátu:" +
		" {\"decision\": \"<OK nebo NOT_OK>\", \"reason\": \"<Vysvětlit proč OK nebo NOT_OK>\"}\n"

	result, err := w.service.ProcessWithTemplate(ctx, prompt, variables)
	if err != nil {
		return nil, &zenclient.WorkerError{
			Err:       fmt.Errorf("OpenAI call failed: %w", err),
			ErrorCode: "AI_ERROR",
		}
	}

	aiResult := map[string]any{
		"decision": result.Decision,
		"reason":   result.Reason,
	}

	log.Printf("[openai-connector] Job %d completed — decision: %s", jobKey, result.Decision)
	return map[string]any{"aiResult": aiResult}, nil
}
