package log

import (
	"context"
	"encoding/json"
	"fmt"
	"log"

	"github.com/pbinitiative/zenbpm/pkg/zenclient"
	"github.com/pbinitiative/zenbpm/pkg/zenclient/proto"
)

// Handler reads the "log" variable from job variables and prints it.
func Handler(ctx context.Context, job *proto.WaitingJob) (map[string]interface{}, error) {
	jobKey := job.GetKey()

	variables := map[string]interface{}{}
	if err := json.Unmarshal(job.Variables, &variables); err != nil {
		return nil, &zenclient.WorkerError{
			Err:       fmt.Errorf("failed to unmarshal variables: %w", err),
			ErrorCode: "PARSE_ERROR",
		}
	}

	logContent, exists := variables["log"]
	if !exists {
		log.Printf("[log-worker] No 'log' variable found in job %d", jobKey)
		return nil, &zenclient.WorkerError{
			Err:       fmt.Errorf("no 'log' variable found in job %d", jobKey),
			ErrorCode: "MISSING_VARIABLE",
		}
	}

	log.Printf("[log-worker] %v", logContent)
	return nil, nil
}
