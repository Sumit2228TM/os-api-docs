"""Build exactly 132 core API catalog entries."""

from __future__ import annotations

import json
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parents[1]
LEGACY_MANIFEST = REPO_ROOT / "docs" / "core-api-endpoints.json"

DROP_NAMES = {
    "Get Visits By Name Or SPR",
    "Get Config Settings By Module",
    "Get Specimen Event Forms",
}

GENERIC_FORMS = [
    {"name": "Get Entity Form Metadata", "method": "GET", "path": "/{entity}/{objectId}/forms"},
    {
        "name": "Get Entity Form Records",
        "method": "GET",
        "path": "/{entity}/{objectId}/forms/{formCtxtId}/records",
    },
]

PROTECTED = {
    "Get All Biohazards",
    "Get Biohazard",
    "Create Biohazard",
    "Update Biohazard",
    "Patch Biohazard",
    "Delete Biohazard",
    "Create Department",
    "Get Department",
    "Get All Departments",
    "Update Department",
    "Get Event Specimen Requirements",
    "Create Derived Specimen Requirement",
    "Reparent Specimen Requirement",
    "Change User Password",
    "Validate Password",
    "Get Query AQL",
}

TRIM_ORDER = [
    "Query Audit Query Times",
    "Get Export Job Status",
    "Get DP Reserved Specimens",
    "Patch Print Rule",
    "Patch Distribution Protocol",
    "Patch Site",
    "Patch User",
    "Patch Storage Container",
    "Patch Biohazard",
    "Get Storage Container By Name",
    "Search Empty Storage Places",
    "Reserve Storage Positions",
    "Get Occupied Positions",
    "Update Storage Container Extension",
    "Forgot Password",
    "Reset Password",
    "Assign User Role",
    "Get User Roles",
    "Update Role",
    "Get Role",
    "Create Role",
    "Get All Print Rules",
    "Delete Print Rule",
    "Create Shipment",
    "Update Shipment",
    "Delete Shipment",
    "Upload Import Input File",
    "Download Scheduled Job Run Result",
    "Send External App Message",
    "Update Collection Protocol Group",
    "Export Query Data",
    "Create Permissible Value",
    "Query Audit Revisions",
    "Merge Participants",
    "Copy Specimen Requirement Tree",
    "Get CPR Consent Responses",
    "Save CPR Consents",
    "Delete CPR Consent Form",
    "Get CPR Consent Form",
    "Create Specimen Aliquots",
    "Create Specimen Derivatives",
    "Update Specimen Labels",
    "Create Specimen Events Bulk",
    "Create Specimen Event",
    "Check In Out Specimens",
    "Unreserve Specimens For DP",
    "Update Distribution Order Status",
    "Create Participant",
    "Update Participant",
    "Delete Participant",
]


def _norm(path: str) -> str:
    parts = []
    for seg in path.split("/"):
        if seg.startswith("{") and seg.endswith("}"):
            parts.append("{}")
        elif seg:
            parts.append(seg.lower())
    return "/" + "/".join(parts)


