# Analysis: [Topic] - YYYY-MM-DD HH:MM

```edn :metadata
{:phase "analysis-detailed"
 :tag #{:set-me}
 :status :active
 :thinking-mode "think harder"
 :analysis-type :gap-analysis  ; or :fit-assessment, :comparison, :audit, :evaluation
 :complexity :high
 :date {:created "YYYY-MM-DD"}}
```

## Analysis Overview

**Objective**: [What this analysis aims to determine or evaluate]
**Scope**: [Boundaries of the analysis - what's in/out]
**Audience**: [Who will consume these findings]
**Decision Support**: [What decisions this analysis informs]

### Success Criteria

| Criterion | Target | Measurement |
|-----------|--------|-------------|
| Coverage depth | [X requirements analyzed] | Count of items assessed |
| Actionability | [All gaps have mitigations] | Gap register completeness |
| Stakeholder alignment | [Findings validated] | Review sign-off |

## Source Materials

### Primary Sources

| Source | Description | Location | Items | Status |
|--------|-------------|----------|-------|--------|
| [Requirements Doc] | [What it contains] | [Path/URL] | [N items] | Reviewed/Pending |
| [Comparison Target] | [What it contains] | [Path/URL] | [N items] | Reviewed/Pending |

### Reference Materials

- [ ] [Standard/Framework]: [How it applies]
- [ ] [Prior Analysis]: [What to build on]
- [ ] [Expert Input]: [Who to consult]

### Research Phase Summary

**Import from research phase** (if applicable):

- Key constraints discovered: [List]
- Critical insights: [List]
- Open questions resolved: [List]

## Analysis Framework

### Methodology

**Approach**: [How the analysis will be conducted]

**Coverage Scoring Model**:

| Level | Weight | Definition | Example |
|-------|--------|------------|---------|
| Full | 1.00 | Requirement fully satisfied | Native capability, no configuration needed |
| High | 0.75 | Most aspects covered; minor gaps | Works with minor configuration or caveats |
| Partial | 0.50 | Some aspects covered; notable gaps | Partial capability; workarounds needed |
| Low | 0.25 | Minimal coverage; significant gaps | Ecosystem/third-party required |
| None | 0.00 | Not addressed | No capability; must build or accept risk |
| N/A | excluded | Not applicable | Requirement doesn't apply to this context |

**Coverage Calculation**:

```text
Coverage % = Sum(weights) / (Total - N/A) x 100

Where:
  weights = (Full x 1.0) + (High x 0.75) + (Partial x 0.5) + (Low x 0.25) + (None x 0.0)
  Total = number of requirements in scope
  N/A = requirements not applicable
```

**Category Taxonomy**:

| Code | Category | Definition | Action Implication |
|------|----------|------------|-------------------|
| N | Native | Capability provided out-of-box | Use as-is |
| C | Configurable | Capability requires setup | Plan configuration |
| A | Add-On | Requires additional licensing | Budget/procure |
| E | Ecosystem | Requires third-party integration | Evaluate vendors |
| CU | Customer | Customer/organizational responsibility | Define process |
| M | Managed | Provider-managed (no customer control) | Accept or change provider |
| G | Gap | No capability; compensating control needed | Build or accept risk |
| - | N/A | Not applicable | Document exclusion rationale |

### Gap Severity Classification

| Severity | Definition | Action Required |
|----------|------------|-----------------|
| Critical | Core requirement not addressable | Must mitigate or accept documented risk |
| High | Significant gap affecting compliance/capability | Should mitigate before go-live |
| Medium | Notable gap with workarounds available | Consider mitigation based on priority |
| Low | Minor gap; minimal impact | Optional mitigation |

### Analysis Dimensions

Structure analysis across these dimensions:

1. **[Dimension 1]**: [What this examines, why it matters]
2. **[Dimension 2]**: [What this examines, why it matters]
3. **[Dimension N]**: [What this examines, why it matters]

**Dimension Weighting** (if applicable):

| Dimension | Weight | Rationale |
|-----------|--------|-----------|
| [Dimension 1] | X% | [Why this weight] |

## Work Breakdown

### Clusters/Sections

| # | Cluster/Section | Requirements | Complexity | Est. Effort | Status | Coverage |
|---|-----------------|--------------|------------|-------------|--------|----------|
| 01 | [Name] | X | High/Med/Low | Xh | Pending | - |
| 02 | [Name] | X | High/Med/Low | Xh | Pending | - |

### Parallelization Strategy

**Batch Approach** (for large analyses):

| Batch | Clusters | Rationale | Agent Assignment |
|-------|----------|-----------|------------------|
| A | 01, 02, 03 | [Grouping logic] | [Agent type or manual] |
| B | 04, 05, 06 | [Grouping logic] | [Agent type or manual] |

**Dependencies**:

- [Cluster X] depends on [Cluster Y] findings
- Synthesis requires all clusters complete

**Pilot Validation**:

- [ ] Select representative cluster for methodology validation
- [ ] Run pilot analysis
- [ ] Validate scoring approach
- [ ] Refine methodology before scaling

## Analysis Execution

### Pilot Cluster: [Name]

**Purpose**: Validate methodology before scaling

**Execution Notes**:

- [What worked]
- [What needed adjustment]
- [Methodology refinements]

**Pilot Results**: [Coverage %] | [Gaps found] | [Time spent]

### Cluster Analysis Log

#### [HH:MM] - Cluster 01: [Name]

**Scope**: [X requirements]
**Approach**: [Specific method]

**Assessment Summary**:

| Metric | Value |
|--------|-------|
| Full | X |
| High | X |
| Partial | X |
| Low | X |
| None | X |
| N/A | X |
| **Coverage** | **X%** |

**Key Findings**:

1. [Finding]
2. [Finding]

**Gaps Identified**:

- GAP-XXX: [Description] (Severity: [X])

**Time Spent**: [Xh]

#### [HH:MM] - Cluster 02: [Name]

[Repeat structure for each cluster]

### Batch Execution Summary

| Batch | Clusters | Start | End | Avg Coverage | Total Gaps |
|-------|----------|-------|-----|--------------|------------|
| A | 01-03 | [Time] | [Time] | X% | Y |
| B | 04-06 | [Time] | [Time] | X% | Y |

## Findings Synthesis

### Overall Coverage

| Dimension/Platform | Full | High | Partial | Low | None | N/A | Coverage % |
|--------------------|------|------|---------|-----|------|-----|------------|
| [Dimension A] | X | X | X | X | X | X | X% |
| [Dimension B] | X | X | X | X | X | X | X% |
| **Overall** | X | X | X | X | X | X | **X%** |

### Coverage by Cluster

| # | Cluster | Dim A | Dim B | Delta | Insight |
|---|---------|-------|-------|-------|---------|
| 01 | [Name] | X% | X% | +/-X% | [Key observation] |

### Gap Register

#### Critical Gaps (Must Address)

| ID | Description | Dimension(s) | Impact | Mitigation | Effort | Owner |
|----|-------------|--------------|--------|------------|--------|-------|
| GAP-001 | [Description] | [A/B/Both] | [What happens if unaddressed] | [Strategy] | H/M/L | [TBD] |

#### High Severity Gaps (Should Address)

| ID | Description | Dimension(s) | Impact | Mitigation | Effort | Owner |
|----|-------------|--------------|--------|------------|--------|-------|
| GAP-00X | [Description] | [A/B/Both] | [Impact] | [Strategy] | H/M/L | [TBD] |

#### Medium Severity Gaps (Consider)

[Table format]

#### Low Severity Gaps (Optional)

[Table format]

### Gap Summary by Dimension

| Gap ID | Description | Dim A | Dim B | Mitigation Available |
|--------|-------------|-------|-------|---------------------|
| GAP-001 | [Brief] | Critical | Critical | [Primary approach] |

### Key Findings

1. **[Finding Title]**
   - **Observation**: [What the data shows]
   - **Implication**: [What it means for the decision]
   - **Evidence**: [Where this is documented]

2. **[Finding Title]**
   [Repeat structure]

### Comparative Analysis (if applicable)

| Aspect | Dimension A | Dimension B | Winner | Notes |
|--------|-------------|-------------|--------|-------|
| [Aspect 1] | [Capability] | [Capability] | A/B/Tie | [Why] |

### Recommendations

| Priority | Recommendation | Rationale | Effort | Dependencies |
|----------|----------------|-----------|--------|--------------|
| P1 | [Action] | [Why critical] | H/M/L | [What's needed first] |
| P2 | [Action] | [Why important] | H/M/L | [Dependencies] |

## Deliverables

### Analysis Documents

| Document | Purpose | Audience | Status |
|----------|---------|----------|--------|
| Executive Summary | 2-page decision brief | Leadership | Pending |
| Comprehensive Analysis | Full findings with methodology | Technical team | Pending |
| Gap Register | Actionable gap list with priorities | Implementation team | Pending |
| Cluster Documents | Detailed per-cluster assessments | Subject matter experts | Pending |

### Supporting Materials

| Material | Purpose | Status |
|----------|---------|--------|
| GLOSSARY.md | Consolidated abbreviations | Pending |
| REFERENCES.md | Evidence links | Pending |

### Visualizations

- [ ] Coverage comparison chart (bar/column)
- [ ] Gap distribution (pie chart with severity colors)
- [ ] Decision flowchart (platform/approach selection)
- [ ] Mitigation flow (gap resolution paths)
- [ ] Architecture/integration diagrams (if applicable)

**Mermaid Diagram Checklist**:

- [ ] Use semantic colors (red=critical, orange=high, yellow=medium, green=low/good)
- [ ] Include `showData` for quantitative charts
- [ ] Style nodes consistently across diagrams

## Quality Assurance

### Methodology Validation

- [ ] Pilot cluster validated approach
- [ ] Scoring applied consistently across all clusters
- [ ] Category assignments justified with evidence
- [ ] Coverage calculations verified (spot-check 3 clusters)

### Content Validation

- [ ] All requirements in scope assessed
- [ ] Gap severities justified (not arbitrary)
- [ ] Mitigations are actionable (not vague)
- [ ] Findings traceable to specific evidence
- [ ] No contradictory assessments

### Documentation Quality

- [ ] DRY principles applied (no duplicated tables)
- [ ] Cross-references work correctly
- [ ] Diagrams render properly
- [ ] Abbreviations defined in glossary
- [ ] Evidence links are valid

### Stakeholder Review

- [ ] Technical accuracy reviewed by SME
- [ ] Completeness verified against scope
- [ ] Recommendations validated as feasible
- [ ] Executive summary approved

## Phase Transition Checklist

Ready for Review/Completion when:

- [ ] All clusters/sections analyzed
- [ ] Coverage percentages calculated and verified
- [ ] All gaps identified, categorized, and prioritized
- [ ] Mitigations proposed for Critical/High gaps
- [ ] Executive summary complete
- [ ] Gap register complete
- [ ] Supporting materials complete (glossary, references)
- [ ] Visualizations complete
- [ ] Quality checks passed
- [ ] Stakeholder review complete (if required)

**Completion Criteria Met**: [ ] Yes / [ ] No - [Blocker if no]

**Next Phase**: Review (Estimated: [Date/Time])

## Retrospective Notes

**What Worked Well**:

- [Approach/technique that was effective]

**Challenges Encountered**:

- [Issue]: [How resolved]

**Process Improvements for Next Analysis**:

- [Suggestion]

## Links & References

- Research RunNotes: [filename if applicable]
- Related analyses: [prior work]
- Source requirements: [location]
- Output location: [where deliverables live]
- Related ADRs: [if decisions documented]

**Analysis Type**: [Gap Analysis / Fit Assessment / Comparison / Audit / Evaluation]
**Analysis Status**: [Active / Complete / Blocked]
