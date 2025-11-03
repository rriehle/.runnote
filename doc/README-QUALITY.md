# RunNotes Quality Enforcement

This document provides quality standards and common pitfalls for RunNotes. For a quick reference, see [CLAUDE.md](../CLAUDE.md).

## Table of Contents

- [Documentation Quality Standards](#documentation-quality-standards)
- [Failure Documentation Standards](#failure-documentation-standards)
- [Phase Discipline](#phase-discipline)
- [Common Pitfalls](#common-pitfalls)

---

## Documentation Quality Standards

RunNotes value depends on documentation quality. Enforce these standards:

### Timestamp Precision

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

### State Indicators

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

### Code Snippets with Language Identifiers

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

### Quantitative Metrics Over Feelings

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

### Cross-References with File:Line Format

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

---

## Failure Documentation Standards

Failed attempts are valuable negative knowledge. Document thoroughly:

### Hypothesis: What You Thought Would Work

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

### Time Investment Quantified

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

### Failure Mode: Exactly How It Failed

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

### Root Cause Analysis

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

### Prevention Strategies

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

### Salvageable Components

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

---

## Phase Discipline

Phase transitions must meet criteria. Don't skip or rush phases:

### Don't Skip Planning

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

### Update Implementation Logs Regularly

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

### Complete Review to Capture Learnings

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

### Meet Transition Criteria Before Advancing

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

---

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

---

## See Also

- [CLAUDE.md](../CLAUDE.md) - Quick reference for AI agents
- [README.md](../README.md) - Human-focused usage guide
- [README-PHASES.md](README-PHASES.md) - Detailed phase documentation
- [README-WORKFLOWS.md](README-WORKFLOWS.md) - Agent workflows
- [README-INTEGRATION.md](README-INTEGRATION.md) - Integration patterns
- [README-FILE-FORMAT.md](README-FILE-FORMAT.md) - File format specifications
