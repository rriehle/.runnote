# Review: [Topic] - YYYY-MM-DD HH:MM

```edn :metadata
{:phase "review-detailed"
 :tag #{:set-me}
 :status :active
 :thinking-mode "think harder"
 :date {:created "YYYY-MM-DD"}}
```

## Objectives vs. Outcomes

| Original Objective | Outcome | Status |
|--------------------|---------|--------|
| [Objective 1] | [What happened] | ✅/⚠️/❌ |

## Session Metrics

- **Duration**: [Total time spent] across [phases completed]
- **Efficiency**: [Planned vs actual time]
- **Quality**: [Bugs found] | [Test coverage] | [Code review feedback]
- **Velocity**: [Work completed vs estimated]
- **Learning**: [New patterns discovered] | [Skills developed]

## Technical Decision Outcomes

**Purpose**: Extract learnings about what technical decisions worked, what didn't, and why. Feed patterns back into future planning.

### Data Structures Outcome

**Planned Decision**: [From planning-detailed]

**Implementation Reality**:

- Actual structure used: [What was implemented]
- Deviations: [Any changes from plan]
- Performance observed: [Actual performance characteristics]

**What Worked Well**:

- [Specific aspect that was successful]
- [Why it worked - evidence]

**What Didn't Work / Challenges**:

- [Specific aspect that was problematic]
- [Why it was problematic - evidence]
- [How it was addressed]

**Lessons Learned**:

- [Pattern to remember for future similar problems]
- [Anti-pattern to avoid]
- [Estimation accuracy: overestimated/underestimated complexity]

**Feed Forward**: [Update to README-TECHNICAL-PLANNING-CLOJURE.md or planning patterns]

### Algorithm Outcome

**Planned Decision**: [From planning-detailed]

**Implementation Reality**:

- Actual algorithm used: [What was implemented]
- Deviations: [Any changes from plan]
- Complexity observed: [Actual time/space complexity]
- Performance measured: [Benchmarks if available]

**What Worked Well**:

- [Specific aspect that was successful]
- [Why it worked - evidence]

**What Didn't Work / Challenges**:

- [Specific aspect that was problematic]
- [Why it was problematic - evidence]
- [How it was addressed]

**Lessons Learned**:

- [Algorithm selection insights]
- [Clojure-specific implementation insights]
- [When this algorithm is/isn't appropriate]

**Feed Forward**: [Update to README-TECHNICAL-PLANNING-CLOJURE.md or planning patterns]

### Concurrency Outcome

**Planned Decision**: [From planning-detailed]

**Implementation Reality**:

- Actual concurrency model used: [Atoms, refs, channels, etc.]
- Deviations: [Any changes from plan]
- Concurrency issues encountered: [Race conditions, deadlocks, etc.]

**What Worked Well**:

- [Specific aspect that was successful]
- [Why it worked - evidence]

**What Didn't Work / Challenges**:

- [Specific concurrency issues]
- [Why they occurred - root cause]
- [How they were resolved]

**Lessons Learned**:

- [Concurrency pattern insights]
- [When to use atoms vs refs vs channels]
- [Testing approaches that worked for concurrent code]

**Feed Forward**: [Update to README-TECHNICAL-PLANNING-CLOJURE.md#concurrency]

### Testing Outcome

**Planned Strategy**: [From planning-detailed]

**Implementation Reality**:

- Tests implemented: [Unit, property, integration counts]
- Coverage achieved: [Percentage or paths covered]
- Deviations: [Any changes from plan]

**What Worked Well**:

- [Testing approach that was effective]
- [Why it worked - evidence]
- [Bugs caught by tests]

**What Didn't Work / Challenges**:

- [Testing gaps or difficulties]
- [Why testing was hard]
- [How it was addressed]

**Test Effectiveness**:

- Bugs caught by unit tests: [Count]
- Bugs caught by property tests: [Count]
- Bugs caught by integration tests: [Count]
- Bugs missed by tests (found in review/production): [Count]

**Lessons Learned**:

- [Testing patterns that worked]
- [Property-based test insights]
- [Integration test approaches]
- [Test data management lessons]

**Feed Forward**: [Update to README-TECHNICAL-PLANNING-CLOJURE.md#testing]

### Security Outcome

**Planned Controls**: [From planning-detailed]

**Implementation Reality**:

- Controls implemented: [What was actually done]
- Deviations: [Any changes from plan]
- Security issues found: [In review, testing, or deployment]

**What Worked Well**:

- [Security control that was effective]
- [Why it worked - evidence]

**What Didn't Work / Challenges**:

- [Security gaps or difficulties]
- [Why security was hard]
- [How it was addressed]

**Security Validation**:

- Threats mitigated: [List]
- Vulnerabilities found: [Count and severity]
- Remediation time: [How long to fix]

**Lessons Learned**:

- [Security patterns that worked]
- [Threat modeling insights]
- [Input validation approaches]
- [Audit logging lessons]

**Feed Forward**: [Update to README-TECHNICAL-PLANNING-CLOJURE.md#security]

### Performance Outcome

**Planned Targets**: [From planning-detailed]

**Implementation Reality**:

- Performance achieved: [Actual metrics]
- Deviations: [Better/worse than planned]
- Optimizations applied: [What techniques used]

**What Worked Well**:

- [Optimization that was effective]
- [Why it worked - evidence, benchmarks]

**What Didn't Work / Challenges**:

- [Performance issues encountered]
- [Why performance was problematic]
- [How it was addressed]

**Performance Measurements**:

- Latency: [P50, P95, P99]
- Throughput: [Operations/second]
- Resource utilization: [CPU, memory]
- vs. Targets: [Met/Exceeded/Missed]

**Lessons Learned**:

- [Performance patterns that worked]
- [Clojure-specific optimizations that helped]
- [Profiling insights]
- [When optimization was premature vs necessary]

**Feed Forward**: [Update to README-TECHNICAL-PLANNING-CLOJURE.md#performance]

### Language Best Practices Outcome

**Planned Approach**: [From planning-detailed]

**Implementation Reality**:

- Idioms followed: [What patterns used]
- Deviations: [Any changes from plan]
- Code quality metrics: [Linter results, review feedback]

**What Worked Well**:

- [Idiomatic pattern that was effective]
- [Why it worked - maintainability, clarity]

**What Didn't Work / Challenges**:

- [Anti-patterns encountered]
- [Why they were problematic]
- [How they were refactored]

**Code Quality**:

- Linter warnings: [Count]
- Code review feedback: [Major themes]
- Refactorings needed: [Count and type]

**Lessons Learned**:

- [Clojure idioms that improved code]
- [Polylith patterns that helped (if applicable)]
- [Error handling approaches that worked]
- [Namespace organization insights]

**Feed Forward**: [Update to README-TECHNICAL-PLANNING-CLOJURE.md#best-practices]

## Estimation Accuracy

**Purpose**: Improve future technical planning by analyzing estimation accuracy.

### Time Estimates vs Actuals

| Technical Area | Estimated Time | Actual Time | Variance | Notes |
|----------------|---------------|-------------|----------|-------|
| Research | [Est] | [Actual] | [%] | [Why difference] |
| Planning | [Est] | [Actual] | [%] | [Why difference] |
| Data Structures | [Est] | [Actual] | [%] | [Why difference] |
| Algorithms | [Est] | [Actual] | [%] | [Why difference] |
| Concurrency | [Est] | [Actual] | [%] | [Why difference] |
| Testing | [Est] | [Actual] | [%] | [Why difference] |
| Security | [Est] | [Actual] | [%] | [Why difference] |
| Performance | [Est] | [Actual] | [%] | [Why difference] |

**Estimation Insights**:

- Consistently overestimated: [What areas]
- Consistently underestimated: [What areas]
- Unexpected time sinks: [What took longer than expected]
- Faster than expected: [What was easier]

**Feed Forward**: [How to improve future estimates for similar work]

## What Worked Well

### Technical Successes

- [Specific technique that was effective]
- [Tool or pattern that helped]
- [Approach that exceeded expectations]

### Process Successes

- [Planning decision that paid off]
- [Communication approach that worked]
- [Workflow optimization that helped]

## Challenges & Failures

### Failed Attempt: [Name]

**Time Investment**: [Hours spent]
**Technical Area**: [Data structures, algorithms, concurrency, etc.]
**Approach Tried**: [What we attempted]
**Failure Mode**: [How/why it didn't work]
**Root Cause**: [Deeper reason for failure - technical analysis]
**Prevention Strategy**: [How to avoid in future - update planning guide?]
**Lessons Learned**: [What we gained from this failure]
**Salvageable Components**: [What can be reused]

**Feed Forward**: [Update to README-TECHNICAL-PLANNING-CLOJURE.md as warning/anti-pattern]

## Code Review Outcomes

### Automated Review Results

- **Linting**: [Pass/Fail] - [Issues found]
- **Security Scan**: [Results]
- **Performance**: [Benchmarks vs targets]
- **Test Coverage**: [Percentage] - [Coverage report]

### Human Review Feedback

- **Architecture**: [Feedback received]
- **Best Practices**: [Suggestions made]
- **Maintainability**: [Concerns raised]
- **Security**: [Issues identified]
- **Technical Decisions**: [Validation or critique of planning choices]

## Documentation Review

### Documentation Completeness

- [ ] All modified modules have updated documentation
- [ ] New architectural patterns are documented
- [ ] Diagrams accurately reflect implementation
- [ ] Performance characteristics are documented
- [ ] Architecture views align with actual system
- [ ] Technical decisions documented in README-TECHNICAL-PLANNING-CLOJURE.md (if patterns extracted)

### Documentation Quality

- [ ] Diagrams follow established conventions
- [ ] Implementation sections explain the "how"
- [ ] Examples demonstrate actual usage patterns
- [ ] Error handling strategies are documented
- [ ] Migration guides included (if breaking changes)

## Knowledge Artifacts Created

- [ ] Documentation updated: [What files/sections]
- [ ] Architecture diagrams created: [Which components/systems]
- [ ] Patterns documented: [Where captured]
- [ ] Team knowledge shared: [How/where]
- [ ] ADRs created: [Which decisions documented]
- [ ] **Technical planning patterns extracted**: [Updates to README-TECHNICAL-PLANNING-CLOJURE.md]

## Performance Analysis

**Actual vs Estimated**:

- Research: [Actual] vs [Estimated]
- Planning: [Actual] vs [Estimated]
- Implementation: [Actual] vs [Estimated]
- Review: [Actual] vs [Estimated]

**Time Distribution**:

- Productive coding: [percentage]
- Debugging: [percentage]
- Testing: [percentage]
- Documentation: [percentage]
- Blocked/waiting: [percentage]
- **Technical planning**: [percentage]

## Next Actions

### Immediate (This Sprint/Iteration)

- [ ] [Specific follow-up task]
  - Owner: [@person]
  - Due: [Date]
  - Context: [Why needed]

### Pattern Extraction Tasks

- [ ] Update README-TECHNICAL-PLANNING-CLOJURE.md with [Pattern/Anti-pattern]
- [ ] Share learnings with team: [How and where]
- [ ] Update ADR if technical decisions changed: [Which ADR]

### Future Improvements

- [ ] [Technical debt to address]
- [ ] [Process improvement to implement]
- [ ] [Refactoring opportunity]

## Retrospective Notes

Using the DAKI (Drop, Add, Keep, Improve) framework:

**Drop**: [Practice that didn't help - stop doing this]

- Technical planning related: [What didn't work in planning process]

**Add**: [New practice to try next time - start doing this]

- Technical planning related: [What to add to planning process]

**Keep**: [Practice that worked well - continue doing this]

- Technical planning related: [What worked in planning process]

**Improve**: [Practice to refine - do this better]

- Technical planning related: [How to improve planning process]

## Links & References

- Research RunNotes: [filename]
- Planning RunNotes: [filename]
- Implementation RunNotes: [filename]
- Related ADRs: [if applicable]
- Code changes: [commit/PR references]
- Related issues: [issue numbers]
- Technical planning guide: `doc/README-TECHNICAL-PLANNING-CLOJURE.md`

**Session Archived**: [Location/ID]
