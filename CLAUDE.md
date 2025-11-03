# RunNotes - AI Agent Quick Reference

This is a condensed quick reference for AI agents working with RunNotes tools.

For detailed documentation:
- **Phases:** [doc/README-PHASES.md](doc/README-PHASES.md) - Complete phase documentation and templates
- **Workflows:** [doc/README-WORKFLOWS.md](doc/README-WORKFLOWS.md) - Starting sessions, phase transitions, active session management
- **Integration:** [doc/README-INTEGRATION.md](doc/README-INTEGRATION.md) - Integration with ADR/Requirements/Code/Tests
- **Quality:** [doc/README-QUALITY.md](doc/README-QUALITY.md) - Quality standards and common pitfalls
- **File Format:** [doc/README-FILE-FORMAT.md](doc/README-FILE-FORMAT.md) - File naming, metadata, EDN syntax
- **Technical Planning:** [doc/README-TECHNICAL-PLANNING.md](doc/README-TECHNICAL-PLANNING.md) - In-depth technical planning with human review gates
- **Human Guide:** [README.md](README.md) - Installation, configuration, usage

---

## Core Principles

| Principle | Description |
|-----------|-------------|
| **Knowledge Extraction Journey** | Development work extracts learning value; capture context in real-time |
| **Real-Time Documentation** | Write during work, not after; captures actual sequence, failures, and decisions |
| **Failure as First-Class** | Failed attempts are negative knowledge preventing future repetition |
| **Four-Phase Discipline** | Research → Planning → Implementation → Review with transition criteria |
| **Institutional Knowledge** | Sessions accumulate into organizational memory enabling pattern recognition |
| **ADR Integration** | Planning RunNotes identify decision candidates; ADRs formalize them |

---

## Tools Quick Reference

### When to Use Each Tool

| Tool | When to Use | Key Output |
|------|-------------|------------|
| **runnote-init** | Setting up new project, migrating from ad-hoc notes | RunNotes directory, `.runnote.edn` config |
| **runnote-launch** | Starting any development work, phase transitions | New session from template with metadata |
| **runnote-search** | Finding similar past work, discovering patterns, summaries | Matching sessions, tag taxonomy, statistics |

**Detailed tool documentation:** See [README.md](README.md) sections on each tool

---

## Phase Quick Reference

| Phase | Objective | Key Output | Transition To |
|-------|-----------|------------|---------------|
| **Research** | Deep problem understanding | Findings with evidence, alternatives, constraints | Planning when questions answered |
| **Planning** | Evaluate alternatives, commit to approach | Decision, implementation plan, estimates | Implementation when plan validated |
| **Implementation** | Execute plan with real-time logging | Working code, tests, time tracked | Review when phases complete |
| **Review** | Extract learnings, calculate metrics | DAKI analysis, patterns, next steps | Archive session |
| **Debug** | Systematic bug investigation | Root cause, fix with rationale | Implementation or Review |
| **Hotfix** | Urgent production fixes | Minimal viable fix, rollback plan | Review |
| **Performance** | Optimization work | Before/after metrics, kept optimizations | Review |

**Complete phase documentation:** [doc/README-PHASES.md](doc/README-PHASES.md)

**State indicators:**
- 🟢 **Active** - Making progress as planned
- 🟡 **Investigating** - Researching unexpected issue
- 🔴 **Blocked** - Cannot proceed, external dependency

---

## Technical Planning Quick Reference

**When to Use Detailed Templates**:
- Complex/distributed systems work
- Performance-critical features
- Security-sensitive features
- Architectural decisions required
- High risk of technical debt
- Learning opportunity (new patterns/technologies)

**Template Variants**:

| Standard Template | Detailed Template | When to Use Detailed |
|-------------------|-------------------|----------------------|
| `research` | `research-detailed` | Technical discovery needed |
| `planning` | `planning-detailed` | Complex technical decisions |
| `implementation` | `implementation-detailed` | Track adherence to technical plan |
| `review` | `review-detailed` | Extract technical decision outcomes |

