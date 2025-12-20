# Comparative Review: [Topic] - YYYY-MM-DD

```edn :metadata
{:phase "review"
 :tag #{:architecture :decision :parallel-experiment :comparative-analysis}
 :status :active
 :thinking-mode "think harder"
 :complexity :high
 :date {:created "YYYY-MM-DD"}
 :context {:parent "[planning-runnotes-path]"
           :option-a-impl "[option-a-implementation-runnotes-path]"
           :option-b-impl "[option-b-implementation-runnotes-path]"}}
```

## Executive Summary

This review compares two parallel experimental implementations of [Topic] to determine which path continues forward.

| Identifier | Approach | Strategy |
|------------|----------|----------|
| [Option A Name] | [Approach Name] | [Brief description of strategy] |
| [Option B Name] | [Approach Name] | [Brief description of strategy] |

**Decision Required**: Select winner, determine architectural disposition, archive lessons from non-selected path.

---

## 1. Quantitative Metrics Comparison

### Code Metrics

| Metric | Option A | Option B | Analysis |
|--------|----------|----------|----------|
| Primary module LOC | | | |
| Supporting module LOC | | | |
| New components created | | | |
| Total implementation LOC | | | |
| Original reference LOC | N/A | | |

### Quality Metrics

| Metric | Option A | Option B | Target | Analysis |
|--------|----------|----------|--------|----------|
| Build/check status | | | Clean | |
| Linting results | | | Clean | |
| Test coverage | | | All pass | |
| Performance metrics | | | [target] | |

### Time Investment

| Phase | A Estimate | A Actual | B Estimate | B Actual |
|-------|------------|----------|------------|----------|
| Phase 1 | | | | |
| Phase 2 | | | | |
| Phase 3 | | | | |
| Phase 4 | | | | |
| **Total to Checkpoint** | | | | |

**Analysis**: [Compare estimated vs actual for both. Note significant deviations.]

---

## 2. Architecture Quality Assessment

### 2.1 Architectural Compliance

**Option A Assessment**:

- [ ] [Primary architectural constraint 1] satisfied?
- [ ] [Primary architectural constraint 2] satisfied?
- [ ] [Primary architectural constraint 3] satisfied?
- [ ] [Build/validation tool] analysis notes

**Option B Assessment**:

- [ ] [Primary architectural constraint 1] satisfied?
- [ ] [Primary architectural constraint 2] satisfied?
- [ ] [Primary architectural constraint 3] satisfied?
- [ ] [Build/validation tool] analysis notes

**Verdict**: [Which approach has superior architectural compliance?]

### 2.2 Module/Component Boundary Design

**Option A Modules**:

- [List modules/components used or created]
- [Interface quality assessment]
- [Structural organization notes]

**Option B Modules**:

- [List modules/components used or created]
- [Interface quality assessment]
- [Structural organization notes]

**Verdict**: [Which has better component/module boundaries?]

### 2.3 State Management Pattern

| Aspect | Option A | Option B |
|--------|----------|----------|
| Pattern used | | |
| Data flow mechanism | | |
| Thread safety approach | | |
| Scalability potential | | |
| Complexity level | | |

**Analysis**: [Compare state management approaches, note trade-offs]

**Verdict**: [Which has more appropriate state management?]

### 2.4 Cross-Cutting Concerns Compliance

| Concern | Option A | Option B |
|---------|----------|----------|
| [Concern 1, e.g., Threading] | | |
| [Concern 2, e.g., Error handling] | | |
| [Concern 3, e.g., Logging] | | |

**Verdict**: [Both compliant / One better than other?]

---

## 3. Code Reusability Analysis

### 3.1 Cross-Project Reusability

| Component/Module | A Reusable? | B Reusable? | Notes |
|------------------|-------------|-------------|-------|
| [Component 1] | | | |
| [Component 2] | | | |
| [Component 3] | | | |

**Reusability Assessment**: [Compare which creates more reusable artifacts]

### 3.2 Interface Clarity

**Option A Interface Assessment**:

- [Public API quality]
- [Documentation completeness]
- [Usage patterns]

**Option B Interface Assessment**:

- [Public API quality]
- [Documentation completeness]
- [Usage patterns]

**Verdict**: [Which has cleaner interfaces?]

### 3.3 Coupling Analysis

| Coupling Type | Option A | Option B |
|---------------|----------|----------|
| Module ↔ Module | | |
| Core ↔ Module | | |
| [Domain 1] ↔ [Domain 2] | | |

**Verdict**: [Which has better decoupling?]

---

## 4. Developer Experience Assessment

### 4.1 Complexity Feel

**Option A**:

- **Approach Feel**: [Was this approach smooth or fighting the architecture?]
- **Pain Points**: [What was difficult?]
- **Cognitive Load**: [HIGH/MODERATE/LOW - What must developer understand?]
- **Development Flow**: [Linear, iterative, exploratory?]

**Option B**:

