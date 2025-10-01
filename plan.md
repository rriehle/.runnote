# RunNotes Tooling Extraction Plan

**Goal**: Extract RunNotes (development knowledge capture) tooling from Xional project into `~/.runnote/` for general use across projects.

**Date**: 2025-10-01
**Status**: Planning Complete → Ready for Implementation

---

## Design Principles

1. **Embrace Clojure ecosystem**: Babashka for scripts, EDN for configuration and metadata
2. **Respect ADR-00035**: Follow Xional's folder and script naming conventions (singular forms, noun-verb pattern)
3. **Avoid Xional disruption**: Greenfield development, minimal migration burden via project-specific `.runnote.edn`
4. **Configuration flexibility**: Global defaults + project-specific overrides
5. **Template flexibility**: Built-in templates + user templates + project templates (like Claude Code subagents)
6. **Shared infrastructure**: Use `~/.lib/` for code shared with ADR tools
7. **ADR integration**: Optional but well-integrated, configurable ADR tool location
8. **Separate state per project**: Each project maintains independent RunNotes

---

## Architecture Overview

### Configuration Hierarchy

```
~/.runnote/config.edn      (global defaults)
  ↓ overridden by
<project>/.runnote.edn     (project-specific)
  ↓ discovered via
git root or cwd search     (automatic project detection)
```

### Directory Structure

```
~/
├── .runnote/                # RunNotes tool installation
│   ├── bin/                # User-facing executable scripts
│   │   ├── runnote-launch
│   │   ├── runnote-search
│   │   ├── runnote-validate
│   │   ├── runnote-transition
│   │   └── runnote          # Main dispatcher (convenience wrapper)
│   ├── template/           # Built-in templates (user-level overrides)
│   │   ├── research.md
│   │   ├── planning.md
│   │   ├── implementation.md
│   │   ├── review.md
│   │   ├── debug.md
│   │   ├── hotfix.md
│   │   ├── performance.md
│   │   ├── security.md
│   │   ├── testing.md
│   │   ├── code-review.md
│   │   └── adr-compliance.md
│   ├── doc/                # Documentation
│   │   ├── CLAUDE.md       # Operational guide for AI assistance
│   │   └── README.md       # User documentation
│   ├── config.edn          # Global configuration defaults
│   ├── plan.md             # This file
│   └── README.md           # Installation and usage
│
├── .lib/                    # Shared libraries (shared with .adr system)
│   ├── config-core.bb      # Config resolution, path discovery (shared)
│   ├── metadata-parser.bb  # EDN metadata parsing with specs (shared)
│   └── project-context.bb  # Project detection utilities (shared)
│
└── .adr/                    # ADR tools (optional, but referenced)
    └── bin/                # ADR scripts (configurable location)
```

### Installation Model

Users clone `~/.runnote` and add `~/.runnote/bin/` to their PATH:

```bash
# Installation
git clone <repo> ~/.runnote
echo 'export PATH="$HOME/.runnote/bin:$PATH"' >> ~/.bashrc  # or ~/.zshrc

# Shared library setup (if not already done for .adr)
mkdir -p ~/.lib

# Usage (from any project)
cd ~/projects/my-app
runnote-launch research UIComponents
runnote-search tag :debugging
```

### Template Hierarchy (Like Claude Code Subagents)

Templates are discovered in order, first match wins:

```
1. <project>/runnote/template/     (project-specific, highest priority)
2. ~/.runnote/template/            (user-level overrides)
3. (built-in templates if above don't exist - these ARE in ~/.runnote/template/)
```

Projects can:
- Use built-in templates (do nothing)
- Override specific templates (add to `~/.runnote/template/`)
- Add project-specific templates (add to `<project>/runnote/template/`)

---

## Configuration Schema

### Global Config: `~/.runnote/config.edn`

Default configuration for all projects:

