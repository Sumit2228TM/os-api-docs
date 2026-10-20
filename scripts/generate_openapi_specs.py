#!/usr/bin/env python3
"""
Generate OpenAPI 3.0.3 YAML from docs/api-list.csv by inspecting Java controllers and DTOs.
"""

from __future__ import annotations

import csv
import re
import sys
from dataclasses import dataclass, field
from pathlib import Path
from typing import Any

REPO_ROOT = Path(__file__).resolve().parents[1]
CSV_PATH = REPO_ROOT / "docs" / "api-list.csv"
API_DIR = REPO_ROOT / "docs" / "api"
JAVA_SRC = REPO_ROOT / "WEB-INF" / "src"
CONTROLLER_PACKAGE_DIR = (
    JAVA_SRC / "com" / "krishagni" / "catissueplus" / "rest" / "controller"
)

PRIMITIVE_OPENAPI: dict[str, dict[str, Any]] = {
    "String": {"type": "string"},
    "string": {"type": "string"},
    "boolean": {"type": "boolean"},
    "Boolean": {"type": "boolean"},
    "int": {"type": "integer", "format": "int32"},
    "Integer": {"type": "integer", "format": "int32"},
    "long": {"type": "integer", "format": "int64"},
    "Long": {"type": "integer", "format": "int64"},
    "double": {"type": "number", "format": "double"},
    "Double": {"type": "number", "format": "double"},
    "float": {"type": "number", "format": "float"},
    "Float": {"type": "number", "format": "float"},
    "BigDecimal": {"type": "number"},
    "Date": {"type": "string", "format": "date-time"},
    "Object": {},
}


@dataclass
class JavaField:
    name: str
    java_type: str
    json_ignore: bool = False


@dataclass
class JavaClassModel:
    simple_name: str
    extends: str | None
    fields: list[JavaField] = field(default_factory=list)
    source_path: Path | None = None


@dataclass
class JavaParameter:
    name: str
    java_type: str
    location: str  # path | query | body | header
    required: bool = True
    default: str | None = None
    description: str | None = None


@dataclass
class ControllerOperation:
    operation_id: str
    summary: str
    description: str
    http_method: str
    java_method_name: str
    return_type: str
    parameters: list[JavaParameter]
    controller_class: str
    controller_path: Path


def kebab_case(api_name: str) -> str:
    return re.sub(r"[^a-z0-9]+", "-", api_name.strip().lower()).strip("-")


def openapi_path(endpoint_uri: str) -> str:
    uri = endpoint_uri.strip()
    for prefix in ("/rest/ng", "/rest"):
        if uri.startswith(prefix):
            uri = uri[len(prefix) :] or "/"
            break
    if not uri.startswith("/"):
        uri = "/" + uri
    return uri


def find_java_sources(simple_name: str) -> list[Path]:
    if not simple_name.endswith(".java"):
        simple_name = f"{simple_name}.java"
    return sorted(JAVA_SRC.rglob(simple_name))


def resolve_java_file(
    class_file: str, *, prefer_controller: bool = False, prefer_events: bool = False
) -> Path | None:
    stem = class_file.replace(".java", "")
    matches = find_java_sources(f"{stem}.java")
    if not matches:
        # Generic plural/singular controller filename tolerance
        if stem.endswith("Controller"):
            alt = stem[:-10] + "sController"
            matches = find_java_sources(f"{alt}.java")
        elif stem.endswith("sController"):
            alt = stem[:-11] + "Controller"
            matches = find_java_sources(f"{alt}.java")
    if not matches:
        return None
    if len(matches) == 1:
        return matches[0]

    def score(path: Path) -> tuple[int, str]:
        s = str(path)
        rank = 0
        if prefer_controller and "/rest/controller/" in s:
            rank += 10
        if prefer_events and "/events/" in s:
            rank += 10
        if "/rest/controller/" in s:
            rank += 2
        if "/events/" in s:
            rank += 1
        return (-rank, s)

    return sorted(matches, key=score)[0]


def strip_generics(java_type: str) -> str:
    java_type = re.sub(r"\s+", " ", java_type.strip())
    return re.sub(r"<.*?>", "", java_type).strip()


def generic_arg(java_type: str) -> str | None:
    m = re.search(r"<([^<>]+)>", java_type)
    return m.group(1).strip() if m else None


def tag_from_controller_class(class_name: str) -> str:
    stem = class_name.replace(".java", "")
    if stem.endswith("Controller"):
        stem = stem[: -len("Controller")]
    return stem or "API"


