# Implementation: [Topic] - YYYY-MM-DD HH:MM

```edn :metadata
{:phase "implementation-detailed"
 :tag #{:set-me}
 :status :active
 :thinking-mode "think harder"
 :complexity :high
 :date {:created "YYYY-MM-DD"}}
```

## Implementation Plan

[Import from planning-detailed phase]

**Technical Decisions to Follow**:

- Data Structures: [From planning]
- Algorithms: [From planning]
- Concurrency: [From planning]
- Testing: [From planning]
- Security: [From planning]
- Performance: [From planning]

## Progress Tracker

### Current Work

- [ ] Task 1 [0%]----[50%]----[100%]
- [ ] Task 2 [0%]----[50%]----[100%]

## Technical Adherence Checkpoints

**Purpose**: Verify that implementation follows planning-detailed technical decisions. Log any deviations with rationale.

### Data Structures Adherence

**Planned Decision**: [Data structure selected in planning]

**Adherence Check**:

- [ ] Using planned data structure as designed
- [ ] Performance characteristics as expected
- [ ] No unexpected memory issues
- [ ] Concurrency behavior as planned

**Deviations** (if any):

| Deviation | Rationale | Impact | Approved By |
|-----------|-----------|--------|-------------|
| [What changed] | [Why changed] | [Effect on system] | [@human] |

**Notes**: [Implementation observations about data structure choice]

### Algorithm Adherence

**Planned Decision**: [Algorithm selected in planning]

**Adherence Check**:

- [ ] Implemented algorithm as designed
- [ ] Complexity matches expectations (O(?) time, O(?) space)
- [ ] Performance targets met (if benchmarked)
- [ ] Edge cases handled as planned

**Deviations** (if any):

| Deviation | Rationale | Impact | Approved By |
|-----------|-----------|--------|-------------|
| [What changed] | [Why changed] | [Effect on system] | [@human] |

**Notes**: [Implementation observations about algorithm choice]

### Concurrency Adherence

**Planned Decision**: [Concurrency model selected in planning]

**Adherence Check**:

- [ ] Using planned concurrency primitives (atoms, refs, channels, etc.)
- [ ] State coordination as designed
- [ ] Error handling as planned
- [ ] No unexpected race conditions or deadlocks observed

**Deviations** (if any):

| Deviation | Rationale | Impact | Approved By |
|-----------|-----------|--------|-------------|
| [What changed] | [Why changed] | [Effect on system] | [@human] |

**Notes**: [Implementation observations about concurrency model]

### Testing Adherence

**Planned Decision**: [Testing strategy from planning]

**Adherence Check**:

- [ ] Unit tests implemented as planned
- [ ] Property-based tests created (if planned)
- [ ] Integration tests implemented (if planned)
- [ ] Coverage goals met
- [ ] Performance benchmarks created (if planned)

**Test Status**:

- Unit tests: [Pass/Fail] - [Count] tests
- Property tests: [Pass/Fail] - [Count] properties
- Integration tests: [Pass/Fail] - [Count] scenarios
- Coverage: [Percentage]

**Deviations** (if any):

| Deviation | Rationale | Impact | Approved By |
|-----------|-----------|--------|-------------|
| [What changed] | [Why changed] | [Effect on testing] | [@human] |

**Notes**: [Implementation observations about testing approach]

### Security Adherence

**Planned Decision**: [Security controls from planning]

**Adherence Check**:

- [ ] Authentication implemented as planned (if applicable)
- [ ] Authorization enforced as designed (if applicable)
- [ ] Input validation comprehensive (schema/spec)
- [ ] Audit logging in place (if planned)
- [ ] Sensitive data protected as specified

**Security Validation**:

- [ ] Input validation tested with malformed data
- [ ] Authorization tested with unauthorized access attempts
- [ ] Audit logs verified for completeness
- [ ] No credentials or secrets in code

**Deviations** (if any):

| Deviation | Rationale | Impact | Approved By |
|-----------|-----------|--------|-------------|
| [What changed] | [Why changed] | [Security implication] | [@human] |

**Notes**: [Implementation observations about security controls]

### Performance Adherence

**Planned Decision**: [Performance strategy from planning]

**Adherence Check**:

- [ ] Performance targets met (if benchmarked)
- [ ] Optimizations applied as planned
- [ ] Type hints added to avoid reflection (if planned)
- [ ] Transients used for batch operations (if planned)
- [ ] No unexpected performance degradation

**Performance Measurements** (if available):

- Baseline: [Metric]
- Current: [Metric]
- Target: [Metric]
- Status: [Met/Not Met/Not Yet Measured]

**Deviations** (if any):

| Deviation | Rationale | Impact | Approved By |
|-----------|-----------|--------|-------------|
| [What changed] | [Why changed] | [Performance implication] | [@human] |

**Notes**: [Implementation observations about performance]

### Language Best Practices Adherence

**Planned Decision**: [Language/framework patterns from planning]

**Adherence Check**:

- [ ] Clojure idioms followed
- [ ] Namespace organization as designed
- [ ] Error handling approach consistent
- [ ] State management as planned
- [ ] Polylith boundaries respected (if applicable)

