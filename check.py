#!/usr/bin/env python3
import argparse
import re
import subprocess
import sys
from dataclasses import dataclass


@dataclass
class RunResult:
    ok: bool
    returncode: int
    stdout: str
    stderr: str


def run_java(
    java_cmd: list[str],
    stdin_text: str,
    timeout_s: float,
    encoding: str = "utf-8",
) -> RunResult:
    """
    Runs a Java CLI program, feeds stdin_text to stdin, captures stdout/stderr.
    """
    try:
        cp = subprocess.run(
            java_cmd,
            input=stdin_text,
            capture_output=True,
            text=True,
            encoding=encoding,
            timeout=timeout_s,
            check=False,  # we decide pass/fail ourselves
        )
        return RunResult(
            ok=(cp.returncode == 0),
            returncode=cp.returncode,
            stdout=cp.stdout,
            stderr=cp.stderr,
        )
    except subprocess.TimeoutExpired as e:
        return RunResult(
            ok=False,
            returncode=124,
            stdout=getattr(e, "stdout", "") or "",
            stderr=f"Timed out after {timeout_s}s\n{getattr(e, 'stderr', '') or ''}",
        )
    except FileNotFoundError as e:
        return RunResult(
            ok=False,
            returncode=127,
            stdout="",
            stderr=f"Command not found: {e}",
        )


def stdout_matches(stdout: str, pattern: str, flags: int = 0) -> bool:
    """
    Uses regex search by default (match anywhere). Use ^...$ if you want full-string.
    """
    return re.search(pattern, stdout, flags=flags) is not None


def main() -> int:
    ap = argparse.ArgumentParser(
        description="Run a Java CLI program, feed stdin, and regex-check stdout."
    )
    ap.add_argument(
        "--cmd",
        nargs="+",
        required=True,
        help='Java command as tokens, e.g. --cmd java -cp build Main',
    )
    ap.add_argument(
        "--input",
        required=True,
        help="Input string to feed to stdin. Use \\n for newlines.",
    )
    ap.add_argument(
        "--regex",
        required=True,
        help=r"Regex that stdout must satisfy (re.search). Example: '^OK\b'",
    )
    ap.add_argument("--timeout", type=float, default=2.0, help="Timeout seconds.")
    ap.add_argument(
        "--dotall",
        action="store_true",
        help="Enable DOTALL so '.' matches newlines.",
    )
    ap.add_argument(
        "--ignorecase",
        action="store_true",
        help="Enable IGNORECASE.",
    )
    ap.add_argument(
        "--require-zero-exit",
        action="store_true",
        help="Fail if Java returns non-zero exit code.",
    )
    ap.add_argument(
        "--verbose",
        action="store_true",
        help="Dump standard output.",
    )

    args = ap.parse_args()

    stdin_text = args.input.encode("utf-8").decode("unicode_escape")  # turns "\n" into newline

    flags = 0
    if args.dotall:
        flags |= re.DOTALL
    if args.ignorecase:
        flags |= re.IGNORECASE

    rr = run_java(args.cmd, stdin_text, timeout_s=args.timeout)
    if args.verbose:
        print(rr.stdout)

    if args.require_zero_exit and rr.returncode != 0:
        print("FAIL: Java program returned non-zero exit code.", file=sys.stderr)
        print(f"Return code: {rr.returncode}", file=sys.stderr)
        print("--- stderr ---", file=sys.stderr)
        print(rr.stderr, file=sys.stderr)
        print("--- stdout ---", file=sys.stderr)
        print(rr.stdout, file=sys.stderr)
        return 2

    if stdout_matches(rr.stdout, args.regex, flags=flags):
        print("PASS")
        return 0

    print("FAIL: stdout did not match regex.", file=sys.stderr)
    print(f"Regex: {args.regex}", file=sys.stderr)
    print("--- stdout ---", file=sys.stderr)
    print(rr.stdout, file=sys.stderr)
    print("--- stderr ---", file=sys.stderr)
    print(rr.stderr, file=sys.stderr)
    return 1


if __name__ == "__main__":
    raise SystemExit(main())
