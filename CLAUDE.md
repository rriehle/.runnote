# RunNotes - AI Agent Guide

This guide is for AI agents working with RunNotes tools. For human documentation, see [README.md](README.md).

## Philosophy and Design Principles

### Development as a Knowledge Extraction Journey

Development work is not just about producing code - it's about extracting maximum learning value from the journey. Every session, successful or failed, contains valuable insights that can improve future work. RunNotes captures this journey in real-time, preserving context that would otherwise be lost to memory decay and team turnover.

### Real-Time Documentation Over Retroactive Reconstruction

Writing documentation after the fact loses the most valuable information: the decision context, the alternatives considered but rejected, the false starts and why they failed. Real-time logging during active work captures:

- The actual sequence of investigation
- Dead ends and why they were abandoned
- Time invested in each approach
- Emotional state and energy level context
- Serendipitous discoveries
- Questions that emerged during exploration

Retroactive documentation is sanitized history. Real-time logging is authentic archaeology.

### Failure Documentation as First-Class Artifact

Failed attempts are not wasted time - they are negative knowledge that prevents future repetition. A well-documented failure is often more valuable than a simple success because it:

- Documents what NOT to do (negative space in the solution landscape)
- Explains why an obvious approach doesn't work (saves future investigation)
- Quantifies time investment (enables cost-benefit analysis)
- Identifies salvageable components (nothing is 100% waste)
- Reveals hidden constraints (failure exposes assumptions)

Document failures with the same rigor as successes.

### Four-Phase Discipline: Research, Planning, Implementation, Review

Development flows through distinct phases, each with different objectives and documentation patterns:

1. **Research**: Understand the problem space deeply before committing to approach
2. **Planning**: Evaluate alternatives and commit to specific approach with estimates
3. **Implementation**: Execute the plan with real-time logging of progress and obstacles
4. **Review**: Extract learnings, calculate metrics, identify patterns

Skipping phases (especially planning) leads to thrashing and rework. Phase discipline is enforced through transition criteria that must be met before advancing.

### Institutional Knowledge Building

RunNotes sessions accumulate into organizational memory. Patterns emerge across sessions:

- Common blockers and their resolutions
- Accurate time estimation for similar work
- Technology-specific gotchas
- Team velocity trends
- Knowledge gaps requiring training

Search across historical RunNotes enables pattern recognition impossible from individual sessions.

### ADR Integration: From Exploration to Decision

Planning phase RunNotes identify architectural decision candidates. When a decision is significant:

1. Research RunNotes document investigation and alternatives
2. Planning RunNotes evaluate approaches and trade-offs
3. ADR formalizes the decision with full context
4. Implementation RunNotes reference ADR during execution
5. Review RunNotes validate or challenge the decision

This creates an audit trail from exploration → decision → implementation → reflection.

## Tool Overview

### runnote-init

Initializes RunNotes directory structure in a project.

**When to use:**
- Setting up RunNotes in a new project
- Migrating from ad-hoc notes to structured RunNotes
- Configuring project-specific settings

**Key capabilities:**
- Creates RunNotes directory (configurable name: `runnote/` or `runnotes/`)
- Generates project `.runnote.edn` configuration
- Creates helpful README in RunNotes directory
- Interactive and non-interactive modes

**Reference:** See README.md section "Initialize New Project" for complete command documentation.

### runnote-launch

Creates a new RunNotes session for a specific phase and topic.

**When to use:**
- Starting any new development work
- Transitioning between phases (research → planning → implementation → review)
- Creating specialized phase sessions (debug, hotfix, performance, etc.)

**Key capabilities:**
- Template-based session creation
- Automatic metadata initialization
- Topic and tag customization
- Thinking mode specification
- Template hierarchy (project → user → built-in)
- Auto-opens session in configured editor

**Reference:** See README.md section "Create New RunNotes Session" for complete command documentation.

### runnote-search

Search and discovery tool for finding historical RunNotes sessions.

**When to use:**
- Finding similar past work before starting new session
- Discovering patterns across multiple sessions
- Locating documentation for specific features
- Generating project summaries
- Identifying tag taxonomy in use

**Key capabilities:**
- Search by tag, content, phase, or state
- List all tags in use
- Summary reports with statistics
- Multiple output formats (default, detailed, JSON)
- Full-text search across session content

**Reference:** See README.md section "Search Existing RunNotes" for complete command documentation.

## Phase-Specific Agent Behaviors

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

### Specialized Phases

#### Debug Phase

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

#### Hotfix Phase

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

#### Performance Phase

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

#### Security Phase

**When to use:**
- Security analysis and hardening
- Vulnerability assessment
- Security feature implementation

**Key characteristics:**
- Threat modeling
- Attack vector analysis
- Defense in depth documentation
- Compliance verification

#### Testing Phase

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

#### Code Review Phase

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

## Agent Workflows

### Starting a New Session

**Pre-flight checks:**

1. **Search for related historical sessions:**
   ```bash
   runnote-search content "similar-topic"
   runnote-search tag relevant-tag
   ```

   Learn from past similar work:
   - What approaches worked?
   - What blockers were encountered?
   - How long did similar work take?
   - What patterns emerged?

2. **Search for relevant ADRs:**
   ```bash
   adr-search content "topic"
   adr-search tag relevant-domain
   ```

   Understand architectural constraints:
   - What decisions constrain this work?
   - What patterns are established?
   - What technologies are standardized?

3. **Choose appropriate phase:**
   - **Research**: Unfamiliar problem, need to understand before planning
   - **Planning**: Problem understood, need to evaluate approaches
   - **Implementation**: Plan exists, ready to execute
   - **Review**: Work complete, need to extract learnings
   - **Specialized**: Debug, hotfix, performance, etc.

