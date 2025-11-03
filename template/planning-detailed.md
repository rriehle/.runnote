# Planning: [Topic] - YYYY-MM-DD HH:MM

```edn :metadata
{:phase "planning-detailed"
 :tag #{:set-me}
 :status :active
 :thinking-mode "think harder"
 :complexity :high
 :date {:created "YYYY-MM-DD"}}
```

## Context From Research

[Import summary from research-detailed phase]

**Key constraints**: [List]
**Critical insights**: [List]
**Technical patterns identified**: [From research-detailed technical discovery]

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
adr-search tag :performance
adr-search tag :security
adr-search tag :concurrency

# Search by topic keywords
adr-search content "[your topic keywords]"
```

### Relevant Existing ADRs

Document findings from ADR searches:

| ADR | Title | Relevance | Impact on This Planning |
|-----|-------|-----------|-------------------------|
| [ADR-#####](path/to/adr.md) | [Title] | [How it relates] | [Constraints/opportunities it creates] |

**Key Patterns to Follow**: [Summarize established patterns this planning should align with]

**Constraints from ADRs**: [List any limitations imposed by existing decisions]

**Decision Gaps Identified**: [Areas where new ADRs may be needed during implementation]

---

## Technical Planning Sections

**Guide Reference**: See `doc/README-TECHNICAL-PLANNING-CLOJURE.md` for comprehensive Clojure-specific guidance on each section below.

---

## 1. Data Structures

**Reference**: `doc/README-TECHNICAL-PLANNING-CLOJURE.md#data-structures`

### Problem Analysis

**From Research Phase**: [Import findings from research-detailed]

**Access Patterns**:

- [ ] Read-heavy (lookup-dominated)
- [ ] Write-heavy (frequent updates)
- [ ] Random access required
- [ ] Sequential access sufficient
- [ ] Range queries needed
- [ ] Sorted traversal required

**Size Characteristics**:

- Expected item count: [N items]
- Growth rate: [Items/time]
- Upper bound: [Maximum size or unbounded]
- Memory constraints: [Available heap, GC pressure concerns]

**Concurrency Requirements**:

- [ ] Single-threaded access
- [ ] Multi-threaded reads (immutable OK)
- [ ] Multi-threaded writes (coordination needed)
- [ ] High contention expected
- [ ] Low contention expected

### Clojure Data Structure Options

Evaluate each option against requirements:

**Persistent Vector** (`[]`):

- Complexity: O(log32 N) access/update, O(1) append
- Best for: Indexed access, sequential iteration, append-heavy
- Trade-offs: Slower than maps for key-value, but excellent for ordered data
- Transient available: Yes (for batch construction)

**Persistent Map** (`{}`):

- Complexity: O(log32 N) lookup/update
- Best for: Key-value access, frequent updates, transient batch operations
- Trade-offs: Unordered, use sorted-map if order needed
- Transient available: Yes

**Sorted-Map** / **Sorted-Set**:

- Complexity: O(log N) operations
- Best for: Range queries, ordered traversal, comparator-based ordering
- Trade-offs: Slower than hash-map, but maintains order

**Transient Collections**:

- Complexity: ~5-10x faster construction than persistent
- Best for: Batch building then convert to persistent
- Trade-offs: Mutable during construction, not thread-safe, must finalize

**Java Interop** (ArrayList, HashMap):

- Complexity: O(1) average for ArrayList/HashMap
- Best for: When persistent overhead unacceptable, tight loops, already-mutable context
- Trade-offs: Loses immutability benefits, manual synchronization if shared

### Decision

**Selected Structure**: [Name and rationale]

**Justification**:

- Access pattern fit: [Why this structure matches access patterns]
- Performance: [Expected complexity, benchmarks if available]
- Memory: [Estimated overhead, GC impact]
- Concurrency: [Thread-safety characteristics]
- Immutability: [Fully persistent, transient-capable, or mutable]

**Alternative Considered**: [Why not chosen]

**Escape Hatches**: [When to reconsider this decision]

### 🚦 Human Review Gate - Data Structures

**STOP**: AI must not proceed until human completes this review.

- [ ] **Data structure selection reviewed by human**
- [ ] **Access patterns accurately captured**
- [ ] **Performance implications understood and acceptable**
- [ ] **Concurrency characteristics appropriate**
- [ ] **Trade-offs explicitly acknowledged**