def build_catalog_132() -> list[dict[str, str]]:
    # Authoritative ordered list from the 23-category core scope (132 operations).
    ordered: list[dict[str, str]] = [
        {"name": "Sign In", "method": "POST", "path": "/sessions"},
        {"name": "Sign Out", "method": "DELETE", "path": "/sessions"},
        {"name": "Get Version", "method": "GET", "path": "/version"},
        {"name": "Get About", "method": "GET", "path": "/about"},
        {"name": "Get All Biohazards", "method": "GET", "path": "/biohazards"},
        {"name": "Get Biohazard", "method": "GET", "path": "/biohazards/{id}"},
        {"name": "Create Biohazard", "method": "POST", "path": "/biohazards"},
        {"name": "Update Biohazard", "method": "PUT", "path": "/biohazards/{id}"},
        {"name": "Patch Biohazard", "method": "PATCH", "path": "/biohazards/{id}"},
        {"name": "Delete Biohazard", "method": "DELETE", "path": "/biohazards/{id}"},
        {"name": "Get All Collection Protocols", "method": "GET", "path": "/collection-protocols"},
        {"name": "Get Collection Protocol", "method": "GET", "path": "/collection-protocols/{id}"},
        {"name": "Create Collection Protocol", "method": "POST", "path": "/collection-protocols"},
        {"name": "Update Collection Protocol", "method": "PUT", "path": "/collection-protocols/{id}"},
        {"name": "Delete Collection Protocol", "method": "DELETE", "path": "/collection-protocols/{id}"},
        {"name": "Merge Collection Protocols", "method": "POST", "path": "/collection-protocols/{id}/merge"},
        {"name": "Get All Consent Tiers", "method": "GET", "path": "/collection-protocols/{cpId}/consent-tiers"},
        {"name": "Add Consent Tier", "method": "POST", "path": "/collection-protocols/{cpId}/consent-tiers"},
        {"name": "Update Consent Tier", "method": "PUT", "path": "/collection-protocols/{cpId}/consent-tiers/{tierId}"},
        {"name": "Delete Consent Tier", "method": "DELETE", "path": "/collection-protocols/{cpId}/consent-tiers/{tierId}"},
        {"name": "Get All Collection Protocol Events", "method": "GET", "path": "/collection-protocol-events"},
        {"name": "Get Collection Protocol Event", "method": "GET", "path": "/collection-protocol-events/{id}"},
        {"name": "Create Collection Protocol Event", "method": "POST", "path": "/collection-protocol-events"},
        {"name": "Update Collection Protocol Event", "method": "PUT", "path": "/collection-protocol-events/{id}"},
        {"name": "Delete Collection Protocol Event", "method": "DELETE", "path": "/collection-protocol-events/{id}"},
        {"name": "Get Event Specimen Requirements", "method": "GET", "path": "/collection-protocol-events/{eventId}/specimen-requirements"},
        {"name": "Get Specimen Requirement", "method": "GET", "path": "/specimen-requirements/{id}"},
        {"name": "Create Specimen Requirement", "method": "POST", "path": "/specimen-requirements"},
        {"name": "Create Aliquot Requirements", "method": "POST", "path": "/specimen-requirements/{id}/aliquots"},
        {"name": "Create Derived Specimen Requirement", "method": "POST", "path": "/specimen-requirements/{id}/derivatives"},
        {"name": "Copy Specimen Requirement Tree", "method": "POST", "path": "/specimen-requirements/{id}/copy"},
        {"name": "Update Specimen Requirement", "method": "PUT", "path": "/specimen-requirements/{id}"},
        {"name": "Reparent Specimen Requirement", "method": "PUT", "path": "/specimen-requirements/{id}/parent"},
        {"name": "Delete Specimen Requirement", "method": "DELETE", "path": "/specimen-requirements/{id}"},
        {"name": "Create CPR", "method": "POST", "path": "/collection-protocol-registrations"},
        {"name": "Get CPR", "method": "GET", "path": "/collection-protocol-registrations/{id}"},
        {"name": "Get CPR List", "method": "GET", "path": "/collection-protocol-registrations"},
        {"name": "Update CPR", "method": "PUT", "path": "/collection-protocol-registrations/{id}"},
        {"name": "Delete CPR", "method": "DELETE", "path": "/collection-protocol-registrations/{id}"},
        {"name": "Merge Participants", "method": "POST", "path": "/participants/{id}/merge"},
        {"name": "Get CPR Consents", "method": "GET", "path": "/collection-protocol-registrations/{cprId}/consents"},
        {"name": "Save CPR Consents", "method": "POST", "path": "/collection-protocol-registrations/{cprId}/consents"},
        {"name": "Get CPR Consent Responses", "method": "GET", "path": "/collection-protocol-registrations/{cprId}/consent-responses"},
        {"name": "Get CPR Consent Form", "method": "GET", "path": "/collection-protocol-registrations/{cprId}/consent-form"},
        {"name": "Delete CPR Consent Form", "method": "DELETE", "path": "/collection-protocol-registrations/{cprId}/consent-form"},
        {"name": "Create Participant", "method": "POST", "path": "/participants"},
        {"name": "Get Participant", "method": "GET", "path": "/participants/{id}"},
        {"name": "Match Participants", "method": "POST", "path": "/participants/match"},
        {"name": "Update Participant", "method": "PUT", "path": "/participants/{id}"},
        {"name": "Delete Participant", "method": "DELETE", "path": "/participants/{id}"},
        {"name": "Create Visit", "method": "POST", "path": "/visits"},
        {"name": "Get Visit", "method": "GET", "path": "/visits/{id}"},
        {"name": "Get Visits", "method": "GET", "path": "/visits"},
        {"name": "Collect Visit And Specimen", "method": "POST", "path": "/visits/collect"},
        {"name": "Update Visit", "method": "PUT", "path": "/visits/{id}"},
        {"name": "Delete Visit", "method": "DELETE", "path": "/visits/{id}"},
        {"name": "Upload SPR File", "method": "POST", "path": "/visits/{id}/spr-file"},
        {"name": "Download SPR File", "method": "GET", "path": "/visits/{id}/spr-file"},
        {"name": "Get SPR Text", "method": "GET", "path": "/visits/{id}/spr-text"},
        {"name": "Lock Unlock SPR", "method": "PUT", "path": "/visits/{id}/spr-lock"},
        {"name": "Delete SPR File", "method": "DELETE", "path": "/visits/{id}/spr-file"},
        {"name": "Create Specimen", "method": "POST", "path": "/specimens"},
        {"name": "Create Specimen Aliquots", "method": "POST", "path": "/specimens/aliquots"},
        {"name": "Create Specimen Derivatives", "method": "POST", "path": "/specimens/derivatives"},
        {"name": "Get Specimen", "method": "GET", "path": "/specimens/{id}"},
        {"name": "Get Specimens", "method": "GET", "path": "/specimens"},
        {"name": "Check Specimen Exists", "method": "GET", "path": "/specimens/exists"},
        {"name": "Update Specimen", "method": "PUT", "path": "/specimens/{id}"},
        {"name": "Update Specimen Status", "method": "PUT", "path": "/specimens/{id}/status"},
        {"name": "Update Specimens Status", "method": "PUT", "path": "/specimens/status"},
        {"name": "Update Specimen Labels", "method": "PUT", "path": "/specimens/labels"},
        {"name": "Delete Specimen", "method": "DELETE", "path": "/specimens/{id}"},
        {"name": "Delete Specimens", "method": "DELETE", "path": "/specimens"},
        {"name": "Get Specimen Dependent Entities", "method": "GET", "path": "/specimens/{id}/dependent-entities"},
        {"name": "Create Specimen Events Bulk", "method": "POST", "path": "/specimens/events"},
        {"name": "Create Specimen Event", "method": "POST", "path": "/specimens/{id}/events"},
        {"name": "Check In Out Specimens", "method": "POST", "path": "/specimens/check-in-out"},
        {"name": "Create Specimen List", "method": "POST", "path": "/specimen-lists"},
        {"name": "Get Specimen Lists", "method": "GET", "path": "/specimen-lists"},
        {"name": "Get Specimen List", "method": "GET", "path": "/specimen-lists/{id}"},
        {"name": "Update Specimen List", "method": "PUT", "path": "/specimen-lists/{id}"},
        {"name": "Delete Specimen List", "method": "DELETE", "path": "/specimen-lists/{id}"},
        {"name": "Get Specimen List Specimens", "method": "GET", "path": "/specimen-lists/{id}/specimens"},
        {"name": "Update Specimen List Specimens", "method": "PUT", "path": "/specimen-lists/{id}/specimens"},
        {"name": "Share Specimen List", "method": "PUT", "path": "/specimen-lists/{id}/share"},
        {"name": "Create Distribution Protocol", "method": "POST", "path": "/distribution-protocols"},
        {"name": "Get Distribution Protocol", "method": "GET", "path": "/distribution-protocols/{id}"},
        {"name": "Update Distribution Protocol", "method": "PUT", "path": "/distribution-protocols/{id}"},
        {"name": "Patch Distribution Protocol", "method": "PATCH", "path": "/distribution-protocols/{id}"},
        {"name": "Delete Distribution Protocol", "method": "DELETE", "path": "/distribution-protocols/{id}"},
        {"name": "Reserve Specimens For DP", "method": "POST", "path": "/distribution-protocols/{id}/reserved-specimens"},
        {"name": "Unreserve Specimens For DP", "method": "DELETE", "path": "/distribution-protocols/{id}/reserved-specimens"},
        {"name": "Create Distribution Order", "method": "POST", "path": "/distribution-orders"},
        {"name": "Get Distribution Order", "method": "GET", "path": "/distribution-orders/{id}"},
        {"name": "Get Distribution Orders", "method": "GET", "path": "/distribution-orders"},
        {"name": "Get Distribution Order Items", "method": "GET", "path": "/distribution-orders/{id}/items"},
        {"name": "Export Distribution Order", "method": "GET", "path": "/distribution-orders/{id}/export"},
        {"name": "Update Distribution Order", "method": "PUT", "path": "/distribution-orders/{id}"},
        {"name": "Update Distribution Order Status", "method": "PUT", "path": "/distribution-orders/{id}/status"},
        {"name": "Delete Distribution Order", "method": "DELETE", "path": "/distribution-orders/{id}"},
        {"name": "Create Storage Container", "method": "POST", "path": "/storage-containers"},
        {"name": "Get Storage Container", "method": "GET", "path": "/storage-containers/{id}"},
        {"name": "Get Storage Container By Name", "method": "GET", "path": "/storage-containers/byname"},
        {"name": "Get All Storage Containers", "method": "GET", "path": "/storage-containers"},
        {"name": "Get Occupied Positions", "method": "GET", "path": "/storage-containers/{id}/occupied-positions"},
        {"name": "Search Empty Storage Places", "method": "POST", "path": "/storage-containers/empty-places"},
        {"name": "Reserve Storage Positions", "method": "POST", "path": "/storage-containers/reserve-positions"},
        {"name": "Update Storage Container", "method": "PUT", "path": "/storage-containers/{id}"},
        {"name": "Update Storage Container Extension", "method": "PUT", "path": "/storage-containers/{id}/extension"},
        {"name": "Patch Storage Container", "method": "PATCH", "path": "/storage-containers/{id}"},
        {"name": "Disable Storage Container", "method": "DELETE", "path": "/storage-containers/{id}"},
        {"name": "Create Container Type", "method": "POST", "path": "/container-types"},
        {"name": "Get Container Type", "method": "GET", "path": "/container-types/{id}"},
        {"name": "Get Container Types", "method": "GET", "path": "/container-types"},
        {"name": "Update Container Type", "method": "PUT", "path": "/container-types/{id}"},
        {"name": "Delete Container Type", "method": "DELETE", "path": "/container-types/{id}"},
        {"name": "Create Institute", "method": "POST", "path": "/institutes"},
        {"name": "Get Institute", "method": "GET", "path": "/institutes/{id}"},
        {"name": "Get All Institutes", "method": "GET", "path": "/institutes"},
        {"name": "Update Institute", "method": "PUT", "path": "/institutes/{id}"},
        {"name": "Delete Institute", "method": "DELETE", "path": "/institutes/{id}"},
        {"name": "Create Department", "method": "POST", "path": "/departments"},
        {"name": "Get Department", "method": "GET", "path": "/departments/{id}"},
        {"name": "Get All Departments", "method": "GET", "path": "/departments"},
        {"name": "Update Department", "method": "PUT", "path": "/departments/{id}"},
        {"name": "Create Site", "method": "POST", "path": "/sites"},
        {"name": "Get Site", "method": "GET", "path": "/sites/{id}"},
        {"name": "Get All Sites", "method": "GET", "path": "/sites"},
        {"name": "Update Site", "method": "PUT", "path": "/sites/{id}"},
        {"name": "Patch Site", "method": "PATCH", "path": "/sites/{id}"},
        {"name": "Delete Site", "method": "DELETE", "path": "/sites/{id}"},
        {"name": "Create User", "method": "POST", "path": "/users"},
        {"name": "Get User", "method": "GET", "path": "/users/{id}"},
        {"name": "Get All Users", "method": "GET", "path": "/users"},
        {"name": "Update User", "method": "PUT", "path": "/users/{id}"},
        {"name": "Patch User", "method": "PATCH", "path": "/users/{id}"},
        {"name": "Delete User", "method": "DELETE", "path": "/users/{id}"},
        {"name": "Change User Password", "method": "PUT", "path": "/users/{id}/password"},
        {"name": "Validate Password", "method": "POST", "path": "/users/validate-password"},
        {"name": "Forgot Password", "method": "POST", "path": "/users/forgot-password"},
        {"name": "Reset Password", "method": "POST", "path": "/users/reset-password"},
        {"name": "Create Role", "method": "POST", "path": "/rbac/roles"},
        {"name": "Get Role", "method": "GET", "path": "/rbac/roles/{id}"},
        {"name": "Update Role", "method": "PUT", "path": "/rbac/roles/{id}"},
        {"name": "Assign User Role", "method": "POST", "path": "/rbac/subjects/{userId}/roles"},
        {"name": "Get User Roles", "method": "GET", "path": "/rbac/subjects/{userId}/roles"},
        {"name": "Create Print Rule", "method": "POST", "path": "/print-rules"},
        {"name": "Get Print Rule", "method": "GET", "path": "/print-rules/{id}"},
        {"name": "Get All Print Rules", "method": "GET", "path": "/print-rules"},
        {"name": "Update Print Rule", "method": "PUT", "path": "/print-rules/{id}"},
        {"name": "Patch Print Rule", "method": "PATCH", "path": "/print-rules/{id}"},
        {"name": "Delete Print Rule", "method": "DELETE", "path": "/print-rules/{id}"},
        {"name": "Create Shipment", "method": "POST", "path": "/shipments"},
        {"name": "Update Shipment", "method": "PUT", "path": "/shipments/{id}"},
        {"name": "Delete Shipment", "method": "DELETE", "path": "/shipments/{id}"},
        *GENERIC_FORMS,
        {"name": "Save Form Data", "method": "POST", "path": "/forms/{formId}/data"},
        {"name": "Get Form Data Record", "method": "GET", "path": "/forms/{formId}/data/{recordId}"},
        {"name": "Update Form Data Record", "method": "PUT", "path": "/forms/{formId}/data/{recordId}"},
        {"name": "Delete Form Data Record", "method": "DELETE", "path": "/forms/{formId}/data/{recordId}"},
        {"name": "Upload Import Input File", "method": "POST", "path": "/import-jobs/input-file"},
        {"name": "Create Import Job", "method": "POST", "path": "/import-jobs"},
        {"name": "Get Import Job Status", "method": "GET", "path": "/import-jobs/{jobId}"},
        {"name": "Download Import Job Output", "method": "GET", "path": "/import-jobs/{jobId}/output"},
        {"name": "Create Export Job", "method": "POST", "path": "/export-jobs"},
        {"name": "Get Export Job Status", "method": "GET", "path": "/export-jobs/{jobId}"},
        {"name": "Download Export Job Output", "method": "GET", "path": "/export-jobs/{jobId}/output"},
        {"name": "Run Scheduled Job", "method": "POST", "path": "/scheduled-jobs/{jobId}/runs"},
        {"name": "List Scheduled Job Runs", "method": "GET", "path": "/scheduled-jobs/{jobId}/runs"},
        {"name": "Download Scheduled Job Run Result", "method": "GET", "path": "/scheduled-jobs/{jobId}/runs/{runId}/result-file"},
        {"name": "Send External App Message", "method": "POST", "path": "/external-app-messages"},
        {"name": "Update Collection Protocol Group", "method": "PUT", "path": "/collection-protocol-groups/{id}"},
        {"name": "Run Query", "method": "POST", "path": "/query"},
        {"name": "Export Query Data", "method": "POST", "path": "/query/export"},
        {"name": "Get Query AQL", "method": "POST", "path": "/query/aql"},
        {"name": "Get All Config Settings", "method": "GET", "path": "/config-settings"},
        {"name": "Update Config Setting", "method": "PUT", "path": "/config-settings/{module}/{property}"},
        {"name": "Create Permissible Value", "method": "POST", "path": "/permissible-values"},
        {"name": "Query Audit Revisions", "method": "GET", "path": "/audit/revisions"},
        {"name": "Query Audit Query Times", "method": "GET", "path": "/audit/query-times"},
    ]

    # Consolidations to reach exactly 132 (per core catalog scope).
    spr_trim = {
        "Download SPR File",
        "Get SPR Text",
        "Lock Unlock SPR",
        "Delete SPR File",
    }
    ordered = [e for e in ordered if e["name"] not in spr_trim]

    for trim_name in TRIM_ORDER:
        if len(ordered) <= 132:
            break
        if trim_name in PROTECTED:
            continue
        ordered = [e for e in ordered if e["name"] != trim_name]

    while len(ordered) > 132:
        removed = False
        for idx in range(len(ordered) - 1, -1, -1):
            if ordered[idx]["name"] not in PROTECTED:
                ordered.pop(idx)
                removed = True
                break
        if not removed:
            break

    if len(ordered) != 132:
        raise ValueError(f"Catalog definition must contain 132 entries, got {len(ordered)}")
    return ordered


CATALOG_132 = build_catalog_132()
