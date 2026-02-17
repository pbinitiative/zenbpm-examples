package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"os/signal"
	"syscall"

	"github.com/pbinitiative/zenbpm/pkg/zenclient"
	"github.com/pbinitiative/zenbpm/pkg/zenclient/proto"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

func main() {
	addr := os.Getenv("ZENBPM_GRPC_ADDR")
	if addr == "" {
		addr = "localhost:9090"
	}

	apiKey := os.Getenv("OPENAI_API_KEY")
	if apiKey == "" {
		log.Fatal("OPENAI_API_KEY environment variable is required")
	}

	log.Printf("Connecting to ZenBPM at %s ...", addr)

	conn, err := grpc.NewClient(addr, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		log.Fatalf("Failed to connect: %v", err)
	}
	defer conn.Close()

	client := zenclient.NewGrpc(conn)
	openaiService := NewOpenAIService(apiKey)

	_, err = client.RegisterWorker(context.Background(), "openai-connector-1", makeHandler(openaiService), "openai-connector")
	if err != nil {
		log.Fatalf("Failed to register worker: %v", err)
	}

	log.Println("openai-connector is running. Press Ctrl+C to stop.")

	sigCh := make(chan os.Signal, 1)
	signal.Notify(sigCh, syscall.SIGINT, syscall.SIGTERM)
	<-sigCh

	log.Println("Shutting down.")
}

func makeHandler(svc *OpenAIService) func(context.Context, *proto.WaitingJob) (map[string]any, *zenclient.WorkerError) {
	return func(ctx context.Context, job *proto.WaitingJob) (map[string]any, *zenclient.WorkerError) {
		log.Printf("[openai-connector] Received job %d", job.GetKey())

		var vars map[string]any
		if err := json.Unmarshal(job.GetVariables(), &vars); err != nil {
			return nil, &zenclient.WorkerError{
				Err:       fmt.Errorf("failed to parse variables: %w", err),
				ErrorCode: "PARSE_ERROR",
			}
		}

		okConditionsVar, exists := vars["okConditions"]
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

		result, err := svc.ProcessWithTemplate(ctx, prompt, vars)
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

		log.Printf("[openai-connector] Job %d completed - decision: %s", job.GetKey(), result.Decision)
		return map[string]any{"aiResult": aiResult}, nil
	}
}
