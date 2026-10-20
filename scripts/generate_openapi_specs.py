#!/usr/bin/env python3
"""
Batch-generate OpenAPI 3.0.3 YAML files from docs/api-list.csv.
Reuses components/schemas structure from docs/api/specimen-create.yaml when present.
"""

from __future__ import annotations

import csv
import re
import sys
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parents[1]
CSV_PATH = REPO_ROOT / "docs" / "api-list.csv"
API_DIR = REPO_ROOT / "docs" / "api"
TEMPLATE_SPEC = API_DIR / "specimen-create.yaml"
CONTROLLER_DIR = (
    REPO_ROOT
    / "WEB-INF/src/com/krishagni/catissueplus/rest/controller"
)
EVENTS_DIR = (
    REPO_ROOT
    / "WEB-INF/src/com/krishagni/catissueplus/core/biospecimen/events"
)

# If kebab output name differs from an existing spec file, skip generation.
OUTPUT_ALIASES: dict[str, list[str]] = {
    "create-specimen": ["specimen-create.yaml"],
}

ERROR_RESPONSES = """        '400':
          description: |
            Invalid request or business rule violation (`ErrorType.USER_ERROR`).
            Body is a JSON array of `ErrorMessage` objects.
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorMessageList'
        '401':
          description: Missing or invalid API token (`ErrorType.UNAUTHORIZED`)
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorMessageList'
        '403':
          description: |
            Authenticated but not authorized (some sites return equivalent errors as HTTP 400).
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorMessageList'
        '500':
          description: Unexpected server error (`ErrorType.SYSTEM_ERROR`, code `internal_error`)
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorMessageList'
"""


def kebab_case(api_name: str) -> str:
    return re.sub(r"[^a-z0-9]+", "-", api_name.strip().lower()).strip("-")


def openapi_path(endpoint_uri: str) -> str:
    uri = endpoint_uri.strip()
    prefix = "/rest/ng"
    if uri.startswith(prefix):
        uri = uri[len(prefix) :] or "/"
    if not uri.startswith("/"):
        uri = "/" + uri
    return uri


def resolve_controller_file(controller_class: str) -> Path | None:
    base = controller_class.replace(".java", "")
    candidates = [
        f"{base}.java",
        f"{base}s.java",
        base.replace("Specimen", "Specimens") + ".java",
        "SpecimensController.java",
    ]
    seen: set[str] = set()
    for name in candidates:
        if name in seen:
            continue
        seen.add(name)
        path = CONTROLLER_DIR / name
        if path.is_file():
            return path
    return None


def resolve_dto_file(dto_class: str) -> Path | None:
    if not dto_class or dto_class.strip().upper() == "N/A":
        return None
    name = dto_class.replace(".java", "") + ".java"
    path = EVENTS_DIR / name
    return path if path.is_file() else None


def inspect_controller(
    controller_path: Path, http_method: str, path_pattern: str
) -> dict[str, str]:
    """Extract @Operation metadata near the matching @RequestMapping."""
    text = controller_path.read_text(encoding="utf-8", errors="replace")
    method_enum = f"RequestMethod.{http_method.upper()}"

    path_frag = ""
    if "{" in path_pattern:
        path_frag = re.escape(path_pattern.split("{")[0].rstrip("/"))
    else:
        path_frag = re.escape(path_pattern.rstrip("/") or "/specimens")

    mapping_re = re.compile(
        rf"@Operation\([^)]*operationId\s*=\s*\"([^\"]+)\"[^)]*summary\s*=\s*\"([^\"]+)\""
        rf"[\s\S]*?@RequestMapping\([^)]*method\s*=\s*{method_enum}[^)]*\)"
        rf"[\s\S]*?public\s+\S+\s+(\w+)\s*\(",
        re.MULTILINE,
    )

    for op_id, summary, java_method in mapping_re.findall(text):
        chunk_start = text.find(f'operationId = "{op_id}"')
        chunk = text[chunk_start : chunk_start + 800]
        if path_frag and path_frag not in chunk:
            continue
        if http_method.upper() == "POST" and "/{id}" in path_pattern:
            continue
        return {
            "operationId": op_id,
            "summary": summary,
            "javaMethod": java_method,
            "controllerFile": controller_path.name,
        }

    # Fallback heuristics per HTTP verb
    fallbacks = {
        "POST": ("createSpecimen", "Create a new specimen", "createSpecimen"),
        "GET": ("getSpecimenById", "Get specimen details by ID", "getSpecimen"),
        "PUT": ("updateSpecimenById", "Update specimen by ID", "updateSpecimen"),
        "DELETE": ("deleteSpecimenById", "Delete single specimen by ID", "deleteSpecimen"),
    }
    op_id, summary, jm = fallbacks.get(
        http_method.upper(), ("operation", "API operation", "handler")
    )
    return {
        "operationId": op_id,
        "summary": summary,
        "javaMethod": jm,
        "controllerFile": controller_path.name,
    }


def load_components_block() -> str:
    if not TEMPLATE_SPEC.is_file():
        return _minimal_components()
    text = TEMPLATE_SPEC.read_text(encoding="utf-8")
    marker = "components:"
    idx = text.find(marker)
    if idx == -1:
        return _minimal_components()
    return text[idx:]


def _minimal_components() -> str:
    return """components:
  securitySchemes:
    OsApiToken:
      type: apiKey
      in: header
      name: X-OS-API-TOKEN
  schemas:
    ErrorMessage:
      type: object
      required: [code, message]
      properties:
        code:
          type: string
        message:
          type: string
    ErrorMessageList:
      type: array
      items:
        $ref: '#/components/schemas/ErrorMessage'
"""


