# Testing: [Feature/Component] - YYYY-MM-DD HH:MM

```edn :metadata
{:phase "testing"
 :tag #{:testing :quality}
 :status :active
 :thinking-mode "think hard"
 :date {:created "YYYY-MM-DD"}}
```

## Testing Objectives

What we aim to validate:

1. **Primary Behavior**: [Core functionality to test]
2. **Edge Cases**: [Boundary conditions]
3. **Error Conditions**: [Failure modes]
4. **Performance**: [Speed/resource targets]

### Testing Architecture Context (if ADR integration enabled)

Review testing patterns: `adr-search tag :testing`

**Established testing strategies**:
- [Testing ADRs that define approaches and tools]
- [Test organization patterns]

## Test Design

### Test Categories

**Unit Tests**:
- [ ] [Component 1] - [What's tested]
- [ ] [Component 2] - [What's tested]

**Integration Tests**:
- [ ] [Workflow 1] - [End-to-end scenario]
- [ ] [Workflow 2] - [Cross-module interaction]

**Property Tests** (if applicable):
- [ ] [Property 1] - [Invariant to verify]
- [ ] [Property 2] - [Relationship to maintain]

### Test Coverage Goals

- **Line Coverage**: [Target %]
- **Branch Coverage**: [Target %]
- **Edge Cases**: [Specific scenarios to cover]

## Test Implementation

### [HH:MM] - Unit Tests

**Module**: [What's being tested]
**Tests Written**: [Count]
**Coverage**: [Percentage]

```[language]
// Example test
```

### [HH:MM] - Integration Tests

**Scenario**: [What workflow is tested]
**Setup**: [Test environment]
**Assertions**: [What's verified]

## Test Results

### Passing Tests

- ✅ [Test category]: [Count passed]
- ✅ [Test category]: [Count passed]

### Failing Tests

- ❌ [Test name]: [Why failing]
  - Root cause: [Issue]
  - Fix: [What's needed]

### Performance Tests

| Operation | Target | Actual | Status |
|-----------|--------|--------|--------|
| [Op 1] | [Time] | [Time] | ✅/❌ |

## Coverage Analysis

**Overall Coverage**: [Percentage]

**Gaps Identified**:
- [Uncovered code path 1]
- [Uncovered code path 2]

**Rationale for Gaps**: [Why some code isn't covered]

## Quality Metrics

- **Test Count**: [Total tests]
- **Test Execution Time**: [Duration]
- **Flaky Tests**: [Count and reasons]
- **Mutation Testing**: [Score if applicable]

## Continuous Testing

- [ ] Tests added to CI/CD pipeline
- [ ] Pre-commit hooks configured
- [ ] Performance regression tests automated
- [ ] Test documentation updated

## Verification Checklist

- [ ] All acceptance criteria tested
- [ ] Edge cases covered
- [ ] Error handling verified
- [ ] Performance targets met
- [ ] Tests are maintainable and clear
- [ ] No flaky tests
- [ ] Test documentation complete