```edn
{:runnote
 {:dir "runnote"                              ; Default runnote directory (singular)
  :template-dir "~/.runnote/template"         ; Built-in templates
  :editor (System/getenv "EDITOR")            ; Default editor
  :phases #{"research" "planning" "implementation" "review"
            "debug" "hotfix" "performance" "security" "testing"}
  :valid-tags #{:feature :bugfix :refactor :architecture :ui :testing
                :security :performance :debugging :documentation}
  :default-thinking-mode "think hard"
  :adr-integration
  {:enabled true                              ; ADR features enabled by default
   :adr-bin-dir "~/.adr/bin"                  ; Location of ADR tools
   :adr-dir "doc/adr"                         ; Default ADR location
   :require-adr-refs false}}}                 ; ADR refs optional by default
```

### Project Config: `<project>/.runnote.edn`

Project-specific overrides and extensions:

```edn
;; Example: Xional project config
{:runnote
 {:dir "runnotes"                             ; Xional uses plural (old convention)
  :phases #{"research" "planning" "implementation" "review" "code-review"}
  :project-name "Xional"
  :project-tags #{:polylith :cljfx :desktop :kafka}
  :default-thinking-mode "think harder"       ; Complex project, deeper thinking

  :adr-integration
  {:enabled true
   :adr-dir "docs/architecture/decisions"     ; Xional's custom ADR path
   :require-adr-refs true                     ; Xional requires ADR references
   :search-on-planning true}                  ; Auto-search ADRs in planning phase

  :template-overrides
  {:planning "runnotes/template-planning.md"  ; Project-specific template
   :research "runnotes/template-research.md"}}}
```

```edn
;; Example: Simple project with defaults
{:runnote
 {:project-name "MyApp"
  :project-tags #{:web :api :clojure}}}       ; Minimal config, use global defaults
```

### Config Resolution Logic

1. **Discover project root**: Search upward from `cwd` to git root
2. **Load global config**: Read `~/.runnote/config.edn`
3. **Load project config**: Read `<project>/.runnote.edn` if exists
4. **Merge configs**: Deep merge with project overriding global
5. **Resolve paths**: Make relative paths absolute from project root
6. **Validate config**: Ensure required fields present

---

## Metadata Schema Design

### RunNotes Metadata (Enhanced from Current)

Standard metadata block (EDN format):

```edn
{:phase "research"                    ; Required: current phase
 :tag #{:ui :debugging :feature}      ; Required: categorization tags
 :status :active                      ; Optional: :active | :blocked | :completed | :investigating
 :thinking-mode "think hard"          ; Optional: Claude thinking mode
 :date {:created "2025-10-01"         ; Optional: timestamps
        :modified "2025-10-02"}
 :complexity :medium}                 ; Optional: :low | :medium | :high | :extreme
```

### Extensible Metadata

Projects can add custom fields:

```edn
{:phase "implementation"
 :tag #{:ui :cljfx}
 :status :active

 ;; Project-specific extensions (namespace-qualified)
 :xional/polylith-component "ui-widgets"
 :xional/requires-review true}
```

---

## Library Design

### `~/.lib/config-core.bb` (NEW - Shared with ADR)

Core utilities for config and path resolution:

**Responsibilities:**
- Discover project root (git root or cwd search)
- Load and merge config files (global + project)
- Resolve paths (runnote dir, ADR dir, templates) to absolute paths
- Provide config accessor functions
- **Shared between ADR and RunNotes systems**

**Key Functions:**
```clojure
(discover-project-root)              ; Find project root
(load-config type)                   ; Load merged config (:runnote or :adr)
(resolve-path config path-key)       ; Get absolute path from config
(get-config-value config path)       ; Access nested config values
```

**Script Discovery:**
```clojure
;; In any script (adr or runnote)
(def lib-dir (str (System/getenv "HOME") "/.lib"))
(load-file (str lib-dir "/config-core.bb"))
(def config (config-core/load-config :runnote))
```

### `~/.lib/metadata-parser.bb` (MIGRATED - Shared with ADR)

Enhanced metadata parsing with extensible schemas:

**Current Features:**
- Parse EDN metadata blocks
- Validate RunNotes and ADR metadata
- Support clojure.spec validation
- Migration from old keyword header format