**Human Notes**: [Human feedback, adjustments, concerns]

---

## 2. Algorithm Selection

**Reference**: `doc/README-TECHNICAL-PLANNING-CLOJURE.md#algorithms`

### Problem Characteristics

**From Research Phase**: [Import complexity requirements from research-detailed]

**Input Characteristics**:

- Input size range: [Small (<100), Medium (<10K), Large (>10K), Unbounded]
- Input distribution: [Random, sorted, nearly-sorted, adversarial]
- Input constraints: [Duplicates, sparse, dense, bounded values]

**Performance Requirements**:

- Target latency: [Time per operation]
- Target throughput: [Operations per second]
- Acceptable complexity: [O(?) time, O(?) space]

### Algorithm Options

Evaluate candidate algorithms:

| Algorithm | Time Complexity | Space Complexity | Pros | Cons | Clojure Fit |
|-----------|----------------|------------------|------|------|-------------|
| [Option 1] | O(?) | O(?) | [Benefits] | [Drawbacks] | [Idiomatic?] |
| [Option 2] | O(?) | O(?) | [Benefits] | [Drawbacks] | [Idiomatic?] |

**Clojure-Specific Considerations**:

- Sequence abstraction: [Can we use lazy seqs, transducers?]
- Reducers: [Parallel fold applicable?]
- Built-in functions: [Can we use filter, map, reduce, group-by, etc.?]
- Type hints: [Where to avoid reflection?]

### Decision

**Selected Algorithm**: [Name and rationale]

**Justification**:

- Complexity fit: [Meets performance requirements]
- Implementation clarity: [Idiomatic Clojure, maintainable]
- Testing approach: [How to validate correctness]

**Performance Validation Plan**: [How to benchmark and verify]

### 🚦 Human Review Gate - Algorithm Selection

**STOP**: AI must not proceed until human completes this review.

- [ ] **Algorithm selection reviewed by human**
- [ ] **Complexity analysis correct**
- [ ] **Implementation approach clear**
- [ ] **Performance validation plan acceptable**

**Human Notes**: [Human feedback, adjustments, concerns]

---

## 3. Concurrency Strategy

**Reference**: `doc/README-TECHNICAL-PLANNING-CLOJURE.md#concurrency`

**Agent Recommendation**: Consider calling `software-architect` agent for concurrency pattern validation.

### Concurrency Requirements

**From Research Phase**: [Import concurrency patterns from research-detailed]

**Concurrency Model Needed**:

- [ ] No concurrency (single-threaded)
- [ ] Immutable reads (no coordination)
- [ ] Coordinated updates (atom, ref, agent)
- [ ] Message passing (core.async)
- [ ] Parallel processing (pmap, reducers, fork/join)

**Coordination Needs**:

- [ ] Single atom updates
- [ ] Coordinated multi-ref updates (STM)
- [ ] Asynchronous processing (agents)
- [ ] Request-response (core.async)
- [ ] Pub-sub (core.async)

### Clojure Concurrency Primitives

Evaluate options:

**Atoms** (uncoordinated synchronous updates):

- Use when: Single piece of independent state, synchronous updates, retry acceptable
- Pattern: `(swap! atom-ref update-fn args)`
- Thread-safe: Yes
- Coordination: No (use refs for coordinated updates)

**Refs + STM** (coordinated synchronous updates):

- Use when: Multiple pieces of state must update together, ACID guarantees
- Pattern: `(dosync (alter ref1 f1) (alter ref2 f2))`
- Thread-safe: Yes
- Coordination: Yes (all-or-nothing transactions)

**Agents** (asynchronous updates):

- Use when: Fire-and-forget updates, I/O operations, error isolation
- Pattern: `(send agent-ref update-fn args)`
- Thread-safe: Yes
- Coordination: No (but serialized per-agent)

**core.async Channels** (message passing):

- Use when: Decoupled communication, backpressure, complex workflows
- Pattern: `(go (>! chan value))` `(go (<! chan))`
- Thread-safe: Yes
- Coordination: Via message passing

**Futures/Promises** (async computation):

- Use when: One-time async computation, parallel tasks
- Pattern: `(future (expensive-computation))` `@future-result`
- Thread-safe: Yes
- Coordination: Via deref blocking

### Decision

**Selected Concurrency Model**: [Name and rationale]

