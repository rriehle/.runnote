# Code Review: [Topic] - YYYY-MM-DD HH:MM

```edn :metadata
{:phase "code-review"
 :tag #{:code-review :quality}
 :status :active
 :thinking-mode "think harder"
 :date {:created "YYYY-MM-DD"}}
```

## Review Objectives

**What this review validates**:

- [ ] Code implements stated intent from planning/implementation
- [ ] Changes align with architectural decisions
- [ ] Quality standards are met
- [ ] Security and performance implications considered

**Scope boundaries**:

- Modules affected: [List]
- Review depth: [Surface review | Deep dive | Architecture focus]
- Special concerns: [Any specific areas of focus]

## Architecture Context Review

### ADR Compliance Check (if ADR integration enabled)

```bash
# Search for relevant architectural decisions
adr-search content "[relevant keywords]"
adr-search tag :architecture
```

**Relevant ADRs**:

| ADR | Compliance | Notes |
|-----|------------|-------|
| [ADR-#####] | ✅/❌ | [Impact on this change] |

**Architectural Boundaries**:

- [ ] Module boundaries respected
- [ ] Interfaces cleanly defined
- [ ] Dependencies flow in correct direction
- [ ] No circular dependencies introduced

## Code Quality Assessment

### Structure & Organization

- **Rating**: [Excellent | Good | Needs Work | Poor]
- **Strengths**: [What's well organized]
- **Concerns**: [What needs improvement]

### Readability

- **Rating**: [Excellent | Good | Needs Work | Poor]
- **Clear Code**: [Examples of good clarity]
- **Confusing Code**: [What needs better naming/comments]

### Maintainability

- **Rating**: [Excellent | Good | Needs Work | Poor]
- **Future-Proof**: [What will age well]
- **Technical Debt**: [What will require future work]

## Security Review

**Security Implications**:

- [ ] Input validation present
- [ ] Authentication/authorization correct
- [ ] No sensitive data exposure
- [ ] Error messages don't leak information
- [ ] Dependencies are up to date and secure

**Security Concerns**:
- [List any security issues found]

## Performance Review

**Performance Implications**:

- [ ] No obvious inefficiencies
- [ ] Appropriate data structures used
- [ ] Resource cleanup handled
- [ ] Scalability considered

**Performance Concerns**:
- [List any performance issues]

## Testing Review

**Test Coverage**:

- [ ] Unit tests present and passing
- [ ] Integration tests cover workflows
- [ ] Edge cases tested
- [ ] Error conditions handled

**Test Quality**:

- Test clarity: [Clear | Needs work]
- Test maintainability: [Good | Needs work]
- Coverage gaps: [What's missing]

## Issues Found

### Critical Issues (Must Fix)

1. **[Issue]**: [Description]
   - Location: [File:line]
   - Impact: [Why critical]
   - Recommendation: [How to fix]

### Major Issues (Should Fix)

1. **[Issue]**: [Description]
   - Location: [File:line]
   - Impact: [Consequence]
   - Recommendation: [Suggestion]

### Minor Issues (Nice to Fix)

1. **[Issue]**: [Description]
   - Location: [File:line]
   - Recommendation: [Optional improvement]

## Positive Observations

**Well Done**:

- [Particularly good code/approach]
- [Clever solution]
- [Excellent documentation]

## Review Summary

**Overall Assessment**: [Approve | Approve with Comments | Request Changes | Reject]

**Confidence Level**: [High | Medium | Low]

**Key Takeaways**:

1. [Main point 1]
2. [Main point 2]

**Required Actions Before Merge**:

- [ ] [Action 1]
- [ ] [Action 2]

**Recommended Actions**:

- [ ] [Nice-to-have 1]
- [ ] [Nice-to-have 2]

## Reviewer Notes

**Review Time**: [Duration]
**Review Method**: [Manual | Automated Tools | Pair Review]
**Tools Used**: [Linters, static analysis, etc.]
