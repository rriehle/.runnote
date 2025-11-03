# RunNotes File Format Reference

This document provides complete file format specifications for RunNotes. For a quick reference, see [CLAUDE.md](../CLAUDE.md).

## Table of Contents

- [File Naming Convention](#file-naming-convention)
- [EDN Metadata Structure](#edn-metadata-structure)
- [EDN Syntax Quick Reference](#edn-syntax-quick-reference)
- [Template Reference](#template-reference)

---

## File Naming Convention

### Format

```
RunNotes-YYYY-MM-DD-TopicName-phase.md
```

### Examples

```
RunNotes-2025-10-14-AuthRefactor-research.md
RunNotes-2025-10-14-AuthRefactor-planning.md
RunNotes-2025-10-14-AuthRefactor-implementation.md
RunNotes-2025-10-14-AuthRefactor-review.md
RunNotes-2025-10-14-LoginBug-debug.md
RunNotes-2025-10-14-SecurityPatch-hotfix.md
```

### Rules

**Date component:**

- Format: `YYYY-MM-DD` (ISO 8601)
- Uses session creation date
- Enables chronological sorting

**Topic component:**

- Format: PascalCase
- Descriptive but concise
- Consistent across phase transitions
- Example: `AuthRefactor`, `DatabaseMigration`, `PerformanceOptimization`

**Phase component:**

- Format: lowercase
- Must be one of the standard phases
- Determines template used
- Automatically added to tags

### Standard Phases

**Core phases:**

- `research` - Investigation and exploration
- `planning` - Decision making and estimation
- `implementation` - Active development work
- `review` - Reflection and learning extraction

**Specialized phases:**

- `debug` - Systematic debugging
- `hotfix` - Urgent production fixes
- `performance` - Optimization work
- `security` - Security analysis/hardening
- `testing` - Test strategy and execution
- `code-review` - Code review documentation

---

## EDN Metadata Structure

### Basic Structure

All RunNotes files include EDN metadata block at the top:

```markdown
# Phase: TopicName - YYYY-MM-DD HH:MM

```edn :metadata
{:phase "implementation"
 :tag #{:authentication :api :planning}
 :status :active
 :thinking-mode "think hard"
 :date {:created "2025-10-14"
        :updated "2025-10-15"}}
```

```

### Required Fields

**:phase**
- Type: String
- Values: One of standard phases
- Example: `"implementation"`
- Purpose: Identifies session type

**:tag**
- Type: Set of keywords
- Minimum: At least one tag
- Example: `#{:authentication :api :database}`
- Purpose: Enables search and discovery

**:status**
- Type: Keyword
- Values: `:active`, `:paused`, `:completed`, `:blocked`
- Example: `:active`
- Purpose: Track session state

**:date**
- Type: Map
- Required key: `:created`
- Example: `{:created "2025-10-14"}`
- Purpose: Temporal tracking

### Optional Standard Fields

**:thinking-mode**
- Type: String
- Values: Any valid Claude thinking mode
- Example: `"think hard"`, `"think harder"`
- Purpose: Claude API configuration

**:date {:updated}**
- Type: String (date)
- Example: `{:created "2025-10-14" :updated "2025-10-15"}`
- Purpose: Track last modification

**:related-documents**
- Type: Map of document types to sets
- Purpose: Link to related documentation
- See [Related Documents Structure](#related-documents-structure)

### Related Documents Structure

```edn
{:related-documents
 {:runnote #{"RunNotes-2025-10-14-Topic-research"
             "RunNotes-2025-10-14-Topic-planning"}
  :adr #{"ADR-00042" "ADR-00043"}
  :requirements #{"REQ-AUTH-001" "REQ-AUTH-002"}
  :code-files #{"src/auth/middleware.py"
                "src/auth/oauth.py"}
  :test-files #{"test/auth/test_middleware.py"}
  :commits #{"a1b2c3d4" "b2c3d4e5"}}}
```

**Standard document types:**

- `:runnote` - Other RunNotes sessions
- `:adr` - Architecture Decision Records
- `:requirements` - Formal requirements
- `:code-files` - Source code files (with line numbers)
- `:test-files` - Test files (with line numbers)
- `:commits` - Git commit hashes

### YAML Frontmatter Fields (in Templates)

Templates may include YAML frontmatter for structured data:

```yaml
objectives:
  - Objective 1
  - Objective 2

assumptions:
  - Assumption 1
  - Assumption 2

adr-candidates:
  - Decision candidate 1
  - Decision candidate 2

success-criteria:
  - Criterion 1
  - Criterion 2
```

**Common frontmatter fields:**

- `objectives` - Session goals
- `assumptions` - Explicit assumptions
- `adr-candidates` - Decisions needing formalization
- `success-criteria` - Definition of done
- `constraints` - Known limitations
- `risks` - Identified risk factors

---

## EDN Syntax Quick Reference

EDN (Extensible Data Notation) is used for metadata. Quick reference:

### Maps

Maps are key-value pairs enclosed in `{}`:

```edn
{:key1 "value1"
 :key2 42}
```

**Nested maps:**

```edn
{:date {:created "2025-10-14"
        :updated "2025-10-15"}
 :related-documents {:adr #{"ADR-00042"}}}
```

### Keywords

Keywords start with `:` and are used as map keys:

```edn
:phase
:active
:authentication
:runnote
```

**Naming conventions:**

- Lowercase
- Hyphen-separated for multi-word
- Example: `:thinking-mode`, `:related-documents`

### Strings

Strings are enclosed in double quotes:

```edn
"2025-10-14"
"think hard"
"RunNotes-2025-10-14-Topic-research"
```

**When to use strings:**

- Dates
- File names
- Free-form text
- Phase names (in metadata)

### Sets

Sets are collections of unique values enclosed in `#{}`:

```edn
#{:tag1 :tag2 :tag3}
#{"file1.md" "file2.md"}
```

**Characteristics:**

- Unordered
- No duplicates
- Used for tags and document references

**Examples:**

```edn
:tag #{:authentication :api :database}
:runnote #{"RunNotes-2025-10-14-Research"}
```

### Vectors

Vectors are ordered sequences enclosed in `[]`:

```edn
[:research :planning :implementation :review]
```

**Less common in RunNotes metadata** but valid EDN.

### Complete Metadata Example

```edn
{:phase "implementation"
 :tag #{:authentication :oauth :security}
 :status :active
 :thinking-mode "think hard"
 :date {:created "2025-10-14"
        :updated "2025-10-15"}
 :related-documents
 {:runnote #{"RunNotes-2025-10-13-OAuth-research"
             "RunNotes-2025-10-14-OAuth-planning"}
  :adr #{"ADR-00042-passport-authentication"}
  :requirements #{"REQ-AUTH-001" "REQ-AUTH-002"}
  :code-files #{"src/auth/oauth.js:45-78"
                "src/auth/passport.config.js:12-34"}
  :test-files #{"test/auth/oauth.test.js:23-67"}
  :commits #{"a1b2c3d4"}}}
```

---

## Template Reference

Templates are stored in `~/.runnote/template/` with template hierarchy:

**Template resolution order:**

1. Project template: `<project>/.runnote/template/<phase>.md`
2. User template: `~/.runnote/template/<phase>.md`
3. Built-in template: (included with runnote-launch)

### Core Phase Templates

**research.md**

- Findings log structure
- Investigation tracking
- Hypothesis documentation
- Source citation format

**planning.md**

- Architecture decision template
- Implementation plan structure
- Risk assessment sections
- Estimation breakdown

**implementation.md**

- Activity log format
- Blocker documentation
- Progress tracking
- Code reference format

**review.md**

- Metrics calculation template
- DAKI analysis structure
- Learnings extraction
- Next steps format

### Specialized Phase Templates

**debug.md**

- Problem statement
- Hypothesis testing log
- Root cause analysis
- Fix documentation

**hotfix.md**

- Incident summary
- Minimal viable fix
- Validation steps
- Follow-up work

**performance.md**

- Baseline metrics
- Target goals
- Optimization attempts
- Before/after comparison

**security.md**

- Security analysis
- Threat model
- Findings by severity
- Hardening measures

**testing.md**

- Test strategy
- Coverage goals
- Test case design
- Execution results

**code-review.md**

- Review scope
- Findings by category
- Patterns observed
- Recommendations

### Customizing Templates

Create project-specific template:

```bash
mkdir -p <project>/.runnote/template
cp ~/.runnote/template/planning.md <project>/.runnote/template/planning.md
# Edit to add project-specific sections
```

Create user-level template:

```bash
cp ~/.runnote/template/planning.md ~/.runnote/template/planning-custom.md
# Edit for personal preferences
```

**Template variables:**

- `{{date}}` - Current date (YYYY-MM-DD)
- `{{time}}` - Current time (HH:MM)
- `{{topic}}` - Topic name from command line
- `{{phase}}` - Phase name from command line

---

## See Also

- [CLAUDE.md](../CLAUDE.md) - Quick reference for AI agents
- [README.md](../README.md) - Human-focused usage guide
- [README-PHASES.md](README-PHASES.md) - Detailed phase documentation
- [README-WORKFLOWS.md](README-WORKFLOWS.md) - Agent workflows
- [README-INTEGRATION.md](README-INTEGRATION.md) - Integration patterns
- [README-QUALITY.md](README-QUALITY.md) - Quality enforcement guidelines
