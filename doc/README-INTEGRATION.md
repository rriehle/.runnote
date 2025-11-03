# RunNotes Integration Patterns

This document provides detailed integration patterns for RunNotes with other documentation systems. For a quick reference, see [CLAUDE.md](../CLAUDE.md).

## Table of Contents

- [Integration with ADR Tools](#integration-with-adr-tools)
- [Integration with Requirements Tools](#integration-with-requirements-tools)
- [Integration with Code and Tests](#integration-with-code-and-tests)

---

## Integration with ADR Tools

RunNotes and ADRs work together to document the journey from exploration to decision.

### Identifying ADR Candidates

During research and planning phases, identify decisions that need formal documentation.

**ADR candidate indicators:**

- Decision affects multiple components
- Decision is expensive to reverse
- Decision establishes or changes pattern
- Decision involves technology adoption
- Decision constrains future choices
- Decision will be questioned later ("why did we do this?")

**Document in RunNotes:**

```yaml
adr-candidates:
  - Use event sourcing for audit trail
    Rationale: Regulatory compliance requires complete history
    Impact: Changes data model and storage architecture

  - Adopt GraphQL for API layer
    Rationale: Frontend needs flexible queries
    Impact: New technology adoption, team learning curve
```

### Linking RunNotes to ADRs

**In planning RunNotes metadata:**

```edn
{:related-documents
 {:adr #{"ADR-00042-event-sourcing"
         "ADR-00043-graphql-api"}}}
```

**In planning RunNotes content:**

```markdown
## Architectural Decisions

### Event Sourcing for Audit Trail
Decision documented in ADR-00042 (link: doc/adr/00042-event-sourcing.md)

Per ADR-00042, all state changes will be persisted as events to maintain complete audit trail for regulatory compliance.
```

**In ADR metadata:**

```edn
{:runnotes #{"RunNotes-2025-10-14-AuditTrail-research"
             "RunNotes-2025-10-14-AuditTrail-planning"}}
```

**In ADR content:**

```markdown
## Context

Investigation documented in RunNotes-2025-10-14-AuditTrail-research identified need for complete state change history for regulatory compliance.

Planning session RunNotes-2025-10-14-AuditTrail-planning evaluated three approaches: audit table, CDC, and event sourcing.
```

### Workflow: RunNotes → ADR → RunNotes

**Phase 1: Research (RunNotes)**

```bash
runnote-launch research AuditTrail
```

Document:

- Business need (regulatory compliance)
- Technical investigation (approaches: audit table, CDC, event sourcing)
- Trade-offs of each approach
- Identify as ADR candidate

**Phase 2: Planning (RunNotes)**

```bash
runnote-launch planning AuditTrail
```

Document:

- Evaluation of alternatives
- Decision: event sourcing
- Rationale and trade-offs
- Mark for ADR creation

**Phase 3: Create ADR**

```bash
cp ~/.adr/template/default.md doc/adr/00042-event-sourcing-audit-trail.md
```

- Import context from research and planning RunNotes
- Formalize decision
- Document consequences
- Link back to RunNotes in metadata

**Phase 4: Implementation (RunNotes)**

```bash
runnote-launch implementation AuditTrail
```

Document:

- Reference ADR-00042 in implementation notes
- Track adherence to architectural decision
- Document any implementation learnings
- Note if ADR needs updates based on implementation reality

**Phase 5: Review (RunNotes)**

```bash
runnote-launch review AuditTrail
```

Document:

- Validate decision (was ADR-00042 right?)
- Document implementation learnings
- Identify if ADR needs updates
- Capture patterns for future similar work

---

## Integration with Requirements Tools

Requirements specify WHAT to build; RunNotes document WHY and HOW.

### Eliciting Requirements from Planning RunNotes

Planning phase RunNotes contain raw material for requirements.

**In planning RunNotes:**

```yaml
objectives:
  - Enable customer service to process refunds
  - Support 30-day refund window
  - Integrate with payment gateway
  - Maintain audit trail of all refund operations

success-criteria:
  - Refunds processed in <24 hours
  - Full audit trail for compliance
  - Integration with Stripe payment gateway
  - Support for full and partial refunds
```

**Extract to formal requirements:**

- REQ-REFUND-001: System MUST calculate refund amount
- REQ-REFUND-002: System MUST verify 30-day window
- REQ-REFUND-003: System MUST integrate with Stripe
- REQ-REFUND-NFR-001: System MUST complete refunds within 24 hours

**Link requirements to planning RunNotes:**

```edn
{:trace {:runnote #{"RunNotes-2025-10-14-RefundProcess-planning"}}}
```

**Link planning RunNotes to requirements:**

```edn
{:related-documents
 {:requirements #{"REQ-REFUND-001"
                  "REQ-REFUND-002"
                  "REQ-REFUND-003"
                  "REQ-REFUND-NFR-001"}}}
```

### Linking Implementation to Requirements

During implementation, track which requirements are being fulfilled.

**In implementation RunNotes:**

```markdown
### [HH:MM] - Implementing Refund Calculation
Implements: REQ-REFUND-001

**Approach**: Creating RefundCalculator class per planning session
**Progress**: Basic calculation logic complete
**Testing**: Unit tests passing for simple cases
**Next**: Handle partial refunds and tax calculations
```

**Update requirements with implementation links:**

```edn
{:trace {:code #{"src/refunds/calculator.py:45-67"}
         :runnote #{"RunNotes-2025-10-14-RefundProcess-implementation"}}}
```

### Workflow: Business Need → Requirements → Implementation

**Phase 1: Planning RunNotes**

- Document business need
- Define objectives and success criteria
- Identify functional and non-functional needs
- Create implementation plan

**Phase 2: Requirements Extraction**

- Create formal requirements from planning notes
- Link requirements to planning RunNotes
- Define acceptance criteria
- Link to relevant ADRs (architectural constraints)

**Phase 3: Implementation RunNotes**

- Reference requirements in implementation logs
- Track implementation against acceptance criteria
- Document deviations with rationale
- Link code to requirements

**Phase 4: Review RunNotes**

- Verify requirements met
- Document any gaps or deferred work
- Update requirements status
- Identify new requirements from implementation learnings

---

## Integration with Code and Tests

RunNotes provide context for code changes and test strategies.

### Linking Code to RunNotes

**In code comments:**

```python
# Implements event sourcing for audit trail
# See: RunNotes-2025-10-14-AuditTrail-implementation
# Architecture: ADR-00042
class EventStore:
    """
    Event sourcing implementation per ADR-00042.

    Context: RunNotes-2025-10-14-AuditTrail-research identified
    need for complete state change history for regulatory compliance.
    """
    pass
```

**In commit messages:**

```
feat: implement event sourcing for audit trail

Implements event sourcing architecture per ADR-00042.

Context: RunNotes-2025-10-14-AuditTrail-implementation
Requirements: REQ-AUDIT-001, REQ-AUDIT-002

See: doc/adr/00042-event-sourcing-audit-trail.md
See: runnote/RunNotes-2025-10-14-AuditTrail-implementation.md
```

**In RunNotes metadata:**

```edn
{:related-documents
 {:code-files #{"src/events/store.py"
                "src/events/replay.py"}
  :commits #{"a1b2c3d4"}}}
```

### Test Strategy Documentation

**In planning RunNotes:**

```markdown
## Test Strategy

### Unit Testing
- Event store persistence (100% coverage target)
- Event replay logic (property-based testing)
- Event serialization/deserialization

### Integration Testing
- End-to-end event recording and replay
- Database transaction boundaries
- Concurrency handling

### Performance Testing
- Event write throughput (target: 1000 events/sec)
- Replay performance (target: complete in <5min for 1M events)
```

**In implementation RunNotes:**

```markdown
### [HH:MM] - Test Implementation
**Testing**: Event store persistence tests
**Coverage**: 95% (missing edge case: concurrent writes)
**Results**: All tests passing
**Next**: Add property-based tests for replay logic
```

**Link to test files:**

```edn
{:related-documents
 {:test-files #{"test/events/test_store.py"
                "test/events/test_replay.py"}}}
```

### From Implementation to Documentation

**Pattern: Implementation reveals requirements gaps**

During implementation, discover unstated requirements:

```markdown
### [HH:MM] - Discovery: Error Handling Requirement Missing
State: 🟡 Investigating

**Discovery**: Planning didn't specify error handling for event replay failures
**Impact**: Need to decide on retry strategy
**Decision**: Implementing exponential backoff with circuit breaker
**Action**: Creating REQ-AUDIT-NFR-005 for this non-functional requirement

Per discussion with architect, documenting as:
- Requirement: REQ-AUDIT-NFR-005 (to be created)
- Decision: Using circuit breaker pattern
- ADR candidate: Circuit breaker configuration strategy
```

This creates a feedback loop:

1. Implementation discovers gap
2. Create requirement retroactively
3. Link requirement to implementation
4. Update planning RunNotes with lesson learned
5. Future planning includes error handling upfront

---

## See Also

- [CLAUDE.md](../CLAUDE.md) - Quick reference for AI agents
- [README.md](../README.md) - Human-focused usage guide
- [README-PHASES.md](README-PHASES.md) - Detailed phase documentation
- [README-WORKFLOWS.md](README-WORKFLOWS.md) - Agent workflows
- [README-QUALITY.md](README-QUALITY.md) - Quality enforcement guidelines
- [README-FILE-FORMAT.md](README-FILE-FORMAT.md) - File format specifications
