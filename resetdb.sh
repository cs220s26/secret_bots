#!/usr/bin/env bash
if ! redis-cli ping >/dev/null 2>&1; then
  echo "Error: Redis is not running or not reachable on port 6379." >&2
  exit 1
fi

redis-cli FLUSHALL
