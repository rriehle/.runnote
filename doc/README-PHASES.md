# RunNotes Phase Documentation

This document provides detailed documentation for all RunNotes phases. For a quick reference, see [CLAUDE.md](../CLAUDE.md).

## Table of Contents

- [Core Phases](#core-phases)
  - [Research Phase](#research-phase)
  - [Planning Phase](#planning-phase)
  - [Implementation Phase](#implementation-phase)
  - [Review Phase](#review-phase)
- [Specialized Phases](#specialized-phases)
  - [Debug Phase](#debug-phase)
  - [Hotfix Phase](#hotfix-phase)
  - [Performance Phase](#performance-phase)
  - [Security Phase](#security-phase)
  - [Testing Phase](#testing-phase)
  - [Code Review Phase](#code-review-phase)

---

## Core Phases

### Research Phase

**Objective:** Deep exploration and problem understanding before committing to any approach.

**When to use:**
- Investigating unfamiliar problem domains
- Evaluating new technologies
- Understanding legacy systems
- Diagnosing complex bugs
- Exploring architectural options

**Documentation pattern:**

```markdown
## Findings Log

### [HH:MM] - Investigation Topic
**Method**: How investigation was conducted (searched docs, read code, ran experiments)
**Findings**: Specific discoveries with evidence
**Implications**: What this means for potential solutions
**New Questions**: What this investigation revealed we don't know
**Confidence**: High/Medium/Low confidence in findings
```

**Agent responsibilities:**
- Document ALL findings, even tangential or "obvious" ones
- Record exact sources (URLs, file paths with line numbers, specific documentation sections)
- Capture hypotheses BEFORE testing them
- Document what DOESN'T work as thoroughly as what does
- Challenge assumptions explicitly
- Identify constraints and risks
- Update every 30-60 minutes during active investigation
- Ask clarifying questions to expose tacit knowledge

**State indicators:**
- 🟢 Active investigation
- 🟡 Investigating/blocked on external dependency
- 🔴 Blocked on missing information

**Transition criteria to Planning:**
- [ ] Research questions answered comprehensively
- [ ] Key findings documented with specific evidence
- [ ] Risk factors identified and assessed
- [ ] Alternative approaches identified
- [ ] Constraints and assumptions documented
- [ ] ADR candidates identified
- [ ] No critical unknowns remain (or unknowns are acknowledged)

**Red flags:**
- Vague findings like "seems to work" (need specifics)
- No sources cited (where did this information come from?)
- Jumping to solution before understanding problem
- No alternative approaches considered
- Assumptions not explicitly stated

---

### Planning Phase

**Objective:** Evaluate alternatives and commit to specific approach with realistic estimates.

**When to use:**
- After completing research (import context from research session)
- Before starting any significant implementation
- When re-planning after major blocker or pivot
- For sprint/iteration planning

**Documentation pattern:**

```markdown
## Architecture Decision - [Topic]

**Context**: Forces and constraints at play (from research + ADRs)
**Decision**: What we're choosing to do (specific, not vague)
**Rationale**: Why this approach over alternatives
**Consequences**:
  - Positive: Benefits and what this enables
  - Negative: Costs, limitations, and risks
**Alternatives Considered**: Other options and specific reasons for rejection

## Implementation Plan

### Phase 1: [Name] (Estimated: X hours)
- Task 1 (0.5h)
- Task 2 (1h)
- Risk: [Potential blocker and mitigation]

### Phase 2: [Name] (Estimated: Y hours)
...

**Total Estimate**: [Sum] hours
**Confidence**: High/Medium/Low with reasoning
```

**Agent responsibilities:**
- Search existing ADRs BEFORE making decisions (`adr-search content "topic"`)
- Reference relevant ADRs in context section
- Identify ADR candidates (significant decisions needing formal documentation)
- Document trade-offs explicitly (every decision has costs)
- Create time estimates broken down by phase
- Identify dependencies and sequencing constraints
- Document assumptions behind estimates
- Define success criteria
- Create risk mitigation strategies

**Transition criteria to Implementation:**
- [ ] Approach validated and specifically documented
- [ ] Implementation broken into phases with time estimates
- [ ] Success criteria defined and measurable
- [ ] Risk mitigation strategies documented
- [ ] Dependencies identified
- [ ] Required ADRs created or scheduled
- [ ] Planning reviewed (by team or self-review if solo)

**Red flags:**
- No alternatives considered (really just one way?)
- Only positive consequences listed (every decision has trade-offs)
- Vague estimates ("a few hours", "not long")
- No breakdown of implementation phases
- No success criteria
- Assumptions buried in prose instead of explicit list
- Skipped searching existing ADRs

---

### Implementation Phase

**Objective:** Execute the plan with real-time logging of progress, obstacles, and adaptations.

**When to use:**
- Active development work
- During implementation of planned features
- When executing on agreed approach

**Documentation pattern:**

```markdown
### [HH:MM] - Activity Description
State: [🟢 Active | 🟡 Investigating | 🔴 Blocked]
**Objective**: What trying to accomplish right now
**Approach**: How attempting it (specific technique/method)
**Result**: What actually happened (success, failure, partial)
**Time Investment**: X minutes
**Next**: Immediate next step (concrete action)
Progress: [=====>    ] 50% (of current phase)

### [HH:MM] - Blocker/Failure Documentation
**Hypothesis**: What I thought would work
**Time Investment**: X minutes/hours invested
**Failure Mode**: Exactly how it failed (error messages, unexpected behavior)
**Root Cause**: Why it failed (after analysis)
**Prevention**: How to avoid this in future
**Salvageable**: What components/knowledge can be reused
**New Approach**: What trying instead
```

**Agent responsibilities:**
- Update session every 30-60 minutes during active work
- Use precise timestamps (HH:MM format)
- Document state changes (Active → Investigating → Blocked)
- Track time investment per activity
- Document failures as thoroughly as successes
- Include code snippets with language identifiers
- Reference files with specific line numbers
- Update progress indicators
- Document deviations from plan with rationale
- Capture questions that arise during implementation
- Link to related ADRs and other RunNotes

**Update frequency:**
- 30-60 minutes during focused work
- Immediately when hitting blockers
- After completing each planned phase
- Before context switches or breaks
- End of work session (even if incomplete)

**State management:**
- 🟢 **Active**: Making progress on planned work
- 🟡 **Investigating**: Researching unexpected issue or blocker
- 🔴 **Blocked**: Cannot proceed, external dependency or decision needed

**Transition criteria to Review:**
- [ ] All planned implementation phases completed OR explicitly deferred with reason
- [ ] Tests passing (or test plan documented if not yet implemented)
- [ ] Code committed or work-in-progress documented
- [ ] Documentation updated (README, API docs, etc.)
- [ ] Handoff notes complete if work stopping before completion
- [ ] Time investment tracked for all major activities

**Red flags:**
- Long gaps without updates (>2 hours with no log entry)
- Vague progress descriptions ("made progress", "worked on feature")
- No timestamps or inconsistent time tracking
- Failed attempts not documented
- State indicators missing or not updated
- No links to code files
- Progress bars not updated
- Deviations from plan not explained

---

### Review Phase

**Objective:** Extract learnings, calculate metrics, identify patterns, and generate actionable next steps.

**When to use:**
- After completing implementation (successful or abandoned)
- End of sprint/iteration
- Post-mortem after incidents
- Periodic retrospectives

**Documentation pattern:**

```markdown
## Session Metrics

**Total Duration**: X hours (across all phases)
  - Research: Y hours
  - Planning: Z hours
  - Implementation: W hours

**Efficiency Rate**: Planned vs Actual
  - Estimated: X hours
  - Actual: Y hours
  - Variance: +/-Z% (explain if >20% variance)

**Velocity Metrics**:
  - Code: X lines added/modified
  - Tests: Y test cases added
  - Coverage Delta: Before% → After%

**Knowledge Artifacts Created**:
  - ADRs: [List]
  - Requirements: [List]
  - Documentation: [List]

## DAKI Analysis

### Drop
- What should we STOP doing?
- What practices/approaches wasted time?
- What tools/techniques didn't help?

### Add
- What should we START doing?
- What practices would have helped?
- What tools/techniques should we adopt?

### Keep
- What's working well?
- What practices/approaches were effective?
- What tools/techniques should continue?

### Improve
- What could be BETTER?
- What practices need refinement?
- What processes need adjustment?

## Key Learnings

### Technical Insights
- [Specific technical discovery]
- [Pattern or anti-pattern identified]
- [Technology-specific gotcha]

### Process Insights
- [Estimation accuracy lessons]
- [Phase discipline observations]
- [Communication or coordination insights]

### Pattern Recognition
- [Similar to previous session X]
- [Recurring blocker Y]
- [Common success factor Z]

## Next Steps

### Immediate (this week)
- [ ] Action item 1
- [ ] Action item 2

### Short-term (this month)
- [ ] Backlog item 1
- [ ] Improvement opportunity 1

### Long-term (this quarter)
- [ ] Strategic initiative 1
- [ ] Technical debt item 1
```

**Agent responsibilities:**
- Calculate actual vs estimated time for all phases
- Identify patterns by comparing to similar historical sessions
- Facilitate DAKI analysis (Drop/Add/Keep/Improve)
- Extract specific learnings (avoid vague statements)
- Quantify outcomes with metrics
- Document failure ROI (what we learned from failures)
- Generate actionable next steps
- Identify ADR candidates from significant decisions
- Tag session for future discoverability
- Update related documentation with learnings

**Metrics to calculate:**
- Time efficiency (estimated vs actual)
- Code velocity (if applicable)
- Test coverage delta (if applicable)
- Blocker count and resolution time
- Phase duration distribution
- Knowledge artifacts produced

**DAKI facilitation:**
- Ask probing questions to extract insights
- Challenge vague statements ("be more specific")
- Cross-reference with historical sessions
- Identify recurring patterns (positive and negative)
- Force prioritization (top 3 in each category)

**Completion criteria:**
- [ ] All metrics calculated and documented
- [ ] DAKI analysis complete with specifics
- [ ] Key learnings extracted (technical and process)
- [ ] Next steps actionable and prioritized
- [ ] Session tagged appropriately for discovery
- [ ] Related RunNotes/ADRs cross-referenced
- [ ] Session archived in appropriate location

**Red flags:**
- No metrics ("it went fine")
- Vague learnings ("learned a lot")
- No DAKI analysis
- No next steps
- Missing time tracking data
- No comparison to estimates
- No pattern recognition across sessions

---

## Specialized Phases

### Debug Phase

**When to use:**
- Systematic debugging of complex issues
- Bug investigation and root cause analysis
- Production incident response

**Key sections:**
```markdown
## Problem Statement
[Precise description of bug/issue]

## Investigation Log
### [HH:MM] - Hypothesis Testing
**Hypothesis**: [What you think is causing it]
**Test**: [How you tested the hypothesis]
**Result**: [Confirmed/Refuted with evidence]
**Next Hypothesis**: [If refuted]

## Root Cause
[Definitive cause once found]

## Fix Applied
[Specific fix with rationale]
```

**Documentation emphasis:**
- Systematic hypothesis testing
- Evidence-based investigation
- Clear root cause identification
- Fix validation

---

### Hotfix Phase

**When to use:**
- Urgent production fixes
- Critical security patches
- Time-sensitive bug fixes

**Key characteristics:**
- Compressed timeline (hours not days)
- Minimal scope (fix only what's broken)
- Risk mitigation (rollback plan documented)
- Fast-track review process

**Documentation emphasis:**
- What broke and impact
- Minimal viable fix
- Testing performed (even if abbreviated)
- Rollback procedure
- Follow-up work needed

**Template sections:**
```markdown
## Incident Summary
**Impact**: Who/what is affected
**Severity**: Critical/High/Medium
**Discovery**: How issue was found
**Timeline**: When it started

## Minimal Viable Fix
**Approach**: Smallest change to resolve
**Risk Assessment**: What could go wrong
**Rollback Plan**: How to revert if needed

## Validation
**Testing**: How fix was verified
**Deployment**: Steps taken
**Monitoring**: What to watch

## Follow-up Work
**Root Cause**: Deep investigation needed?
**Proper Fix**: Better long-term solution?
**Prevention**: Process improvements?
```

---

### Performance Phase

**When to use:**
- Performance optimization work
- Scalability improvements
- Resource utilization analysis

**Key sections:**
```markdown
## Baseline Metrics
[Current performance measurements]

## Target Metrics
[Desired performance goals]

## Optimization Attempts
### [HH:MM] - Optimization: [Name]
**Hypothesis**: [Expected improvement]
**Change**: [Specific modification]
**Result**: [Actual impact with measurements]
**Kept/Reverted**: [Decision and rationale]

## Final Results
[Before/after comparison]
```

**Documentation emphasis:**
- Baseline measurement before changes
- Hypothesis-driven optimization
- Quantified results for each attempt
- Keep/revert decisions with rationale
- Final before/after comparison

---

### Security Phase

**When to use:**
- Security analysis and hardening
- Vulnerability assessment
- Security feature implementation

**Key characteristics:**
- Threat modeling
- Attack vector analysis
- Defense in depth documentation
- Compliance verification

**Template sections:**
```markdown
## Security Analysis
**Scope**: What is being analyzed
**Threat Model**: Potential attack vectors
**Risk Assessment**: Likelihood and impact

## Findings
### [Severity] - [Finding Name]
**Description**: What vulnerability exists
**Attack Scenario**: How it could be exploited
**Impact**: What damage could occur
**Mitigation**: How to address

## Hardening Measures
**Implemented**: Security improvements made
**Validation**: How effectiveness was verified
**Compliance**: Standards/regulations addressed
```

---

### Testing Phase

**When to use:**
- Test strategy planning
- Test suite implementation
- Test coverage analysis

**Key sections:**
- Test strategy and approach
- Coverage goals
- Test case design
- Execution results
- Gap analysis

**Template sections:**
```markdown
## Test Strategy
**Scope**: What is being tested
**Approach**: Testing methodology
**Types**: Unit/Integration/E2E/Performance
**Coverage Goals**: Target percentages

## Test Cases
### [Test Category]
- **TC-001**: Test case description
  - Setup: Prerequisites
  - Steps: Actions to perform
  - Expected: What should happen
  - Actual: What did happen

## Results
**Execution**: Pass/Fail counts
**Coverage**: Achieved percentages
**Gaps**: What's not covered
**Issues**: Bugs found during testing
```

---

### Code Review Phase

**When to use:**
- Structured code review documentation
- Pre-merge review sessions
- Architectural review

**Key sections:**
- Review scope and objectives
- Findings by category (critical/major/minor/nit)
- Patterns observed
- Recommendations
- Follow-up actions

**Template sections:**
```markdown
## Review Scope
**Code**: What is being reviewed
**Author**: Who wrote it
**Purpose**: What it's supposed to do
**Context**: Related ADRs/Requirements

## Findings

### Critical Issues
- **C-001**: [Issue requiring immediate fix]

### Major Issues
- **M-001**: [Significant concern to address]

### Minor Issues
- **m-001**: [Small improvement suggestion]

### Nits
- **N-001**: [Style/preference items]

## Patterns Observed
**Positive**: Good practices to encourage
**Negative**: Anti-patterns to avoid

## Recommendations
**Approve**: With/without changes
**Request Changes**: List required fixes
**Comments**: Optional improvements

## Follow-up
- [ ] Action item 1
- [ ] Action item 2
```

---

## See Also

- [CLAUDE.md](../CLAUDE.md) - Quick reference for AI agents
- [README.md](../README.md) - Human-focused usage guide
- [README-WORKFLOWS.md](README-WORKFLOWS.md) - Agent workflows
- [README-INTEGRATION.md](README-INTEGRATION.md) - Integration patterns
- [README-QUALITY.md](README-QUALITY.md) - Quality enforcement guidelines
- [README-FILE-FORMAT.md](README-FILE-FORMAT.md) - File format specifications
