package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"syscall"

	logworker "zenbpm-workers/workers/log"
	openaiworker "zenbpm-workers/workers/openai"

	"github.com/pbinitiative/zenbpm/pkg/zenclient"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

func main() {
	grpcAddr := os.Getenv("ZENBPM_GRPC_ADDR")
	if grpcAddr == "" {
		grpcAddr = "localhost:9090"
	}

	openaiKey := os.Getenv("OPENAI_API_KEY")

	log.Printf("Connecting to ZenBPM at %s ...", grpcAddr)

	conn, err := grpc.NewClient(grpcAddr, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		log.Fatalf("Failed to connect: %v", err)
	}
	defer conn.Close()

	client := zenclient.NewGrpc(conn)

	// log-worker (used by: 01-hello-world, 02-commission-payout)
	_, err = client.RegisterWorker(context.Background(), "log-worker-1", logworker.Handler, "log-worker")
	if err != nil {
		log.Fatalf("Failed to register log-worker: %v", err)
	}
	log.Println("Registered log-worker")

	// openai-connector (used by: 02-commission-payout)
	if openaiKey != "" {
		worker := openaiworker.NewWorker(openaiKey)
		_, err = client.RegisterWorker(context.Background(), "openai-connector-1", worker.Handler, "openai-connector")
		if err != nil {
			log.Fatalf("Failed to register openai-connector: %v", err)
		}
		log.Println("Registered openai-connector")
	} else {
		log.Println("OPENAI_API_KEY not set, openai-connector disabled")
	}

	log.Println("All workers running. Press Ctrl+C to stop.")

	sigCh := make(chan os.Signal, 1)
	signal.Notify(sigCh, syscall.SIGINT, syscall.SIGTERM)
	<-sigCh

	log.Println("Shutting down.")
}