**Launch Detailed Templates**:
```bash
# Research with technical discovery
runnote-launch research-detailed Topic

# Planning with 7 technical sections + human review gates
runnote-launch planning-detailed Topic

# Implementation with adherence tracking
runnote-launch implementation-detailed Topic

# Review with technical outcomes extraction
runnote-launch review-detailed Topic
```

**Technical Sections in planning-detailed** (each with 🚦 Human Review Gate):
1. **Data Structures** - Select appropriate data structures with trade-off analysis
2. **Algorithms** - Evaluate algorithmic approaches with complexity analysis
3. **Concurrency** - Choose concurrency primitives and patterns
4. **Testing** - Plan unit, property-based, and integration tests
5. **Security** - Address threats, authentication, authorization, audit logging
6. **Performance** - Establish targets and optimization strategies
7. **Language Best Practices** - Apply language-specific idioms (Clojure, Go, Python, etc.)

**Human Review Gates**:
- AI **must stop** at each 🚦 gate and prompt human to review/approve
- Human checks off gate after reviewing decision and trade-offs
- Only after approval can AI proceed to next section

**Language-Specific Guides**:
- **[Clojure/JVM](doc/README-TECHNICAL-PLANNING-CLOJURE.md)** - Comprehensive Clojure patterns
- Future: Go, Python, Rust guides as you work in those languages

**Complete technical planning documentation:** [doc/README-TECHNICAL-PLANNING.md](doc/README-TECHNICAL-PLANNING.md)

---

## Workflow Quick Reference

### Starting a New Session

**Pre-flight checks:**
1. Search related sessions: `runnote-search content "topic"`, `runnote-search tag :tag`
2. Search ADRs: `adr-search content "topic"`
3. Choose appropriate phase

**Launch:**
```bash
runnote-launch <phase> <topic> --tags tag1,tag2 --thinking-mode "think harder"
```

**Initialize:**
1. Review template sections
2. Fill metadata (tags, status)
3. Import context (link related RunNotes, ADRs)
4. Set objectives
5. Begin logging with timestamp

