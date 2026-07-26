package main

import (
	"context"
	"fmt"

	"github.com/pbinitiative/zenbpm/pkg/proto"
	"github.com/pbinitiative/zenbpm/pkg/zenclient"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

// Handles the "log-worker" service task of the hello-world process: reads the
// "log" variable, prints it, and completes the job so the instance can finish.
//
// verify: proto import path against the zenclient release.
func main() {
	conn, err := grpc.NewClient(
		"127.0.0.1:9090",
		grpc.WithTransportCredentials(insecure.NewCredentials()),
	)
	if err != nil {
		panic(err)
	}
	defer conn.Close()

	zen := zenclient.NewGrpc(conn)

	// Subscribe to "log-worker" jobs.
	zen.RegisterWorker(context.Background(), "hello-world-worker",
		func(ctx context.Context, job *proto.WaitingJob) (map[string]any, *zenclient.WorkerError) {
			fmt.Printf("[log-worker] %v\n", job.GetVariables()["log"])
			return map[string]any{}, nil // no output variables; job complete
		},
		"log-worker",
	)

	select {} // keep the worker running
}
