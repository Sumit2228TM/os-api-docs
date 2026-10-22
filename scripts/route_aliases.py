"""Canonical catalog path -> implemented REST route aliases."""

from __future__ import annotations

ROUTE_ALIASES: dict[tuple[str, str], tuple[str, str] | None] = {
    ("GET", "/specimens/exists"): ("HEAD", "/specimens"),
    ("GET", "/distribution-orders/{id}/export"): ("GET", "/distribution-orders/{id}/report"),
    ("POST", "/collection-protocols/{id}/merge"): ("POST", "/collection-protocols/merge"),
    ("POST", "/distribution-protocols/{id}/reserved-specimens"): (
        "PUT",
        "/distribution-protocols/{id}/reserved-specimens",
    ),
    ("PUT", "/specimen-lists/{id}/share"): ("PUT", "/specimen-lists/{listId}/users"),
    ("PATCH", "/distribution-protocols/{id}"): ("PUT", "/distribution-protocols/{id}/activity-status"),
    ("PATCH", "/print-rules/{id}"): ("PUT", "/print-rules/{id}"),
    ("GET", "/config-settings/{module}"): ("GET", "/config-settings"),
    ("PUT", "/config-settings/{module}/{property}"): ("PUT", "/config-settings"),
    ("GET", "/version"): ("GET", "/config-settings/app-props"),
    ("GET", "/about"): ("GET", "/config-settings/app-props"),
    ("PUT", "/forms/{formId}/data/{recordId}"): ("PUT", "/forms/{id}/data"),
    ("PUT", "/rbac/roles/{id}"): ("PUT", "/rbac/roles/{roleId}"),
    ("POST", "/rbac/subjects/{userId}/roles"): ("POST", "/rbac/subjects/{id}/roles"),
    ("GET", "/rbac/subjects/{userId}/roles"): ("GET", "/rbac/subjects/{subjectId}/roles"),
    ("GET", "/collection-protocol-registrations/{id}"): ("GET", "/collection-protocol-registrations/{cprId}"),
    ("GET", "/collection-protocol-registrations/{cprId}/consents"): (
        "GET",
        "/collection-protocol-registrations/{id}/consents",
    ),
    ("POST", "/collection-protocol-registrations/{cprId}/consents"): (
        "PUT",
        "/collection-protocol-registrations/{id}/consents",
    ),
    ("GET", "/collection-protocol-registrations/{cprId}/consent-responses"): (
        "GET",
        "/collection-protocol-registrations/{id}/consent-responses",
    ),
    ("GET", "/collection-protocol-registrations/{cprId}/consent-form"): (
        "GET",
        "/collection-protocol-registrations/{id}/consent-form",
    ),
    ("DELETE", "/collection-protocol-registrations/{cprId}/consent-form"): (
        "DELETE",
        "/collection-protocol-registrations/{id}/consent-form",
    ),
    ("GET", "/specimen-lists/{id}"): ("GET", "/specimen-lists/{listId}"),
    ("PUT", "/specimen-lists/{id}"): ("PUT", "/specimen-lists/{listId}"),
    ("DELETE", "/specimen-lists/{id}"): ("DELETE", "/specimen-lists/{listId}"),
    ("GET", "/specimen-lists/{id}/specimens"): ("GET", "/specimen-lists/{listId}/specimens"),
    ("PUT", "/specimen-lists/{id}/specimens"): ("PUT", "/specimen-lists/{listId}/specimens"),
    ("GET", "/import-jobs/{jobId}"): ("GET", "/import-jobs/{id}"),
    ("GET", "/import-jobs/{jobId}/output"): ("GET", "/import-jobs/{id}/output"),
    (
        "GET",
        "/collection-protocol-events/{eventId}/specimen-requirements",
    ): ("GET", "/specimen-requirements"),
    ("POST", "/specimen-requirements/{id}/derivatives"): ("POST", "/specimen-requirements/{id}/derived"),
    ("PUT", "/users/{id}/password"): ("PUT", "/users/password"),
    ("GET", "/{entity}/{objectId}/forms"): ("GET", "/specimens/{id}/forms"),
    (
        "GET",
        "/{entity}/{objectId}/forms/{formCtxtId}/records",
    ): ("GET", "/specimens/{id}/forms/{formCtxtId}/records"),
}


def resolve_route(method: str, path: str) -> tuple[str, str] | None:
    key = (method.upper(), path)
    alias = ROUTE_ALIASES.get(key)
    if alias is None and key in ROUTE_ALIASES:
        return None
    if alias:
        return alias
    return method.upper(), path
