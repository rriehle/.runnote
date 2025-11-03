# RunNotes Technical Planning

## Overview

Technical planning extends RunNotes with structured, in-depth analysis of technical decisions across the development lifecycle. By using detailed templates with human review gates, you can achieve better software outcomes while keeping humans in the loop for knowledge maintenance and skill development.

## What is Technical Planning?

Traditional RunNotes capture the **journey** of development. Technical Planning adds **structured decision frameworks** for:

- **Data structure selection and design** - Choose the right data structures with explicit trade-off analysis
- **Algorithm selection** - Evaluate algorithmic approaches with complexity analysis
- **Concurrency strategies** - Select appropriate concurrency primitives and patterns
- **Testing strategies** - Plan comprehensive testing with unit, property-based, and integration tests
- **Security considerations** - Systematically address threats and controls
- **Performance considerations** - Establish targets and optimization strategies
- **Language best practices** - Apply language-specific idioms and patterns

## Why Technical Planning?

### Better Outcomes

Systematic technical consideration reduces rework, catches issues early, and builds higher-quality software.

### Human in the Loop

Review gates require human engagement at critical decision points, maintaining knowledge and developing skills in real-time.

### Knowledge Capture

Technical decisions and rationale are documented, creating institutional memory that improves future planning.

### Pattern Building

The Review phase extracts successful patterns and anti-patterns, continuously improving your technical planning over time.

## When to Use Detailed Templates

### Use Standard Templates When

- Feature is straightforward CRUD
- Technical decisions are obvious
- Low complexity, low risk
- Time pressure favors speed over depth

### Use Detailed Templates When

- Complex distributed systems work
- Performance-critical features
- Security-sensitive features
- Architectural decisions required
- Learning opportunity (new patterns, technologies)
- High risk of technical debt
- Polylith component boundaries unclear

**Rule of thumb**: If you're uncertain about technical approaches, use detailed templates.

## The Detailed Template Lifecycle

```
research-detailed      Gather technical discovery
        ↓
planning-detailed      Make technical decisions with human review gates
        ↓
implementation-detailed Track adherence to technical decisions
        ↓
review-detailed        Extract learnings and patterns
        ↓
[Feed patterns back to technical planning guides]
```

### Research-Detailed

**Purpose**: Gather technical context to inform planning decisions.

**Sections**:

- Data structure patterns in codebase
- Algorithm complexity requirements
- Concurrency patterns currently used
- Performance baselines
- Security model and threat landscape
- Testing patterns
- Language-specific idioms

**Output**: Technical discovery findings that feed into planning-detailed.

**Launch**: `runnote-launch research-detailed [Topic]`

### Planning-Detailed

**Purpose**: Make technical decisions with structured analysis and human approval.

**Sections** (each with 🚦 Human Review Gate):

1. Data Structures
2. Algorithm Selection
3. Concurrency Strategy
4. Testing Strategy
5. Security Considerations
6. Performance Analysis
7. Language Best Practices

**Output**: Technical roadmap with explicit decisions, trade-offs, and human sign-off.

**Launch**: `runnote-launch planning-detailed [Topic]`

### Implementation-Detailed

**Purpose**: Verify adherence to planning decisions and track deviations.

**Sections**:

- Technical adherence checkpoints (verify each planning decision)
- Deviation log (document when/why deviating from plan)
- Active work log (real-time progress with technical notes)

**Output**: Working code with documented adherence or approved deviations.

**Launch**: `runnote-launch implementation-detailed [Topic]`

### Review-Detailed

**Purpose**: Extract technical decision outcomes and feed patterns back to guides.

**Sections**:

- Technical decision outcomes (what worked, what didn't)
- Estimation accuracy (improve future planning)
- Pattern extraction (update technical guides)

**Output**: Learnings, metrics, and patterns to improve future technical planning.

**Launch**: `runnote-launch review-detailed [Topic]`

## Language-Specific Guides

Technical planning is **language-specific** because data structures, concurrency models, and best practices differ across languages.

### Available Guides

- **[Clojure/JVM](README-TECHNICAL-PLANNING-CLOJURE.md)** - Comprehensive guide for Clojure, Java interop, Polylith, Datomic

### Future Guides

As you work in other languages, create additional guides:

- `README-TECHNICAL-PLANNING-GO.md` - Go-specific patterns (goroutines, channels, etc.)
- `README-TECHNICAL-PLANNING-PYTHON.md` - Python patterns (async/await, type hints, etc.)
- `README-TECHNICAL-PLANNING-RUST.md` - Rust patterns (ownership, lifetimes, traits, etc.)

**Template**: Use the Clojure guide as a template for creating language-specific guides.

## Human Review Gates

Each technical section in `planning-detailed.md` includes a **🚦 Human Review Gate**:

```markdown
### 🚦 Human Review Gate - Data Structures

**STOP**: AI must not proceed until human completes this review.

- [ ] **Data structure selection reviewed by human**
- [ ] **Access patterns accurately captured**
- [ ] **Performance implications understood and acceptable**
- [ ] **Concurrency characteristics appropriate**
- [ ] **Trade-offs explicitly acknowledged**

**Human Notes**: [Human feedback, adjustments, concerns]
```

### Why Gates?

1. **Human Ownership**: Ensures humans remain responsible for technical decisions
2. **Knowledge Transfer**: Forces articulation and understanding of trade-offs
3. **Skill Development**: Humans learn by reviewing and approving decisions
4. **Audit Trail**: Documents who approved what and when

### How AI Agents Use Gates

AI agents (like `runnotes-manager`, `software-architect`) **must stop** at each gate and prompt the human to:

1. Review the technical analysis
2. Validate the decision and trade-offs
3. Approve or request changes
4. Check off the gate boxes

Only after human approval can the AI proceed to the next section.

## Agent Integration

Technical planning integrates with Claude Code agents:

### runnotes-manager

**Responsibilities**:

- Recommend `-detailed` templates when appropriate
- Reference language-specific guides
- Validate human review gates during phase transitions
- Coordinate with specialized agents

### Specialized Agents

Planning sections can call specialized agents for deep analysis:

- `test-strategist` - Testing strategy section
- `security-analyst` - Security considerations section
- `performance-engineer` - Performance analysis section
- `software-architect` - Overall architectural alignment
- `polylith-architect` - Polylith component boundaries (if applicable)
- `datalog-database-architect` - Datomic/Datalevin schema (if applicable)

**Pattern**: In planning-detailed, sections include agent recommendations:

```markdown
## 4. Testing Strategy

**Agent Recommendation**: Consider calling `test-strategist` agent
for comprehensive test planning.
```

## Configuration

No configuration needed! Template variants provide flexibility without complexity:

```bash
# Simple feature
runnote-launch planning SimpleFeature

# Complex feature
runnote-launch planning-detailed ComplexFeature
```

Templates live in `~/.runnote/template/`:

- `research-detailed.md`
- `planning-detailed.md`
- `implementation-detailed.md`
- `review-detailed.md`

## Example Workflow

### Starting a Complex Feature

```bash
# 1. Research phase - gather technical context
runnote-launch research-detailed EventSourcingRefactor

# ... conduct research, fill in technical discovery sections ...

# 2. Planning phase - make technical decisions
runnote-launch planning-detailed EventSourcingRefactor

# ... work through 7 technical sections with human review gates ...
# ... human checks off each gate after reviewing ...

# 3. Implementation phase - track adherence
runnote-launch implementation-detailed EventSourcingRefactor

# ... implement, track adherence, log deviations ...

# 4. Review phase - extract learnings
runnote-launch review-detailed EventSourcingRefactor

# ... analyze outcomes, extract patterns, update guides ...
```

### Pattern Extraction Loop

After review-detailed, feed patterns back:

```bash
# Update Clojure guide with learnings
vi doc/README-TECHNICAL-PLANNING-CLOJURE.md

# Add successful pattern to data structures section
# Add anti-pattern warning to concurrency section
# Update estimation guidance based on actuals
```

**Result**: Future technical planning improves continuously.

## Best Practices

### Do

- Use detailed templates for complex/risky features
- Fill in all technical sections thoroughly
- Get human approval at every review gate
- Track deviations with rationale
- Extract patterns in review phase
- Update language-specific guides with learnings

### Don't

- Skip human review gates (defeats the purpose)
- Use detailed templates for simple CRUD (overkill)
- Ignore deviations from plan (track them!)
- Forget to extract patterns (learning loop)
- Rush through sections (defeats systematic analysis)

## Quality Standards

Technical planning follows RunNotes quality standards:

- **Precision**: Use metrics, not adjectives (O(log N) not "fast")
- **Evidence**: Link to code, benchmarks, research (file:line references)
- **Quantification**: Time estimates, complexity analysis, performance targets
- **Explicit Trade-offs**: Every decision documents what you're giving up
- **Human Approval**: Gates checked off by humans, not AI

See [README-QUALITY.md](README-QUALITY.md) for comprehensive quality standards.

## Troubleshooting

### "Too much overhead for simple features"

Use standard templates! Detailed templates are for complex work.

### "AI keeps proceeding past review gates"

Update `~/.claude/agents/runnotes-manager.md` to enforce gate stops. AI must prompt human and wait for approval.

### "Hard to extract patterns from review"

Use the review-detailed template sections systematically. Each technical area has an "Outcome" section with explicit pattern extraction prompts.

### "Deviations from plan feel like failure"

Deviations are normal! Planning is hypothesis; implementation is reality. Document deviations and learn from them. Update guides with "when to deviate" patterns.

## See Also

- **[README-TECHNICAL-PLANNING-CLOJURE.md](README-TECHNICAL-PLANNING-CLOJURE.md)** - Comprehensive Clojure technical planning guide
- **[README-WORKFLOWS.md](README-WORKFLOWS.md)** - When to use detailed templates
- **[README-QUALITY.md](README-QUALITY.md)** - Quality standards
- **[README-INTEGRATION.md](README-INTEGRATION.md)** - Integration with ADRs, Requirements, Code
- **[CLAUDE.md](../CLAUDE.md)** - Quick reference for AI agents
