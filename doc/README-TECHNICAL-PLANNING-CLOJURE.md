# Clojure Technical Planning Guide

## Overview

This guide provides Clojure-specific technical planning patterns for use with `planning-detailed` RunNotes. Use this as a reference when making decisions in each technical area.

**Audience**: Developers using RunNotes for Clojure/JVM projects, including Polylith architectures and Datomic/Datalevin databases.

**Purpose**: Provide concrete guidance, examples, and decision frameworks for technical planning in the Clojure ecosystem.

---

## Table of Contents

- [Data Structures](#data-structures)
- [Algorithms](#algorithms)
- [Concurrency](#concurrency)
- [Testing](#testing)
- [Security](#security)
- [Performance](#performance)
- [Best Practices](#best-practices)
- [Polylith-Specific](#polylith-specific)
- [Datomic/Datalevin](#datomicdatalevin)

---

## Data Structures

### Clojure Persistent Collections

#### Persistent Vector (`[]`)

**Characteristics**:

- Complexity: O(log32 N) ≈ O(1) for practical sizes for get/assoc/update
- O(1) for conj (append to end)
- Indexed access via `(nth vector index)`
- Sequential iteration

**Best for**:

- Indexed collections (need position-based access)
- Append-heavy workloads
- Sequential iteration
- Ordered data

**Avoid when**:

- Key-value lookups dominate (use map instead)
- Frequent insertions in middle (use lists for that, but rare)
- Need sorted traversal (use sorted-set or sorted-map)

**Example**:

```clojure
;; Append-heavy collection
(def events (atom []))
(swap! events conj {:type :user-login :timestamp (System/currentTimeMillis)})

;; Indexed access
(nth events 0) ;; First event
(get events 0) ;; Same as nth, but returns nil if out of bounds

;; Update by index
(update events 0 assoc :processed true)
```

**Transient for Batch Construction**:

```clojure
;; 5-10x faster for large batch builds
(defn build-large-vector [n]
  (persistent!
    (loop [i 0 acc (transient [])]
      (if (< i n)
        (recur (inc i) (conj! acc i))
        acc))))
```

#### Persistent Map (`{}`)

**Characteristics**:

- Complexity: O(log32 N) ≈ O(1) for practical sizes for get/assoc/dissoc/update
- Unordered (hash-map)
- Key-value pairs

**Best for**:

- Key-value lookups
- Frequent updates to specific keys
- Flexible schemas (keyword keys)
- Configuration data

**Avoid when**:

- Need sorted traversal (use sorted-map)
- Need stable iteration order (use array-map for small maps, or sorted-map)
- Indexed access needed (use vector)

**Example**:

```clojure
;; Entity modeling
(def user {:id 123
           :name "Alice"
           :email "alice@example.com"
           :roles #{:admin :user}})

;; Lookup
(get user :name) ;; "Alice"
(:name user) ;; Same, idiomatic

;; Update
(assoc user :last-login (System/currentTimeMillis))
(update user :roles conj :superuser)
(dissoc user :email) ;; Remove key

;; Transient for batch updates
(persistent!
  (reduce (fn [acc item]
            (assoc! acc (:id item) item))
          (transient {})
          items))
```

**Nested Updates**:

```clojure
;; Update nested value
(assoc-in user [:profile :avatar] "https://...")
(update-in user [:profile :followers] inc)

;; Get nested value
(get-in user [:profile :avatar])
```

#### Persistent Set (`#{}`)

**Characteristics**:

- Complexity: O(log32 N) for contains?, conj, disj
- Unordered (hash-set)
- No duplicates

**Best for**:

- Membership testing
- Unique collections
- Set operations (union, intersection, difference)

**Example**:

```clojure
(def active-users #{123 456 789})

;; Membership
(contains? active-users 123) ;; true
(active-users 123) ;; 123 (acts as function returning member or nil)

;; Add/remove
(conj active-users 999)
(disj active-users 456)

;; Set operations
(require '[clojure.set :as set])
(set/union active-users #{111 222})
(set/intersection active-users other-users)
(set/difference active-users banned-users)
```

#### Sorted-Map / Sorted-Set

**Characteristics**:

- Complexity: O(log N) for operations (slower than hash-map)
- Ordered by comparator
- Supports range queries

**Best for**:

- Need ordered traversal
- Range queries (subseq, rsubseq)
- Comparator-based ordering

**Example**:

```clojure
;; Time-series events sorted by timestamp
(def events (sorted-map))
(def events (assoc events timestamp-1 event-1))

;; Range query: all events between t1 and t2
(subseq events >= start-time <= end-time)

;; Sorted set for ordered unique values
(def priority-queue (sorted-set-by compare-priority task-1 task-2))
```

### Transient Collections

**When to Use**:

- Building large collections in batch
- Performance-critical loops
- Converting to persistent at end

**When to Avoid**:

- Collection shared across threads (not thread-safe during construction)
- Small collections (overhead not worth it)
- Incremental updates (persistent already efficient)

**Pattern**:

```clojure
(defn build-index [items]
  (persistent!
    (reduce (fn [acc item]
              (assoc! acc (:id item) item))
            (transient {})
            items)))
```

**Performance**: 5-10x faster for large batch operations.

### Java Interop Collections

**When to Use**:

- Interop with Java libraries expecting mutable collections
- Performance-critical tight loops where persistent overhead too high
- Already in mutable context (rare in Clojure)

**Trade-offs**:

- Lose immutability benefits
- Lose STM compatibility
- Manual synchronization if shared
- Requires defensive copying for safety

**Example**:

```clojure
(import '[java.util ArrayList HashMap])

;; Mutable ArrayList for performance-critical batch build
(defn process-large-batch [items]
  (let [results (ArrayList.)]
    (doseq [item items]
      (.add results (process item)))
    (vec results))) ;; Convert back to persistent vector
```

**Anti-pattern**: Using Java collections by default. Prefer Clojure persistent collections unless profiling shows bottleneck.

### Decision Framework

**Questions to Ask**:

1. **What's the primary access pattern?**
   - Key-value lookup → map
   - Indexed access → vector
   - Membership testing → set
   - Sequential iteration → vector or list

2. **What's the size?**
   - Small (<100 items) → any structure works, prefer simplicity
   - Medium (100-10K) → persistent collections excellent
   - Large (>10K) → consider transients for batch construction
   - Unbounded → lazy sequences or core.async channels

3. **Do you need ordering?**
   - No → hash-map, hash-set
   - Yes, insertion order → array-map (small), or track separately
   - Yes, sorted order → sorted-map, sorted-set

4. **Is this concurrent?**
   - Single-threaded → any structure
   - Multi-threaded reads → persistent (safe by default)
   - Multi-threaded writes → use atoms/refs with persistent, or core.async

5. **Is performance critical?**
   - No → persistent collections (simple, safe)
   - Yes → measure first, then consider transients or Java interop

---

## Algorithms

### Complexity Analysis

Clojure collections have different complexity characteristics than imperative languages:

**Persistent collections cost**:

- Vector get/assoc: O(log32 N) ≈ O(1) for practical sizes
- Map get/assoc/dissoc: O(log32 N) ≈ O(1) for practical sizes
- Sorted-map/sorted-set: O(log N)

**Structural sharing**: Updates create new versions sharing structure, not full copies.

### Sequence Abstraction

**Lazy Sequences**:

- Use for: Large or infinite sequences, pipeline transformations
- Avoid for: Realized anyway, side effects, holding resources

**Example**:

```clojure
;; Process large file lazily
(with-open [rdr (io/reader "large-file.txt")]
  (doall
    (map process-line (line-seq rdr))))
;; Note: doall forces realization inside with-open

;; Infinite sequence (lazy)
(def fibonacci
  ((fn fib [a b]
     (lazy-seq (cons a (fib b (+ a b)))))
   0 1))

(take 10 fibonacci) ;; (0 1 1 2 3 5 8 13 21 34)
```

**Pitfall**: Holding onto head of lazy sequence causes memory leak.

```clojure
;; BAD: Holding head
(let [xs (range 1000000)]
  (last xs)) ;; Realizes entire sequence, then gets last

;; GOOD: Don't hold head
(last (range 1000000)) ;; Only keeps last element
```

### Transducers

**When to Use**:

- Composable transformations
- No intermediate collections
- Performance-critical pipelines

**Example**:

```clojure
;; Without transducers: creates intermediate collections
(reduce + (map inc (filter even? (range 1000))))

;; With transducers: single pass, no intermediates
(transduce (comp (filter even?) (map inc)) + (range 1000))

;; Reusable transducer
(def xf (comp (filter even?) (map inc)))
(transduce xf + (range 1000))
(into [] xf (range 100))
(sequence xf (range 100))
```

**Performance**: Can be significantly faster for large collections (no intermediate allocations).

### Reducers

**When to Use**:

- Parallel processing of large collections
- CPU-bound transformations
- Fork-join parallelism

**Example**:

```clojure
(require '[clojure.core.reducers :as r])

;; Parallel fold
(r/fold + (r/map inc (vec (range 1000000))))

;; Parallel filter + map
(r/fold +
        (r/map inc
               (r/filter even? (vec (range 1000000)))))
```

**Requirements**:

- Collection must support reduce (vectors, maps do; lazy seqs don't)
- Operations must be associative and commutative for `fold`

**Trade-offs**:

- Overhead for small collections
- Requires ForkJoinPool (JVM threads)

### Common Algorithms in Clojure

**Filtering**:

```clojure
(filter pred coll) ;; Lazy
(filterv pred coll) ;; Eager, returns vector
(remove pred coll) ;; Lazy, opposite of filter
```

**Mapping**:

```clojure
(map f coll) ;; Lazy
(mapv f coll) ;; Eager, returns vector
(map-indexed (fn [idx item] ...) coll) ;; With index
```

**Grouping**:

```clojure
(group-by :type events)
;; {:login [...] :logout [...]}

(frequencies [1 2 1 3 2 1])
;; {1 3, 2 2, 3 1}
```

**Sorting**:

```clojure
(sort coll) ;; Natural order
(sort-by :timestamp events) ;; By key
(sort-by :priority compare-priority tasks) ;; Custom comparator
```

**Searching**:

```clojure
(some pred coll) ;; Find first truthy result
(filter pred coll) ;; Find all matching
(keep f coll) ;; Like map, but removes nils
```

### Decision Framework

**Questions to Ask**:

1. **What's the expected input size?**
   - Small (<1K) → Any approach works, prefer clarity
   - Medium (1K-100K) → Sequence operations, transducers
   - Large (>100K) → Transducers, reducers, consider parallelism

2. **What's the time complexity requirement?**
   - O(1) → Hash-map lookup, vector indexed access
   - O(log N) → Sorted-map, sorted-set, binary search
   - O(N) → Sequence operations (filter, map, reduce)
   - O(N log N) → Sorting
   - O(N²) → Avoid if possible, or use caching

3. **Is this CPU-bound or I/O-bound?**
   - CPU-bound → Consider reducers for parallelism
   - I/O-bound → Consider core.async for concurrency

4. **Do I need lazy evaluation?**
   - Yes → Use lazy sequences (map, filter, etc.)
   - No → Use eager variants (mapv, filterv) or transducers

5. **Is this a one-time operation or repeated?**
   - One-time → Simple approach
   - Repeated → Consider memoization, caching, or pre-computation

---

## Concurrency

Clojure provides multiple concurrency primitives, each for different use cases.

### Atoms

**Use Case**: Uncoordinated, synchronous state updates

**Characteristics**:

- Synchronous (caller waits for completion)
- Compare-and-swap (CAS) - retries on contention
- No coordination with other refs/atoms
- Thread-safe

**When to Use**:

- Single piece of independent state
- Updates are fast (retry is acceptable)
- No coordination with other state needed

**Example**:

```clojure
(def counter (atom 0))

;; Swap: apply function to current value
(swap! counter inc) ;; 1
(swap! counter + 10) ;; 11

;; Reset: set to new value directly
(reset! counter 0)

;; Swap with validation
(def positive-counter
  (atom 0 :validator (fn [x] (>= x 0))))

(swap! positive-counter dec) ;; Throws if result < 0
```

**High Contention**:

```clojure
;; If many threads updating same atom, consider:
;; 1. Reduce contention by sharding
(def counters (vec (repeatedly 10 #(atom 0))))
(defn increment-sharded [thread-id]
  (swap! (nth counters (mod thread-id 10)) inc))

;; 2. Use agents for async updates
```

### Refs + STM

**Use Case**: Coordinated, synchronous state updates (transactions)

**Characteristics**:

- Synchronous (caller waits)
- ACID transactions via Software Transactional Memory (STM)
- Coordinated updates (all-or-nothing)
- Automatic retry on conflicts

**When to Use**:

- Multiple pieces of state must update together
- Need ACID guarantees
- Complex state transitions requiring consistency

**Example**:

```clojure
(def account-a (ref 1000))
(def account-b (ref 500))

;; Transfer money between accounts (atomic)
(defn transfer [from to amount]
  (dosync
    (alter from - amount)
    (alter to + amount)))

(transfer account-a account-b 100)
;; Both accounts updated, or neither (if validation fails)

;; With validation
(def validated-account
  (ref 1000 :validator (fn [x] (>= x 0))))

;; Transfer fails if would make account negative
```

**commute vs alter**:

```clojure
(dosync
  (alter account + 10)) ;; Order matters, may retry

(dosync
  (commute account + 10)) ;; Order doesn't matter (commutative)
                           ;; More efficient, fewer retries
```

**When to Avoid**:

- Single piece of state (use atom instead)
- I/O inside transaction (not allowed, use agents)
- High contention (STM retries expensive)

### Agents

**Use Case**: Asynchronous, independent state updates

**Characteristics**:

- Asynchronous (returns immediately)
- Updates serialized per-agent (no concurrent updates to same agent)
- Actions run in thread pool
- I/O allowed (unlike refs)

**When to Use**:

- Fire-and-forget updates
- I/O operations
- Background processing
- Error isolation (errors don't crash main thread)

**Example**:

```clojure
(def log-agent (agent []))

;; Send: update asynchronously in thread pool
(send log-agent conj {:level :info :msg "Application started"})

;; Send-off: for potentially blocking operations (separate thread pool)
(send-off log-agent (fn [log] (write-to-disk log) log))

;; Error handling
(defn handle-error [agent exception]
  (println "Error in agent:" exception))

(set-error-handler! log-agent handle-error)

;; Wait for all actions to complete
(await log-agent)
```

**Agent vs Atom**:

- Atom: Synchronous, fast updates, no I/O
- Agent: Asynchronous, can do I/O, serialized per-agent

### core.async Channels

**Use Case**: Message passing, decoupled communication, complex workflows

**Characteristics**:

- Asynchronous message passing
- Channels for communication
- Go blocks for lightweight "processes"
- Backpressure via bounded buffers

**When to Use**:

- Producer-consumer patterns
- Request-response workflows
- Decoupled components
- Event-driven systems
- Pipeline processing

**Example**:

```clojure
(require '[clojure.core.async :refer [chan go >! <! close!]])

;; Create channel
(def ch (chan 10)) ;; Bounded buffer of 10

;; Producer: put values on channel
(go
  (doseq [x (range 5)]
    (>! ch x))
  (close! ch))

;; Consumer: take values from channel
(go
  (loop []
    (when-some [x (<! ch)]
      (println "Received:" x)
      (recur))))

;; Timeout
(require '[clojure.core.async :refer [timeout]])
(go
  (let [result (<! (timeout 1000))]
    (println "Timeout after 1 second")))

;; Select: wait for first available channel
(require '[clojure.core.async :refer [alts!]])
(go
  (let [[value port] (alts! [ch1 ch2 (timeout 1000)])]
    (cond
      (= port ch1) (println "From ch1:" value)
      (= port ch2) (println "From ch2:" value)
      :else (println "Timeout"))))
```

**Pub-Sub Pattern**:

```clojure
(require '[clojure.core.async :refer [pub sub]])

(def events-ch (chan))
(def events-pub (pub events-ch :type))

;; Subscribe to :login events
(def login-ch (chan))
(sub events-pub :login login-ch)

;; Publish event
(go (>! events-ch {:type :login :user "alice"}))
```

**Pipeline Processing**:

```clojure
(require '[clojure.core.async :refer [pipeline]])

(def input-ch (chan 100))
(def output-ch (chan 100))

;; Parallel processing with 4 threads
(pipeline 4 output-ch (map process) input-ch)
```

### Futures / Promises

**Use Case**: One-time asynchronous computations

**Characteristics**:

- Future: Computation starts immediately in background
- Promise: Value delivered later
- Deref blocks until value available

**When to Use**:

- One-time expensive computation
- Parallel tasks
- Fire-and-forget with result needed later

**Example**:

```clojure
;; Future: starts immediately
(def result (future (expensive-computation)))

;; Do other work...

;; Block until result available
@result
(deref result 1000 :timeout) ;; With timeout

;; Promise: deliver value later
(def p (promise))

;; In another thread
(future (deliver p (fetch-data)))

;; Block until delivered
@p
```

**Parallel Tasks**:

```clojure
(defn parallel-sum [colls]
  (let [futures (map #(future (reduce + %)) colls)]
    (reduce + (map deref futures))))
```

### Decision Framework

**Questions to Ask**:

1. **Is this coordinated or independent state?**
   - Independent → Atom or Agent
   - Coordinated (must update together) → Refs + STM

2. **Is this synchronous or asynchronous?**
   - Synchronous (wait for result) → Atom or Refs
   - Asynchronous (fire-and-forget) → Agent or core.async

3. **Does this involve I/O?**
   - Yes → Agent (serialized I/O) or core.async (concurrent I/O)
   - No → Atom or Refs

4. **Is this message passing or shared state?**
   - Message passing → core.async channels
   - Shared state → Atom/Refs/Agents

5. **What's the contention level?**
   - Low → Atom, Refs
   - High → Shard atoms, or use core.async

6. **Is this a one-time operation?**
   - Yes → Future/Promise
   - Ongoing → Atom/Agent/core.async

**Concurrency Primitives Table**:

| Primitive | Sync/Async | Coordinated | I/O Allowed | Retry | Use Case |
|-----------|------------|-------------|-------------|-------|----------|
| Atom | Sync | No | No | Yes (CAS) | Independent state |
| Ref | Sync | Yes (STM) | No | Yes (STM) | Coordinated state |
| Agent | Async | No | Yes | No | Async updates, I/O |
| core.async | Async | Via messages | Yes | No | Message passing |
| Future | Async | No | Yes | No | One-time async task |

---

## Testing

### Unit Testing (clojure.test)

**Basic Pattern**:

```clojure
(ns myapp.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [myapp.core :as core]))

(deftest addition-test
  (testing "Addition of positive numbers"
    (is (= 4 (core/add 2 2)))
    (is (= 0 (core/add 0 0))))
  (testing "Addition with negative numbers"
    (is (= 0 (core/add 2 -2)))
    (is (= -4 (core/add -2 -2)))))
```

**Fixtures** (setup/teardown):

```clojure
(use-fixtures :each
  (fn [f]
    ;; Setup
    (reset! test-db {})
    ;; Run test
    (f)
    ;; Teardown
    (reset! test-db {})))

(use-fixtures :once
  (fn [f]
    ;; Setup once for all tests
    (start-test-server)
    (f)
    (stop-test-server)))
```

**Testing Exceptions**:

```clojure
(is (thrown? Exception (/ 1 0)))
(is (thrown-with-msg? Exception #"Divide by zero" (divide 1 0)))
```

### Property-Based Testing (test.check)

**When to Use**:

- Complex business logic
- Data transformations
- Invariants that hold across large input spaces
- Round-trip encoding/decoding

**Example**:

```clojure
(ns myapp.properties-test
  (:require [clojure.test :refer [deftest]]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]))

;; Property: reverse is its own inverse
(defspec reverse-twice-is-identity 100
  (prop/for-all [v (gen/vector gen/int)]
    (= v (reverse (reverse v)))))

;; Property: sort is idempotent
(defspec sort-idempotent 100
  (prop/for-all [v (gen/vector gen/int)]
    (= (sort v) (sort (sort v)))))

;; Round-trip property
(defspec serialize-deserialize-roundtrip 100
  (prop/for-all [data (gen/map gen/keyword gen/any)]
    (= data (deserialize (serialize data)))))
```

**Custom Generators**:

```clojure
;; Generate valid email addresses
(def email-gen
  (gen/fmap (fn [[user domain]]
              (str user "@" domain ".com"))
            (gen/tuple (gen/not-empty gen/string-alphanumeric)
                       (gen/not-empty gen/string-alphanumeric))))

(defspec email-validation-property 100
  (prop/for-all [email email-gen]
    (valid-email? email)))
```

**Shrinking** (automatic):

- test.check automatically finds minimal failing example
- Example: If `[1 2 3 4 5]` fails, shrinks to `[1]` or `[]` (minimal)

### Integration Testing

**Testing with External Dependencies**:

```clojure
;; Test with embedded database
(defn with-test-db [f]
  (let [db (create-embedded-db)]
    (try
      (binding [*db* db]
        (f))
      (finally
        (destroy-db db)))))

(use-fixtures :each with-test-db)

(deftest database-integration-test
  (is (= user (fetch-user *db* user-id))))
```

**Testing HTTP APIs**:

```clojure
;; Using clj-http
(deftest api-integration-test
  (let [response (http/get "http://localhost:3000/api/users/123")]
    (is (= 200 (:status response)))
    (is (= "Alice" (get-in response [:body :name])))))
```

**Testing core.async**:

```clojure
(deftest async-channel-test
  (let [ch (chan)]
    (go (>! ch :value))
    (is (= :value (<!! ch))))) ;; <!! blocks until value available
```

### Performance Benchmarking

**Using Criterium**:

```clojure
(require '[criterium.core :refer [bench quick-bench]])

;; Quick benchmark
(quick-bench (reduce + (range 1000)))

;; Full benchmark (more accurate)
(bench (reduce + (range 1000)))
;; Reports: mean, variance, outliers, etc.
```

**Comparing Alternatives**:

```clojure
;; Approach A: Lazy sequence
(quick-bench (doall (map inc (range 1000))))

;; Approach B: Transducer
(quick-bench (into [] (map inc) (range 1000)))

;; Approach C: Transient
(quick-bench
  (persistent!
    (reduce (fn [acc x] (conj! acc (inc x)))
            (transient [])
            (range 1000))))
```

### Test Organization

**Per-namespace Testing**:

```
src/
  myapp/
    core.clj
    utils.clj
test/
  myapp/
    core_test.clj
    utils_test.clj
```

**Test Categories**:

```clojure
;; Unit tests
(ns myapp.core-test
  (:require [clojure.test :refer [deftest is]]))

;; Integration tests
(ns myapp.integration-test
  (:require [clojure.test :refer [deftest is]]))

;; Property tests
(ns myapp.properties-test
  (:require [clojure.test.check.clojure-test :refer [defspec]]))
```

**Running Tests**:

```bash
# All tests
lein test

# Specific namespace
lein test myapp.core-test

# With test selector (requires metadata)
lein test :integration
```

### Decision Framework

**Test Approach Selection**:

1. **Unit Tests** - Always
   - Test individual functions
   - Fast, deterministic
   - High coverage of core logic

2. **Property-Based Tests** - When:
   - Complex data transformations
   - Invariants across large input spaces
   - Round-trip operations (serialize/deserialize, encode/decode)
   - Hard-to-enumerate edge cases

3. **Integration Tests** - When:
   - Testing external dependencies (DB, APIs, file systems)
   - End-to-end workflows
   - Component interaction

4. **Performance Benchmarks** - When:
   - Performance-critical code
   - Comparing alternative approaches
   - Regression prevention

**Coverage Goals**:

- Critical business logic: 100%
- Happy paths: 90%+
- Edge cases: As many as practical
- Integration: Key workflows

---

## Security

### Input Validation

**Use clojure.spec for Schema Validation**:

```clojure
(require '[clojure.spec.alpha :as s])

;; Define specs
(s/def ::id pos-int?)
(s/def ::name (s/and string? #(re-matches #"[A-Za-z ]+" %)))
(s/def ::email (s/and string? #(re-matches #".+@.+\..+" %)))
(s/def ::age (s/int-in 0 150))

(s/def ::user (s/keys :req-un [::id ::name ::email ::age]))

;; Validate
(s/valid? ::user {:id 1 :name "Alice" :email "alice@example.com" :age 30})
;; true

(s/valid? ::user {:id -1 :name "Alice" :email "invalid" :age 30})
;; false

;; Explain why invalid
(s/explain-data ::user {:id -1 :name "Alice" :email "invalid" :age 30})
;; {:problems [{:path [:id] :pred 'pos-int? :val -1 :via [::user ::id] :in [:id]}
;;             {:path [:email] :pred (fn [%] (re-matches #".+@.+\..+" %)) ...}]}
```

**Conform for Coercion**:

```clojure
(s/def ::quantity (s/and int? pos?))

;; Conform: transform input to spec
(s/conform ::quantity "10") ;; 10 (if spec includes coercion)
(s/conform ::quantity "invalid") ;; :clojure.spec.alpha/invalid
```

**Reject Invalid Input**:

```clojure
(defn create-user [user-data]
  (if (s/valid? ::user user-data)
    (save-user user-data)
    (throw (ex-info "Invalid user data"
                    {:type :validation-error
                     :problems (s/explain-data ::user user-data)}))))
```

### Authentication

**JWT Tokens** (common pattern):

```clojure
(require '[buddy.sign.jwt :as jwt])

(def secret "your-secret-key")

;; Create token
(defn create-token [user-id]
  (jwt/sign {:user-id user-id
             :exp (+ (System/currentTimeMillis) (* 1000 60 60))} ;; 1 hour
            secret))

;; Verify token
(defn verify-token [token]
  (try
    (jwt/unsign token secret)
    (catch Exception e
      nil))) ;; Invalid or expired

;; Middleware
(defn wrap-authentication [handler]
  (fn [request]
    (if-let [token (get-in request [:headers "authorization"])]
      (if-let [claims (verify-token token)]
        (handler (assoc request :user claims))
        {:status 401 :body "Unauthorized"})
      {:status 401 :body "Missing token"})))
```

### Authorization

**Role-Based Access Control (RBAC)**:

```clojure
(defn has-role? [user role]
  (contains? (:roles user) role))

(defn authorize [required-role handler]
  (fn [request]
    (if (has-role? (:user request) required-role)
      (handler request)
      {:status 403 :body "Forbidden"})))

;; Usage
(def admin-handler
  (-> handle-admin-request
      (authorize :admin)))
```

**Attribute-Based Access Control (ABAC)**:

```clojure
(defn can-edit-document? [user document]
  (or (= (:owner-id document) (:id user))
      (contains? (:roles user) :admin)))

(defn edit-document [user document-id updates]
  (let [document (fetch-document document-id)]
    (if (can-edit-document? user document)
      (update-document document updates)
      (throw (ex-info "Unauthorized" {:type :authorization-error})))))
```

### Audit Logging

**Structured Logging**:

```clojure
(require '[clojure.tools.logging :as log])

(defn audit-log [event-type user-id details]
  (log/info {:event-type event-type
             :user-id user-id
             :timestamp (System/currentTimeMillis)
             :details details}))

;; Usage
(defn login [credentials]
  (let [user (authenticate credentials)]
    (audit-log :user-login (:id user) {:ip (:remote-addr request)})
    user))

(defn delete-resource [user resource-id]
  (audit-log :resource-deleted (:id user) {:resource-id resource-id})
  (delete resource-id))
```

### Secrets Management

**Environment Variables**:

```clojure
(defn get-env [key]
  (or (System/getenv key)
      (throw (ex-info "Missing required environment variable" {:key key}))))

(def db-password (get-env "DB_PASSWORD"))
```

**Never Hardcode Secrets**:

```clojure
;; BAD
(def db-config {:password "super-secret-password"})

;; GOOD
(def db-config {:password (get-env "DB_PASSWORD")})
```

### SQL Injection Prevention

**Use Parameterized Queries**:

```clojure
(require '[clojure.java.jdbc :as jdbc])

;; BAD: String concatenation (SQL injection vulnerability)
(defn find-user-bad [username]
  (jdbc/query db [(str "SELECT * FROM users WHERE username = '" username "'")]))

;; GOOD: Parameterized query
(defn find-user-good [username]
  (jdbc/query db ["SELECT * FROM users WHERE username = ?" username]))
```

### Decision Framework

**Security Checklist**:

1. **Input Validation**
   - [ ] All user input validated with clojure.spec
   - [ ] Reject invalid input (fail-fast)
   - [ ] Sanitize for context (HTML escaping, SQL params, etc.)

2. **Authentication**
   - [ ] Secure password storage (bcrypt, scrypt)
   - [ ] Session/token management (JWTs, secure cookies)
   - [ ] Multi-factor authentication (if appropriate)

3. **Authorization**
   - [ ] Enforce principle of least privilege
   - [ ] Check permissions at every access point
   - [ ] RBAC or ABAC model documented

4. **Audit Logging**
   - [ ] Log authentication events
   - [ ] Log authorization failures
   - [ ] Log sensitive data access
   - [ ] Include timestamp, user ID, IP

5. **Secrets Management**
   - [ ] No hardcoded secrets
   - [ ] Environment variables or secrets manager
   - [ ] Rotate secrets regularly

6. **Data Protection**
   - [ ] Encrypt sensitive data at rest
   - [ ] Use HTTPS for data in transit
   - [ ] Scrub PII from logs

---

## Performance

### Profiling

**YourKit / VisualVM**:

- CPU profiling: Find hot spots
- Memory profiling: Find allocations
- Thread profiling: Find contention

**clj-async-profiler**:

```clojure
(require '[clj-async-profiler.core :as prof])

;; Start profiling
(prof/start {})

;; Run code
(your-function)

;; Stop and generate flame graph
(prof/stop {})
(prof/serve-files 8080) ;; View at http://localhost:8080
```

### Avoiding Reflection

**Type Hints**:

```clojure
;; Reflection warning: call to method someMethod can't be resolved
(defn slow [obj]
  (.someMethod obj))

;; Fixed with type hint
(defn fast [^SomeClass obj]
  (.someMethod obj))

;; Multiple type hints
(defn process [^String s ^Integer n]
  (.substring s 0 n))

;; Return type hint
(defn ^long add [^long a ^long b]
  (+ a b))
```

**Enable Reflection Warnings**:

```clojure
(set! *warn-on-reflection* true)
```

### Transients for Performance

**When to Use**:

- Batch construction of large collections
- Performance-critical loops

**Example**:

```clojure
;; Slow: Creates intermediate persistent collections
(defn build-slow [n]
  (loop [i 0 acc []]
    (if (< i n)
      (recur (inc i) (conj acc i))
      acc)))

;; Fast: Uses transient during construction
(defn build-fast [n]
  (loop [i 0 acc (transient [])]
    (if (< i n)
      (recur (inc i) (conj! acc i))
      (persistent! acc))))

;; 5-10x faster for large n
```

### Transducers

**When to Use**:

- Composable transformations
- No intermediate collections needed

**Example**:

```clojure
;; Creates intermediate collections
(defn process-slow [items]
  (reduce +
          (map #(* % %)
               (filter even? items))))

;; Single pass, no intermediates
(defn process-fast [items]
  (transduce (comp (filter even?) (map #(* % %)))
             +
             items))
```

### Memoization

**When to Use**:

- Pure functions
- Expensive computation
- Repeated calls with same arguments

**Example**:

```clojure
;; Slow: Recalculates every time
(defn fibonacci [n]
  (if (<= n 1)
    n
    (+ (fibonacci (- n 1))
       (fibonacci (- n 2)))))

;; Fast: Memoized
(def fibonacci-memo
  (memoize
    (fn [n]
      (if (<= n 1)
        n
        (+ (fibonacci-memo (- n 1))
           (fibonacci-memo (- n 2)))))))

;; Much faster for repeated calls
(fibonacci-memo 40) ;; Caches results
```

**Custom Memoization** (with bounded cache):

```clojure
(defn memoize-lru [f cache-size]
  (let [cache (atom (array-map))]
    (fn [& args]
      (if-let [cached (get @cache args)]
        cached
        (let [result (apply f args)]
          (swap! cache (fn [c]
                         (let [c' (assoc c args result)]
                           (if (> (count c') cache-size)
                             (into (array-map) (rest c'))
                             c'))))
          result)))))
```

### Parallel Processing

**Reducers**:

```clojure
(require '[clojure.core.reducers :as r])

;; Parallel fold
(r/fold + (r/map inc (vec (range 1000000))))
```

**pmap** (parallel map):

```clojure
;; Use for CPU-bound tasks with significant per-item cost
(defn process-parallel [items]
  (doall (pmap expensive-function items)))

;; Note: pmap is lazy, use doall to force realization
```

**core.async Pipeline**:

```clojure
(require '[clojure.core.async :refer [pipeline chan]])

(def input (chan 100))
(def output (chan 100))

;; Parallel processing with 4 threads
(pipeline 4 output (map process) input)
```

### Lazy Sequences Pitfalls

**Holding Head**:

```clojure
;; BAD: Realizes entire sequence into memory
(let [xs (map inc (range 1000000))]
  (println (first xs))
  (println (last xs))) ;; Still holds onto head

;; GOOD: Don't hold head
(println (first (map inc (range 1000000))))
(println (last (map inc (range 1000000))))
```

**Chunked Sequences**:

- Clojure realizes lazy sequences in chunks (32 items)
- Can cause unexpected eagerness
- Solution: Use transducers or unchunk

### Decision Framework

**Performance Optimization Checklist**:

1. **Measure First**
   - [ ] Establish baseline
   - [ ] Profile to find hot spots
   - [ ] Don't optimize prematurely

2. **Low-Hanging Fruit**
   - [ ] Add type hints to avoid reflection
   - [ ] Use transients for batch construction
   - [ ] Use transducers for pipelines

3. **Algorithmic**
   - [ ] Choose appropriate data structures
   - [ ] Optimize algorithm complexity
   - [ ] Consider caching/memoization

4. **Parallelism**
   - [ ] Use reducers for parallel fold
   - [ ] Use pmap for CPU-bound tasks
   - [ ] Use core.async for I/O concurrency

5. **Validate**
   - [ ] Benchmark after changes
   - [ ] Verify improvement
   - [ ] Document trade-offs

---

## Best Practices

### Data-Oriented Programming

**Prefer Maps Over Records** (unless performance-critical):

```clojure
;; Maps: Flexible, extensible
(def user {:id 123 :name "Alice" :email "alice@example.com"})

;; Records: Faster field access, type-based dispatch
(defrecord User [id name email])
(def user (->User 123 "Alice" "alice@example.com"))
```

**Keyword Access**:

```clojure
(:name user) ;; Idiomatic
(get user :name) ;; Also fine
(user :name) ;; Works but less common
```

### Namespace Organization

**Standard Layout**:

```
src/
  myapp/
    core.clj       ;; Main entry point
    utils.clj      ;; Utilities
    models/
      user.clj     ;; User domain logic
      order.clj    ;; Order domain logic
    api/
      handlers.clj ;; HTTP handlers
      routes.clj   ;; Routing
```

**Require with Aliases**:

```clojure
(ns myapp.core
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [myapp.utils :as utils]
            [myapp.models.user :as user]))
```

### Error Handling

**ex-info for Rich Errors**:

```clojure
(defn divide [a b]
  (if (zero? b)
    (throw (ex-info "Division by zero"
                    {:type :division-error
                     :dividend a
                     :divisor b}))
    (/ a b)))

;; Catch and inspect
(try
  (divide 10 0)
  (catch Exception e
    (let [data (ex-data e)]
      (println "Error type:" (:type data))
      (println "Details:" data))))
```

**Either/Result Monad** (for expected errors):

```clojure
;; Using library like cats or custom
(defn divide-safe [a b]
  (if (zero? b)
    {:error :division-by-zero}
    {:ok (/ a b)}))

;; Pattern match on result
(let [result (divide-safe 10 0)]
  (if (:ok result)
    (println "Result:" (:ok result))
    (println "Error:" (:error result))))
```

### Function Design

**Pure Functions Preferred**:

```clojure
;; Pure: Same input → same output, no side effects
(defn calculate-tax [amount rate]
  (* amount rate))

;; Impure: Depends on external state or causes side effects
(defn save-order [order]
  (jdbc/insert! db :orders order)) ;; Side effect: DB write
```

**Small, Focused Functions**:

```clojure
;; Good: Single responsibility
(defn validate-email [email]
  (re-matches #".+@.+\..+" email))

(defn create-user [user-data]
  (when (validate-email (:email user-data))
    (save-user user-data)))

;; Bad: Too much in one function
(defn create-user-bad [user-data]
  (when (re-matches #".+@.+\..+" (:email user-data))
    (jdbc/insert! db :users user-data)
    (send-welcome-email user-data)
    (log-user-creation user-data)))
```

### Destructuring

```clojure
;; Map destructuring
(defn greet [{:keys [first-name last-name]}]
  (str "Hello, " first-name " " last-name))

(greet {:first-name "Alice" :last-name "Smith"})

;; Vector destructuring
(defn process [[first second & rest]]
  {:first first :second second :rest rest})

(process [1 2 3 4 5])

;; Nested destructuring
(defn process-user [{:keys [name address] :as user}]
  (let [{:keys [city state]} address]
    (str name " lives in " city ", " state)))
```

### Threading Macros

**-> (thread-first)**:

```clojure
;; Nested calls (hard to read)
(str/upper-case (str/trim (str/replace "  hello world  " "world" "clojure")))

;; Thread-first (easier to read)
(-> "  hello world  "
    (str/replace "world" "clojure")
    str/trim
    str/upper-case)
```

**->> (thread-last)**:

```clojure
;; Filter, map, reduce
(->> (range 100)
     (filter even?)
     (map #(* % %))
     (reduce +))
```

**as-> (thread with custom position)**:

```clojure
(as-> {:a 1 :b 2} $
  (assoc $ :c 3)
  (dissoc $ :b)
  (update $ :a inc))
```

---

## Polylith-Specific

See `polylith-architect` agent for comprehensive Polylith patterns.

**Component Independence**:

```clojure
;; WRONG: Component depends on another component's internals
(ns cloud.xional.orders.core
  (:require [cloud.xional.inventory.core :as inventory])) ;; NO!

;; RIGHT: Component uses only interfaces
(ns cloud.xional.orders.core
  (:require [cloud.xional.inventory.interface :as inventory]))
```

**Interface Contracts**:

```clojure
;; interface.clj is the ONLY public API
(ns cloud.xional.payment.interface)

(defn process-payment
  "Process payment with stable contract.

  Input: {:amount decimal :currency string :method keyword}
  Output: {:status keyword :transaction-id string}

  This signature is stable. Breaking changes require new function."
  [payment-data]
  ;; Delegate to core
  (core/process-payment payment-data))
```

**Dependency Flow**:

```
Project → Base → Component
(No upward dependencies!)
```

---

## Datomic/Datalevin

See `datalog-database-architect` agent for comprehensive Datomic patterns.

**Schema Design**:

```clojure
;; Datomic schema
(def schema
  [{:db/ident :user/id
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity}
   {:db/ident :user/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/email
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity}
   {:db/ident :user/friends
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many}])
```

**Datalog Queries**:

```clojure
;; Find all users
(d/q '[:find ?e ?name
       :where [?e :user/name ?name]]
     db)

;; Find user by email
(d/q '[:find ?e .
       :in $ ?email
       :where [?e :user/email ?email]]
     db "alice@example.com")

;; Join: Find friends of Alice
(d/q '[:find ?friend-name
       :in $ ?alice-email
       :where
       [?alice :user/email ?alice-email]
       [?alice :user/friends ?friend]
       [?friend :user/name ?friend-name]]
     db "alice@example.com")
```

**Pull Syntax**:

```clojure
;; Pull specific attributes
(d/pull db [:user/name :user/email] user-id)

;; Pull with navigation
(d/pull db [:user/name {:user/friends [:user/name]}] user-id)
;; {:user/name "Alice"
;;  :user/friends [{:user/name "Bob"} {:user/name "Charlie"}]}
```

---

## Pattern Extraction Template

When completing `review-detailed` phase, use this template to extract patterns:

**Successful Pattern**:

- **Context**: [When this pattern applies]
- **Problem**: [What problem it solves]
- **Solution**: [Specific Clojure implementation]
- **Trade-offs**: [What you give up]
- **Example**: [Code example]

**Anti-Pattern**:

- **Context**: [When this might be tempting]
- **Problem**: [Why it's problematic]
- **Symptom**: [How to recognize it]
- **Better Approach**: [What to do instead]
- **Example**: [Code example showing both]

Add extracted patterns to appropriate sections above to continuously improve this guide.

---

## See Also

- **[README-TECHNICAL-PLANNING.md](README-TECHNICAL-PLANNING.md)** - Overview and workflow
- **[README-WORKFLOWS.md](README-WORKFLOWS.md)** - When to use detailed templates
- **[Clojure Documentation](https://clojure.org/reference)** - Official reference
- **[Polylith Documentation](https://polylith.gitbook.io/)** - Polylith architecture
- **[Datomic Documentation](https://docs.datomic.com/)** - Datomic database