**Pattern Details**:

- Primitives used: [Atoms, refs, agents, channels, futures]
- State coordination: [How state is synchronized]
- Error handling: [How errors propagate and are handled]
- Backpressure: [How to handle overload]

**Rationale**:

- Matches concurrency needs: [How pattern fits requirements]
- Clojure idiomatic: [Standard patterns in ecosystem]
- Testability: [How to test concurrent behavior]

**Risks**:

- Deadlock potential: [Analysis]
- Race conditions: [Analysis]
- Performance bottlenecks: [Analysis]

### 🚦 Human Review Gate - Concurrency Strategy

**STOP**: AI must not proceed until human completes this review.

- [ ] **Concurrency strategy reviewed by human**
- [ ] **Primitives appropriate for use case**
- [ ] **Error handling strategy clear**
- [ ] **Risks identified and mitigated**
- [ ] **Testing approach for concurrent behavior defined**

**Human Notes**: [Human feedback, adjustments, concerns]

---

## 4. Testing Strategy

**Reference**: `doc/README-TECHNICAL-PLANNING-CLOJURE.md#testing`

**Agent Recommendation**: Consider calling `test-strategist` agent for comprehensive test planning.

### Test Requirements

**From Research Phase**: [Import testing patterns from research-detailed]

**Coverage Goals**:

- Unit test coverage: [Percentage or critical paths]
- Integration test needs: [External dependencies to test]
- Property-based test opportunities: [Complex logic to validate]
- Performance benchmarks: [Targets to validate]

### Test Approach

**Unit Tests** (clojure.test):

- Testing philosophy: [Test-first, test-after, critical paths only]
- Test organization: [Per-function, per-feature, per-behavior]
- Fixtures needed: [Setup/teardown requirements]
- Mocking strategy: [What to mock, how to mock]

**Property-Based Tests** (test.check):

- Applicable to: [Which functions benefit from generative testing]
- Generators needed: [Custom generators to create]
- Properties to validate: [Invariants, round-trip, idempotence, etc.]
- Example:

  ```clojure
  (defspec round-trip-property
    (prop/for-all [data (gen/map gen/keyword gen/any)]
      (= data (parse (serialize data)))))
  ```

**Integration Tests**:

- External dependencies: [Databases, APIs, file systems]
- Test data strategy: [Fixtures, factories, generated]
- Test isolation: [How to avoid test interdependencies]
- Cleanup strategy: [Reset state between tests]

**Performance Benchmarks**:

- Baseline measurements: [Current performance if applicable]
- Target metrics: [Acceptable latency/throughput]
- Benchmarking tools: [Criterium, custom harness]

### Test Coverage Plan

| Component | Unit Tests | Property Tests | Integration Tests | Benchmarks |
|-----------|-----------|----------------|-------------------|------------|
| [Module A] | ✓ | - | - | - |
| [Module B] | ✓ | ✓ | ✓ | - |
| [Module C] | ✓ | - | ✓ | ✓ |

### 🚦 Human Review Gate - Testing Strategy

**STOP**: AI must not proceed until human completes this review.

- [ ] **Testing strategy reviewed by human**
- [ ] **Coverage goals appropriate**
- [ ] **Property-based tests identified for complex logic**
- [ ] **Integration test plan clear**
- [ ] **Performance benchmarks defined**

**Human Notes**: [Human feedback, adjustments, concerns]

---

## 5. Security Considerations

**Reference**: `doc/README-TECHNICAL-PLANNING-CLOJURE.md#security`

**Agent Recommendation**: Consider calling `security-analyst` agent for threat modeling.

### Security Requirements

**From Research Phase**: [Import security model from research-detailed]

**Threat Model**:

- Trust boundaries: [What components trust each other]
- Threat actors: [Who might attack, what capabilities]
- Assets to protect: [Data, operations, resources]
- Attack vectors: [Injection, XSS, CSRF, DoS, etc.]

### Security Controls

**Authentication**:

- Required: [Yes/No]
- Mechanism: [JWT, session, API key, etc.]
- Integration: [How to verify identity]

**Authorization**:

- Model: [RBAC, ABAC, resource-based]
- Enforcement points: [Where to check permissions]
- Implementation: [Guards, middleware, decorators]

**Input Validation**:

