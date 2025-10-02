# Planning: [Topic] - YYYY-MM-DD HH:MM

```edn :metadata
{:phase "planning"
 :tag #{:set-me}
 :status :active
 :thinking-mode "think harder"
 :complexity :medium
 :date {:created "YYYY-MM-DD"}}
```

## Context From Research

[Import summary from research phase]

**Key constraints**: [List]
**Critical insights**: [List]

## Related ADRs Analysis

**Claude Recommendation**: Before designing solutions, search existing Architecture Decision Records to:

1. Avoid contradicting established decisions
2. Build on proven patterns
3. Understand system constraints and rationale
4. Identify decision gaps requiring new ADRs

### ADR Search Commands

```bash
# Search by relevant tags (adjust based on your topic)
adr-search tag :architecture
adr-search tag :ui
adr-search tag :security
adr-search tag :performance

# Search by topic keywords
adr-search content "[your topic keywords]"
adr-search content "state management"
adr-search content "authentication"

# List all accepted decisions
adr-search status accepted
```

### Relevant Existing ADRs

Document findings from ADR searches:

| ADR | Title | Relevance | Impact on This Planning |
|-----|-------|-----------|-------------------------|
| [ADR-#####](path/to/adr.md) | [Title] | [How it relates] | [Constraints/opportunities it creates] |

**Key Patterns to Follow**: [Summarize established patterns this planning should align with]

**Constraints from ADRs**: [List any limitations imposed by existing decisions]

**Decision Gaps Identified**: [Areas where new ADRs may be needed during implementation]

## Solution Design

### Approach Evaluation Matrix

| Approach | Complexity | Risk | Recommendation |
|----------|------------|------|----------------|
| Option A | Low | Medium | Consider if... |
| Option B | High | Low | Best when... |

### Selected Approach: [Name]

**Rationale**: [Why this approach best fits our constraints]

### Architecture Decisions

**Integration with ADR System**: Document decisions that warrant formal ADRs here, then create actual ADR files during implementation.

1. **Potential ADR**: [Decision Title]
   - Context: [What forces are at play]
   - Decision: [What we've decided]
   - Consequences: [What this implies]
   - ADR Required?: [Yes/No - justify based on scope and impact]
   - ADR Tags: [Suggest tags like :architecture, :ui, :security, etc.]

**ADR Creation Tasks**: [List decisions that need formal ADRs during implementation]

- [ ] Create ADR for [Decision Title] with tags [:tag1, :tag2]
- [ ] Update existing ADR if this decision modifies previous choices

## Implementation Strategy

### Phase Breakdown

1. **Foundation** (Est: Xh)
   - [ ] [Specific deliverable]
   - Success criteria: [Measurable outcome]

2. **Core Implementation** (Est: Yh)
   - [ ] [Specific deliverable]
   - Success criteria: [Measurable outcome]

### Risk Mitigation Plan

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|------------|
| [Risk] | High/Med/Low | High/Med/Low | [Strategy] |

## Test Strategy

- **Unit test approach**: [Strategy]
- **Integration test needs**: [What to verify]
- **Performance benchmarks**: [Targets]

## Phase Transition Checklist

Ready for Implementation when:

- [ ] ADR searches completed and relevant decisions reviewed (if ADR integration enabled)
- [ ] No conflicts identified with existing ADRs
- [ ] Approach validated with team
- [ ] All technical decisions documented
- [ ] ADR creation tasks identified for implementation phase
- [ ] Test strategy defined
- [ ] Rollback plan established

**Next Phase**: Implementation (Scheduled: [Date/Time])
