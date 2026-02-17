#!/bin/sh
# Start every worker binary found in ./workers/.
# If a worker exits (e.g. missing env var), the rest keep running.

for bin in ./workers/*; do
  [ -x "$bin" ] || continue
  echo "Starting $(basename "$bin") ..."
  "$bin" &
done

# Wait for all remaining background processes.
wait
