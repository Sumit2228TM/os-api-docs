#!/usr/bin/env bash
# Populate api-list.csv and generate OpenAPI YAML for a given git ref into docs/<output_name>/.
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$REPO_ROOT"

REF="${1:?Usage: run_openapi_pipeline.sh <git-ref> <output-dir-name>}"
OUT_NAME="${2:?Usage: run_openapi_pipeline.sh <git-ref> <output-dir-name>}"
OUTPUT_DIR="$REPO_ROOT/docs/$OUT_NAME"
JAVA_SRC="$REPO_ROOT/WEB-INF/src"

echo "== OpenAPI pipeline: ref=$REF output=$OUTPUT_DIR =="

CURRENT_REF="$(git rev-parse HEAD)"
RESTORE=0
if [[ "$(git rev-parse "$REF")" != "$CURRENT_REF" ]]; then
  RESTORE=1
  echo "Checking out Java sources from $REF ..."
  git checkout "$REF" -- WEB-INF/src
fi

cleanup() {
  if [[ "$RESTORE" -eq 1 ]]; then
    echo "Restoring Java sources to $CURRENT_REF ..."
    git checkout "$CURRENT_REF" -- WEB-INF/src
  fi
}
trap cleanup EXIT

python3 "$REPO_ROOT/scripts/sync_core_catalog.py"
python3 "$REPO_ROOT/scripts/populate_api_list.py"
find "$OUTPUT_DIR" -maxdepth 1 -name '*.yaml' -delete 2>/dev/null || true
python3 "$REPO_ROOT/scripts/generate_openapi_specs.py" --output-dir "$OUTPUT_DIR" --force

COUNT="$(find "$OUTPUT_DIR" -maxdepth 1 -name '*.yaml' | wc -l | tr -d ' ')"
echo "Generated $COUNT OpenAPI files in docs/$OUT_NAME/"