**Launch session:**

```bash
# Basic launch
runnote-launch <phase> <topic>

# With tags and thinking mode
runnote-launch planning DatabaseMigration --tags database,migration,planning --thinking-mode "think harder"
```

**Initialize session:**

1. **Review template sections** - Understand what needs to be documented
2. **Fill metadata** - Add appropriate tags, update status
3. **Import context** - Reference related RunNotes and ADRs
4. **Set objectives** - Define session goals
5. **Begin logging** - Start with initial timestamp and state

### Phase Transitions

Phases represent distinct mental modes. Transitioning requires checking criteria and carrying forward context.

#### Research → Planning Transition

**Check transition criteria:**
- [ ] Research questions answered comprehensively
- [ ] Key findings documented with evidence
- [ ] Risk factors identified
- [ ] ADR candidates identified
- [ ] Constraints and assumptions explicit

**Transition workflow:**

1. **Complete research session:**
   - Final update with all findings
   - Update status to "completed"
   - Tag appropriately

2. **Extract planning context:**
   - Key findings summary
   - Identified alternatives
   - Constraints list
   - Risk factors
   - ADR candidates

3. **Launch planning session:**
   ```bash
   runnote-launch planning SameTopic --tags tag1,tag2
   ```

4. **Import context into planning session:**
   ```markdown
   ## Context from Research

   Per RunNotes-2025-10-14-Topic-research:
   - Finding 1: [Summary with link to research section]
   - Finding 2: [Summary]
   - Risk identified: [Description]

   Alternatives to evaluate:
   - Alternative A (pros/cons from research)
   - Alternative B (pros/cons from research)
   ```

5. **Begin planning work:**
   - Evaluate alternatives with research context
   - Make architectural decisions
   - Create implementation plan
   - Generate estimates

#### Planning → Implementation Transition

**Check transition criteria:**
- [ ] Approach selected and documented
- [ ] Implementation plan with time estimates
- [ ] Success criteria defined
- [ ] Risk mitigation documented
- [ ] Dependencies identified
- [ ] Required ADRs created

**Transition workflow:**

1. **Complete planning session:**
   - Final decision documented
   - Implementation plan complete
   - Estimates recorded
   - Update status to "completed"

2. **Create any required ADRs:**
   - For significant decisions
   - Link ADRs in planning session metadata
   - Reference planning RunNotes in ADR

3. **Extract implementation context:**
   - Selected approach and rationale
   - Implementation phases
   - Time estimates per phase
   - Success criteria
   - Risk mitigation strategies

4. **Launch implementation session:**
   ```bash
   runnote-launch implementation SameTopic --tags tag1,tag2
   ```

5. **Import planning context:**
   ```markdown
   ## Implementation Plan

   Per RunNotes-2025-10-14-Topic-planning and ADR-00042:

   **Approach**: [Decision from planning]
   **Rationale**: [Why this approach]
   **Success Criteria**: [From planning]

   ### Phase 1: [Name] (Est: Xh)
   - Task details from planning

   ### Phase 2: [Name] (Est: Yh)
   - Task details from planning
   ```

6. **Begin implementation:**
   - Start first phase
   - Begin real-time logging
   - Track time against estimates

#### Implementation → Review Transition

**Check transition criteria:**
- [ ] All phases completed OR explicitly deferred
- [ ] Tests passing OR test plan documented
- [ ] Code committed OR WIP documented
- [ ] Documentation updated
- [ ] Handoff notes if incomplete

**Transition workflow:**

1. **Complete implementation session:**
   - Final progress update
   - Document final state
   - Capture any incomplete work
   - Update status (completed/deferred/blocked)

2. **Extract review context:**
   - Time tracked per phase
   - Original estimates
   - Blockers encountered
   - Deviations from plan
   - Outcomes achieved

3. **Launch review session:**
   ```bash
   runnote-launch review SameTopic --tags tag1,tag2
   ```

4. **Calculate metrics:**
   - Estimated vs actual time per phase
   - Total duration
   - Velocity metrics (if applicable)
   - Efficiency rate

5. **Conduct DAKI analysis:**
   - What to drop/add/keep/improve
   - Reference specific examples from implementation

6. **Extract learnings:**
   - Technical insights
   - Process insights
   - Pattern recognition

7. **Generate next steps:**
   - Immediate follow-ups
   - Backlog items
   - Improvement opportunities

### Active Session Management

During active work (especially implementation), maintain session hygiene:

#### Update Frequency Guidelines

**Every 30-60 minutes during focused work:**
```markdown
### [HH:MM] - [Activity]
State: 🟢 Active
**Objective**: Implementing authentication middleware
**Approach**: Using passport.js library per ADR-00042
**Result**: Basic middleware working, testing with mock users
**Time**: 45 minutes
**Next**: Integrate with real user database
Progress: [====>     ] 40%
```

**Immediately when hitting blockers:**
```markdown
### [HH:MM] - BLOCKED: Database Connection Failing
State: 🔴 Blocked
**Blocker**: PostgreSQL connection timeout in test environment
**Impact**: Cannot test authentication flow
**Investigation**: Checked connection string, verified database running
**Time Lost**: 30 minutes so far
**Mitigation**: Switching to sqlite for local testing while investigating
```

**After completing each planned phase:**
```markdown
### [HH:MM] - Phase 1 Complete: Authentication Middleware
State: 🟢 Active
**Objective**: Complete authentication middleware implementation
**Result**: ✅ Middleware implemented and tested
**Time**: 2.5 hours (estimated: 2 hours)
**Variance**: +25% due to passport configuration issues
**Next**: Begin Phase 2 - Authorization layer
Progress: [======>   ] 60%
```

