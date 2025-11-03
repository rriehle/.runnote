# Implementation: [Topic] - YYYY-MM-DD HH:MM

```edn :metadata
{:phase "implementation"
 :tag #{:set-me}
 :status :active
 :thinking-mode "think hard"
 :complexity :medium
 :date {:created "YYYY-MM-DD"}}
```

## Implementation Plan

[Import from planning phase]

## Progress Tracker

### Current Work

- [ ] Task 1 [0%]----[50%]----[100%]
- [ ] Task 2 [0%]----[50%]----[100%]

## Architecture Checkpoint

### ADR Assessment (if ADR integration enabled)

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

Implementation details:

```[language]
// Current code being written
```

### [HH:MM] - Progress Update

State: 🟡 Investigating | 🔴 Blocked | 🟢 Active

**Progress**: [What's been completed]
**Challenges**: [Issues encountered]
**Next Steps**: [Immediate next actions]

## Blockers & Issues

### 🔴 BLOCKED - [Blocker Title]

**Issue**: [What's blocking progress]
**Attempted Solutions**: [What's been tried]
**Required to Unblock**: [What's needed]
**Impact**: [What can't proceed]

## Phase Transition Checklist

Ready for Review Phase when:

- [ ] All planned features implemented
- [ ] Tests passing (unit + integration)
- [ ] Documentation updated
- [ ] Code review completed
- [ ] Performance targets met
- [ ] ADRs created/updated (if applicable)
- [ ] No critical blockers remaining

**Next Phase**: Review (Estimated: [Date/Time])
