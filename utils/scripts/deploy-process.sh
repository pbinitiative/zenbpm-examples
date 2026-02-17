#!/usr/bin/env bash
# Deploy a BPMN file to ZenBPM via the REST API.
#
# Usage:
#   ./deploy-process.sh <bpmn-file> [server-url]
#
# Examples:
#   ./deploy-process.sh ./process/hello-world.bpmn
#   ./deploy-process.sh ./process/hello-world.bpmn http://localhost:8080

set -euo pipefail

BPMN_FILE="${1:?Usage: deploy-process.sh <bpmn-file> [server-url]}"
SERVER_URL="${2:-http://localhost:8080}"

if [ ! -f "$BPMN_FILE" ]; then
  echo "Error: file not found: $BPMN_FILE"
  exit 1
fi

echo "Deploying $BPMN_FILE to $SERVER_URL ..."

curl -s -X POST "${SERVER_URL}/v1/process-definitions" \
  -F "resource=@${BPMN_FILE}" | tee /dev/stderr | python3 -m json.tool 2>/dev/null || true

echo ""
echo "Done."
