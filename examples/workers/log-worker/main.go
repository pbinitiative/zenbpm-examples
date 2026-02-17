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

	log.Printf("Connecting to ZenBPM at %s ...", addr)

	conn, err := grpc.NewClient(addr, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		log.Fatalf("Failed to connect: %v", err)
	}
	defer conn.Close()

	client := zenclient.NewGrpc(conn)

	_, err = client.RegisterWorker(context.Background(), "log-worker-1", logHandler, "log-worker")
	if err != nil {
		log.Fatalf("Failed to register worker: %v", err)
	}

	log.Println("log-worker is running. Press Ctrl+C to stop.")

	sigCh := make(chan os.Signal, 1)
	signal.Notify(sigCh, syscall.SIGINT, syscall.SIGTERM)
	<-sigCh

	log.Println("Shutting down.")
}

// logHandler reads the "log" variable and prints its content.
func logHandler(ctx context.Context, job *proto.WaitingJob) (map[string]any, *zenclient.WorkerError) {
	log.Printf("[log-worker] Received job %d", job.GetKey())

	var vars map[string]any
	if err := json.Unmarshal(job.GetVariables(), &vars); err != nil {
		return nil, &zenclient.WorkerError{
			Err:       fmt.Errorf("failed to parse variables: %w", err),
			ErrorCode: "PARSE_ERROR",
		}
	}

	logContent, exists := vars["log"]
	if !exists {
		return nil, &zenclient.WorkerError{
			Err:       fmt.Errorf("no 'log' variable found"),
			ErrorCode: "MISSING_VARIABLE",
		}
	}

	log.Printf("[log-worker] %v", logContent)
	return nil, nil
}
