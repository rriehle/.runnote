# Performance: [Component/Operation] - YYYY-MM-DD HH:MM

```edn :metadata
{:phase "performance"
 :tag #{:performance :optimization}
 :status :active
 :thinking-mode "think harder"
 :date {:created "YYYY-MM-DD"}}
```

## Performance Objectives

What we're optimizing:

1. **Primary Metric**: [Response time | Throughput | Memory | CPU]
2. **Current**: [Baseline measurement]
3. **Target**: [Specific measurable goal]
4. **Constraints**: [What we can't compromise - correctness, features, etc.]

### Performance Architecture Context (if ADR integration enabled)

Review existing performance decisions: `adr-search tag :performance`

**Established performance patterns**:

- [Performance ADRs that affect this component]
- [Architectural constraints on optimization approaches]

## Initial Measurements

**Methodology**: [How measured - profiling tools, benchmarks]
**Environment**: [Test conditions]

| Metric | Current | Target | Gap |
|--------|---------|--------|-----|
| [Metric 1] | [Value] | [Goal] | [Delta] |

## Profiling Results

### Hotspots Identified

1. **[Function/Operation]**: [Time/resources consumed]
   - Cause: [Why slow]
   - Impact: [% of total]

## Optimization Strategies

### Approach: [Strategy Name]

**Theory**: [Why this should help]
**Implementation**: [What to change]
**Expected Improvement**: [Predicted gain]
**Risk**: [Potential downsides]

### Experiment: [Name]

**Change**: [What was modified]
**Result**: [Performance delta]
**Trade-off**: [What was sacrificed]
**Decision**: [Keep | Revert | Refine]

## Benchmark Results

**Before Optimization**:

```
[Benchmark output]
```

**After Optimization**:

```
[Benchmark output]
```

**Improvement**: [Percentage or absolute delta]

## Final Measurements

| Metric | Before | After | Improvement |
|--------|--------|-------|-------------|
| [Metric] | [Value] | [Value] | [%] |

## Lessons Learned

**Effective Techniques**: [What worked]
**Ineffective Attempts**: [What didn't help]
**Unexpected Findings**: [Surprises]
**Future Optimization Opportunities**: [What's left]

## Verification Checklist

- [ ] Targets met
- [ ] No functionality regression
- [ ] Performance sustained under load
- [ ] Benchmarks added to prevent regression
- [ ] Documentation updated with performance characteristics
