# RunNotes Agent Workflows

This document provides detailed workflows for AI agents working with RunNotes. For a quick reference, see [CLAUDE.md](../CLAUDE.md).

## Table of Contents

- [Starting a New Session](#starting-a-new-session)
- [Phase Transitions](#phase-transitions)
- [Active Session Management](#active-session-management)
- [Session Search and Discovery](#session-search-and-discovery)

---

## Starting a New Session

### Pre-flight Checks

**1. Search for related historical sessions:**
```bash
runnote-search content "similar-topic"
runnote-search tag relevant-tag
```

Learn from past similar work:
- What approaches worked?
- What blockers were encountered?
- How long did similar work take?
- What patterns emerged?

**2. Search for relevant ADRs:**
```bash
adr-search content "topic"
adr-search tag relevant-domain
```

Understand architectural constraints:
- What decisions constrain this work?
- What patterns are established?
- What technologies are standardized?

**3. Choose appropriate phase:**
- **Research**: Unfamiliar problem, need to understand before planning
- **Planning**: Problem understood, need to evaluate approaches
- **Implementation**: Plan exists, ready to execute
- **Review**: Work complete, need to extract learnings
- **Specialized**: Debug, hotfix, performance, etc.

### Launch Session

```bash
# Basic launch
runnote-launch <phase> <topic>

# With tags and thinking mode
runnote-launch planning DatabaseMigration --tags database,migration,planning --thinking-mode "think harder"
```

### Initialize Session

1. **Review template sections** - Understand what needs to be documented
2. **Fill metadata** - Add appropriate tags, update status
3. **Import context** - Reference related RunNotes and ADRs
4. **Set objectives** - Define session goals
5. **Begin logging** - Start with initial timestamp and state

---

## Phase Transitions

Phases represent distinct mental modes. Transitioning requires checking criteria and carrying forward context.

### Research → Planning Transition

**Check transition criteria:**
- [ ] Research questions answered comprehensively
- [ ] Key findings documented with evidence
- [ ] Risk factors identified
- [ ] ADR candidates identified
- [ ] Constraints and assumptions explicit

**Transition workflow:**

**1. Complete research session:**
- Final update with all findings
- Update status to "completed"
- Tag appropriately

**2. Extract planning context:**
- Key findings summary
- Identified alternatives
- Constraints list
- Risk factors
- ADR candidates

**3. Launch planning session:**
```bash
runnote-launch planning SameTopic --tags tag1,tag2
```

**4. Import context into planning session:**
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

**5. Begin planning work:**
- Evaluate alternatives with research context
- Make architectural decisions
- Create implementation plan
- Generate estimates

---

### Planning → Implementation Transition

**Check transition criteria:**
- [ ] Approach selected and documented
- [ ] Implementation plan with time estimates
- [ ] Success criteria defined
- [ ] Risk mitigation documented
- [ ] Dependencies identified
- [ ] Required ADRs created

**Transition workflow:**

**1. Complete planning session:**
- Final decision documented
- Implementation plan complete
- Estimates recorded
- Update status to "completed"

**2. Create any required ADRs:**
- For significant decisions
- Link ADRs in planning session metadata
- Reference planning RunNotes in ADR

**3. Extract implementation context:**
- Selected approach and rationale
- Implementation phases
- Time estimates per phase
- Success criteria
- Risk mitigation strategies

**4. Launch implementation session:**
```bash
runnote-launch implementation SameTopic --tags tag1,tag2
```

**5. Import planning context:**
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

**6. Begin implementation:**
- Start first phase
- Begin real-time logging
- Track time against estimates

---

### Implementation → Review Transition

**Check transition criteria:**
- [ ] All phases completed OR explicitly deferred
- [ ] Tests passing OR test plan documented
- [ ] Code committed OR WIP documented
- [ ] Documentation updated
- [ ] Handoff notes if incomplete

**Transition workflow:**

**1. Complete implementation session:**
- Final progress update
- Document final state
- Capture any incomplete work
- Update status (completed/deferred/blocked)

**2. Extract review context:**
- Time tracked per phase
- Original estimates
- Blockers encountered
- Deviations from plan
- Outcomes achieved

**3. Launch review session:**
```bash
runnote-launch review SameTopic --tags tag1,tag2
```

**4. Calculate metrics:**
- Estimated vs actual time per phase
- Total duration
- Velocity metrics (if applicable)
- Efficiency rate

**5. Conduct DAKI analysis:**
- What to drop/add/keep/improve
- Reference specific examples from implementation

**6. Extract learnings:**
- Technical insights
- Process insights
- Pattern recognition

**7. Generate next steps:**
- Immediate follow-ups
- Backlog items
- Improvement opportunities

---

## Active Session Management

During active work (especially implementation), maintain session hygiene.

### Update Frequency Guidelines

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

### State Tracking Best Practices

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

### Progress Tracking

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

---

## Session Search and Discovery

RunNotes value compounds through search and pattern recognition.

### Effective Search Strategies

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

### Pattern Extraction Across Sessions

**When searching, look for patterns:**

**1. Time estimation accuracy:**
- How long did similar work actually take?
- What factors caused estimation errors?
- Are certain types of work consistently over/underestimated?

**2. Common blockers:**
- What blockers recur across sessions?
- How were they resolved?
- Can we prevent them?

**3. Successful approaches:**
- What patterns consistently work?
- What tools/techniques are effective?
- What processes yield good outcomes?

**4. Knowledge gaps:**
- What areas consistently require research?
- What expertise is missing?
- What training is needed?

**5. Technology-specific insights:**
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

### Tag Strategy

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

---

## See Also

- [CLAUDE.md](../CLAUDE.md) - Quick reference for AI agents
- [README.md](../README.md) - Human-focused usage guide
- [README-PHASES.md](README-PHASES.md) - Detailed phase documentation
- [README-INTEGRATION.md](README-INTEGRATION.md) - Integration patterns
- [README-QUALITY.md](README-QUALITY.md) - Quality enforcement guidelines
- [README-FILE-FORMAT.md](README-FILE-FORMAT.md) - File format specifications