def path_template(path: str) -> str:
    parts = [p for p in path.split("/") if p]
    normalized = []
    for part in parts:
        if part.startswith("{") and part.endswith("}"):
            normalized.append("{}")
        else:
            normalized.append(part.lower())
    return "/" + "/".join(normalized)


def join_paths(base: str, sub: str) -> str:
    base = base.strip()
    sub = sub.strip()
    if not base:
        combined = sub
    elif not sub:
        combined = base
    else:
        combined = f"{base.rstrip('/')}/{sub.lstrip('/')}"
    if not combined.startswith("/"):
        combined = "/" + combined
    return re.sub(r"/{2,}", "/", combined)


def parse_request_mapping_block(block: str) -> dict[str, Any]:
    result: dict[str, Any] = {"methods": [], "value": ""}
    methods = re.findall(r"RequestMethod\.(\w+)", block)
    result["methods"] = [m.upper() for m in methods]
    value_match = re.search(r'value\s*=\s*"([^"]*)"', block)
    if value_match:
        result["value"] = value_match.group(1)
    else:
        shorthand = re.search(r'@RequestMapping\s*\(\s*"([^"]*)"', block)
        if shorthand:
            result["value"] = shorthand.group(1)
    return result


def parse_operation_annotation(block: str) -> dict[str, str]:
    op: dict[str, str] = {}
    for key in ("operationId", "summary", "description"):
        m = re.search(rf'{key}\s*=\s*"([^"]*)"', block)
        if m:
            op[key] = m.group(1)
    return op


def parse_method_parameters(params_src: str) -> list[JavaParameter]:
    params: list[JavaParameter] = []
    if not params_src.strip():
        return params

    chunks = split_parameters(params_src)
    for chunk in chunks:
        chunk = chunk.strip()
        if not chunk:
            continue

        loc: str | None = None
        required = True
        default = None
        param_name = None

        if "@PathVariable" in chunk:
            loc = "path"
            m = re.search(r'@PathVariable\s*\(\s*(?:value\s*=\s*)?"([^"]+)"', chunk)
            if m:
                param_name = m.group(1)
        elif "@RequestParam" in chunk:
            loc = "query"
            m = re.search(r'@RequestParam\s*\([^)]*value\s*=\s*"([^"]+)"', chunk)
            if m:
                param_name = m.group(1)
            req_m = re.search(r"required\s*=\s*(false|true)", chunk)
            if req_m:
                required = req_m.group(1) == "true"
            def_m = re.search(r'defaultValue\s*=\s*"([^"]*)"', chunk)
            if def_m:
                default = def_m.group(1)
                required = False
        elif "@RequestBody" in chunk:
            loc = "body"
        else:
            continue

        chunk_stripped = re.sub(r"@\w+(?:\([^)]*\))?\s*", "", chunk, flags=re.DOTALL).strip()
        sig = re.match(r"([\w<>,\?\s]+)\s+(\w+)\s*$", chunk_stripped.replace("\n", " "))
        if not sig:
            continue
        type_name = sig.group(1).strip()
        name = sig.group(2).strip()
        if param_name is None:
            param_name = name

        params.append(
            JavaParameter(
                name=param_name,
                java_type=type_name,
                location=loc,
                required=required,
                default=default,
            )
        )
    return params


def split_parameters(params_src: str) -> list[str]:
    parts: list[str] = []
    depth = 0
    in_string = False
    string_char = ""
    current: list[str] = []
    for char in params_src:
        if in_string:
            current.append(char)
            if char == string_char:
                in_string = False
            continue
        if char in ("\"", "'"):
            in_string = True
            string_char = char
            current.append(char)
            continue
        if char == "(":
            depth += 1
        elif char == ")":
            depth = max(0, depth - 1)
        if char == "," and depth == 0:
            parts.append("".join(current))
            current = []
            continue
        current.append(char)
    if current:
        parts.append("".join(current))
    return parts


def extract_class_level_path(controller_text: str) -> str:
    class_idx = controller_text.find("public class")
    if class_idx == -1:
        return ""
    header = controller_text[:class_idx]
    mappings = re.findall(
        r"@RequestMapping\s*\(\s*(?:value\s*=\s*)?\"([^\"]*)\"\s*\)",
        header,
    )
    if mappings:
        return mappings[-1]
    shorthand = re.findall(r"@RequestMapping\s*\(\s*\"([^\"]+)\"\s*\)", header)
    return shorthand[-1] if shorthand else ""


