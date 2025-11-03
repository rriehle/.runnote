# Research: [Topic] - YYYY-MM-DD HH:MM

```edn :metadata
{:phase "research-detailed"
 :tag #{:set-me}
 :status :active
 :thinking-mode "think harder"
 :date {:created "YYYY-MM-DD"}}
```

## Research Questions

What we need to understand:

1. [Primary question that drives this research]
2. [Secondary questions that support understanding]
3. [Edge cases or constraints to investigate]

## Investigation Approach

- [ ] Codebase analysis: [What to examine]
- [ ] External research: [What to look up]
- [ ] **ADR Review** (if ADR integration enabled): Search existing architectural decisions
  - [ ] Search by topic: `adr-search content "[topic keywords]"`
  - [ ] Search by tag: `adr-search tag :[relevant-tag]`
  - [ ] List all ADRs: `adr-search list`
- [ ] Experiments needed: [What to test]
- [ ] Stakeholders to consult: [Who might have insights]

## Technical Discovery

### Data Structure Patterns

**Objective**: Understand existing data structure usage to inform planning decisions

**Investigation:**

- [ ] Identify similar features and their data structures
- [ ] Analyze access patterns (read-heavy? write-heavy? random? sequential?)
- [ ] Document size/scale characteristics
- [ ] Note performance characteristics observed

**Findings:**

| Feature/Component | Data Structure Used | Access Pattern | Performance Notes |
|-------------------|---------------------|----------------|-------------------|
| [Example] | `persistent-map` | Key-value lookup | Sub-ms access |

**Patterns Observed**: [What works well in this codebase]

**Anti-Patterns Found**: [What to avoid based on past issues]

### Algorithm & Complexity Requirements

**Objective**: Understand performance constraints and algorithmic needs

**Investigation:**

- [ ] Identify input size ranges (small? medium? large? unbounded?)
- [ ] Document performance requirements (latency? throughput?)
- [ ] Analyze similar algorithms in codebase
- [ ] Research complexity requirements for this problem class

**Findings:**

- **Expected Input Size**: [N items, growth rate, upper bounds]
- **Performance Requirements**: [Target latency/throughput]
- **Acceptable Complexity**: [O(?) time, O(?) space]
- **Existing Patterns**: [Similar algorithms used successfully]

**Research Notes**: [Algorithm options to explore in planning]

### Concurrency Patterns

**Objective**: Understand concurrency needs and existing patterns

**Investigation:**

- [ ] Is this feature thread-safe by nature or requires synchronization?
- [ ] Analyze existing concurrency patterns in codebase
- [ ] Document concurrency primitives currently used
- [ ] Identify coordination requirements

**Findings:**

**Current Patterns in Codebase:**

- `core.async` channels: [Where used, for what purpose]
- Atoms: [State management patterns]
- Refs/STM: [Coordinated state updates]
- Agents: [Asynchronous operations]
- Futures/Promises: [Fire-and-forget tasks]

**Concurrency Needs for This Feature:**

- [ ] Single-threaded (no concurrency needed)
- [ ] Multi-threaded reads (immutable data)
- [ ] Multi-threaded writes (coordination required)
- [ ] Message passing (decoupled communication)
- [ ] Coordinated updates (multiple refs/atoms)

**Notes**: [Concurrency requirements discovered]

### Performance Baselines

**Objective**: Establish current performance characteristics and targets

**Investigation:**

- [ ] Measure baseline performance of similar features
- [ ] Document resource utilization (CPU, memory, I/O)
- [ ] Identify bottlenecks in related code
- [ ] Research performance requirements from stakeholders

**Findings:**

**Current Performance:**

- Similar Feature A: [metrics]
- Similar Feature B: [metrics]

**Resource Constraints:**

- Memory: [Available, typical usage]
- CPU: [Cores, utilization patterns]
- I/O: [Disk, network characteristics]

**Performance Targets:**

- Latency: [Target response time]
- Throughput: [Target operations/second]
- Scalability: [Expected load growth]

