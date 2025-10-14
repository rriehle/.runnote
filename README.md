# RunNotes - Development Knowledge Capture System

RunNotes is a structured system for capturing development knowledge in real-time across four distinct phases: Research, Planning, Implementation, and Review. It helps preserve decision rationale, document failed attempts, track time investments, and build institutional knowledge.

## Quick Start

### Installation

1. Clone this repository to `~/.runnote`:

   ```bash
   git clone <repo-url> ~/.runnote
   ```

2. Add `~/.runnote/bin` to your PATH:

   ```bash
   # For bash
   echo 'export PATH="$HOME/.runnote/bin:$PATH"' >> ~/.bashrc
   source ~/.bashrc

   # For zsh
   echo 'export PATH="$HOME/.runnote/bin:$PATH"' >> ~/.zshrc
   source ~/.zshrc
   ```

3. **Initialize in your project**:

   ```bash
   cd your-project
   runnote-init
   ```

4. **Create your first RunNotes session**:

   ```bash
   runnote-launch research YourTopic
   ```

## Core Concepts

### Four-Phase Development Process

1. **Research Phase**: Deep exploration and problem understanding
   - Investigate the problem space
   - Document all findings, even tangential ones
   - Challenge assumptions
   - Identify constraints and risks

2. **Planning Phase**: Architecture and approach decisions
   - Import context from research
   - Evaluate multiple approaches
   - Document trade-offs explicitly
   - Create implementation plan with time estimates

3. **Implementation Phase**: Active development with real-time logging
   - Update logs every 30-60 minutes
   - Track state (Active/Blocked/Investigating)
   - Document failures and solutions
   - Maintain progress metrics

4. **Review Phase**: Reflection and knowledge extraction
   - Calculate actual vs estimated metrics
   - Categorize learnings
   - Document failure ROI
   - Generate actionable next steps

### Specialized Phases

Additional phases for specific workflows:

- **Debug**: Systematic debugging and problem diagnosis
- **Hotfix**: Urgent fixes with minimal risk
- **Performance**: Performance analysis and optimization
- **Security**: Security analysis and hardening
- **Testing**: Test strategy and execution
- **Code Review**: Structured code review documentation

## Usage

### Create New RunNotes Session

```bash
# Basic usage
runnote-launch <phase> <topic>

# Examples
runnote-launch research AuthenticationSystem
runnote-launch planning DatabaseMigration
runnote-launch implementation FeatureX
runnote-launch review Sprint23

# With options
runnote-launch research UIComponents --tags ui,architecture
runnote-launch performance LoadTesting --thinking-mode ultrathink
```

### Search Existing RunNotes

```bash
# Search by tag
runnote-search tag :debugging
runnote-search tag :architecture

# Full-text search
runnote-search text "authentication"
runnote-search text "performance"

# Search by phase
runnote-search phase research
runnote-search phase implementation

# Search by state
runnote-search state active
runnote-search state blocked

# List all tags
runnote-search list-tags

# Summary report
runnote-search summary

# Output format options
runnote-search tag :ui --format detailed
runnote-search summary --format json
```

### Initialize New Project

```bash
# Interactive setup
runnote-init

# Non-interactive with custom directory
runnote-init --dir runnotes

# Skip config creation (directory only)
runnote-init --no-config
```

## Configuration

### Global Configuration

`~/.runnote/config.edn` - Default settings for all projects

```edn
{:runnote
 {:dir "runnote"                      ; Default directory name (singular)
  :template-dir "~/.runnote/template" ; Built-in templates
  :editor nil                         ; Use $EDITOR from environment
  :phases #{"research" "planning" "implementation" "review" ...}
  :default-thinking-mode "think hard"

  :adr-integration
  {:enabled true                      ; Enable ADR integration
   :adr-bin-dir "~/.adr/bin"          ; ADR tools location
   :adr-dir "doc/adr"                 ; Default ADR directory
   :require-adr-refs false}}}         ; ADR refs optional
```

### Project Configuration

`<project>/.runnote.edn` - Project-specific overrides

```edn
{:runnote
 {:dir "runnotes"                     ; Project uses plural (legacy)
  :project-name "MyProject"
  :project-tags #{:web :api :clojure}
  :default-thinking-mode "think harder"

  :adr-integration
  {:enabled true
   :adr-dir "docs/architecture/decisions"
   :require-adr-refs true}}}
```

**Configuration Hierarchy**: Project config overrides global config

## File Naming Convention

RunNotes files follow a strict naming pattern:

```
RunNotes-YYYY-MM-DD-TopicName-phase.md
```

Examples:
- `RunNotes-2025-10-01-AuthRefactor-research.md`
- `RunNotes-2025-10-01-AuthRefactor-planning.md`
- `RunNotes-2025-10-01-PerformanceIssue-debug.md`

## Metadata Format

RunNotes use EDN metadata for machine-readable information:

```markdown
# Research: TopicName - 2025-10-01 14:30

\`\`\`edn :metadata
{:phase "research"
 :tag #{:architecture :ui :feature}
 :status :active
 :thinking-mode "think hard"
 :date {:created "2025-10-01"}}
\`\`\`
```

## Templates

### Template Hierarchy

Templates are discovered in order (first match wins):

1. `<project>/runnote/template/<phase>.md` - Project-specific
2. `~/.runnote/template/<phase>.md` - User overrides
3. Built-in templates (in `~/.runnote/template/`)

### Available Templates