def extract_balanced_parens(text: str, open_index: int) -> str | None:
    if open_index >= len(text) or text[open_index] != "(":
        return None
    depth = 0
    for i in range(open_index, len(text)):
        ch = text[i]
        if ch == "(":
            depth += 1
        elif ch == ")":
            depth -= 1
            if depth == 0:
                return text[open_index + 1 : i]
    return None


def find_controller_operation(
    controller_path: Path,
    http_method: str,
    target_path: str,
) -> ControllerOperation | None:
    text = controller_path.read_text(encoding="utf-8", errors="replace")
    class_base = extract_class_level_path(text)
    target_norm = path_template(target_path)
    http_method = http_method.upper()

    method_re = re.compile(r"public\s+([\w<>,\?\s]+)\s+(\w+)\s*\(")
    best: ControllerOperation | None = None

    for match in method_re.finditer(text):
        return_type = match.group(1).strip()
        method_name = match.group(2)
        params_src = extract_balanced_parens(text, match.end() - 1)
        if params_src is None:
            continue

        window_start = max(0, match.start() - 1200)
        window = text[window_start : match.start()]

        mapping_blocks = re.findall(r"@RequestMapping\s*\(([^)]*)\)", window)
        if not mapping_blocks:
            continue
        mapping = parse_request_mapping_block(mapping_blocks[-1])
        if mapping["methods"] and http_method not in mapping["methods"]:
            continue

        full_path = join_paths(class_base, mapping["value"])
        if path_template(full_path) != target_norm:
            continue

        op_blocks = re.findall(r"@Operation\s*\((.*?)\)\s*(?=@[A-Z]|\s*public\s)", window, re.DOTALL)
        op_meta = parse_operation_annotation(op_blocks[-1] if op_blocks else "")
        parameters = parse_method_parameters(params_src)
        operation_id = op_meta.get("operationId") or method_name
        summary = op_meta.get("summary") or method_name
        description = op_meta.get(
            "description",
            f"{http_method} {full_path} — `{controller_path.name}`.`{method_name}`",
        )

        best = ControllerOperation(
            operation_id=operation_id,
            summary=summary,
            description=description,
            http_method=http_method,
            java_method_name=method_name,
            return_type=return_type,
            parameters=parameters,
            controller_class=controller_path.name,
            controller_path=controller_path,
        )
        break

    return best


def parse_json_ignore_fields(java_text: str) -> set[str]:
    ignored: set[str] = set()
    for m in re.finditer(
        r"@JsonIgnore\s*\n\s*public\s+\S+\s+get(\w+)\s*\(",
        java_text,
    ):
        prop = m.group(1)
        if prop.startswith("is") and len(prop) > 2:
            ignored.add(prop[2].lower() + prop[3:])
        else:
            ignored.add(prop[0].lower() + prop[1:])
    return ignored


def parse_java_class(path: Path) -> JavaClassModel:
    text = path.read_text(encoding="utf-8", errors="replace")
    class_m = re.search(r"public\s+class\s+(\w+)(?:\s+extends\s+(\w+))?", text)
    if not class_m:
        raise ValueError(f"Could not parse class declaration in {path}")
    simple_name = class_m.group(1)
    extends = class_m.group(2)
    json_ignore = parse_json_ignore_fields(text)

    fields: list[JavaField] = []
    for block, java_type, fname in re.findall(
        r"((?:@\w+(?:\([^)]*\))?\s*)*)private\s+([\w<>,\s]+)\s+(\w+)\s*;",
        text,
    ):
        if "@JsonIgnore" in block:
            continue
        if fname in json_ignore:
            continue
        fields.append(
            JavaField(
                name=fname,
                java_type=re.sub(r"\s+", " ", java_type.strip()),
                json_ignore=False,
            )
        )

    return JavaClassModel(
        simple_name=simple_name,
        extends=extends,
        fields=fields,
        source_path=path,
    )


SKIP_EXTENDS = frozenset(
    {
        "AttributeModifiedSupport",
        "Serializable",
        "Comparable",
        "Object",
    }
)