**Detailed workflow:** [doc/README-WORKFLOWS.md#starting-a-new-session](doc/README-WORKFLOWS.md#starting-a-new-session)

### Phase Transitions

**Research → Planning:**
- ✅ Questions answered, findings documented, risks identified
- Launch planning session, import research context
- **Details:** [doc/README-WORKFLOWS.md#research--planning-transition](doc/README-WORKFLOWS.md#research--planning-transition)

**Planning → Implementation:**
- ✅ Approach selected, plan with estimates, success criteria defined
- Create required ADRs, launch implementation session
- **Details:** [doc/README-WORKFLOWS.md#planning--implementation-transition](doc/README-WORKFLOWS.md#planning--implementation-transition)

**Implementation → Review:**
- ✅ Phases complete (or deferred), tests passing, code committed
- Launch review, calculate metrics, conduct DAKI
- **Details:** [doc/README-WORKFLOWS.md#implementation--review-transition](doc/README-WORKFLOWS.md#implementation--review-transition)

### Active Session Management

**Update frequency:**
- Every 30-60 minutes during focused work
- Immediately when hitting blockers
- After completing each planned phase
- Before context switches or breaks

**Example update:**
```markdown
### [HH:MM] - Activity Description
State: 🟢 Active
**Objective**: What attempting
**Approach**: How attempting it
**Result**: What happened
**Time**: X minutes
**Next**: Concrete next step
Progress: [=====>    ] 50%
```

**Detailed guidelines:** [doc/README-WORKFLOWS.md#active-session-management](doc/README-WORKFLOWS.md#active-session-management)

---

## Integration Quick Reference

### With ADR Tools

**Identify ADR candidates** during research/planning:
- Affects multiple components
- Expensive to reverse
- Establishes pattern
- Technology adoption

**Link bidirectionally:**
```edn
# In RunNotes metadata
{:related-documents {:adr #{"ADR-00042"}}}

# In ADR metadata
{:runnotes #{"RunNotes-2025-10-14-Topic-planning"}}
```

**Details:** [doc/README-INTEGRATION.md#integration-with-adr-tools](doc/README-INTEGRATION.md#integration-with-adr-tools)

### With Requirements Tools

**Extract from planning RunNotes:**
```yaml
# In planning RunNotes
objectives:
  - Enable customer service to process refunds

# Becomes requirements
REQ-REFUND-001: System MUST calculate refund amount
```

**Link implementation to requirements:**
```markdown
### [HH:MM] - Implementing Refund Calculation
Implements: REQ-REFUND-001
```

**Details:** [doc/README-INTEGRATION.md#integration-with-requirements-tools](doc/README-INTEGRATION.md#integration-with-requirements-tools)

### With Code and Tests

**Link code to RunNotes:**
```python
# See: RunNotes-2025-10-14-Feature-implementation
# Architecture: ADR-00042
```

**In RunNotes metadata:**
```edn
{:related-documents
 {:code-files #{"src/module/file.py:45-67"}
  :test-files #{"test/module/test_file.py:12"}}}
```

**Details:** [doc/README-INTEGRATION.md#integration-with-code-and-tests](doc/README-INTEGRATION.md#integration-with-code-and-tests)

---

## Quality Standards Quick Reference

### Documentation Quality

| Standard | Correct | Incorrect |
|----------|---------|-----------|
| **Timestamps** | `14:30 - Activity` | `2:30pm - Activity` |
| **State** | `State: 🟢 Active` | `State: Active` |
| **Code** | ` ```python ` | ` ``` ` (no language) |
| **Metrics** | `45 minutes`, `70% improvement` | `A while`, `Much faster` |
| **References** | `src/auth.py:45-67` | `auth file` |

**Details:** [doc/README-QUALITY.md#documentation-quality-standards](doc/README-QUALITY.md#documentation-quality-standards)

### Failure Documentation

**Always document:**
1. **Hypothesis** - What you thought would work (with reasoning)
2. **Time** - Quantified investment (30min research, 2h implementation, etc.)
3. **Failure Mode** - Exactly how it failed (with evidence)
4. **Root Cause** - Why it failed (analysis)
5. **Prevention** - How to avoid in future
6. **Salvageable** - What can be reused (nothing is 100% waste)

**Details:** [doc/README-QUALITY.md#failure-documentation-standards](doc/README-QUALITY.md#failure-documentation-standards)

### Common Pitfalls

❌ **Avoid:**
- Skipping phases (especially planning)
- Retroactive documentation (losing context)
- Vague descriptions ("made progress")
- Missing timestamps (>60min gaps)
- No links to related docs
- Abandoned sessions (no handoff)
- Inconsistent tags

✅ **Solution:** [doc/README-QUALITY.md#common-pitfalls](doc/README-QUALITY.md#common-pitfalls)

---

## Decision Framework

When managing RunNotes, agents MUST always ask:

### 1. Is This Work in the Correct Phase?

Research? Planning? Implementation? Review? Debug? Hotfix?

**If wrong phase:** Transition to correct phase, import relevant context

### 2. Has Sufficient Context Been Captured?

Can future developer understand **why**? Assumptions explicit? Alternatives documented? Trade-offs explained? Sources cited? Related docs linked?

**If insufficient:** Ask clarifying questions, request detail, prompt for assumptions

### 3. Are Phase Transition Criteria Met?

Review checklist for current phase. Verify all items completed. Document incomplete items.

**If not met:** Don't transition, identify gaps, plan to complete

### 4. What Patterns Are Emerging?

Search similar past sessions. Compare estimates vs actuals. Identify blockers. Note successful patterns.

**Extract and apply:** Improve estimates, avoid pitfalls, reuse approaches

### 5. Which Decisions Need Elevation to ADRs?

Architecturally significant? Technology adoption? Pattern establishment? Expensive to reverse? Multi-component impact?

**When identified:** Flag as ADR candidate, document in RunNotes, recommend formal ADR

### 6. Is Learning Value Being Maximized?

Failures documented? Hypotheses captured? Time tracked? Root causes identified? Salvageable components noted? Preventions documented?

**If underutilized:** Prompt for failure docs, ask about hypotheses, dig for root causes

---

## Agent Responsibilities

When working with RunNotes, AI agents MUST:

### 1. Prompt for Updates Every 30-60 Minutes

During implementation: "It's been [X] minutes since last RunNotes update. Let's log current progress."

Capture: current activity, state, time, progress, next steps

### 2. Validate Phase Transition Criteria Before Advancing

Review transition criteria checklist. Ask user to confirm criteria met. Don't advance if not met.

### 3. Extract Patterns from Historical RunNotes

When starting new work: Search for similar past sessions. Extract learnings. Apply patterns (time estimates, approaches, blockers).

### 4. Identify ADR Candidates

During research and planning: Watch for architecturally significant decisions. Flag as ADR candidates. Recommend creating ADRs.

### 5. Ensure Complete Context Capture

In every session: Link related RunNotes, ADRs, requirements. Document assumptions. Include file paths with line numbers. Explain "why" not just "what".

### 6. Link Related Documentation Obsessively

Maintain bidirectional links: RunNotes ↔ RunNotes, RunNotes ↔ ADRs, RunNotes ↔ Requirements, RunNotes → Code, RunNotes → Tests

**Never create isolated documentation.**

### 7. Recommend Detailed Templates When Appropriate

When starting new work: Assess complexity, risk, and learning value. Recommend `-detailed` templates for:
- Complex/distributed systems work
- Performance-critical features
- Security-sensitive features
- Architectural decisions
- High technical debt risk

**Reference language-specific guides:** Point users to `README-TECHNICAL-PLANNING-CLOJURE.md` (or other language guides) during planning-detailed.

### 8. Enforce Human Review Gates in Technical Planning

When using `planning-detailed`: **STOP** at each 🚦 Human Review Gate. Prompt human to:
1. Review technical analysis and decisions
2. Validate trade-offs are understood
3. Approve or request changes
4. Check off gate boxes

**Only proceed after human approval.** Do not skip gates or proceed without explicit human sign-off.

---

## File Format Quick Reference

### File Naming

```
RunNotes-YYYY-MM-DD-TopicName-phase.md
```

Example: `RunNotes-2025-10-14-AuthRefactor-planning.md`

**Details:** [doc/README-FILE-FORMAT.md#file-naming-convention](doc/README-FILE-FORMAT.md#file-naming-convention)

### Metadata Structure

```edn
{:phase "implementation"
 :tag #{:authentication :api}
 :status :active
 :date {:created "2025-10-14" :updated "2025-10-15"}
 :related-documents
 {:runnote #{"RunNotes-2025-10-14-Topic-research"}
  :adr #{"ADR-00042"}
  :code-files #{"src/auth/middleware.py:45-67"}}}
```

**Required:** `:phase`, `:tag`, `:status`, `:date`

**Details:** [doc/README-FILE-FORMAT.md#edn-metadata-structure](doc/README-FILE-FORMAT.md#edn-metadata-structure)

---

## See Also

- **[README.md](README.md)** - Installation, configuration, usage (for humans)
- **[doc/README-PHASES.md](doc/README-PHASES.md)** - Complete phase documentation and templates
- **[doc/README-WORKFLOWS.md](doc/README-WORKFLOWS.md)** - Starting sessions, phase transitions, session management
- **[doc/README-INTEGRATION.md](doc/README-INTEGRATION.md)** - Integration with ADR, Requirements, Code, Tests
- **[doc/README-QUALITY.md](doc/README-QUALITY.md)** - Quality standards and common pitfalls
- **[doc/README-FILE-FORMAT.md](doc/README-FILE-FORMAT.md)** - File naming, metadata, EDN syntax
- **Templates:** `~/.runnote/template/` - Phase-specific session templates
- **Configuration:** `.runnote.edn` in project root and `~/.runnote/config.edn`