**Enhancements Needed:**
- Support project-specific metadata extensions
- Validate against merged schema (standard + extensions)
- Return detailed validation errors with suggestions

**Key Functions:**
```clojure
(extract-edn-metadata content)              ; Extract EDN block
(validate-metadata metadata type config)    ; Validate with extensions
(parse-metadata content type config)        ; Parse + validate
```

### `~/.lib/project-context.bb` (NEW - Shared)

Project detection and context utilities:

**Responsibilities:**
- Detect project type (Clojure, Polylith, generic)
- Analyze project structure for tag suggestions
- Find related files (components, tests, docs)
- **Shared utility for both systems**

**Key Functions:**
```clojure
(detect-project-type root)           ; :polylith | :clojure | :generic
(analyze-codebase root topic)        ; Suggest tags based on codebase
(find-related-files root pattern)    ; Find related project files
```

---

## Script Migration

### General Approach

1. **Add config support**: Replace hardcoded paths with config lookups
2. **Library discovery**: Load from `~/.lib/` using `$HOME/.lib`
3. **Preserve functionality**: All existing features must work
4. **Follow ADR-00035**: Use noun-verb naming pattern
5. **Project awareness**: Detect and use project context

### Script-by-Script Plan

#### `bin/runnote-launch` (MIGRATED)

**Source**: `~/src/xional/ci/runnotes-launcher.bb`

**Changes:**
- Load config via `config-core/load-config :runnote`
- Resolve runnote directory from config
- Resolve template directory from config (with hierarchy)
- Support ADR integration if enabled
- Load `metadata-parser.bb` and `project-context.bb` from `~/.lib/`

**Features Preserved:**
- Phase validation
- Intelligent tag suggestions from codebase
- Thinking mode suggestions
- Interactive mode for tag/thinking selection
- Template processing with placeholder replacement
- EDN metadata generation
- Editor integration

**New Features:**
- Template hierarchy (project > user > built-in)
- ADR search integration in templates
- Project name interpolation
- Configurable phases

#### `bin/runnote-search` (MIGRATED)

**Source**: `~/src/xional/ci/runnotes-search.bb`

**Changes:**
- Load config for runnote directory location
- Support EDN metadata (already implemented)
- Add output format options (json, table, simple)

**Features Preserved:**
- Search by tag
- Search by text (full-text)
- Search by phase
- Search by state
- List all tags
- Summary report

**New Features:**
- Cross-project search (if desired)
- JSON output for scripting
- Better formatted output

#### `bin/runnote-validate` (MIGRATED)

**Source**: `~/src/xional/ci/runnotes-validate.bb`

**Changes:**
- Load config for validation rules
- Use `metadata-parser.bb` from `~/.lib/`
- Support project-specific metadata extensions

**Features Preserved:**
- Filename format validation
- Metadata completeness checks
- Phase transition readiness checks

**New Features:**
- Configurable validation rules
- CI mode (exit codes, minimal output)
- Pre-commit hook support

#### `bin/runnote-transition` (MIGRATED)

**Source**: `~/src/xional/ci/runnotes-transition-manager.bb`

**Changes:**
- Load config for runnote directory
- Support template hierarchy
- Use config for phase definitions

**Features Preserved:**
- Phase transition validation
- Context extraction from current phase
- New phase file generation
- Checklist verification

**New Features:**
- Configurable transition requirements
- Template-driven transitions
- ADR checkpoint during transitions

#### `bin/runnote` (NEW - Main Dispatcher)

**Purpose**: Unified entry point (convenience wrapper)

**Functionality:**
```bash
# Dispatch to specific scripts
runnote launch <phase> <topic>       # → runnote-launch
runnote search <query>               # → runnote-search
runnote validate                     # → runnote-validate
runnote transition <phase>           # → runnote-transition
runnote init                         # → runnote-init (new)
runnote config                       # → runnote-config (new)
```

**Implementation:**
Simple dispatcher that executes scripts in `~/.runnote/bin/` with arguments

