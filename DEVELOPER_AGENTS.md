# Developer Agents Setup

This repository incorporates a single, consolidated AI agent designed to help maintain documentation, tests, context-receiver wrappers, and the project's README. The instructions for this agent are stored in the `.agents/` directory.

## Consolidated Agent: Time Maintainer

The **Time Maintainer Agent** ([time_maintainer.md](file:///Users/vladimirtintera/Develop/time-dev/.agents/time_maintainer.md)) consolidates all repository maintenance tasks. Having a single agent prevent conflicts, improves context utilization, and ensures that documentation, tests, and context receivers remain fully synchronized.

### Core Skills

1. **KDoc Documentation Automator**
   - **Role**: Automatically generates and updates KDocs in English for all public Kotlin declarations.
   - **Goal**: Ensure all public APIs are documented with clear, active-voice descriptions, parameter explanations, return types, and compileable usage examples.

2. **README Maintainer**
   - **Role**: Maintains and updates the project's main `README.md`.
   - **Goal**: Keep all README examples, Gradle configurations, and module descriptions up-to-date and syntactically correct.

3. **Test Coverage Engineer**
   - **Role**: Manages and creates unit tests under `commonTest`.
   - **Goal**: Guarantee that all public APIs are covered by multiplatform unit tests using the standard `kotlin.test` framework.

4. **Context Integrator**
   - **Role**: Scans standard codebase modules for configuration-dependent public functions (using `AppLocale` or `TimeZone`) and ensures their context-receiver wrappers are generated and maintained in the context-aware modules.

---

## Release Agent: Time Releaser

The **Release Agent** ([release_agent.md](file:///Users/vladimirtintera/Develop/time-dev/.agents/release_agent.md)) automates and guides the process of publishing releases from the development repository to the public repository.

### Core Skills

1. **Prerelease Validator & Builder**
   - **Role**: Runs tests, builds the JS web application, builds Dokka documentation, and stages files.
   - **Goal**: Ensure the project compiles and passes all checks before syncing changes.
2. **Interactive Release Guide**
   - **Role**: Guides the developer step-by-step through tagging and GitHub release creation.
   - **Goal**: Make the release process foolproof and easily repeatable.
3. **Changelog Planner**
   - **Role**: Drafts categorized release notes based on the git history since the last release tag.

---

## How to Run the Agents

### 1. In Antigravity / Gemini Code Assistant Sessions
You can invoke the agents directly in an active coding assistant session by utilizing their subagent TypeName:

#### Time Maintainer:
- **TypeName**: `time_maintainer`
- **Role**: `Time Maintainer`
- **Example Prompt**: *"Run the `time_maintainer` agent to scan the codebase, verify KDocs, check context receivers, and verify README examples."*

#### Release Agent:
- **TypeName**: `release_agent`
- **Role**: `Release Agent`
- **Example Prompt**: *"Run the `release_agent` to start the prerelease validation and squash-sync to the public repository. Use commit message 'Release v1.0.0'."*

### 2. In Cursor or Cline
If you use Cursor or Cline, you can link the markdown files as rules:
- Point to `.agents/time_maintainer.md` and `.agents/release_agent.md` in your settings.

---

## Verification Setup
You can run all multiplatform tests across modules using the Gradle command:
```bash
./gradlew jvmTest
```
Or build the project to verify configuration:
```bash
./gradlew build
```
