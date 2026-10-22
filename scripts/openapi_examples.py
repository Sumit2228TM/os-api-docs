"""Biobanking-oriented OpenAPI example values derived from DTO property names."""

from __future__ import annotations

import re
from typing import Any

# Shared IDs for cohesive examples across related endpoints
LINK = {
    "cpId": 101,
    "cprId": 5001,
    "visitId": 1001,
    "specimenId": 9001,
    "participantId": 3001,
    "userId": 42,
    "siteId": 7,
    "containerId": 2001,
    "orderId": 6001,
    "dpId": 801,
    "eventId": 1101,
    "requirementId": 1201,
    "formId": 50,
    "recordId": 90001,
    "jobId": 7001,
    "runId": 70001,
    "tierId": 15,
    "roleId": 3,
}

PV_ENUMS: dict[str, list[str]] = {
    "specimenClass": ["Tissue", "Fluid", "Cell", "Molecular"],
    "type": ["Frozen Tissue", "Serum", "Plasma", "PBMC", "DNA"],
    "collectionStatus": ["Collected", "Pending", "Not Collected"],
    "activityStatus": ["Active", "Disabled"],
    "status": ["PENDING", "COMPLETE", "ACTIVE", "EXECUTION"],
    "gender": ["Male", "Female", "Unknown"],
    "vitalStatus": ["Alive", "Dead", "Unknown"],
    "race": ["White", "Black or African American", "Asian", "Unknown"],
    "ethnicity": ["Not Hispanic or Latino", "Hispanic or Latino", "Unknown"],
    "biohazard": ["None", "Level 2", "Level 3"],
    "pathologicalStatus": ["Malignant", "Benign", "Unknown"],
    "clinicalStatus": ["Normal", "Abnormal", "Unknown"],
    "receivedQuality": ["Acceptable", "Unacceptable"],
    "collectionProcedure": ["Surgical Resection", "Core Needle Biopsy", "Venipuncture"],
    "anatomicSite": ["Breast", "Lung", "Liver", "Colon"],
    "laterality": ["Left", "Right", "Not Applicable"],
    "orderStatus": ["PENDING", "EXECUTION", "COMPLETED", "REJECTED"],
    "importType": ["CREATE", "UPDATE"],
}

SAMPLE_STRINGS: dict[str, str] = {
    "label": "BBCP-2024-001-S1",
    "barcode": "BBCP2024001S1",
    "loginName": "biobank.tech",
    "email": "biobank.tech@example.org",
    "firstName": "Alex",
    "lastName": "Rivera",
    "name": "Baseline Visit 1",
    "shortTitle": "BBCP",
    "title": "Breast Cancer Biobank Protocol",
    "ppid": "BBCP-001",
    "sprNumber": "SPR-24-001234",
    "module": "biospecimen",
    "property": "specimen_label_format",
    "objectType": "specimen",
    "entityType": "SpecimenExtension",
    "token": "a1b2c3d4e5f6789012345678901234ab",
    "message": "Operation completed successfully.",
    "code": "USER_ERROR",
    "objectName": "com.krishagni.catissueplus.core.biospecimen.domain.Specimen",
    "aql": "select s.id, s.label from Specimen s where s.id = 9001",
    "query": "specimens in freezers",
    "filename": "import-template.csv",
    "fileId": "f8e7d6c5-b4a3-2109-8765-4321fedcba09",
    "transactionId": "hl7-tx-20241022-001",
    "deviceDetails": "Chrome 129 / macOS",
    "ipAddress": "192.168.10.45",
}


def _id_for_field(name: str) -> int | None:
    lower = name.lower()
    for key, val in LINK.items():
        if key.lower() == lower or key.lower().replace("id", "") in lower and lower.endswith("id"):
            return val
    if lower.endswith("id"):
        return 1001
    return None


def example_for_property(name: str, schema: dict[str, Any], depth: int = 0) -> Any:
    if depth > 6:
        return None

    if "$ref" in schema:
        return {"id": _id_for_field(name) or 1001}

    if "enum" in schema and schema["enum"]:
        return schema["enum"][0]

    if name in PV_ENUMS:
        return PV_ENUMS[name][0]

    if name in SAMPLE_STRINGS:
        return SAMPLE_STRINGS[name]

    st = schema.get("type")
    fmt = schema.get("format")

    if st == "boolean":
        return name.lower().startswith("include") or name.lower() in ("admin", "exactMatch", "active")

    if st == "integer":
        return _id_for_field(name) or (5 if "qty" in name.lower() else 1)

    if st == "number":
        return 2.5 if "qty" in name.lower() else 1.0

    if st == "string":
        if fmt == "date-time":
            return "2024-10-22T14:30:00Z"
        if fmt == "date":
            return "2024-10-22"
        lid = _id_for_field(name)
        if lid is not None:
            return lid
        if "email" in name.lower():
            return SAMPLE_STRINGS["email"]
        if "label" in name.lower():
            return SAMPLE_STRINGS["label"]
        if "password" in name.lower():
            return "ChangeMe!2024"
        if "login" in name.lower():
            return SAMPLE_STRINGS["loginName"]
        return f"sample-{re.sub(r'[^a-zA-Z0-9]', '-', name).lower()}"

    if st == "array":
        items = schema.get("items") or {}
        child = example_for_property(name + "Item", items, depth + 1)
        return [child] if child is not None else []

    if st == "object" or "properties" in schema:
        props = schema.get("properties") or {}
        if not props and schema.get("additionalProperties"):
            return {"customField": "Biobank cohort A"}
        out: dict[str, Any] = {}
        for prop_name, prop_schema in list(props.items())[:25]:
            val = example_for_property(prop_name, prop_schema, depth + 1)
            if val is not None:
                out[prop_name] = val
        return out

    return None


def build_examples(schema: dict[str, Any], *, label: str = "biobankExample") -> dict[str, Any]:
    value = example_for_property("root", schema)
    if value is None:
        return {}
    return {label: {"summary": "Biobanking workflow example", "value": value}}


ERROR_EXAMPLES: dict[str, dict[str, Any]] = {
    "400": {
        "validationError": {
            "summary": "Business rule violation",
            "value": [{"code": "SPECIMEN_LABEL_REQUIRED", "message": "Specimen label is required."}],
        }
    },
    "401": {
        "unauthorized": {
            "summary": "Missing API token",
            "value": [{"code": "UNAUTHORIZED", "message": "Invalid or missing X-OS-API-TOKEN header."}],
        }
    },
    "403": {
        "forbidden": {
            "summary": "Insufficient privileges",
            "value": [{"code": "RBAC_ACCESS_DENIED", "message": "You are not authorized to perform this operation."}],
        }
    },
    "404": {
        "notFound": {
            "summary": "Entity not found",
            "value": [{"code": "NOT_FOUND", "message": "Requested resource was not found."}],
        }
    },
}