#### `bin/runnote-init` (NEW)

**Purpose**: Initialize RunNotes in a project

**Functionality:**
- Create runnote directory (from config or default)
- Create `.runnote.edn` with project-specific config
- Copy default templates if requested
- Create initial README in runnote/
- Optionally integrate with git (add to .gitignore patterns)

**Usage:**
```bash
cd ~/projects/my-app
runnote-init                         # Interactive setup
runnote-init --dir runnotes          # Specify custom directory
runnote-init --templates             # Copy templates locally
```

#### `bin/runnote-config` (NEW)

**Purpose**: Inspect and validate configuration

**Functionality:**
- Show merged config for current project
- Validate config files
- Show resolved paths
- Show template hierarchy
- Test ADR integration

**Usage:**
```bash
runnote-config show                  # Display merged config
runnote-config validate              # Validate config files
runnote-config paths                 # Show resolved paths
```

---

## Migration Phases

### Phase 1: Foundation (Tasks 1-5)

**Objective**: Establish core infrastructure and shared libraries

**Tasks:**
1. ✅ Design configuration schema (global + project with overrides)
2. ✅ Design metadata schema (aligned with ADR system)
3. Create `~/.runnote/` directory structure (bin/, template/, doc/)
4. Create shared `~/.lib/` directory (if not exists from ADR system)
5. Define template hierarchy and discovery rules

**Deliverables:**
- `~/.runnote/` structure created
- `~/.runnote/config.edn` with defaults
- `~/.lib/` directory ready
- Empty bin/ and template/ directories

### Phase 2: Shared Libraries (Tasks 6-8)

**Objective**: Build reusable library code (shared with ADR system)

**Tasks:**
6. Create `config-core.bb` library (config resolution, shared)
7. Migrate and enhance `metadata-parser.bb` (already exists, may need RunNotes-specific enhancements)
8. Create `project-context.bb` library (project detection, tag suggestions)

**Deliverables:**
- `~/.lib/config-core.bb` with config management
- `~/.lib/metadata-parser.bb` enhanced for RunNotes + ADR
- `~/.lib/project-context.bb` with project analysis

### Phase 3: Template Migration (Tasks 9-10)

**Objective**: Migrate and adapt templates for general use

**Tasks:**
9. Copy all templates from Xional to `~/.runnote/template/`
10. Update templates for project-agnostic use (remove Xional-specific references)
11. Add configurable ADR references in templates
12. Update placeholder system for project name, tags, etc.

**Deliverables:**
- All templates in `~/.runnote/template/`
- Templates use config values and are project-agnostic
- ADR integration is optional in templates

### Phase 4: Script Migration (Tasks 11-15)

**Objective**: Migrate and adapt existing scripts

**Tasks:**
11. Migrate `runnotes-launcher.bb` → `runnote-launch`
12. Migrate `runnotes-search.bb` → `runnote-search`
13. Migrate `runnotes-validate.bb` → `runnote-validate`
14. Migrate `runnotes-transition-manager.bb` → `runnote-transition`
15. Create new scripts: `runnote-init`, `runnote-config`, `runnote` (dispatcher)

**Deliverables:**
- Working scripts in `~/.runnote/bin/`
- All existing functionality preserved
- Config-driven, project-agnostic
- New initialization and config tools

### Phase 5: Documentation (Tasks 16-17)

**Objective**: Migrate and adapt documentation

**Tasks:**
16. Migrate `CLAUDE.md` to `~/.runnote/doc/CLAUDE.md` (update for new paths)
17. Create `~/.runnote/README.md` with installation, quick start, and usage
18. Document configuration options and examples
19. Create project adoption guide

**Deliverables:**
- `~/.runnote/doc/CLAUDE.md` (operational guide)
- `~/.runnote/README.md` (user guide)
- Configuration reference
- Adoption guide for new projects

### Phase 6: Xional Integration Testing (Tasks 18-20)

**Objective**: Validate with Xional, ensure no disruption