**Notes**: [Performance considerations for planning]

### Security Model & Threat Landscape

**Objective**: Understand security requirements and existing patterns

**Investigation:**

- [ ] Review existing security model in codebase
- [ ] Identify trust boundaries for this feature
- [ ] Document authentication/authorization patterns
- [ ] Analyze input validation approaches
- [ ] Research threat vectors specific to this feature

**Findings:**

**Existing Security Patterns:**

- Authentication: [Current approach]
- Authorization: [RBAC, ABAC, etc.]
- Input Validation: [Schema validation, sanitization]
- Audit Logging: [What's logged, where]

**Security Requirements for This Feature:**

- [ ] User authentication required?
- [ ] Authorization model: [What needs to be checked]
- [ ] Input validation: [What needs validation]
- [ ] Sensitive data: [PII, secrets, etc.]
- [ ] Audit requirements: [What must be logged]

**Threat Considerations:**

- [Threat 1]: [Description and mitigation approach]
- [Threat 2]: [Description and mitigation approach]

### Testing Patterns

**Objective**: Understand testing approaches and requirements

**Investigation:**

- [ ] Review existing test patterns in codebase
- [ ] Identify test coverage expectations
- [ ] Analyze testing tools and frameworks used
- [ ] Document integration test patterns

**Findings:**

**Current Testing Approach:**

- Unit Tests: [Framework, patterns, coverage expectations]
- Property-Based: [test.check usage, generators]
- Integration: [Approach, fixtures, test data]
- Performance: [Benchmarking tools, baselines]

**Testing Requirements for This Feature:**

- [ ] Unit test coverage target: [percentage or critical paths]
- [ ] Property-based tests needed?: [For what logic]
- [ ] Integration tests needed?: [External dependencies]
- [ ] Performance benchmarks needed?: [Success criteria]

**Test Data Needs**: [What fixtures, generators, or test data required]

### Language-Specific Idioms & Best Practices

**Objective**: Understand language/framework patterns in this codebase

**Investigation:**

- [ ] Review coding standards documentation
- [ ] Analyze idiomatic patterns in similar features
- [ ] Document framework/library usage patterns
- [ ] Identify anti-patterns to avoid

**Findings:**

**Clojure Idioms Observed:**

- Data modeling: [Records, maps, protocols]
- Namespace organization: [Conventions]
- Error handling: [ex-info, Either monad, exceptions]
- State management: [Atoms, refs, agents usage]

**Framework Patterns:**

- [Framework/Library]: [How it's used, conventions]

**Polylith Considerations** (if applicable):

- Component boundaries: [How similar features are componentized]
- Interface contracts: [Patterns for interface.clj]
- Dependency flow: [How components interact]

**Anti-Patterns to Avoid**: [Known issues from past experience]

## Findings Log

### [HH:MM] - Initial Discovery

[Real-time findings as you investigate]

### [HH:MM] - Deep Dive into [Component]

[Detailed analysis with code snippets]

## Emerging Patterns

[Patterns, problems, or opportunities discovered]

## Constraints Identified

- **Technical**: [Hard limitations]
- **Business**: [Policy or requirement constraints]
- **Resource**: [Time, budget, or personnel limits]
- **Performance**: [Latency, throughput, scalability constraints]
- **Security**: [Compliance, threat model constraints]

## Open Questions

Questions that need answers before planning can begin:

- [ ] [Question] - Owner: [@teammate]
- [ ] [Question] - Needs: [Research/Experiment/Decision]

## Phase Transition Checklist

Ready for Planning Phase when:

- [ ] All critical questions answered
- [ ] Constraints fully understood
- [ ] Technical patterns documented
- [ ] Performance baselines established
- [ ] Security requirements clear
- [ ] At least 2 viable approaches identified
- [ ] Risks catalogued
- [ ] ADR review completed (if applicable)

**Next Phase**: Planning-Detailed (Estimated: [Date/Time])

**Recommendation**: Use `runnote-launch planning-detailed [Topic]` for in-depth technical planning with human review gates.