- `research.md` - Investigation and discovery
- `planning.md` - Architecture and planning
- `implementation.md` - Active development
- `review.md` - Retrospective and analysis
- `debug.md` - Debugging and problem diagnosis
- `hotfix.md` - Urgent fixes
- `performance.md` - Performance optimization
- `security.md` - Security analysis
- `testing.md` - Test strategy
- `code-review.md` - Code review documentation

### Customizing Templates

Copy built-in template and modify:

```bash
# User-level override (affects all projects)
cp ~/.runnote/template/research.md ~/.runnote/template/research.md
# Edit to customize

# Project-specific template
mkdir -p myproject/runnote/template
cp ~/.runnote/template/planning.md myproject/runnote/template/
# Edit for project-specific needs
```

## ADR Integration (Optional)

If you have the ADR system installed (`~/.adr`), RunNotes integrates seamlessly:

### In Planning Phase

- Search existing ADRs before making decisions
- Reference relevant architectural decisions
- Identify gaps requiring new ADRs

### Search Commands

```bash
# Search ADRs by topic
adr-search content "authentication"

# Search by tag
adr-search tag :architecture

# List all accepted
adr-search status accepted
```

### Configuration

Enable/configure in `.runnote.edn`:

```edn
{:runnote
 {:adr-integration
  {:enabled true
   :adr-bin-dir "~/.adr/bin"           ; Where ADR tools are
   :adr-dir "doc/adr"                  ; Where ADRs are stored
   :require-adr-refs true              ; Require ADR refs in planning
   :search-on-planning true}}}         ; Auto-prompt for ADR search
```

## Project Adoption

### Minimal Adoption

Just create the directory:

```bash
mkdir runnote
runnote-launch research FirstTopic
```

### Recommended Adoption

Initialize with config:

```bash
runnote-init
# Edit .runnote.edn to set project tags and preferences
runnote-launch research FirstTopic
```

### Legacy Project Migration

If you already have `runnotes/` (plural):

```edn
;; .runnote.edn
{:runnote
 {:dir "runnotes"   ; Keep existing directory
  :project-name "YourProject"}}
```

## Directory Structure

After initialization:

```
your-project/
├── .runnote.edn              # Project config (optional)
├── runnote/                  # RunNotes directory (configurable)
│   ├── README.md             # Usage guide (auto-generated)
│   └── RunNotes-*.md         # Your RunNotes files
```

## Best Practices

### Phase Discipline

- **Start in Research** for new problems or unclear requirements
- **Don't skip Planning** - time invested here saves implementation time
- **Update Implementation logs** every 30-60 minutes
- **Complete Review** to capture learnings

### Documentation Quality

- **Timestamps** - Use HH:MM format consistently
- **State Indicators** - Use 🟢🟡🔴 for Active/Investigating/Blocked
- **Code Snippets** - Always include language identifier
- **Metrics** - Use quantitative measures (numbers, not feelings)
- **Cross-references** - Link to specific files and line numbers

### Failure Documentation

Document failed attempts thoroughly:

- **Hypothesis**: What you thought would work
- **Time Investment**: Hours spent
- **Failure Mode**: Exactly how it failed
- **Root Cause**: Why it failed
- **Prevention**: How to avoid in future
- **Salvageable**: What can be reused

### Phase Transitions

Before transitioning:

- Complete the phase transition checklist
- Extract key context for next phase
- Archive completed phase file
- Create new phase file with imported context

## AI Integration (Claude Code)

RunNotes is designed for AI-assisted development. See `~/.runnote/CLAUDE.md` for:

- Phase-specific AI behaviors
- Quality enforcement standards
- ADR integration protocols
- Workflow orchestration guidelines

## Troubleshooting

### RunNotes directory not found

```bash
# Check your config
cd your-project
cat .runnote.edn  # or use global default

# Or reinitialize
runnote-init
```

### Scripts not found

```bash
# Ensure ~/.runnote/bin is in PATH
echo $PATH | grep runnote

# Add to PATH if needed
export PATH="$HOME/.runnote/bin:$PATH"
# Add to ~/.bashrc or ~/.zshrc to persist
```

### Config not loading

```bash
# Verify config syntax
cat ~/.runnote/config.edn       # Global
cat .runnote.edn                # Project (in project root)

# Check for EDN syntax errors
```

## Advanced Usage

### Multiple Projects

Each project can have its own config:

```bash
cd project-a
cat .runnote.edn
# {:runnote {:dir "runnotes" ...}}

cd project-b
cat .runnote.edn
# {:runnote {:dir "doc/runnote" ...}}
```

### Custom Phases

Add custom phases in project config:

```edn
{:runnote
 {:phases #{"research" "planning" "implementation" "review"
            "spike" "prototype" "migration"}}}  ; Custom phases
```

### Tag Taxonomy

Define project-specific tags:

```edn
{:runnote
 {:project-tags #{:backend :frontend :database :api :mobile}
  :valid-tags #{:feature :bugfix :refactor :debt :spike}}}
```

## Contributing

RunNotes is designed to be extensible:

- **Templates**: Add new phase templates in `~/.runnote/template/`
- **Scripts**: Extend functionality in `~/.runnote/bin/`
- **Configuration**: Add custom settings in configs

## References

- **ADR-00035**: Folder and Script Naming Conventions
- **ADR-00016**: RunNotes Integration with Development
- **CLAUDE.md**: AI Assistant Operational Guide
- **Templates**: `~/.runnote/template/*.md`

## License

See project license file.
