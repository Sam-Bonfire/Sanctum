# PROJECT_RULES.md

Version: 1.0

---

# Purpose

This document defines the engineering standards for the Sanctum project. It defines the guidelines for writing code, using VCS bookmarks, and triggering CI/CD operations.

---

# Project Overview

Project Name: Sanctum (PrayerApp)

Repository: https://github.com/Sam-Bonfire/Sanctum.git

Primary Goal: Beautiful, dynamic, multi-tenant white-label scripture and prayer application (Android, WASM Web, iOS).

Current Phase: Development & CI/CD Pipeline Hardening

---

# Git & VCS Standards (Jujutsu / jj)

### Bookmark Branching Protocol
* **`dev` Bookmark**: The primary integration branch. All active development, bug fixes, and feature integrations must be pushed exclusively to the `dev` bookmark. Pushing to `dev` runs code verification, SEO verification, and test builds in CI.
* **`main` Bookmark**: Production-ready code representation. Direct pushes to the `main` bookmark by AI agents are **STRICTLY FORBIDDEN**.
* **Release Process**: Updates to the `main` bookmark should only be performed by the user at their convenience (e.g. fast-forwarding `main` to `dev` once the dev verification build succeeds).

### Committing in Jujutsu
* Commits should have a clear conventional description format: `feat: ...`, `fix: ...`, `style: ...`, `docs: ...`, `chore: ...`.
* Before pushing `dev`, run formatting checks locally using `mise run format`.

---

# AI-Specific Rules

* **DO NOT** push to the `main` bookmark directly under any circumstance unless explicitly requested by the user in a prompt.
* **DO NOT** use task-specific branch names (e.g. ones with randomly generated identifiers) when pushing or submitting changes. All active development, bug fixes, and feature integrations must be submitted directly to the `dev` branch to follow the project's VCS conventions.
* Always check `jj log` and `jj status` (or equivalent git commands) to verify bookmark positions before running a push command.
* Never introduce technologies not listed in `ARCHITECTURE.md`.
* Never generate placeholder implementations.
* Always update the automation guides and logs when changing CI/CD configurations.

---

# Rule Changes

Every modification should record:
* **Date**: 2026-07-15
* **Reason**: Initial setup of project rules to enforce VCS branching protocol.
* **Approved By**: Sam (User)
* **Affected Rules**: VCS Bookmark Branching Protocol.

* **Date**: 2026-08-06
* **Reason**: Clarified AI Agent branch naming conventions to always submit directly to `dev`.
* **Approved By**: Sam (User)
* **Affected Rules**: AI-Specific Rules.
