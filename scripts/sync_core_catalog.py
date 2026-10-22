#!/usr/bin/env python3
"""Write docs/core-api-endpoints.json with exactly 132 catalog endpoints."""

from __future__ import annotations

import json
import sys
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parents[1]
OUT = REPO_ROOT / "docs" / "core-api-endpoints.json"


def main() -> int:
    sys.path.insert(0, str(REPO_ROOT / "scripts"))
    from catalog_132 import CATALOG_132  # noqa: WPS433

    if len(CATALOG_132) != 132:
        print(f"ERROR: expected 132 catalog entries, got {len(CATALOG_132)}", file=sys.stderr)
        return 1

    OUT.write_text(json.dumps(CATALOG_132, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {len(CATALOG_132)} entries to {OUT.relative_to(REPO_ROOT)}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