**Tasks:**
18. Create `.runnote.edn` config for Xional project
19. Test all tools in Xional (read existing RunNotes)
20. Test new RunNotes creation in Xional
21. Verify backward compatibility (old scripts still work)

**Deliverables:**
- `~/src/xional/.runnote.edn`
- Verified working in Xional
- All existing RunNotes readable
- New RunNotes creation works
- No disruption to Xional workflow

### Phase 7: Additional Tools (Tasks 21-23)

**Objective**: Migrate remaining specialized tools

**Tasks:**
21. Migrate `runnotes-adr-extract.bb` (optional, ADR-specific)
22. Migrate `runnotes-issue-generator.bb` (optional, GitHub-specific)
23. Migrate performance and security tools (optional)

**Deliverables:**
- Optional specialized scripts in `~/.runnote/bin/`
- Make these tools discoverable but not required

---

## Xional Migration Strategy

### Minimal Disruption Approach

**Step 1**: Add `.runnote.edn` to Xional project root

```edn
;; ~/src/xional/.runnote.edn
{:runnote
 {:dir "runnotes"                              ; Keep plural for Xional
  :project-name "Xional"
  :project-tags #{:polylith :cljfx :desktop}
  :default-thinking-mode "think harder"

  :adr-integration
  {:enabled true
   :adr-dir "docs/architecture/decisions"
   :require-adr-refs true
   :search-on-planning true}}}
```

**Step 2**: Xional scripts ignore `.runnote.edn`

- Old scripts in `ci/runnotes*.bb` use hardcoded paths
- New scripts in `~/.runnote/bin/` read `.runnote.edn`
- **No collision** because old system doesn't know about `.runnote.edn`

**Step 3**: Gradual adoption (optional)

- Developers can start using `~/.runnote/bin/` scripts
- Old scripts remain in place for stability
- Eventually deprecate old scripts in CI

**No changes needed:**
- Existing RunNotes files remain unchanged
- Old naming pattern still works (plural "runnotes")
- All metadata remains valid
- CI continues to work

---

## Design Decisions

### 1. Library Location: `~/.lib/`

**Rationale:**
- Hidden folder for system-level tools
- **Shared across multiple tool suites (`.adr`, `.runnote`)**
- Keeps user home directory clean
- Follows Unix convention for dot-files
- Prevents duplication of metadata parser

**Script Discovery:**
Scripts find libraries via:
```clojure
(def lib-dir (str (System/getenv "HOME") "/.lib"))
(load-file (str lib-dir "/config-core.bb"))
```

**Alternative considered**: `~/.runnote/lib/` → Rejected due to ADR sharing needs

### 2. Default RunNotes Path: `runnote/` (Singular)

**Rationale:**
- Singular form aligns with ADR-00035 conventions
- Consistent with ADR system (`doc/adr`)
- Shorter, cleaner path
- Xional can override via `.runnote.edn` to keep `runnotes/`

**Alternative**: `runnotes/` → Old convention, still supported via config

### 3. Script Naming: `runnote-*` (Singular)

**Rationale:**
- Follows ADR-00035 noun-verb pattern
- Consistent with ADR scripts (`adr-search`, `adr-validate`)
- Shorter, easier to type
- **Avoids collision with Xional's `runnotes-*` scripts**

**Examples:**
- `runnote-launch` (not `runnotes-launch`)
- `runnote-search` (not `runnotes-search`)

### 4. Template Hierarchy: Like Claude Code Subagents

**Rationale:**
- Flexibility: users can override built-in templates
- Project-specific customization supported
- Familiar pattern (Claude Code uses similar approach)
- Graceful fallback (built-in → user → project)

**Discovery Order:**
1. `<project>/runnote/template/<phase>.md` (project-specific)
2. `~/.runnote/template/<phase>.md` (user-level override)
3. Built-in templates are stored in `~/.runnote/template/` (fallback)

### 5. Configuration: Two-Tier System

**Rationale:**
- Global defaults reduce per-project configuration
- Project overrides support diverse project structures
- Deep merge allows partial overrides
- Aligned with ADR system for consistency