- **Approach Feel**: [Was this approach smooth or fighting the architecture?]
- **Pain Points**: [What was difficult?]
- **Cognitive Load**: [HIGH/MODERATE/LOW - What must developer understand?]
- **Development Flow**: [Linear, iterative, exploratory?]

**Verdict**: [Which had lower cognitive load?]

### 4.2 Debugging Ease

| Scenario | Option A | Option B |
|----------|----------|----------|
| [Common issue 1] | | |
| [Common issue 2] | | |
| [Common issue 3] | | |
| [Common issue 4] | | |

**Verdict**: [Which is easier to debug?]

### 4.3 Extension Ease (Next Phase Readiness)

| Factor | Option A | Option B |
|--------|----------|----------|
| Adding [next feature 1] | | |
| Adding [next feature 2] | | |
| Adding [next feature 3] | | |
| [Extension pattern] | | |

**Verdict**: [Which is better positioned for extension?]

---

## 5. Strategic Decision Criteria

From the original planning document ([planning-doc-reference]):

### 5.1 [Criterion 1: e.g., Speed to checkpoint]

| Path | Planned | Actual | Delta | Winner |
|------|---------|--------|-------|--------|
| Option A | | | | |
| Option B | | | | |

**Assessment**: [Analysis of why one was faster/slower]

**Winner**: [Option A/B] ([reason])

### 5.2 [Criterion 2: e.g., Code structure cleanliness]

| Dimension | Option A | Option B | Winner |
|-----------|----------|----------|--------|
| [Sub-criterion 1] | | | |
| [Sub-criterion 2] | | | |
| [Sub-criterion 3] | | | |
| [Sub-criterion 4] | | | |

**Assessment**: [Analysis of structural differences]

**Winner**: [Option A/B] ([reason])

### 5.3 [Criterion 3: e.g., Long-term maintainability]

| Factor | Option A | Option B | Winner |
|--------|----------|----------|--------|
| [Factor 1] | | | |
| [Factor 2] | | | |
| [Factor 3] | | | |
| [Factor 4] | | | |

**Assessment**: [Analysis of maintainability trade-offs]

**Winner**: [Option A/B] ([reason])

---

## Strategic Criteria Summary

| Criterion | Weight | Option A | Option B | Winner |
|-----------|--------|----------|----------|--------|
| [Criterion 1] | 33% | | | |
| [Criterion 2] | 33% | | | |
| [Criterion 3] | 33% | | | |

**Overall Assessment**: [Summary of which option wins and why]

---

## 6. Decision

> **CHECKPOINT**: Stop here and present findings to human user for review.
> Do NOT proceed with decision recommendations until sections 1-5 are complete
> and the user has reviewed the comparative analysis. The strategic decision
> requires human judgment and approval.

### 6.1 Selected Path

**Winner**: _Pending human decision after review of sections 1-5_

**Rationale**:

1. [Primary reason for selection]
2. [Secondary reason]
3. [Additional consideration]

### 6.2 Architectural Disposition

Per the planning document:

| Outcome | Required Action |
|---------|-----------------|
| Option A wins | [Action if A wins, e.g., deprecate related ADR] |
| Option B wins | [Action if B wins, e.g., retain with migration plan] |

**Decision**: [Pending human approval]

**Required Updates**:

- [ ] [Document update 1]
- [ ] [Document update 2]
- [ ] [Document update 3]

### 6.3 Next Steps for Selected Path

1. [ ] [Merge/integration step]
2. [ ] [Continue to next phase]
3. [ ] [Documentation updates]

---

## 7. Lessons from Non-Selected Path

### 7.1 Technical Lessons

What did the non-selected path teach us that we should preserve?

1. [Technical insight 1]
2. [Technical insight 2]
3. [Technical insight 3]

### 7.2 Process Lessons

What did running parallel experiments teach us?

1. [Process insight 1]
2. [Process insight 2]
3. [Process insight 3]

### 7.3 Patterns to Extract

Are there any patterns from the non-selected path worth extracting?

| Pattern | Source | Potential Use |
|---------|--------|---------------|
| [Pattern name] | [Where found] | [Future application] |

---

## 8. Retrospective

### 8.1 Parallel Experiment Methodology

**What worked well**:

- [Methodology success 1]
- [Methodology success 2]

**What could improve**:

- [Improvement opportunity 1]
- [Improvement opportunity 2]

### 8.2 DAKI

**Drop**: [Practice that didn't help]

**Add**: [New practice to try next time]

**Keep**: [Practice that worked well]

**Improve**: [Practice to refine]

---

## References

### Source Documents

- Planning: `[planning-runnotes-path]`
- Option A Planning: `[option-a-planning-path]`
- Option A Implementation: `[option-a-implementation-path]`
- Option B Planning: `[option-b-planning-path]`
- Option B Implementation: `[option-b-implementation-path]`

### Related ADRs

- [ADR-XXXXX: Relevant architectural decision]
- [ADR-XXXXX: Another relevant decision]

### Code Locations

- Option A: `[path-to-option-a-code]`
- Option B: `[path-to-option-b-code]`