class SchemaRegistry:
    def __init__(self) -> None:
        self.schemas: dict[str, dict[str, Any]] = {}

    def ensure_standard_errors(self) -> None:
        self.schemas.setdefault(
            "ErrorMessage",
            {
                "type": "object",
                "description": "REST error entry (`ErrorMessage`)",
                "required": ["code", "message"],
                "properties": {
                    "code": {"type": "string"},
                    "message": {"type": "string"},
                },
            },
        )
        self.schemas.setdefault(
            "ErrorMessageList",
            {
                "type": "array",
                "items": {"$ref": "#/components/schemas/ErrorMessage"},
            },
        )

    def java_type_to_schema(self, java_type: str) -> dict[str, Any]:
        raw = java_type.strip()
        if not raw:
            return {"type": "string"}
        if raw.startswith("List<") or raw.startswith("Collection<") or raw.startswith("Set<"):
            inner = generic_arg(raw) or "Object"
            return {
                "type": "array",
                "items": self.java_type_to_schema(inner),
            }
        base = strip_generics(raw)
        if not base:
            return {"type": "string"}
        if base in PRIMITIVE_OPENAPI:
            schema = dict(PRIMITIVE_OPENAPI[base])
            return schema
        if base.endswith("[]"):
            inner = base[:-2]
            return {"type": "array", "items": self.java_type_to_schema(inner)}
        self.ensure_class_schema(base)
        return {"$ref": f"#/components/schemas/{base}"}

    def _collect_fields(self, model: JavaClassModel, visited: set[str]) -> list[JavaField]:
        if model.simple_name in visited:
            return []
        visited.add(model.simple_name)
        fields: list[JavaField] = []
        if model.extends and model.extends not in SKIP_EXTENDS:
            parent_path = resolve_java_file(f"{model.extends}.java")
            if parent_path:
                parent_model = parse_java_class(parent_path)
                fields.extend(self._collect_fields(parent_model, visited))
        fields.extend(model.fields)
        return fields

    def ensure_class_schema(self, simple_name: str) -> None:
        if simple_name in self.schemas or simple_name in PRIMITIVE_OPENAPI:
            return
        if simple_name in SKIP_EXTENDS:
            self.schemas[simple_name] = {"type": "object"}
            return

        path = resolve_java_file(f"{simple_name}.java")
        if not path:
            self.schemas[simple_name] = {
                "type": "object",
                "description": f"Type `{simple_name}` (Java source not found in repository)",
            }
            return

        # Placeholder breaks recursive DTO references (e.g. nested children lists).
        self.schemas[simple_name] = {"type": "object", "properties": {}}

        model = parse_java_class(path)
        properties: dict[str, Any] = {}
        for fld in self._collect_fields(model, set()):
            properties[fld.name] = self.java_type_to_schema(fld.java_type)

        rel = model.source_path.relative_to(REPO_ROOT) if model.source_path else simple_name
        self.schemas[model.simple_name] = {
            "type": "object",
            "description": f"From `{rel}`",
            "properties": properties,
        }


def return_type_to_response_schema(registry: SchemaRegistry, return_type: str) -> dict[str, Any]:
    rt = return_type.strip()
    if rt.startswith("List<") or rt.startswith("Collection<"):
        inner = generic_arg(rt) or "Object"
        return {
            "type": "array",
            "items": registry.java_type_to_schema(inner),
        }
    return registry.java_type_to_schema(rt)