**Precedence:**
```
Project config > Global config > Built-in defaults
```

### 6. ADR Integration: Optional but Configurable

**Rationale:**
- Not all projects use ADRs
- Xional heavily uses ADRs, needs tight integration
- Configurable location supports different ADR systems
- Gracefully degrades if ADR tools not available

**Configuration:**
```edn
{:adr-integration
 {:enabled true                    ; Can be disabled
  :adr-bin-dir "~/.adr/bin"        ; Configurable location
  :require-adr-refs false}}        ; Optional by default
```

### 7. Backward Compatibility: Config-Based Isolation

**Rationale:**
- `.runnote.edn` ignored by old Xional scripts
- New scripts use different names (`runnote-*` vs `runnotes-*`)
- Projects can adopt at their own pace
- No breaking changes to existing workflows

**Risk Mitigation:**
- Old and new systems coexist peacefully
- Configuration file naming prevents collisions
- Script naming prevents collisions

---

## Testing Strategy

### Validation Tests (in Xional)

1. **Existing RunNotes**: All current RunNotes readable by new tools
2. **Search**: Tag, text, phase, and state searches work
3. **Tag Suggestions**: Codebase analysis suggests relevant tags
4. **Transitions**: Phase transitions work correctly
5. **Validation**: Metadata validation catches errors
6. **Templates**: New RunNotes created with correct format
7. **ADR Integration**: ADR searches work in planning phase

### New Project Tests

Create test project with:
- Default config (use global defaults)
- Initialize with `runnote-init`
- Create RunNotes in each phase
- Verify all tools work without project config
- Test template hierarchy

### Cross-Project Tests

- Multiple projects with different configs
- Verify no cross-contamination
- Config resolution works correctly
- Project detection is accurate

---

## Future Enhancements (Post-MVP)

### RunNotes Analytics

- Time tracking per phase
- Velocity metrics (LOC/hour, features/sprint)
- Tag frequency analysis
- Blocked time reporting
- Failed attempt ROI calculations

### Integration Enhancements

- GitHub Issues integration (create issues from blockers)
- Jira integration (link to tickets)
- Slack notifications (blockers, phase transitions)
- CI/CD integration (report status in pipelines)

### Editor Integration

- VSCode extension (create/navigate RunNotes)
- Vim plugin (jump to RunNotes for current file)
- Emacs mode (RunNotes major mode)

### Team Collaboration

- Shared RunNotes repository
- Multi-author attribution
- Merge conflict resolution
- Team dashboards

### AI Enhancements

- Automatic tag suggestions using AI
- Summary generation (weekly rollups)
- Pattern detection (common blockers)
- Recommendation engine (similar past solutions)

---

## Success Criteria

### Must Have (MVP)

- [ ] All scripts work from `~/.runnote/bin/` via PATH
- [ ] Global + project config with override working
- [ ] Shared `~/.lib/` libraries work for both ADR and RunNotes
- [ ] Template hierarchy (project > user > built-in) functional
- [ ] Xional RunNotes readable with new tools
- [ ] New RunNotes creation works in Xional
- [ ] No disruption to Xional development
- [ ] ADR integration optional and configurable
- [ ] Documentation complete (README, CLAUDE.md)

### Should Have

- [ ] `runnote-init` for easy project adoption
- [ ] `runnote-config` for config inspection
- [ ] `runnote` dispatcher for convenience
- [ ] Project detection works reliably
- [ ] Tag suggestions based on codebase analysis

### Could Have (Future)

- [ ] Analytics and reporting
- [ ] Team collaboration features
- [ ] Editor integrations
- [ ] CI/CD integration
- [ ] AI enhancements

---

## Open Questions & Decisions

### Resolved

✅ **Config strategy**: Both global and project configs
✅ **RunNotes location**: Configurable, default `runnote/` (singular)
✅ **Script naming**: `runnote-*` (singular, avoids collisions)
✅ **Library location**: `~/.lib/` (shared with ADR)
✅ **Template hierarchy**: Project > User > Built-in
✅ **ADR integration**: Optional, configurable location
✅ **Xional compatibility**: Via `.runnote.edn` + script naming
✅ **Backward compatibility**: Config-based isolation prevents disruption

