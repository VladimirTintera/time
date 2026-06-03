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

## How to Run the Agent

### 1. In Antigravity / Gemini Code Assistant Sessions
You can invoke the consolidated agent directly in an active coding assistant session by utilizing the subagent type:
- **TypeName**: `time_maintainer`
- **Role**: `Time Maintainer`

#### Example Prompt to Run All Tasks:
> "Run the `time_maintainer` agent to scan the codebase, verify that all public functions are documented with correct KDocs, ensure all context receiver wrappers are present and documented, and verify that the README examples match the current APIs."

#### Example Prompt to Run a Single Skill (e.g. Test Coverage):
> "Run the `time_maintainer` agent with the **Test Coverage Engineer** skill to inspect the `:time:core` module and add unit tests for any untested public calculations."

### 2. In Cursor or Cline
If you use Cursor or Cline, you can link the markdown file as a rule:
- In Cursor, configure your settings to point to `.agents/time_maintainer.md` or copy its contents into `.cursorrules` / `.cursor/rules`.
- In Cline, copy the instructions into `.clinerules`.

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