**Before context switches or breaks:**
```markdown
### [HH:MM] - Context Switch: Breaking for Lunch
State: 🟡 Paused
**Current**: Implementing role-based authorization
**Progress**: 70% through Phase 2
**Next**: Complete admin role checking logic
**Blocker**: None, work proceeding normally
**Estimated Resume**: 13:00
```

**End of work session:**
```markdown
### [HH:MM] - End of Session
State: 🟡 Paused
**Today's Progress**: Completed Phase 1 and Phase 2, started Phase 3
**Total Time**: 5.5 hours (estimated: 6 hours)
**Next Session**: Complete Phase 3 authorization tests
**Handoff**: All code committed to feature branch, tests passing
```

#### State Tracking Best Practices

**State indicators communicate session health:**

- 🟢 **Active**: Work proceeding as planned
  - Making steady progress
  - No unexpected blockers
  - Following plan

- 🟡 **Investigating**: Researching unexpected issue
  - Hit unexpected problem
  - Researching solution
  - Still making progress but slower
  - May need to adjust plan

- 🔴 **Blocked**: Cannot proceed
  - External dependency (waiting for API access, PR review, etc.)
  - Technical blocker (can't solve without help)
  - Decision needed (architectural choice required)
  - Requires plan revision or escalation

**Update state whenever it changes, not just during regular updates.**

#### Progress Tracking

**Use visual progress indicators:**

```markdown
Progress: [          ] 0%   - Just started
Progress: [==>       ] 20%  - Initial setup
Progress: [====>     ] 40%  - Core functionality
Progress: [======>   ] 60%  - Testing
Progress: [========> ] 80%  - Refinement
Progress: [==========] 100% - Complete
```

**Track progress at phase level and overall:**

```markdown
## Overall Progress

Phase 1: Authentication [==========] 100% ✅ Complete
Phase 2: Authorization  [======>   ] 60% 🟢 Active
Phase 3: Audit Logging  [          ] 0% ⏸️ Pending

Overall: [=====>    ] 53%
```

### Session Search and Discovery

RunNotes value compounds through search and pattern recognition.

#### Effective Search Strategies

**1. Find similar past work:**
```bash
# Content-based search
runnote-search content "authentication"
runnote-search content "database migration"

# Tag-based search
runnote-search tag :authentication
runnote-search tag :database

# Phase-specific search
runnote-search phase implementation
runnote-search phase review

# State-based search
runnote-search state active  # Find active sessions
runnote-search state blocked # Find blocked sessions needing attention
```

**2. Discover tag taxonomy:**
```bash
# See all tags in use
runnote-search list-tags

# Understand tag usage patterns
# (Review output to maintain consistent taxonomy)
```

**3. Generate project summaries:**
```bash
# High-level summary
runnote-search summary

# Detailed format
runnote-search summary --format detailed

# JSON for processing
runnote-search summary --format json
```

#### Pattern Extraction Across Sessions

**When searching, look for patterns:**

1. **Time estimation accuracy:**
   - How long did similar work actually take?
   - What factors caused estimation errors?
   - Are certain types of work consistently over/underestimated?

2. **Common blockers:**
   - What blockers recur across sessions?
   - How were they resolved?
   - Can we prevent them?

3. **Successful approaches:**
   - What patterns consistently work?
   - What tools/techniques are effective?
   - What processes yield good outcomes?

4. **Knowledge gaps:**
   - What areas consistently require research?
   - What expertise is missing?
   - What training is needed?

5. **Technology-specific insights:**
   - What gotchas exist with specific tools?
   - What best practices have emerged?
   - What anti-patterns should be avoided?

**Document patterns in review sessions:**

```markdown
## Pattern Recognition

**Pattern**: Database migration sessions consistently take 2x estimated time
**Evidence**:
  - RunNotes-2025-09-15-UserTableMigration: 4h actual vs 2h estimated
  - RunNotes-2025-10-01-OrderTableMigration: 6h actual vs 3h estimated
  - RunNotes-2025-10-14-ProductTableMigration: 5h actual vs 2.5h estimated

**Root Cause**: Estimates don't account for data validation and testing time

**Improvement**: For future migrations, estimate 2x base time for validation/testing
```

#### Tag Strategy

**Maintain consistent tag taxonomy:**

**Domain tags** (what area):
- `:authentication`, `:authorization`, `:database`, `:api`, `:ui`, `:testing`

**Technology tags** (what tech):
- `:postgres`, `:react`, `:python`, `:docker`, `:kubernetes`

**Type tags** (what kind of work):
- `:feature`, `:bugfix`, `:refactor`, `:performance`, `:security`

**Phase tags** (automatically added):
- `:research`, `:planning`, `:implementation`, `:review`, `:debug`, etc.

**Project tags** (configured in `.runnote.edn`):
- Project-specific domain tags

**Cross-cutting tags:**
- `:blocker`, `:learning`, `:pattern`, `:failure`, `:success`

**Tag consistently to enable discovery:**
- Use existing tags when appropriate (search first)
- Create new tags when needed (document in project)
- Avoid tag proliferation (merge similar tags)

## Integration Patterns

### With ADR Tools

RunNotes and ADRs work together to document the journey from exploration to decision.

#### Identifying ADR Candidates

During research and planning phases, identify decisions that need formal documentation:

**ADR candidate indicators:**
- Decision affects multiple components
- Decision is expensive to reverse
- Decision establishes or changes pattern
- Decision involves technology adoption
- Decision constrains future choices
- Decision will be questioned later ("why did we do this?")

**Document in RunNotes:**

```yaml
adr-candidates:
  - Use event sourcing for audit trail
    Rationale: Regulatory compliance requires complete history
    Impact: Changes data model and storage architecture

  - Adopt GraphQL for API layer
    Rationale: Frontend needs flexible queries
    Impact: New technology adoption, team learning curve
```

#### Linking RunNotes to ADRs

**In planning RunNotes metadata:**
```edn
{:related-documents
 {:adr #{"ADR-00042-event-sourcing"
         "ADR-00043-graphql-api"}}}
```

**In planning RunNotes content:**
```markdown
## Architectural Decisions

### Event Sourcing for Audit Trail
Decision documented in ADR-00042 (link: doc/adr/00042-event-sourcing.md)

Per ADR-00042, all state changes will be persisted as events to maintain complete audit trail for regulatory compliance.
```

**In ADR metadata:**
```edn
{:runnotes #{"RunNotes-2025-10-14-AuditTrail-research"
             "RunNotes-2025-10-14-AuditTrail-planning"}}
```

**In ADR content:**
```markdown
## Context

Investigation documented in RunNotes-2025-10-14-AuditTrail-research identified need for complete state change history for regulatory compliance.

Planning session RunNotes-2025-10-14-AuditTrail-planning evaluated three approaches: audit table, CDC, and event sourcing.
```

#### Workflow: RunNotes → ADR → RunNotes

**Phase 1: Research (RunNotes)**
```bash
runnote-launch research AuditTrail
```

Document:
- Business need (regulatory compliance)
- Technical investigation (approaches: audit table, CDC, event sourcing)
- Trade-offs of each approach
- Identify as ADR candidate

**Phase 2: Planning (RunNotes)**
```bash
runnote-launch planning AuditTrail
```

Document:
- Evaluation of alternatives
- Decision: event sourcing
- Rationale and trade-offs
- Mark for ADR creation

**Phase 3: Create ADR**
```bash
cp ~/.adr/template/default.md doc/adr/00042-event-sourcing-audit-trail.md
```

- Import context from research and planning RunNotes
- Formalize decision
- Document consequences
- Link back to RunNotes in metadata

**Phase 4: Implementation (RunNotes)**
```bash
runnote-launch implementation AuditTrail
```

Document:
- Reference ADR-00042 in implementation notes
- Track adherence to architectural decision
- Document any implementation learnings
- Note if ADR needs updates based on implementation reality

**Phase 5: Review (RunNotes)**
```bash
runnote-launch review AuditTrail
```

Document:
- Validate decision (was ADR-00042 right?)
- Document implementation learnings
- Identify if ADR needs updates
- Capture patterns for future similar work

### With Requirements Tools

Requirements specify WHAT to build; RunNotes document WHY and HOW.

#### Eliciting Requirements from Planning RunNotes

Planning phase RunNotes contain raw material for requirements:

**In planning RunNotes:**
```yaml
objectives:
  - Enable customer service to process refunds
  - Support 30-day refund window
  - Integrate with payment gateway
  - Maintain audit trail of all refund operations

success-criteria:
  - Refunds processed in <24 hours
  - Full audit trail for compliance
  - Integration with Stripe payment gateway
  - Support for full and partial refunds
```

**Extract to formal requirements:**
- REQ-REFUND-001: System MUST calculate refund amount
- REQ-REFUND-002: System MUST verify 30-day window
- REQ-REFUND-003: System MUST integrate with Stripe
- REQ-REFUND-NFR-001: System MUST complete refunds within 24 hours

**Link requirements to planning RunNotes:**
```edn
{:trace {:runnote #{"RunNotes-2025-10-14-RefundProcess-planning"}}}
```

**Link planning RunNotes to requirements:**
```edn
{:related-documents
 {:requirements #{"REQ-REFUND-001"
                  "REQ-REFUND-002"
                  "REQ-REFUND-003"
                  "REQ-REFUND-NFR-001"}}}
```

#### Linking Implementation to Requirements

During implementation, track which requirements are being fulfilled:

**In implementation RunNotes:**
```markdown
### [HH:MM] - Implementing Refund Calculation
Implements: REQ-REFUND-001

**Approach**: Creating RefundCalculator class per planning session
**Progress**: Basic calculation logic complete
**Testing**: Unit tests passing for simple cases
**Next**: Handle partial refunds and tax calculations
```

**Update requirements with implementation links:**
```edn
{:trace {:code #{"src/refunds/calculator.py:45-67"}
         :runnote #{"RunNotes-2025-10-14-RefundProcess-implementation"}}}
```

#### Workflow: Business Need → Requirements → Implementation

**Phase 1: Planning RunNotes**
- Document business need
- Define objectives and success criteria
- Identify functional and non-functional needs
- Create implementation plan

**Phase 2: Requirements Extraction**
- Create formal requirements from planning notes
- Link requirements to planning RunNotes
- Define acceptance criteria
- Link to relevant ADRs (architectural constraints)

**Phase 3: Implementation RunNotes**
- Reference requirements in implementation logs
- Track implementation against acceptance criteria
- Document deviations with rationale
- Link code to requirements

**Phase 4: Review RunNotes**
- Verify requirements met
- Document any gaps or deferred work
- Update requirements status
- Identify new requirements from implementation learnings

### With Code and Tests

RunNotes provide context for code changes and test strategies.

#### Linking Code to RunNotes

**In code comments:**
```python
# Implements event sourcing for audit trail
# See: RunNotes-2025-10-14-AuditTrail-implementation
# Architecture: ADR-00042
class EventStore:
    """
    Event sourcing implementation per ADR-00042.

    Context: RunNotes-2025-10-14-AuditTrail-research identified
    need for complete state change history for regulatory compliance.
    """
    pass
```

**In commit messages:**
```
feat: implement event sourcing for audit trail

Implements event sourcing architecture per ADR-00042.

Context: RunNotes-2025-10-14-AuditTrail-implementation
Requirements: REQ-AUDIT-001, REQ-AUDIT-002

See: doc/adr/00042-event-sourcing-audit-trail.md
See: runnote/RunNotes-2025-10-14-AuditTrail-implementation.md
```

**In RunNotes metadata:**
```edn
{:related-documents
 {:code-files #{"src/events/store.py"
                "src/events/replay.py"}
  :commits #{"a1b2c3d4"}}}
```

#### Test Strategy Documentation

**In planning RunNotes:**
```markdown
## Test Strategy

### Unit Testing
- Event store persistence (100% coverage target)
- Event replay logic (property-based testing)
- Event serialization/deserialization

### Integration Testing
- End-to-end event recording and replay
- Database transaction boundaries
- Concurrency handling

### Performance Testing
- Event write throughput (target: 1000 events/sec)
- Replay performance (target: complete in <5min for 1M events)
```

**In implementation RunNotes:**
```markdown
### [HH:MM] - Test Implementation
**Testing**: Event store persistence tests
**Coverage**: 95% (missing edge case: concurrent writes)
**Results**: All tests passing
**Next**: Add property-based tests for replay logic
```

**Link to test files:**
```edn
{:related-documents
 {:test-files #{"test/events/test_store.py"
                "test/events/test_replay.py"}}}
```

## Quality Enforcement

### Documentation Quality Standards

RunNotes value depends on documentation quality. Enforce these standards:

#### Timestamp Precision

**Format: HH:MM (24-hour)**

✅ Correct:
```markdown
### 14:30 - Implementing authentication
### 09:15 - Blocker: Database connection failed
```

❌ Incorrect:
```markdown
### 2:30pm - Implementing authentication  (use 24-hour)
### Morning - Started work               (too vague)
### Around 3 - Hit blocker               (be precise)
```

#### State Indicators

**Use emoji indicators consistently:**

✅ Correct:
```markdown
State: 🟢 Active
State: 🟡 Investigating
State: 🔴 Blocked
```

❌ Incorrect:
```markdown
State: Active         (missing emoji)
State: Working on it  (vague, no standard indicator)
State: ⚠️ Problem    (non-standard emoji)
```

#### Code Snippets with Language Identifiers

**Always specify language for syntax highlighting:**

✅ Correct:
````markdown
```python
def calculate_refund(order):
    return order.total
```

```bash
npm install --save express
```
````

❌ Incorrect:
````markdown
```
def calculate_refund(order):  # No language specified
    return order.total
```
````

#### Quantitative Metrics Over Feelings

**Use numbers, not adjectives:**

✅ Correct:
```markdown
**Time**: 45 minutes
**Performance**: 150ms → 45ms (70% improvement)
**Coverage**: 65% → 82% (+17 percentage points)
**Velocity**: 250 lines of code in 3 hours
```

❌ Incorrect:
```markdown
**Time**: A while
**Performance**: Much faster now
**Coverage**: Pretty good
**Velocity**: Made good progress
```

#### Cross-References with File:Line Format

**Link to specific locations:**

✅ Correct:
```markdown
**Implementation**: src/auth/middleware.py:45-67
**Test**: test/auth/test_middleware.py:23-45
**Related**: RunNotes-2025-10-14-Authentication-planning.md
**Decision**: doc/adr/00042-passport-authentication.md
```

❌ Incorrect:
```markdown
**Implementation**: auth middleware file
**Test**: in the test directory
**Related**: other auth notes
**Decision**: see ADR about auth
```

### Failure Documentation Standards

Failed attempts are valuable negative knowledge. Document thoroughly:

#### Hypothesis: What You Thought Would Work

**Be specific about assumptions:**

✅ Correct:
```markdown
**Hypothesis**: Connection pooling with 50 max connections will handle 1000 req/sec

**Reasoning**:
- Average request takes 50ms
- 50ms * 50 connections = 1000 requests/sec theoretical throughput
- Assumed negligible connection overhead
```

❌ Incorrect:
```markdown
**Hypothesis**: Thought connection pooling would make it faster
```

#### Time Investment Quantified

**Track time spent on failed approach:**

✅ Correct:
```markdown
**Time Investment**:
- Initial research: 30 minutes
- Implementation attempt: 2 hours
- Debugging: 1.5 hours
- Total: 4 hours invested before abandoning approach
```

❌ Incorrect:
```markdown
**Time**: Spent a while on this
```

#### Failure Mode: Exactly How It Failed

**Describe specific failure with evidence:**

✅ Correct:
```markdown
**Failure Mode**: Connection pool exhaustion under load

**Evidence**:
- Load test showed 500 req/sec max (not 1000 target)
- Error logs: "PoolExhausted: All 50 connections in use"
- Response time degraded from 50ms to 2000ms at 500 req/sec
- CPU utilization only 30% (not CPU bound)
```

❌ Incorrect:
```markdown
**Failure**: Didn't work, got errors
```

#### Root Cause Analysis

**Explain WHY it failed:**

✅ Correct:
```markdown
**Root Cause**:
Database query optimization was the actual bottleneck, not connection count.

Each request held connection for 200ms on average (not 50ms as assumed) due to unoptimized queries with N+1 problem.

50 connections * 5 req/sec/connection = 250 req/sec max throughput

Connection pooling couldn't help because query time was the limiting factor.
```

❌ Incorrect:
```markdown
**Root Cause**: Connection pooling was wrong approach
```

#### Prevention Strategies

**How to avoid this in future:**

✅ Correct:
```markdown
**Prevention**:
1. Profile queries BEFORE optimizing connection pooling
2. Measure actual request duration, don't assume
3. Use APM tooling to identify real bottleneck
4. Load test incremental changes to validate hypotheses
5. For database performance: optimize queries first, scale connections second
```

❌ Incorrect:
```markdown
**Prevention**: Don't use connection pooling
```

#### Salvageable Components

**Nothing is 100% waste:**

✅ Correct:
```markdown
**Salvageable**:
- Connection pool configuration is sound (can keep current settings)
- Load testing infrastructure is valuable for future optimization
- Learned pg_stat_statements for query profiling
- Database monitoring dashboard created is reusable
- Identified N+1 query problem (separate fix)
```

❌ Incorrect:
```markdown
**Salvageable**: Nothing, complete waste
```

### Phase Discipline

Phase transitions must meet criteria. Don't skip or rush phases:

#### Don't Skip Planning

**Anti-pattern:**
```markdown
# RunNotes-2025-10-14-Feature-implementation

[Start coding without research or planning]
```

**Why it's wrong:**
- No understanding of problem space
- No evaluation of alternatives
- No time estimates
- No success criteria
- High risk of thrashing and rework

**Correct pattern:**
```bash
# 1. Research (if unfamiliar)
runnote-launch research Feature

# 2. Planning (always)
runnote-launch planning Feature

# 3. Implementation (after planning complete)
runnote-launch implementation Feature
```

#### Update Implementation Logs Regularly

**Anti-pattern:**
```markdown
### 09:00 - Started work
...
[6 hours of silence]
...
### 15:00 - Finished feature
```

**Why it's wrong:**
- Lost context (what happened in those 6 hours?)
- No failure documentation
- No blocker tracking
- Can't learn from the journey

**Correct pattern:**
```markdown
### 09:00 - Started Phase 1: Authentication middleware
State: 🟢 Active

### 10:15 - Blocker: Passport configuration unclear
State: 🔴 Blocked
Time: 1.25 hours (0.25 hours blocked)

### 10:45 - Resolved: Found example in docs
State: 🟢 Active
Time: 1.75 hours

### 12:00 - Phase 1 complete, starting Phase 2
State: 🟢 Active
Time: 3 hours

### 13:30 - Testing authorization logic
State: 🟡 Investigating (edge case in role checking)
Time: 4.5 hours

### 14:30 - Tests passing
State: 🟢 Active
Time: 5.5 hours

### 15:00 - Phase 2 complete
State: ✅ Complete
Time: 6 hours
```

#### Complete Review to Capture Learnings

**Anti-pattern:**
```markdown
# Just tag implementation session as "done" and move on
# No review, no metrics, no DAKI, no pattern extraction
```

**Why it's wrong:**
- Learnings lost
- Can't improve estimation
- Patterns not identified
- Knowledge not shared
- Failure ROI not captured

**Correct pattern:**
```bash
runnote-launch review Feature
```

Then:
- Calculate all metrics
- Compare to estimates
- Document failures and learnings
- Extract patterns
- Generate actionable next steps
- Tag for discoverability

#### Meet Transition Criteria Before Advancing

**Anti-pattern:**
```markdown
# Research phase with unanswered questions
## Research Questions
1. How does authentication work? [Partially investigated]
2. What are the security requirements? [Unknown]
3. Performance requirements? [Need to ask product team]

# But transition to planning anyway
```

**Why it's wrong:**
- Planning without complete understanding
- Decisions made with missing information
- Risk of rework when gaps discovered
- Low-quality plan

**Correct pattern:**

Check transition criteria:
- [ ] Research questions answered ❌ (Questions 2 and 3 unanswered)
- [ ] Findings documented ✅
- [ ] Constraints identified ❌ (Security requirements unknown)

**Action**: Don't transition yet. Complete research:
- Ask product team about security and performance requirements
- Document answers
- Update findings
- Then transition to planning

## Common Pitfalls

### Avoid: Skipping Phases (Especially Planning)

**Symptom**: Jumping straight from idea to implementation

**Problem**:
- No evaluation of alternatives
- No time estimates
- Thrashing during implementation
- Rework and wasted effort

**Solution**:
```bash
# Always follow phase discipline
runnote-launch research Topic    # If unfamiliar
runnote-launch planning Topic    # Always
runnote-launch implementation Topic
```

### Avoid: Retroactive Documentation (Losing Context)

**Symptom**: Writing notes hours or days after the work

**Problem**:
- Context lost to memory decay
- Sanitized history (forget failures)
- Missing timestamps and actual timeline
- Vague descriptions
- Lost decision rationale

**Solution**:
- Update every 30-60 minutes during active work
- Document in real-time, not retrospectively
- If forced to document retroactively, mark it as such:
  ```markdown
  **Note**: This section documented retroactively on [date].
  Some context may be lost or approximate.
  ```

### Avoid: Vague Descriptions Lacking Specifics

**Symptom**: Generic statements without evidence

❌ Vague:
```markdown
### Morning - Worked on feature
Made some progress. Hit some issues but figured them out.
Feature is mostly working now.
```

✅ Specific:
```markdown
### 09:30 - Implementing OAuth2 callback handler
State: 🟢 Active
**Approach**: Using passport-oauth2 library per ADR-00042
**File**: src/auth/oauth.js:45-78
**Progress**: Callback handler implemented, basic flow working

### 11:15 - Blocker: Token validation failing
State: 🔴 Blocked
**Error**: "Invalid signature" from token verification
**Investigation**: Token uses RS256 but config expected HS256
**Resolution**: Updated config to use correct algorithm
**Time Lost**: 45 minutes
**File**: config/auth.json:15

### 12:00 - OAuth flow complete and tested
State: ✅ Complete
**Result**: Full OAuth2 flow working with Google
**Tests**: test/auth/oauth.test.js:23-67 all passing
**Coverage**: 95% (missing edge case: expired tokens)
```

### Avoid: Missing Timestamps or Progress Updates

**Symptom**: Long stretches without entries

❌ Missing timestamps:
```markdown
# Started work
...
[Hours of silence]
...
# Finished
```

✅ Regular timestamps:
```markdown
### 09:00 - Session start
### 10:30 - Progress update
### 12:00 - Blocker encountered
### 12:45 - Blocker resolved
### 14:00 - Phase complete
### 15:30 - Session end
```

**Rule**: Update at least every 60 minutes during active work

### Avoid: No Links to Related RunNotes or ADRs

**Symptom**: Orphaned sessions with no context connections

❌ Isolated:
```markdown
# RunNotes-2025-10-14-Feature-implementation

[No references to research, planning, ADRs, or requirements]
```

✅ Connected:
```markdown
# RunNotes-2025-10-14-Feature-implementation

## Context

Per RunNotes-2025-10-14-Feature-research and RunNotes-2025-10-14-Feature-planning:

Implementing event sourcing approach per ADR-00042.

Requirements: REQ-AUDIT-001, REQ-AUDIT-002

Related sessions:
- Research: RunNotes-2025-10-14-Feature-research
- Planning: RunNotes-2025-10-14-Feature-planning
- ADR: doc/adr/00042-event-sourcing
```

```edn :metadata
{:phase "implementation"
 :related-documents
 {:runnote #{"RunNotes-2025-10-14-Feature-research"
             "RunNotes-2025-10-14-Feature-planning"}
  :adr #{"ADR-00042"}
  :requirements #{"REQ-AUDIT-001" "REQ-AUDIT-002"}}}
```

### Avoid: Abandoned Sessions Without Handoff Notes

**Symptom**: Work stops but session has no closure or next steps

❌ Abandoned:
```markdown
### 14:30 - Hit blocker with database connection
State: 🔴 Blocked

[Session never updated again]
```

**Problem**:
- Future you (or teammates) don't know what happened
- Context lost
- Work may be duplicated
- Blockers not escalated

✅ Proper handoff:
```markdown
### 14:30 - Hit blocker with database connection
State: 🔴 Blocked
**Blocker**: PostgreSQL connection timeout in production
**Investigation**: Checked connection pool settings, logs show pool exhaustion
**Root Cause**: Not yet determined, may be connection leak

### 15:00 - End of session (work incomplete)
State: 🔴 Blocked on external dependency

**Current Progress**: 60% complete (Phases 1-2 done, Phase 3 blocked)

**Blocker Summary**:
- PostgreSQL connection pool exhaustion in production
- Needs DBA investigation of connection patterns
- May need connection leak fix before proceeding
- Escalated to @database-team in #engineering Slack

**Next Steps for Resume**:
1. Wait for DBA analysis results
2. If connection leak: fix in Phase 3
3. If configuration issue: adjust pool settings and proceed
4. Complete Phase 3 after blocker resolved

**Handoff**: All code committed to feature branch `feature/audit-trail`
Tests passing for completed phases. See blocker tracking issue #1234.
```

### Avoid: Inconsistent Tags

**Symptom**: Tag proliferation and inconsistent naming

❌ Inconsistent:
```markdown
# Session 1
:tag #{:auth :Authentication :user-auth}

# Session 2
:tag #{:authentication :login :AuthN}

# Session 3
:tag #{:auth-system :user-authentication}
```

**Problem**:
- Cannot find related sessions
- Tag search ineffective
- No standard taxonomy

✅ Consistent:
```markdown
# Establish project taxonomy in .runnote.edn
{:project-tags #{:authentication :authorization :database :api}}

# Use consistently across sessions
# Session 1
:tag #{:authentication :planning}

# Session 2
:tag #{:authentication :implementation}

# Session 3
:tag #{:authentication :review}
```

**Guidelines**:
1. Search existing tags before creating new ones: `runnote-search list-tags`
2. Use lowercase keywords
3. Use singular form (`:database` not `:databases`)
4. Be specific but not overly granular
5. Document project taxonomy in `.runnote.edn`

## File Format Reference

### RunNotes File Naming

**Format:**
```
RunNotes-YYYY-MM-DD-TopicName-phase.md
```

**Examples:**
```
RunNotes-2025-10-14-AuthRefactor-research.md
RunNotes-2025-10-14-AuthRefactor-planning.md
RunNotes-2025-10-14-AuthRefactor-implementation.md
RunNotes-2025-10-14-AuthRefactor-review.md
RunNotes-2025-10-14-LoginBug-debug.md
RunNotes-2025-10-14-SecurityPatch-hotfix.md
```

**Rules:**
- Date: YYYY-MM-DD format (ISO 8601)
- Topic: PascalCase, descriptive but concise
- Phase: lowercase, one of the standard phases

**Standard phases:**
- `research`, `planning`, `implementation`, `review`
- `debug`, `hotfix`, `performance`, `security`, `testing`, `code-review`

### EDN Metadata Structure

**Basic structure:**
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

**Required fields:**
- `:phase` - String, one of standard phases
- `:tag` - Set of keywords, at least one tag
- `:status` - Keyword, `:active`, `:paused`, `:completed`, `:blocked`
- `:date` - Map with `:created` at minimum

**Optional standard fields:**
- `:thinking-mode` - String, Claude thinking mode
- `:date {:updated "YYYY-MM-DD"}` - Last update date
- `:related-documents` - Map of document type to sets of filenames

**Related documents structure:**
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

**YAML frontmatter fields (in templates):**
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

### EDN Syntax Quick Reference

**Maps:**
```edn
{:key1 "value1"
 :key2 42}
```

**Keywords:**
```edn
:phase :active :authentication
```

**Strings:**
```edn
"2025-10-14"
"think hard"
"RunNotes-2025-10-14-Topic-research"
```

**Sets:**
```edn
#{:tag1 :tag2 :tag3}
#{"file1.md" "file2.md"}
```

**Nested maps:**
```edn
{:date {:created "2025-10-14"
        :updated "2025-10-15"}
 :related-documents {:adr #{"ADR-00042"}}}
```

For complete specifications, see [README.md section "File Naming Convention" and "Metadata Format"](README.md#file-naming-convention).

## Agent Responsibilities

When working with RunNotes, AI agents MUST:

### 1. Prompt for Updates Every 30-60 Minutes During Active Work

**During implementation phase:**
- After 30-60 minutes: "It's been [X] minutes since last RunNotes update. Let's log current progress."
- Prompt even if user doesn't mention it
- Capture: current activity, state, time, progress, next steps

### 2. Validate Phase Transition Criteria Before Advancing

**Before transitioning phases:**
- Review transition criteria checklist
- Ask user to confirm all criteria met
- Don't advance if criteria not met
- Document incomplete criteria and plan to complete

**Example prompt:**
```
Before transitioning to planning, let's verify research is complete:
- [ ] Research questions answered?
- [ ] Key findings documented?
- [ ] Risks identified?
- [ ] ADR candidates identified?

I see we still have unanswered question about performance requirements.
Should we complete that research first?
```

### 3. Extract Patterns from Historical RunNotes

**When starting new work:**
- Search for similar past sessions
- Extract relevant learnings
- Apply patterns (time estimates, approaches, blockers)
- Reference historical sessions in new session

**Example:**
```
I searched historical RunNotes and found 3 similar authentication sessions:
- RunNotes-2025-09-15-OAuth-implementation: Took 8h (estimated 4h) due to token validation issues
- RunNotes-2025-10-01-SSO-implementation: Took 6h (estimated 5h), smooth process
- RunNotes-2025-10-05-JWT-implementation: Took 10h (estimated 4h) due to library incompatibilities

Pattern: Authentication work averages 2x initial estimates.
Recommend: Estimate 8h for this similar work.
```

### 4. Identify ADR Candidates

**During research and planning:**
- Watch for architecturally significant decisions
- Flag decisions as ADR candidates
- Document in RunNotes
- Recommend creating ADRs for significant decisions

**Example:**
```
This decision to use event sourcing appears to be architecturally significant:
- Affects data model across multiple components
- Expensive to reverse
- Establishes pattern for audit trail

Recommendation: Create ADR-00042 to document this decision formally.
Should I help create that ADR now?
```

### 5. Ensure Complete Context Capture

**In every session:**
- Link to related RunNotes
- Link to relevant ADRs
- Reference requirements
- Document assumptions
- Include specific file paths and line numbers
- Capture decision rationale
- Explain "why" not just "what"

### 6. Link Related Documentation Obsessively

**Maintain bidirectional links:**
- RunNotes ↔ RunNotes (research → planning → implementation → review)
- RunNotes ↔ ADRs (planning ↔ decisions)
- RunNotes ↔ Requirements (planning ↔ specifications)
- RunNotes → Code (implementation → files)
- RunNotes → Tests (implementation → verification)

**Never create isolated documentation.**

## Decision Framework

When managing RunNotes, agents MUST always ask:

### 1. Is This Work in the Correct Phase?

**Check:**
- Are we researching? → Research phase
- Are we planning? → Planning phase
- Are we implementing? → Implementation phase
- Are we reflecting? → Review phase
- Are we debugging? → Debug phase
- Is this urgent? → Hotfix phase

**If wrong phase:**
- Don't force it
- Transition to correct phase
- Import relevant context

### 2. Has Sufficient Context Been Captured?

**Check:**
- Can future developer understand why?
- Are assumptions explicit?
- Are alternatives documented?
- Are trade-offs explained?
- Are sources cited?
- Are related docs linked?

**If insufficient:**
- Ask clarifying questions
- Request more detail
- Prompt for assumptions
- Request sources

### 3. Are Phase Transition Criteria Met?

**Before any phase transition:**
- Review criteria checklist for current phase
- Verify all items completed
- Document incomplete items
- Get user confirmation

**If not met:**
- Don't transition
- Identify gaps
- Plan to complete

### 4. What Patterns Are Emerging?

**Regularly check:**
- Search for similar past sessions
- Compare time estimates vs actuals
- Identify recurring blockers
- Note successful patterns
- Document anti-patterns

**Extract and apply patterns:**
- Improve estimates
- Avoid known pitfalls
- Reuse successful approaches
- Share patterns in review

### 5. Which Decisions Need Elevation to ADRs?

**Watch for:**
- Architecturally significant decisions
- Technology adoption choices
- Pattern establishment
- Expensive-to-reverse decisions
- Multi-component impacts

**When identified:**
- Flag as ADR candidate
- Document in RunNotes
- Recommend formal ADR creation
- Link when ADR created

### 6. Is Learning Value Being Maximized?

**Check:**
- Are failures documented thoroughly?
- Are hypotheses captured before testing?
- Is time investment tracked?
- Are root causes identified?
- Are salvageable components noted?
- Are preventions documented?

**If learning underutilized:**
- Prompt for failure documentation
- Ask about hypotheses
- Request time tracking
- Dig for root causes
- Find salvageable value

---

**For human documentation:** See [README.md](README.md)

**For templates:** See `~/.runnote/template/`

**For tool documentation:** See individual tool help (`runnote-init --help`, etc.)

**For configuration:** See `.runnote.edn` in project root and `~/.runnote/config.edn`
