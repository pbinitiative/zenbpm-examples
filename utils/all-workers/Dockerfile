# Builds all workers from examples/workers/ into a single container.
# Build context: repository root (.)
#
# Any directory under examples/workers/ with a go.mod is built automatically.

FROM golang:1.25-alpine AS builder

RUN apk add --no-cache git ca-certificates

COPY examples/workers/ /src/

RUN for dir in /src/*/; do \
      [ -f "$dir/go.mod" ] || continue; \
      name=$(basename "$dir"); \
      echo "Building $name ..."; \
      cd "$dir" && go mod download && \
      CGO_ENABLED=0 GOOS=linux go build -o /out/"$name" . && \
      cd /; \
    done

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /app
COPY --from=builder /out/ ./workers/
COPY utils/all-workers/start.sh .
RUN chmod +x start.sh
USER 1001
CMD ["./start.sh"]