**Deviations** (if any):

| Deviation | Rationale | Impact | Approved By |
|-----------|-----------|--------|-------------|
| [What changed] | [Why changed] | [Code quality implication] | [@human] |

**Notes**: [Implementation observations about language practices]

## Architecture Checkpoint

### ADR Assessment

**ADR Validation Commands**:

```bash
# Verify no conflicts with existing decisions
adr-search tag :architecture
adr-search content "[implementation topic]"

# Check ADR validation
adr-validate
```

Before implementing:

- [ ] Searched for related ADRs to avoid conflicts
- [ ] Is this an architecturally significant decision? → Create ADR
- [ ] Does this change an existing architectural decision? → Update/supersede ADR
- [ ] Will this affect multiple components or modules? → Document in ADR

**ADR Creation Queue**: [List ADRs identified in planning that need creation]

- [ ] Create ADR for [Decision Title] with tags [:tag1, :tag2]

### Documentation Impact Assessment

Before implementing:

- [ ] Will this change affect module interfaces? → Update documentation
- [ ] Does this introduce new data flows? → Update diagrams
- [ ] Are there new architectural patterns? → Document in implementation section
- [ ] Will this impact system performance? → Update performance characteristics
- [ ] Creating new modules/packages? → Add documentation immediately

After implementing:

- [ ] Created/updated relevant ADRs (if applicable)
- [ ] Updated affected documentation with new diagrams
- [ ] Verified all diagrams still render correctly
- [ ] Updated architecture views if system boundaries changed
- [ ] Added documentation to all new modules

## Test Strategy & Test Design

### Test Approach Selection

Choose your testing approach based on implementation scope:

**Unit Tests**:

- [ ] Standard unit tests for individual functions/classes
- [ ] Property-based tests for complex logic
- [ ] Commands: [Project-specific test commands]

**Integration Tests**:

- [ ] Cross-module integration tests
- [ ] End-to-end workflows
- [ ] External system integration

**Test-Driven Development (TDD) Workflow**:

1. **Red** - Write failing test that captures desired behavior
2. **Green** - Write minimal code to pass the test
3. **Refactor** - Improve code while keeping tests green
4. **Repeat** - Continue in small increments

### Test Implementation Checklist

**Before coding**:

- [ ] Identified appropriate test location/framework
- [ ] Written failing tests that define expected behavior
- [ ] Validated test commands work

**During implementation**:

- [ ] Running tests frequently (ideally after each small change)
- [ ] Tests guide implementation decisions
- [ ] Refactoring with green tests for safety

**Property Testing** (for complex business logic):

- [ ] Consider property tests for data transformations
- [ ] Use when testing invariants across large input spaces

## Active Work Log

### [HH:MM] - Starting [Component/Feature]

State: 🟢 Active

**Objective**: [What you're trying to accomplish]
**Approach**: [How you're implementing it]
**Test Status**: [Red 🔴 | Green 🟢 | Refactor 🟡]
**Technical Decisions Being Applied**: [From planning-detailed]

Implementation details:

```[language]
// Current code being written
```

**Adherence Notes**: [How this follows or deviates from planning]

### [HH:MM] - Progress Update

State: 🟡 Investigating | 🔴 Blocked | 🟢 Active

**Progress**: [What's been completed]
**Challenges**: [Issues encountered]
**Technical Decisions Validated**: [Which planning decisions proven correct]
**Technical Decisions Questioned**: [Which planning decisions may need revision]
**Next Steps**: [Immediate next actions]

## Blockers & Issues

### 🔴 BLOCKED - [Blocker Title]

**Issue**: [What's blocking progress]
**Attempted Solutions**: [What's been tried]
**Required to Unblock**: [What's needed]
**Impact**: [What can't proceed]
**Technical Implication**: [How this affects planning decisions]

## Deviation Log

**Purpose**: Track when and why implementation deviates from planning-detailed technical decisions. All deviations must be human-approved.

| Timestamp | Technical Area | Original Plan | Actual Implementation | Rationale | Impact | Approved By |
|-----------|---------------|---------------|----------------------|-----------|--------|-------------|
| [HH:MM] | [Data/Algo/Concurrency/etc.] | [What was planned] | [What was done instead] | [Why changed] | [Effect] | [@human] |

**Note**: Major deviations may require:

1. Updating planning-detailed RunNote with lessons learned
2. Creating/updating ADR if architecturally significant
3. Updating related documentation

## Phase Transition Checklist

Ready for Review Phase when:

- [ ] All planned features implemented
- [ ] Tests passing (unit + integration)
- [ ] **Technical adherence checkpoints completed**
- [ ] **All deviations documented and approved**
- [ ] Performance targets met (or deviations explained)
- [ ] Security controls validated
- [ ] Documentation updated
- [ ] Code review completed
- [ ] ADRs created/updated (if applicable)
- [ ] No critical blockers remaining

**Next Phase**: Review-Detailed (Estimated: [Date/Time])

**Recommendation**: Use `runnote-launch review-detailed [Topic]` to extract technical decision outcomes and learnings.
