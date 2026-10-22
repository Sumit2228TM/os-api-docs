#!/usr/bin/env python3
"""
Populate docs/api-list.csv from docs/core-api-endpoints.json by scanning REST controllers.
"""

from __future__ import annotations

import csv
import json
import sys
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parents[1]
MANIFEST = REPO_ROOT / "docs" / "core-api-endpoints.json"
CSV_PATH = REPO_ROOT / "docs" / "api-list.csv"

sys.path.insert(0, str(REPO_ROOT / "scripts"))
import generate_openapi_specs as gen  # noqa: E402
from route_aliases import resolve_route  # noqa: E402

# Fallback controller/DTO when no @RequestMapping match (catalog still documents the operation).
CATALOG_HINTS: dict[str, tuple[str, str]] = {
    "Get All Biohazards": ("PermissibleValueController.java", "PermissibleValueDetails.java"),
    "Get Biohazard": ("PermissibleValueController.java", "PermissibleValueDetails.java"),
    "Create Biohazard": ("PermissibleValueController.java", "PermissibleValueDetails.java"),
    "Update Biohazard": ("PermissibleValueController.java", "PermissibleValueDetails.java"),
    "Patch Biohazard": ("PermissibleValueController.java", "PermissibleValueDetails.java"),
    "Delete Biohazard": ("PermissibleValueController.java", "N/A"),
    "Create Department": ("InstitutesController.java", "InstituteDetail.java"),
    "Get Department": ("InstitutesController.java", "InstituteDetail.java"),
    "Get All Departments": ("InstitutesController.java", "InstituteDetail.java"),
    "Update Department": ("InstitutesController.java", "InstituteDetail.java"),
    "Reparent Specimen Requirement": ("SpecimenRequirementsController.java", "SpecimenRequirementDetail.java"),
    "Create Participant": ("ParticipantController.java", "ParticipantDetail.java"),
    "Update Participant": ("ParticipantController.java", "ParticipantDetail.java"),
    "Delete Participant": ("ParticipantController.java", "N/A"),
    "Create Specimen Aliquots": ("SpecimensController.java", "SpecimenDetail.java"),
    "Create Specimen Derivatives": ("SpecimensController.java", "SpecimenDetail.java"),
    "Update Specimen Labels": ("SpecimensController.java", "SpecimenDetail.java"),
    "Create Specimen Events Bulk": ("SpecimensController.java", "SpecimenDetail.java"),
    "Create Specimen Event": ("SpecimensController.java", "SpecimenDetail.java"),
    "Check In Out Specimens": ("SpecimensController.java", "SpecimenDetail.java"),
    "Unreserve Specimens For DP": ("DistributionProtocolController.java", "N/A"),
    "Update Distribution Order Status": ("DistributionOrderController.java", "DistributionOrderDetail.java"),
    "Update Storage Container Extension": ("StorageContainersController.java", "StorageContainerDetail.java"),
    "Validate Password": ("UserController.java", "PasswordDetails.java"),
    "Get Export Job Status": ("ExportJobsController.java", "ExportJobDetail.java"),
    "Get Query AQL": ("QueryController.java", "SavedQueryDetail.java"),
    "Query Audit Query Times": ("QueryAuditLogsController.java", "QueryAuditLogDetail.java"),
    "Merge Participants": ("ParticipantController.java", "ParticipantDetail.java"),
    "Get CPR Consent Responses": ("CollectionProtocolRegistrationsController.java", "N/A"),
    "Save CPR Consents": ("CollectionProtocolRegistrationsController.java", "ConsentDetail.java"),
}


def infer_dto(operation: gen.ControllerOperation, http_method: str) -> str:
    for param in operation.parameters:
        if param.location == "body":
            return gen.strip_generics(param.java_type).split(".")[-1] + ".java"
    rt = gen.strip_generics(operation.return_type)
    simple = rt.split(".")[-1]
    if http_method.upper() in ("POST", "PUT", "PATCH") and simple not in ("void", "Boolean", "boolean", "Map", "Long"):
        if simple.endswith("Detail") or simple.endswith("Op") or simple.endswith("Criteria"):
            return f"{simple}.java"
    if http_method.upper() == "GET" and simple.endswith("Detail"):
        return f"{simple}.java"
    if simple in ("void", "Void"):
        return "N/A"
    return "N/A"