### To Be Determined

- Multiple working directories support? (e.g., monorepos)
- Cross-project search/reporting?
- Automatic updates mechanism?
- Version compatibility checking?

---

## Implementation Checklist

- [x] **Phase 1: Foundation** ✅ COMPLETED
  - [x] Design configuration schema ✅
  - [x] Design metadata schema ✅
  - [x] Create `~/.runnote/` structure (bin/, template/, doc/) ✅
  - [x] Create `~/.lib/` directory (already exists from ADR) ✅
  - [x] Define template hierarchy rules ✅

- [x] **Phase 2: Shared Libraries** ✅ COMPLETED
  - [x] Create `config-core.bb` (shared with ADR) ✅
  - [x] Maintain `adr-core.bb` as backward-compatible wrapper ✅
  - [x] `metadata-parser.bb` already supports RunNotes ✅
  - [ ] Create `project-context.bb` (tag suggestions, project detection) - DEFERRED

- [ ] **Phase 3: Template Migration** ⏳ IN PROGRESS
  - [x] Copy research template to `~/.runnote/template/` ✅
  - [ ] Copy remaining templates (planning, implementation, review, etc.)
  - [x] Update templates for project-agnostic use ✅
  - [x] Add configurable ADR references ✅
  - [x] Update placeholder system ✅

- [ ] **Phase 4: Script Migration** ⏳ IN PROGRESS
  - [x] Migrate `runnote-launch` ✅ (proof-of-concept working)
  - [ ] Migrate `runnote-search`
  - [ ] Migrate `runnote-validate`
  - [ ] Migrate `runnote-transition`
  - [ ] Create `runnote-init`
  - [ ] Create `runnote-config`
  - [ ] Create `runnote` dispatcher

- [ ] **Phase 5: Documentation**
  - [ ] Migrate `CLAUDE.md` to `doc/`
  - [ ] Create `README.md`
  - [ ] Document configuration options
  - [ ] Create adoption guide

- [ ] **Phase 6: Xional Integration Testing** ⏳ IN PROGRESS
  - [x] Create `~/src/xional/.runnote.edn` ✅
  - [ ] Test reading existing RunNotes
  - [x] Test creating new RunNotes ✅ (runnote-launch verified)
  - [ ] Verify no disruption (in progress)

- [ ] **Phase 7: Additional Tools**
  - [ ] Migrate specialized tools (optional)

---

## References

- **ADR-00035**: Folder and Script Naming Conventions
- **ADR-00016**: RunNotes Integration with Development
- **ADR Extraction Plan**: `~/.adr/plan.md` (parallel extraction effort)
- **Xional RunNotes**: `~/src/xional/runnotes/`
- **Xional Templates**: `~/src/xional/runnotes/template-*.md`
- **CLAUDE.md**: `~/src/xional/runnotes/CLAUDE.md`
- **Existing scripts**: `~/src/xional/ci/runnotes*.bb`

---

## Status Update - 2025-10-01

### ✅ Completed (Proof-of-Concept Working!)

**Phase 1: Foundation** - Complete
- Directory structure created (`~/.runnote/bin/`, `template/`, `doc/`)
- Configuration system designed and implemented
- Global config file created (`~/.runnote/config.edn`)

**Phase 2: Shared Libraries** - Complete
- `~/.lib/config-core.bb` created (generalized from ADR system)
- `~/.lib/adr-core.bb` updated as backward-compatible wrapper
- `~/.lib/metadata-parser.bb` already supports both systems
- Both ADR and RunNotes now share config infrastructure

**Phase 3: Template Migration** - Partially Complete
- Research template migrated and tested
- Template system working (project-agnostic, ADR references included)
- Placeholder replacement system functional

**Phase 4: Script Migration** - Partially Complete
- `runnote-launch` script created and working
- Loads config correctly (global + project)
- Creates RunNotes files with proper naming
- Generates EDN metadata correctly
- Template processing works