- Schema validation: [clojure.spec, schema, malli]
- Sanitization: [What inputs need sanitization]
- Rejection strategy: [Fail-fast vs graceful degradation]
- Example:

  ```clojure
  (s/def ::user-input (s/keys :req-un [::name ::email ::age]))
  (s/valid? ::user-input data) ;; Validate before processing
  ```

**Audit Logging**:

- Events to log: [Authentication, authorization failures, data access]
- Log format: [Structured logging, JSON, EDN]
- Retention: [How long to keep logs]
- Privacy: [PII scrubbing, redaction]

**Sensitive Data Handling**:

- Encryption: [At rest, in transit]
- Secrets management: [Environment vars, vault, AWS Secrets Manager]
- PII protection: [What data is PII, how to protect]

### 🚦 Human Review Gate - Security Considerations

**STOP**: AI must not proceed until human completes this review.

- [ ] **Security considerations reviewed by human**
- [ ] **Threat model appropriate**
- [ ] **Authentication/authorization strategy clear**
- [ ] **Input validation comprehensive**
- [ ] **Audit logging sufficient**
- [ ] **Sensitive data protection adequate**

**Human Notes**: [Human feedback, adjustments, concerns]

---

## 6. Performance Analysis

**Reference**: `doc/README-TECHNICAL-PLANNING-CLOJURE.md#performance`

**Agent Recommendation**: Consider calling `performance-engineer` agent for optimization strategy.

### Performance Requirements

**From Research Phase**: [Import performance baselines from research-detailed]

**Performance Targets**:

- Latency: [P50, P95, P99 targets]
- Throughput: [Operations per second]
- Resource limits: [Memory, CPU, I/O budgets]
- Scalability: [Expected load growth]

### Performance Strategy

**Optimization Approach**:

1. Measure first (establish baseline)
2. Identify bottlenecks (profiling)
3. Optimize hot paths
4. Validate improvements (benchmarking)

**Clojure Performance Techniques**:

**Avoid Reflection**:

```clojure
;; Bad: Reflection on every call
(defn slow [^Object obj] (.someMethod obj))

;; Good: Type hint eliminates reflection
(defn fast [^SomeClass obj] (.someMethod obj))
```

**Use Transients for Batch Operations**:

```clojure
;; 5-10x faster for large batch builds
(persistent!
  (reduce (fn [acc item] (conj! acc item))
          (transient [])
          items))
```

**Leverage Transducers**:

```clojure
;; Single pass, no intermediate collections
(transduce (comp (filter even?) (map #(* % %))) + (range 1000))
```

**Reducers for Parallel Processing**:

```clojure
;; Automatic parallelization with fold
(require '[clojure.core.reducers :as r])
(r/fold + (r/map inc (range 1000000)))
```

**Lazy Sequences** (when appropriate):

- Use for: Large or infinite sequences, defer computation
- Avoid for: Realized anyway, side effects, resource holding

**Memoization** (when appropriate):

- Use for: Pure functions, expensive computation, repeated calls
- Consider: Memory trade-off, cache invalidation

### Profiling Plan

**Tools**:

- YourKit, VisualVM: [JVM profiling]
- Criterium: [Microbenchmarking]
- clj-async-profiler: [Flame graphs]

**Metrics to Measure**:

- CPU time per operation
- Memory allocation rate
- GC pause time
- Thread contention

### Performance Validation

**Benchmarks to Create**:

1. [Scenario 1]: [Target metric]
2. [Scenario 2]: [Target metric]

**Acceptance Criteria**: [What performance is acceptable]

### 🚦 Human Review Gate - Performance Analysis

**STOP**: AI must not proceed until human completes this review.

- [ ] **Performance analysis reviewed by human**
- [ ] **Performance targets realistic**
- [ ] **Optimization strategy sound**
- [ ] **Profiling plan clear**
- [ ] **Premature optimization avoided**

**Human Notes**: [Human feedback, adjustments, concerns]

---

## 7. Language Best Practices

**Reference**: `doc/README-TECHNICAL-PLANNING-CLOJURE.md#best-practices`

### Clojure Idioms for This Feature

**Data Modeling**:

- [ ] Use maps for flexible entities
- [ ] Use records for performance-critical types
- [ ] Use protocols for polymorphism
- [ ] Prefer data over objects

**Namespace Organization**:

- [ ] Follow standard layout (interface.clj, core.clj, impl/...)
- [ ] Keep namespaces focused (single responsibility)
- [ ] Use require with explicit aliases
- [ ] Avoid circular dependencies

**Error Handling**:

- [ ] Use ex-info for rich error data
- [ ] Consider Either/Result monad for expected errors
- [ ] Let exceptions bubble for unexpected errors
- [ ] Provide helpful error messages

**State Management**:

- [ ] Minimize mutable state
- [ ] Use atoms for independent state
- [ ] Use refs for coordinated state
- [ ] Use agents for async state

### Polylith Considerations

**If this is a Polylith workspace:**

**Agent Recommendation**: Consider calling `polylith-architect` agent for component design.

**Component Boundaries**:

- [ ] Single, well-defined responsibility
- [ ] Clear interface contract (interface.clj)
- [ ] No direct access to other component internals
- [ ] Dependencies flow one direction only

**Interface Design**:

- [ ] Minimal surface area (expose only what's needed)
- [ ] Stable contracts (versioning strategy for changes)
- [ ] Data in, data out (pure functions preferred)
- [ ] Well-documented with examples

**Example Interface**:

```clojure
(ns cloud.xional.feature.interface
  "Public API for feature component")

(defn process-data
  "Process data according to feature logic.

  Input: {:data map :options map}
  Output: {:result any :status keyword}

  Contract: This signature is stable. Breaking changes require new function."
  [input]
  ;; Delegate to core
  )
```

**Dependency Management**:

- [ ] Components depend on other interfaces only
- [ ] No circular dependencies
- [ ] Validate with `poly check`

### 🚦 Human Review Gate - Language Best Practices

**STOP**: AI must not proceed until human completes this review.

- [ ] **Language best practices reviewed by human**
- [ ] **Clojure idioms appropriate**
- [ ] **Polylith boundaries clear (if applicable)**
- [ ] **Interface contracts well-defined**
- [ ] **Anti-patterns avoided**

**Human Notes**: [Human feedback, adjustments, concerns]

---

## Implementation Strategy

### Phase Breakdown

1. **Foundation** (Est: Xh)
   - [ ] [Specific deliverable]
   - Success criteria: [Measurable outcome]
   - Technical focus: [From data structures, algorithms, etc.]

2. **Core Implementation** (Est: Yh)
   - [ ] [Specific deliverable]
   - Success criteria: [Measurable outcome]
   - Technical focus: [From concurrency, performance, etc.]

3. **Testing & Validation** (Est: Zh)
   - [ ] [Specific deliverable]
   - Success criteria: [Measurable outcome]
   - Technical focus: [From testing strategy]

### Risk Mitigation Plan

| Risk | Probability | Impact | Mitigation | Technical Area |
|------|-------------|--------|------------|----------------|
| [Risk] | High/Med/Low | High/Med/Low | [Strategy] | [Data structures, algorithms, etc.] |

### Architecture Decisions

**Integration with ADR System**: Document decisions that warrant formal ADRs here, then create actual ADR files during implementation.

1. **Potential ADR**: [Decision Title]
   - Context: [What forces are at play]
   - Decision: [What we've decided]
   - Consequences: [What this implies]
   - Technical Areas: [Data structures, concurrency, etc.]
   - ADR Required?: [Yes/No - justify based on scope and impact]
   - ADR Tags: [Suggest tags like :architecture, :performance, :security, etc.]

**ADR Creation Tasks**: [List decisions that need formal ADRs during implementation]

- [ ] Create ADR for [Decision Title] with tags [:tag1, :tag2]
- [ ] Update existing ADR if this decision modifies previous choices

## Phase Transition Checklist

Ready for Implementation when:

- [ ] ADR searches completed and relevant decisions reviewed
- [ ] No conflicts identified with existing ADRs
- [ ] **All 7 technical sections reviewed and human-approved**
- [ ] All human review gates checked off
- [ ] Technical decisions documented and rationale clear
- [ ] ADR creation tasks identified for implementation phase
- [ ] Test strategy defined and comprehensive
- [ ] Performance targets established
- [ ] Security considerations addressed
- [ ] Rollback plan established

**Next Phase**: Implementation-Detailed (Scheduled: [Date/Time])

**Recommendation**: Use `runnote-launch implementation-detailed [Topic]` to track adherence to technical decisions made here.