def build_spec(row: dict[str, str], meta: dict[str, str]) -> str:
    api_name = row["API_Name"]
    method = row["HTTP_Method"].lower()
    path = openapi_path(row["Endpoint_URI"])
    dto = row.get("DTO_Class", "N/A")
    controller = meta.get("controllerFile", row["Controller_Class"])

    title = f"OpenSpecimen API — {api_name}"
    description_lines = [
        f"{api_name} endpoint derived from `{controller}`",
        f"Java method `{meta.get('javaMethod', 'n/a')}` (`{row['HTTP_Method']} {path}`).",
    ]
    if dto and dto.upper() != "N/A":
        description_lines.append(f"DTO: `{dto.replace('.java', '')}`.")
    else:
        description_lines.append("No request DTO; response uses `SpecimenInfo` summary shape.")

    dto_note = resolve_dto_file(dto)
    if dto_note:
        description_lines.append(f"DTO source: `{dto_note.relative_to(REPO_ROOT)}`.")

    description = "\n".join(f"    {line}" for line in description_lines)

    security_block = """      security:
        - OsApiToken: []"""

    request_body = ""
    parameters = ""
    success_schema = "#/components/schemas/SpecimenDetail"
    success_desc = "Operation completed successfully"

    if method == "post":
        request_body = """
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/SpecimenCreateRequest'"""
        success_desc = "Specimen created and saved"
    elif method == "put":
        parameters = """
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: integer
            format: int64
          description: Specimen identifier"""
        request_body = """
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/SpecimenCreateRequest'"""
        success_desc = "Specimen updated and saved"
    elif method == "get":
        parameters = """
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: integer
            format: int64
          description: Specimen identifier"""
        success_desc = "Specimen details returned"
    elif method == "delete":
        parameters = """
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: integer
            format: int64
          description: Specimen identifier
        - name: forceDelete
          in: query
          required: false
          schema:
            type: boolean
            default: false
          description: Force delete when dependent entities exist
        - name: reason
          in: query
          required: false
          schema:
            type: string
            default: ""
          description: Delete reason"""
        success_schema = "#/components/schemas/SpecimenInfoSummary"
        success_desc = "Specimen deleted (soft delete); returns deleted specimen summary"

    body = f"""openapi: 3.0.3
info:
  title: {title}
  version: 1.0.0
  description: |
{description}

servers:
  - url: /rest/ng
    description: OpenSpecimen REST servlet context

tags:
  - name: Specimens
    description: Biospecimen create/read/update/delete operations

paths:
  {path}:
    {method}:
      operationId: {meta.get("operationId", "operation")}
      summary: {meta.get("summary", api_name)}
      description: |
        Authenticate with header `X-OS-API-TOKEN` (from `POST /sessions`).
      tags:
        - Specimens
{security_block}
{parameters}{request_body}
      responses:
        '200':
          description: {success_desc}
          content:
            application/json:
              schema:
                $ref: '{success_schema}'
{ERROR_RESPONSES}

"""
    return body + load_components_block()


def output_file_for(api_name: str) -> Path:
    return API_DIR / f"{kebab_case(api_name)}.yaml"


def should_skip(out_path: Path) -> tuple[bool, str]:
    if out_path.is_file():
        return True, f"file already exists: {out_path.relative_to(REPO_ROOT)}"
    stem = out_path.stem
    for alias in OUTPUT_ALIASES.get(stem, []):
        alias_path = API_DIR / alias
        if alias_path.is_file():
            return True, f"alias exists: {alias_path.relative_to(REPO_ROOT)} (skip {out_path.name})"
    return False, ""


def main() -> int:
    if not CSV_PATH.is_file():
        print(f"ERROR: CSV not found: {CSV_PATH}", file=sys.stderr)
        return 1

    API_DIR.mkdir(parents=True, exist_ok=True)
    created: list[str] = []
    skipped: list[str] = []

    with CSV_PATH.open(newline="", encoding="utf-8") as fh:
        reader = csv.DictReader(fh)
        required = {"API_Name", "HTTP_Method", "Endpoint_URI", "Controller_Class", "DTO_Class"}
        if not required.issubset(set(reader.fieldnames or [])):
            print(f"ERROR: CSV must contain columns: {sorted(required)}", file=sys.stderr)
            return 1

        for row in reader:
            api_name = row["API_Name"].strip()
            out_path = output_file_for(api_name)
            skip, reason = should_skip(out_path)
            if skip:
                skipped.append(f"{out_path.name} — {reason}")
                print(f"SKIP: {out_path.relative_to(REPO_ROOT)} ({reason})")
                continue

            controller_path = resolve_controller_file(row["Controller_Class"].strip())
            if not controller_path:
                print(
                    f"WARN: Controller not found for {row['Controller_Class']}; using defaults",
                    file=sys.stderr,
                )
                meta = {
                    "operationId": kebab_case(api_name).replace("-", "_"),
                    "summary": api_name,
                    "javaMethod": "unknown",
                    "controllerFile": row["Controller_Class"],
                }
            else:
                meta = inspect_controller(
                    controller_path,
                    row["HTTP_Method"].strip(),
                    openapi_path(row["Endpoint_URI"]),
                )

            yaml_text = build_spec(row, meta)
            out_path.write_text(yaml_text, encoding="utf-8")
            created.append(str(out_path.relative_to(REPO_ROOT)))
            print(f"CREATE: {out_path.relative_to(REPO_ROOT)}")

    print("\n--- Summary ---")
    print(f"Created: {len(created)}")
    for path in created:
        print(f"  + {path}")
    print(f"Skipped: {len(skipped)}")
    for item in skipped:
        print(f"  - {item}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