**Phase 6: Xional Integration** - Partially Complete
- `.runnote.edn` config created for Xional
- Tested `runnote-launch` in Xional - works correctly
- Uses `runnotes/` (plural) as configured (project override working)
- No disruption to existing Xional workflow

### End-to-End Test Results

**Test 1: Generic Project** (uses global defaults)
```bash
cd /tmp/test-project
runnote-launch research TestFeature
# ✅ Created: /tmp/test-project/runnote/RunNotes-2025-10-01-TestFeature-research.md
# ✅ Used singular 'runnote/' directory (global default)
```

**Test 2: Xional Project** (uses project config)
```bash
cd ~/src/xional
runnote-launch research ConfigTest
# ✅ Created: ~/src/xional/runnotes/RunNotes-2025-10-01-ConfigTest-research.md
# ✅ Used plural 'runnotes/' directory (project override)
# ✅ No collision with old system
```

### 🎯 Key Achievements

1. **Shared library system works**: Both ADR and RunNotes use `~/.lib/config-core.bb`
2. **Configuration hierarchy works**: Global defaults + project overrides
3. **Backward compatibility confirmed**: Xional uses plural `runnotes/`, new projects use singular `runnote/`
4. **No disruption**: `.runnote.edn` ignored by old Xional scripts
5. **Template system proven**: Research template works end-to-end

### 📋 Next Steps

To complete the extraction:

1. **Remaining templates** (Phase 3):
   - Copy and adapt: planning, implementation, review, debug, hotfix, performance, security, testing

2. **Remaining scripts** (Phase 4):
   - `runnote-search` - Search existing RunNotes
   - `runnote-validate` - Validate RunNotes format
   - `runnote-transition` - Phase transitions
   - `runnote-init` - Project initialization
   - `runnote-config` - Config inspection
   - `runnote` - Main dispatcher

3. **Documentation** (Phase 5):
   - Migrate CLAUDE.md
   - Create README.md
   - Document configuration options

4. **Additional testing** (Phase 6):
   - Test reading existing Xional RunNotes
   - Verify all phases work
   - Confirm no disruption to ongoing work

---

## Alignment with ADR System

This plan deliberately aligns with the ADR extraction plan (`~/.adr/plan.md`) to ensure:

1. **Shared libraries**: Both systems use `~/.lib/` for common code
2. **Config patterns**: Same two-tier config system (global + project)
3. **Naming conventions**: Both follow ADR-00035 (singular forms, noun-verb)
4. **Project detection**: Same discovery logic (git root, cwd fallback)
5. **Installation model**: Both use PATH-based CLI tools
6. **Xional compatibility**: Both use project config to avoid disruption

**Key Shared Components:**
- `~/.lib/config-core.bb` - ✅ Generalized from `adr-core.bb`, used by both systems
- `~/.lib/adr-core.bb` - ✅ Backward compatibility wrapper for ADR scripts
- `~/.lib/metadata-parser.bb` - ✅ Already validates both ADR and RunNotes metadata
- `~/.lib/adr-metadata-extractor.bb` - ✅ Available from ADR extraction
- `~/.lib/project-context.bb` - ⏸️ Deferred (tag suggestions embedded in scripts for now)

This ensures the two systems work well together while remaining independent.

### Implementation Notes (2025-10-01)

The shared library extraction is complete:

1. **`config-core.bb`** generalizes the ADR-specific `adr-core.bb` to support both `:adr` and `:runnote` system types
2. **Backward compatibility** maintained via `adr-core.bb` wrapper - existing ADR scripts work unchanged
3. **`metadata-parser.bb`** already supported both systems from ADR extraction
4. Scripts load libraries via: `(load-file (str (System/getenv "HOME") "/.lib/config-core.bb"))`

The generalization allows both systems to share:
- Project root discovery (git root fallback to cwd)
- Config loading and deep-merge logic
- Path resolution utilities
- EDN metadata validation
