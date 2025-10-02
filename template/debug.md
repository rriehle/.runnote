# Debug: [Issue] - YYYY-MM-DD HH:MM

```edn :metadata
{:phase "debug"
 :tag #{:debugging :bugfix}
 :status :active
 :thinking-mode "think hard"
 :date {:created "YYYY-MM-DD"}}
```

## Problem Statement

**Symptom**: [What is visibly wrong - error messages, incorrect behavior]
**Context**: [When/where does it occur - specific conditions]
**Impact**: [Who/what is affected and how severely]
**Reproduction Rate**: [Always | Often | Sometimes | Once]

### Architectural Context Review (if ADR integration enabled)

Check relevant architectural decisions: `adr-search content "[component/feature name]"`

**Known architectural constraints affecting this area**:
- [Any relevant ADR decisions that might explain the behavior]
- [Architectural patterns this component follows]

## Reproduction Steps

1. [Exact step to reproduce]
2. [Expected vs actual behavior]

## Investigation Log

### [HH:MM] - Initial Investigation

**Hypothesis**: [What might be causing this]
**Evidence**: [What supports this theory]
**Test**: [How to verify]
**Result**: [What happened]

### [HH:MM] - Deep Dive

**Found**: [Specific discovery]
**Location**: [File:line or module]
**Root Cause**: [Why this is happening]

## Experiments Conducted

### Experiment: [Name]

**Hypothesis**: [What we're testing]
**Method**: [How we tested]
**Result**: [What we found]
**Conclusion**: [What this means]

## Solution

**Fix**: [What needs to change]
**Rationale**: [Why this fixes it]
**Side Effects**: [What else this might affect]
**Testing**: [How to verify the fix]

## Prevention

**Root Cause Category**: [Code | Design | Testing | Documentation]
**Prevention Strategy**: [How to avoid similar issues]
**Detection Strategy**: [How to catch this earlier]

## Resolution Checklist

- [ ] Fix implemented
- [ ] Tests added to prevent regression
- [ ] Documentation updated
- [ ] Related code reviewed for similar issues
- [ ] Fix verified in production-like environment