def build_openapi_document(
    row: dict[str, str],
    operation: ControllerOperation,
    registry: SchemaRegistry,
    dto_class: str,
) -> dict[str, Any]:
    api_name = row["API_Name"].strip()
    path = openapi_path(row["Endpoint_URI"])
    tag = tag_from_controller_class(operation.controller_class)

    dto_name = dto_class.replace(".java", "") if dto_class else ""
    if dto_name and dto_name.upper() != "N/A":
        registry.ensure_class_schema(dto_name)

    parameters: list[dict[str, Any]] = []
    request_body: dict[str, Any] | None = None

    for param in operation.parameters:
        if param.location == "body":
            schema = registry.java_type_to_schema(param.java_type)
            request_body = {
                "required": True,
                "content": {"application/json": {"schema": schema}},
            }
            continue
        if param.location == "path":
            schema = registry.java_type_to_schema(param.java_type)
            entry: dict[str, Any] = {
                "name": param.name,
                "in": "path",
                "required": True,
                "schema": schema,
            }
            parameters.append(entry)
        elif param.location == "query":
            schema = registry.java_type_to_schema(param.java_type)
            entry = {
                "name": param.name,
                "in": "query",
                "required": param.required,
                "schema": schema,
            }
            if param.default is not None:
                if schema.get("type") == "boolean":
                    entry["schema"]["default"] = param.default.lower() == "true"
                elif schema.get("type") == "integer":
                    try:
                        entry["schema"]["default"] = int(param.default)
                    except ValueError:
                        entry["schema"]["default"] = param.default
                else:
                    entry["schema"]["default"] = param.default
            parameters.append(entry)

    if request_body is None and dto_name and dto_name.upper() != "N/A":
        if row["HTTP_Method"].upper() in ("POST", "PUT", "PATCH"):
            request_body = {
                "required": True,
                "content": {
                    "application/json": {
                        "schema": {"$ref": f"#/components/schemas/{dto_name}"},
                    }
                },
            }

    success_schema = return_type_to_response_schema(registry, operation.return_type)
    if dto_name and dto_name.upper() != "N/A" and success_schema.get("$ref", "").endswith("/Object"):
        success_schema = {"$ref": f"#/components/schemas/{dto_name}"}

    error_ref = {"$ref": "#/components/schemas/ErrorMessageList"}
    responses: dict[str, Any] = {
        "200": {
            "description": f"Successful response for {api_name}",
            "content": {"application/json": {"schema": success_schema}},
        },
        "400": {
            "description": "Invalid request or business rule violation (`ErrorType.USER_ERROR`)",
            "content": {"application/json": {"schema": error_ref}},
        },
        "401": {
            "description": "Missing or invalid API token (`ErrorType.UNAUTHORIZED`)",
            "content": {"application/json": {"schema": error_ref}},
        },
        "403": {
            "description": "Authenticated but not authorized for this operation",
            "content": {"application/json": {"schema": error_ref}},
        },
        "500": {
            "description": "Unexpected server error (`internal_error`)",
            "content": {"application/json": {"schema": error_ref}},
        },
    }

    op_obj: dict[str, Any] = {
        "operationId": operation.operation_id,
        "summary": operation.summary,
        "description": operation.description,
        "tags": [tag],
        "security": [{"OsApiToken": []}],
        "responses": responses,
    }
    if parameters:
        op_obj["parameters"] = parameters
    if request_body:
        op_obj["requestBody"] = request_body

    registry.ensure_standard_errors()

    controller_rel = operation.controller_path.relative_to(REPO_ROOT)
    dto_path = resolve_java_file(dto_class) if dto_name and dto_name.upper() != "N/A" else None
    desc_lines = [
        f"{api_name} — generated from repository inspection.",
        f"Controller: `{controller_rel}` (`{operation.java_method_name}`).",
    ]
    if dto_path:
        desc_lines.append(f"DTO: `{dto_path.relative_to(REPO_ROOT)}`.")

    return {
        "openapi": "3.0.3",
        "info": {
            "title": f"OpenSpecimen API — {api_name}",
            "version": "1.0.0",
            "description": "\n".join(desc_lines),
        },
        "servers": [
            {"url": "/rest/ng", "description": "OpenSpecimen REST servlet context"},
        ],
        "tags": [{"name": tag, "description": f"Operations from `{operation.controller_class}`"}],
        "paths": {path: {operation.http_method.lower(): op_obj}},
        "components": {
            "securitySchemes": {
                "OsApiToken": {
                    "type": "apiKey",
                    "in": "header",
                    "name": "X-OS-API-TOKEN",
                    "description": "Token from `POST /sessions`",
                }
            },
            "schemas": registry.schemas,
        },
    }


def json_escape(s: str) -> str:
    return '"' + s.replace("\\", "\\\\").replace('"', '\\"') + '"'


def _format_scalar(val: Any) -> str:
    if isinstance(val, bool):
        return "true" if val else "false"
    if val is None:
        return "null"
    if isinstance(val, (int, float)):
        return str(val)
    if isinstance(val, str):
        if "\n" in val:
            return None
        if re.match(r"^[\w./{}$-]+$", val):
            return val
        return json_escape(val)
    return str(val)


