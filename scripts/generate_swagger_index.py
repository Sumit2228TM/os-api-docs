#!/usr/bin/env python3
"""Generate docs/api/endpoints.html (master + v12.2) and docs/index.html redirect."""

from __future__ import annotations

import html
import json
import re
import sys
from pathlib import Path
from urllib.parse import quote

REPO_ROOT = Path(__file__).resolve().parents[1]
MANIFEST = REPO_ROOT / "docs" / "core-api-endpoints.json"
OUT_ENDPOINTS = REPO_ROOT / "docs" / "api" / "endpoints.html"
OUT_INDEX = REPO_ROOT / "docs" / "index.html"


def kebab(name: str) -> str:
    return re.sub(r"[^a-z0-9]+", "-", name.strip().lower()).strip("-")


def spec_relative_path(version: str, api_name: str) -> str:
    return f"../{version}/{kebab(api_name)}.yaml"


def swagger_link(version: str, api_name: str, label: str) -> str:
    rel = spec_relative_path(version, api_name)
    q = quote(rel, safe="")
    return (
        f'<a class="open-spec" href="swagger-view.html?yaml={q}" '
        f'data-yaml="{html.escape(rel, quote=True)}">{html.escape(label)}</a>'
    )


def build_endpoints_html(entries: list[dict[str, str]]) -> str:
    rows: list[str] = []
    for ep in entries:
        name = ep["name"]
        method = ep["method"]
        path = ep["path"]
        master_link = swagger_link("master", name, "master")
        v122_link = swagger_link("v12.2", name, "v12.2")
        search_blob = f"{method} {path} {name}".lower()
        rows.append(
            f"""    <tr class="endpoint-row" data-search="{html.escape(search_blob, quote=True)}">
      <td><code>{html.escape(method)}</code></td>
      <td><code>{html.escape(path)}</code></td>
      <td>{html.escape(name)}</td>
      <td>{master_link}</td>
      <td>{v122_link}</td>
    </tr>"""
        )

    return f"""<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>OpenSpecimen Core API — 183 endpoints</title>
  <style>
    body {{ font-family: system-ui, sans-serif; margin: 1.5rem 2rem; max-width: 1100px; }}
    h1 {{ font-size: 1.35rem; }}
    .hint {{ color: #444; margin-bottom: 1rem; }}
    #q {{
      width: 100%; max-width: 32rem; padding: 0.45rem 0.6rem;
      font-size: 1rem; margin-bottom: 1rem; box-sizing: border-box;
    }}
    table {{ border-collapse: collapse; width: 100%; font-size: 0.92rem; }}
    th, td {{ text-align: left; padding: 0.35rem 0.5rem; border-bottom: 1px solid #e8e8e8; vertical-align: top; }}
    th {{ background: #f6f8fa; position: sticky; top: 0; }}
    code {{ background: #f0f0f0; padding: 0.1rem 0.35rem; border-radius: 3px; font-size: 0.88em; }}
    a {{ color: #0366d6; text-decoration: none; }}
    a:hover {{ text-decoration: underline; }}
    tr.hidden {{ display: none; }}
    .count {{ font-size: 0.9rem; color: #666; }}
  </style>
</head>
<body>
  <h1>OpenSpecimen Core API (183 operations)</h1>
  <p class="hint">
    Pick <strong>master</strong> or <strong>v12.2</strong> to open
    <a href="swagger-view.html">Swagger UI</a> for that operation&apos;s OpenAPI file.
  </p>
  <input type="search" id="q" placeholder="Filter by method, path, or name…" autocomplete="off">
  <p class="count"><span id="visible">183</span> / 183 shown</p>
  <table>
    <thead>
      <tr>
        <th>Method</th>
        <th>Path</th>
        <th>Name</th>
        <th>Spec</th>
        <th>Spec</th>
      </tr>
      <tr>
        <th colspan="3"></th>
        <th>master</th>
        <th>v12.2</th>
      </tr>
    </thead>
    <tbody>
{chr(10).join(rows)}
    </tbody>
  </table>
  <script>
    (function () {{
      var input = document.getElementById("q");
      var rows = document.querySelectorAll(".endpoint-row");
      var visible = document.getElementById("visible");
      function apply() {{
        var p = (input.value || "").trim().toLowerCase();
        var n = 0;
        rows.forEach(function (row) {{
          var hay = row.getAttribute("data-search") || "";
          var show = !p || hay.indexOf(p) !== -1;
          row.classList.toggle("hidden", !show);
          if (show) n++;
        }});
        visible.textContent = String(n);
      }}
      input.addEventListener("input", apply);
    }})();
    document.querySelectorAll("a.open-spec").forEach(function (link) {{
      link.addEventListener("click", function (ev) {{
        var rel = link.getAttribute("data-yaml");
        if (rel) {{
          sessionStorage.setItem("osOpenApiSpec", rel);
        }}
      }});
    }});
  </script>
</body>
</html>
"""


def build_index_redirect() -> str:
    return """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="refresh" content="0; url=api/endpoints.html">
  <title>OpenSpecimen REST APIs</title>
  <script>location.replace("api/endpoints.html");</script>
</head>
<body>
  <p><a href="api/endpoints.html">OpenSpecimen API index (master &amp; v12.2)</a></p>
</body>
</html>
"""


def main() -> int:
    entries = json.loads(MANIFEST.read_text(encoding="utf-8"))
    if len(entries) != 183:
        print(f"ERROR: expected 183 manifest entries, got {len(entries)}", file=sys.stderr)
        return 1

    OUT_ENDPOINTS.parent.mkdir(parents=True, exist_ok=True)
    OUT_ENDPOINTS.write_text(build_endpoints_html(entries), encoding="utf-8")
    OUT_INDEX.write_text(build_index_redirect(), encoding="utf-8")
    print(f"Wrote {OUT_ENDPOINTS.relative_to(REPO_ROOT)}")
    print(f"Wrote {OUT_INDEX.relative_to(REPO_ROOT)} (redirect to api/endpoints.html)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