def find_operation(method: str, path: str) -> tuple[Path, gen.ControllerOperation] | None:
    resolved = resolve_route(method, path)
    if resolved is None:
        return None
    r_method, r_path = resolved
    target_norm = gen.path_template(r_path)

    for controller_path in sorted(gen.CONTROLLER_PACKAGE_DIR.glob("*Controller.java")):
        text = controller_path.read_text(encoding="utf-8", errors="replace")
        class_base = gen.extract_class_level_path(text)
        method_re = __import__("re").compile(r"public\s+([\w<>,\?\s]+)\s+(\w+)\s*\(")

        for match in method_re.finditer(text):
            params_src = gen.extract_balanced_parens(text, match.end() - 1)
            if params_src is None:
                continue
            window_start = max(0, match.start() - 1200)
            window = text[window_start : match.start()]
            mapping_blocks = __import__("re").findall(r"@RequestMapping\s*\(([^)]*)\)", window)
            if not mapping_blocks:
                continue
            mapping = gen.parse_request_mapping_block(mapping_blocks[-1])
            if mapping["methods"] and r_method not in mapping["methods"]:
                continue
            full_path = gen.join_paths(class_base, mapping["value"])
            if gen.path_template(full_path) != target_norm:
                continue

            op_blocks = __import__("re").findall(
                r"@Operation\s*\((.*?)\)\s*(?=@[A-Z]|\s*public\s)", window, __import__("re").DOTALL
            )
            op_meta = gen.parse_operation_annotation(op_blocks[-1] if op_blocks else "")
            parameters = gen.parse_method_parameters(params_src)
            operation = gen.ControllerOperation(
                operation_id=op_meta.get("operationId") or match.group(2),
                summary=op_meta.get("summary") or match.group(2),
                description=op_meta.get(
                    "description",
                    f"{r_method} {full_path} — `{controller_path.name}`.`{match.group(2)}`",
                ),
                http_method=r_method,
                java_method_name=match.group(2),
                return_type=match.group(1).strip(),
                parameters=parameters,
                controller_class=controller_path.name,
                controller_path=controller_path,
            )
            return controller_path, operation
    return None


def main() -> int:
    if not MANIFEST.is_file():
        print(f"ERROR: manifest not found: {MANIFEST}", file=sys.stderr)
        return 1

    endpoints = json.loads(MANIFEST.read_text(encoding="utf-8"))
    if len(endpoints) != 132:
        print(f"ERROR: manifest must contain 132 entries, found {len(endpoints)}", file=sys.stderr)
        return 1

    rows: list[dict[str, str]] = []
    missing: list[str] = []

    for ep in endpoints:
        name = ep["name"]
        method = ep["method"].upper()
        path = ep["path"]

        resolved = resolve_route(method, path)
        if resolved is None:
            missing.append(f"{name}: {method} {path}")
            ctrl, dto = CATALOG_HINTS.get(name, ("SupportController.java", "N/A"))
            rows.append(
                {
                    "API_Name": name,
                    "HTTP_Method": method,
                    "Endpoint_URI": f"/rest/ng{path}",
                    "Controller_Class": ctrl,
                    "DTO_Class": dto,
                }
            )
            continue
        r_method, _r_path = resolved

        found = find_operation(method, path)
        if not found:
            missing.append(f"{name}: {method} {path} (hint row)")
            ctrl, dto = CATALOG_HINTS.get(name, ("SupportController.java", "N/A"))
            rows.append(
                {
                    "API_Name": name,
                    "HTTP_Method": method,
                    "Endpoint_URI": f"/rest/ng{path}",
                    "Controller_Class": ctrl,
                    "DTO_Class": dto,
                }
            )
            continue

        _ctrl_path, operation = found
        dto = infer_dto(operation, r_method)
        rows.append(
            {
                "API_Name": name,
                "HTTP_Method": method,
                "Endpoint_URI": f"/rest/ng{path}",
                "Controller_Class": operation.controller_class,
                "DTO_Class": dto,
            }
        )

    CSV_PATH.parent.mkdir(parents=True, exist_ok=True)
    with CSV_PATH.open("w", newline="", encoding="utf-8") as fh:
        writer = csv.DictWriter(
            fh,
            fieldnames=["API_Name", "HTTP_Method", "Endpoint_URI", "Controller_Class", "DTO_Class"],
        )
        writer.writeheader()
        writer.writerows(rows)

    if len(rows) != 132:
        print(f"ERROR: expected 132 CSV rows, wrote {len(rows)}", file=sys.stderr)
        return 1

    print(f"Wrote {len(rows)} rows to {CSV_PATH.relative_to(REPO_ROOT)}")
    if missing:
        print(f"Catalog rows using hints / unresolved Java mapping ({len(missing)}):", file=sys.stderr)
        for item in missing:
            print(f"  ! {item}", file=sys.stderr)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
