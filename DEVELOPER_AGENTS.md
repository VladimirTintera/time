# Developer Agents Setup

This repository incorporates automated AI agents designed to help maintain documentation, tests, and the project's README. The instructions for these agents are stored in the `.agents/` directory as Markdown documents.

## Available Agents

1. **Documentation Agent** ([documentation_agent.md](file:///Users/vladimirtintera/Develop/time-dev/.agents/documentation_agent.md))
   - **Role**: Automatically generates and updates KDocs in English for all public classes, members, properties, and functions.
   - **Goal**: Keep all public APIs 100% documented with clear, active-voice descriptions, parameter explanations, return types, and throws.

2. **README Agent** ([readme_agent.md](file:///Users/vladimirtintera/Develop/time-dev/.agents/readme_agent.md))
   - **Role**: Maintains and regenerates the project's main `README.md`.
   - **Goal**: Keep the documentation and usage examples up-to-date and compileable by cross-referencing real library APIs.

3. **Testing Agent** ([testing_agent.md](file:///Users/vladimirtintera/Develop/time-dev/.agents/testing_agent.md))
   - **Role**: Manages and creates unit tests under `commonTest`.
   - **Goal**: Guarantee that all public functions are covered by multiplatform unit tests using the standard `kotlin.test` framework.

4. **Context Integration Agent** ([context_agent.md](file:///Users/vladimirtintera/Develop/time-dev/.agents/context_agent.md))
   - **Role**: Automatically generates context-receiver wrappers in the context-aware modules for public functions dependent on regional parameters (`AppLocale`, `TimeZone`).
   - **Goal**: Keep the `:time:core-context` and `:time:format-context` modules in sync with `:time:core` and `:time:format` public APIs.

---

## How to Run the Agents

### 1. In Antigravity / Gemini Code Assistant Sessions
You can invoke these agents directly in an active coding assistant session.
- **Documentation**: Ask the assistant to invoke the Documentation Agent using the instructions in `.agents/documentation_agent.md` to document the codebase.
- **Testing**: Ask the assistant to invoke the Testing Agent using `.agents/testing_agent.md` to scan the library and write missing unit tests.
- **README**: Ask the assistant to invoke the README Agent using `.agents/readme_agent.md` to update the `README.md`.

### 2. In Cursor or Cline
If you use Cursor or Cline, you can link these markdown files as rules:
- In Cursor, configure your rules settings to point to `.agents/` or copy the contents into `.cursorrules` / `.cursor/rules`.
- In Cline, copy the instructions into `.clinerules`.

---

## Testing Setup
All library modules have been configured with the `commonTest` source set. You can run all multiplatform tests across modules using the Gradle command:
```bash
./gradlew allTests
```
Or build the project to verify configuration:
```bash
./gradlew build
```