def dump_yaml(obj: Any, indent: int = 0) -> str:
    sp = "  " * indent
    if isinstance(obj, dict):
        lines: list[str] = []
        for key, val in obj.items():
            key_str = str(key)
            if key_str.isdigit():
                key_str = f"'{key_str}'"
            if isinstance(val, dict):
                lines.append(f"{sp}{key_str}:")
                lines.append(dump_yaml(val, indent + 1))
            elif isinstance(val, list):
                lines.append(f"{sp}{key_str}:")
                lines.append(dump_yaml(val, indent + 1))
            elif isinstance(val, str) and "\n" in val:
                lines.append(f"{sp}{key_str}: |")
                for line in val.splitlines():
                    lines.append(f"{sp}  {line}")
            else:
                formatted = _format_scalar(val)
                lines.append(f"{sp}{key_str}: {formatted}")
        return "\n".join(lines)
    if isinstance(obj, list):
        if not obj:
            return f"{sp}[]"
        lines: list[str] = []
        for item in obj:
            if isinstance(item, dict):
                keys = list(item.keys())
                for idx, key in enumerate(keys):
                    val = item[key]
                    bullet = f"{sp}- " if idx == 0 else f"{sp}  "
                    if isinstance(val, list) and len(val) == 0:
                        lines.append(f"{bullet}{key}: []")
                    elif isinstance(val, dict):
                        lines.append(f"{bullet}{key}:")
                        lines.append(dump_yaml(val, indent + 2))
                    elif isinstance(val, list):
                        lines.append(f"{bullet}{key}:")
                        lines.append(dump_yaml(val, indent + 2))
                    elif isinstance(val, str) and "\n" in val:
                        lines.append(f"{bullet}{key}: |")
                        for line in val.splitlines():
                            lines.append(f"{sp}    {line}")
                    else:
                        lines.append(f"{bullet}{key}: {_format_scalar(val)}")
            else:
                lines.append(f"{sp}- {_format_scalar(item)}")
        return "\n".join(lines)
    formatted = _format_scalar(obj)
    return f"{sp}{formatted}"


def output_file_for(api_name: str) -> Path:
    return API_DIR / f"{kebab_case(api_name)}.yaml"


def main() -> int:
    if not CSV_PATH.is_file():
        print(f"ERROR: CSV not found: {CSV_PATH}", file=sys.stderr)
        return 1

    API_DIR.mkdir(parents=True, exist_ok=True)
    written: list[str] = []
    skipped: list[str] = []
    failed: list[str] = []

    with CSV_PATH.open(newline="", encoding="utf-8") as fh:
        reader = csv.DictReader(fh)
        required = {"API_Name", "HTTP_Method", "Endpoint_URI", "Controller_Class", "DTO_Class"}
        if not required.issubset(set(reader.fieldnames or [])):
            print(f"ERROR: CSV must contain columns: {sorted(required)}", file=sys.stderr)
            return 1

        for row in reader:
            api_name = row["API_Name"].strip()
            out_path = output_file_for(api_name)

            if out_path.is_file():
                msg = "file already exists"
                skipped.append(f"{out_path.name} — {msg}")
                print(f"SKIP: {out_path.relative_to(REPO_ROOT)} ({msg})")
                continue

            controller_path = resolve_java_file(
                row["Controller_Class"].strip(), prefer_controller=True
            )
            if not controller_path:
                failed.append(f"{api_name}: controller not found ({row['Controller_Class']})")
                print(f"FAIL: {api_name} — controller not found: {row['Controller_Class']}", file=sys.stderr)
                continue

            target_path = openapi_path(row["Endpoint_URI"])
            operation = find_controller_operation(
                controller_path,
                row["HTTP_Method"].strip(),
                target_path,
            )
            if not operation:
                failed.append(
                    f"{api_name}: no matching @RequestMapping on {controller_path.name} "
                    f"for {row['HTTP_Method']} {target_path}"
                )
                print(
                    f"FAIL: {api_name} — no matching mapping for "
                    f"{row['HTTP_Method']} {target_path} in {controller_path.name}",
                    file=sys.stderr,
                )
                continue

            registry = SchemaRegistry()
            doc = build_openapi_document(
                row,
                operation,
                registry,
                row.get("DTO_Class", "N/A").strip(),
            )
            yaml_text = dump_yaml(doc) + "\n"
            out_path.write_text(yaml_text, encoding="utf-8")
            written.append(str(out_path.relative_to(REPO_ROOT)))
            print(f"WRITE: {out_path.relative_to(REPO_ROOT)}")

    print("\n--- Summary ---")
    print(f"Written: {len(written)}")
    for path in written:
        print(f"  + {path}")
    print(f"Skipped: {len(skipped)}")
    for item in skipped:
        print(f"  - {item}")
    if failed:
        print(f"Failed: {len(failed)}")
        for item in failed:
            print(f"  ! {item}")
        return 1
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
