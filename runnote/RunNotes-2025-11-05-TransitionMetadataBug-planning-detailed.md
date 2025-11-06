170                   (str "# " (str/capitalize next-ph) ": " topic " - " date "\n\n"
171                        "> **Phase**: " (str/capitalize next-ph) "\n"
172                        "> **Previous Phase**: " (str/capitalize current-phase) " (Completed: " date ")\n"
173                        "> **Tags**: " (:tags metadata) "\n\n"
174                        "## Context From Previous Phase\n\n"
175                        (pr-str context)))]# Planning: TransitionMetadataBug - 2025-11-05 15:37

```edn :metadata
{:phase "planning-detailed"
 :tag #{:tooling :bug-fix}
 :status :completed
 :thinking-mode "think harder"
 :complexity :medium
 :date {:created "2025-11-05" :updated "2025-11-05"}
 :related-documents
 {:runnote #{"RunNotes-2025-11-05-TransitionMetadataBug-research-detailed"}
  :code-files #{"bin/runnote-transition:162-177" "bin/runnote-transition:289"}}}
```

## Context From Research

**Problem**: `runnote-transition` generates broken metadata due to template path mismatch (bin/runnote-transition:167)

**Root Cause**:
- Incorrect path construction: `runnotes-dir "/template-" phase ".md"`
- Should be: `template-dir "/" phase ".md"`
- Template lookup ALWAYS fails, triggering broken fallback code

**Impact**: 18 files in ~/src/xional/runnotes/ with invalid EDN metadata (markdown blockquote format instead of EDN)

**Key Decision from Research**: Use fail-fast approach (Option 1) - remove fallback code, provide clear recovery guidance

**Key constraints**:
- Must fix template path resolution to use `template-dir` from config
- Must not break existing `runnote-launch` behavior
- 18 existing files need migration to valid metadata
- `runnote-validate` can detect broken metadata

**Critical insights**:
- `runnote-launch` has correct template resolution logic we can reference
- Validation tool already exists and works correctly
- Single function (`create-phase-file`) needs fixing
- Single call site (line 289) needs updating

**Technical patterns identified**:
- Template path construction: `template-dir "/" phase ".md"` (from runnote-launch)
- Config resolution: Use `resolve-runnote-template-dir` from config system
- Error handling: `ex-info` with recovery guidance (fail-fast pattern)
- Validation: `runnote-validate check` confirms metadata integrity

## Related ADRs Analysis

**N/A**: This is a bug fix in tooling, not an architectural decision. No ADR search required.

**Architectural Impact**: None - this fix restores correct behavior, does not introduce new patterns or decisions.

---

## Technical Planning Sections

**Guide Reference**: See `doc/README-TECHNICAL-PLANNING-CLOJURE.md` for comprehensive Clojure-specific guidance on each section below.

---

## 1. Data Structures

**Reference**: `doc/README-TECHNICAL-PLANNING-CLOJURE.md#data-structures`

### Problem Analysis

**From Research Phase**: Simple file I/O and string manipulation - no complex data structures needed

**Data Needs**:

- [x] String manipulation (path construction)
- [x] Sequential file processing (for migration)
- [x] Simple map for error context (ex-info data)

**Access Patterns**:

- [x] Sequential access sufficient (file processing)
- File paths: simple string concatenation
- Error context: simple map construction

**Size Characteristics**:

- 18 files to migrate (bounded, small)
- Path strings: < 256 chars typically
- No memory constraints (small data)

**Concurrency Requirements**:

- [x] Single-threaded access (file I/O)

### Decision

**Selected Structures**:

1. **Strings** for path construction (native Java/Clojure strings)
2. **Maps** (`{}`) for error context in `ex-info`
3. **Vector** (`[]`) for list of affected files (migration script)

**Justification**:

- **Path construction**: Simple string concatenation with `str` is idiomatic and sufficient
  ```clojure
  (str template-dir "/" phase ".md")
  ```

- **Error context**: Map provides structured error data for `ex-info`
  ```clojure
  {:template-file template-file
   :phase next-ph
   :template-dir template-dir}
  ```

- **File list**: Vector for sequential processing of 18 files
  ```clojure
  (def affected-files
    ["RunNotes-2025-06-20-TestHarnessShoringUp-implementation.md"
     ...])
  ```

**No complex data structures needed** - this is straightforward file manipulation.

**Performance**: Not a concern - tooling operation, human-in-loop, small data

**Concurrency**: N/A - single-threaded file I/O

### 🚦 Human Review Gate - Data Structures

**STOP**: AI must not proceed until human completes this review.

- [x] **Data structure selection reviewed by human**
- [x] **Access patterns accurately captured**
- [x] **Performance implications understood and acceptable**
- [x] **Concurrency characteristics appropriate**
- [x] **Trade-offs explicitly acknowledged**

**Human Notes**: Approved - straightforward approach for file manipulation

---

## 2. Algorithm Selection

**Reference**: `doc/README-TECHNICAL-PLANNING-CLOJURE.md#algorithms`

### Problem Characteristics

**From Research Phase**:
- Fix: O(1) - Single function fix in `create-phase-file`
- Migration: O(n) - Linear scan and replace for n=18 files

**Input Characteristics**:

- Input size: Small (18 files, bounded)
- Input format: Markdown files with broken metadata stanzas
- Processing: Sequential file read, regex match/replace, write

**Performance Requirements**:

- Target latency: Not critical (human-in-loop tooling)
- Acceptable complexity: O(n) linear processing is fine
- Space: O(1) - process one file at a time

### Algorithm Design

#### Fix Algorithm: Template Path Resolution

**Simple String Substitution**:

```clojure
;; BEFORE (broken)
template-file (str runnotes-dir "/template-" next-ph ".md")

;; AFTER (fixed)
template-file (str template-dir "/" next-ph ".md")
```

**Complexity**: O(1) - constant time string concatenation

#### Migration Algorithm: Metadata Replacement

**Two-Phase Approach**:

1. **Identify broken files** (already done via grep)
   - Complexity: O(n) files to scan
   - Pattern: `^> \*\*Phase\*\*:` (markdown blockquote)

2. **Parse and reconstruct metadata** for each file:
   ```clojure
   (defn migrate-broken-metadata [file-path]
     (let [content (slurp file-path)
           ;; Extract broken metadata fields
           phase (extract-field content #"> \*\*Phase\*\*: (.+)")
           tags (extract-field content #"> \*\*Tags\*\*: (.+)")

           ;; Construct valid EDN metadata
           edn-metadata (format "```edn :metadata\n{:phase \"%s\"\n :tag %s\n :status :active\n :date {:created \"...\"}}\n```"
                               phase tags)

           ;; Replace broken section with valid EDN
           fixed-content (str/replace-first content
                                           #"(?s)> \*\*Phase\*\*:.*?(?=\n##)"
                                           edn-metadata)]
       (spit file-path fixed-content)))
   ```

**Complexity Analysis**:

| Operation | Time | Space | Notes |
|-----------|------|-------|-------|
| Read file | O(m) | O(m) | m = file size |
| Regex match | O(m) | O(1) | Single-pass scan |
| String replace | O(m) | O(m) | Construct new string |
| Write file | O(m) | O(1) | Stream output |
| **Total per file** | **O(m)** | **O(m)** | Linear in file size |
| **Total migration** | **O(n·m)** | **O(m)** | n=18 files, process one at a time |

**Clojure-Specific Considerations**:

- Use `slurp`/`spit` for file I/O (idiomatic)
- Use `str/replace-first` for metadata replacement
- Use regex for pattern matching broken metadata
- No need for lazy seqs (small, bounded data)
- No reflection concerns (string/file operations)

### Decision

**Fix Algorithm**: Direct string replacement in function signature
- Update path construction from `runnotes-dir` to `template-dir`
- Replace fallback with `ex-info` exception

**Migration Algorithm**: Sequential file processing with regex-based metadata extraction and reconstruction
- Read file → Parse broken metadata → Reconstruct valid EDN → Write file
- Process files one at a time (O(1) space usage)
- Human review each migration (safety)

**Justification**:
- Simple, maintainable approach
- Complexity appropriate for small dataset (18 files)
- Idiomatic Clojure (slurp/spit, regex, string manipulation)
- No premature optimization needed

**Safety Measures**:
- Dry-run mode: Preview changes before applying
- Backup files before modification
- Validate output with `runnote-validate`

### 🚦 Human Review Gate - Algorithm Selection

**STOP**: AI must not proceed until human completes this review.

- [x] **Algorithm selection reviewed by human**
- [x] **Complexity analysis correct**
- [x] **Implementation approach clear**
- [x] **Performance validation plan acceptable**

**Human Notes**: Approved - migration algorithm and safety measures are sound

---

## 3. Concurrency Strategy

**Reference**: `doc/README-TECHNICAL-PLANNING-CLOJURE.md#concurrency`

### Concurrency Requirements

**From Research Phase**: Single-threaded file I/O, no concurrency needed

**Concurrency Model Needed**:

- [x] No concurrency (single-threaded)
- [ ] Immutable reads (no coordination)
- [ ] Coordinated updates (atom, ref, agent)
- [ ] Message passing (core.async)
- [ ] Parallel processing (pmap, reducers, fork/join)

**Coordination Needs**: None

### Analysis

**File I/O Characteristics**:
- Reading/writing files is inherently sequential for safety
- No shared mutable state between operations
- Each file processed independently
- No concurrent access to same file

**Why No Concurrency**:

1. **Safety**: File operations should be atomic (read entire file, process, write entire file)
2. **Simplicity**: 18 files is small enough that sequential processing is fast (<1 second)
3. **Human review**: Migration includes manual review step, making parallelism unnecessary
4. **Error handling**: Sequential processing makes error diagnosis simpler

**Code Pattern**:

```clojure
;; Fix function - single-threaded, called during transition
(defn create-phase-file
  [topic date current-phase context metadata template-dir runnotes-dir]
  ;; Simple sequential execution
  (let [next-ph (next-phase current-phase)
        filename (str runnotes-dir "/RunNotes-" date "-" topic "-" next-ph ".md")
        template-file (str template-dir "/" next-ph ".md")]
    ;; Read template, write new file - all synchronous
    ...))

;; Migration script - sequential processing
(doseq [file affected-files]
  (migrate-broken-metadata file)
  (println "Migrated:" file))
```

### Decision

**Selected Concurrency Model**: None - single-threaded sequential processing

**Rationale**:

1. **File I/O safety**: Sequential processing prevents file corruption
2. **Performance adequate**: 18 files process in <1 second
3. **Simplicity**: No concurrency bugs possible
4. **Debugging**: Sequential execution easier to reason about
5. **Standard pattern**: File manipulation tools are typically single-threaded

**No Concurrency Primitives Needed**:
- No atoms, refs, agents, channels, or futures
- No locks or synchronization
- No race conditions possible
- No deadlock possible

**Error Handling**: Simple exception propagation - if file processing fails, stop and report error

### 🚦 Human Review Gate - Concurrency Strategy

**STOP**: AI must not proceed until human completes this review.

- [x] **Concurrency strategy reviewed by human**
- [x] **Primitives appropriate for use case**
- [x] **Error handling strategy clear**
- [x] **Risks identified and mitigated**
- [x] **Testing approach for concurrent behavior defined**

**Human Notes**: Approved - single-threaded approach is appropriate

---

## 4. Testing Strategy

**Reference**: `doc/README-TECHNICAL-PLANNING-CLOJURE.md#testing`

### Test Requirements

**From Research Phase**:
- Validation tool (`runnote-validate`) can verify metadata correctness
- Need to test both fix and migration
- Manual review for migration safety

**Coverage Goals**:

- Unit test coverage: Critical paths (template path resolution, error handling)
- Integration test: End-to-end transition workflow
- Validation: Use `runnote-validate` to verify output
- No performance benchmarks needed (not performance-critical)

### Test Approach

#### Unit Tests (clojure.test)

**Testing Philosophy**: Test-after for bug fix (regression prevention)

**Test Cases for `create-phase-file` Fix**:

```clojure
(ns runnote.transition-test
  (:require [clojure.test :refer :all]
            [runnote.transition :refer [create-phase-file]]))

(deftest template-path-resolution-test
  (testing "Template path uses template-dir parameter"
    (let [topic "TestTopic"
          date "2025-11-05"
          current-phase "research"
          context {:findings "..."}
          metadata {:tags "#{:test}"}
          template-dir "/home/user/.runnote/template"
          runnotes-dir "/home/user/project/runnotes"]

      ;; Should look for template at correct path
      ;; Previously looked at: /home/user/project/runnotes/template-planning.md (WRONG)
      ;; Now looks at: /home/user/.runnote/template/planning.md (CORRECT)

      ;; Test will verify template lookup path
      (with-redefs [babashka.fs/exists? (fn [path]
                                          (= path (str template-dir "/planning.md")))]
        (is (not (nil? (create-phase-file topic date current-phase
                                         context metadata
                                         template-dir runnotes-dir)))))))

(deftest template-not-found-error-test
  (testing "Fails with helpful error when template missing"
    (let [template-dir "/nonexistent"
          runnotes-dir "/tmp"]

      (is (thrown-with-msg?
            clojure.lang.ExceptionInfo
            #"Template not found"
            (create-phase-file "Topic" "2025-11-05" "research"
                              {} {} template-dir runnotes-dir)))

      ;; Verify error message includes recovery guidance
      (try
        (create-phase-file "Topic" "2025-11-05" "research"
                          {} {} template-dir runnotes-dir)
        (catch Exception e
          (is (re-find #"Recovery steps" (.getMessage e)))
          (is (re-find #"runnote-init" (.getMessage e))))))))
```

**Test Cases for Migration Script**:

```clojure
(deftest migrate-broken-metadata-test
  (testing "Converts broken markdown metadata to valid EDN"
    (let [broken-content (str "# Planning: Topic - 2025-08-09\n\n"
                              "> **Phase**: Planning (2 of 4)\n"
                              "> **Previous Phase**: Research (Completed: 2025-08-09)\n"
                              "> **Tags**: :architecture :performance\n\n"
                              "## Context\nSome content")

          expected-pattern #"```edn :metadata\n\{:phase \"planning\""]

      (let [result (migrate-broken-metadata-string broken-content)]
        (is (re-find expected-pattern result))
        (is (not (re-find #"^>" result)))))))

(deftest extract-tags-test
  (testing "Extracts tags from broken metadata format"
    (is (= "#{:architecture :performance}"
           (extract-tags "> **Tags**: :architecture :performance")))))
```

#### Integration Tests

**End-to-End Transition Workflow**:

```clojure
(deftest transition-workflow-integration-test
  (testing "Full transition creates valid metadata"
    (with-temp-dir [temp-dir]
      ;; Setup: Create research file with valid metadata
      ;; Execute: Run transition to planning
      ;; Verify: Planning file has valid EDN metadata
      ;; Verify: runnote-validate passes
      )))
```

#### Validation Tests

**Use `runnote-validate` as Test Oracle**:

```clojure
(deftest migration-output-validation-test
  (testing "Migrated files pass runnote-validate"
    (doseq [file migrated-files]
      (is (= 0 (shell/sh "runnote-validate" "check" file))
          (str "Validation failed for " file)))))
```

### Test Coverage Plan

| Component | Unit Tests | Property Tests | Integration Tests | Validation |
|-----------|-----------|----------------|-------------------|------------|
| `create-phase-file` (fixed) | ✓ Template path resolution<br>✓ Error handling | - | ✓ Full transition workflow | ✓ validate passes |
| Migration script | ✓ Metadata extraction<br>✓ EDN reconstruction | - | - | ✓ validate passes |
| Error messages | ✓ Recovery guidance included | - | - | - |

**Test Data Strategy**:
- Use actual broken file samples for migration tests
- Create minimal test fixtures for unit tests
- Temp directories for integration tests

**No Property-Based Tests Needed**:
- Simple string/file manipulation
- Deterministic behavior
- Edge cases limited and well-defined

**Test Execution**:
- Run before implementation (TDD for regression test)
- Run after each change (CI/CD)
- Manual validation of migration on actual 18 files

### 🚦 Human Review Gate - Testing Strategy

**STOP**: AI must not proceed until human completes this review.

- [x] **Testing strategy reviewed by human**
- [x] **Coverage goals appropriate**
- [x] **Property-based tests identified for complex logic**
- [x] **Integration test plan clear**
- [x] **Performance benchmarks defined**

**Human Notes**: Approved - comprehensive test coverage with validation oracle

---

## 5. Security Considerations

**Reference**: `doc/README-TECHNICAL-PLANNING-CLOJURE.md#security`

### Security Requirements

**From Research Phase**: Local file I/O tooling - minimal security concerns, but safe file handling required

**Threat Model**:

- **Trust boundaries**: User's local filesystem only
- **Threat actors**: Minimal - local user running tool on their own files
- **Assets to protect**: User's RunNotes files from corruption
- **Attack vectors**: Path traversal, file corruption, information disclosure in error messages

### Security Controls

**Authentication**: N/A - local command-line tool

**Authorization**: N/A - relies on filesystem permissions

**Input Validation**:

**File Path Validation**:

```clojure
(defn validate-path [path expected-dir]
  "Ensure path is within expected directory (prevent path traversal)"
  (let [canonical-path (.getCanonicalPath (io/file path))
        canonical-expected (.getCanonicalPath (io/file expected-dir))]
    (when-not (.startsWith canonical-path canonical-expected)
      (throw (ex-info "Invalid path: outside expected directory"
                     {:path path :expected-dir expected-dir})))))
```

**Phase Validation**:

```clojure
;; Validate phase is one of allowed values
(def valid-phases #{"research" "planning" "implementation" "review"
                    "debug" "hotfix" "performance" "security" "testing"})

(defn validate-phase [phase]
  (when-not (contains? valid-phases phase)
    (throw (ex-info "Invalid phase" {:phase phase :valid-phases valid-phases}))))
```

**File Existence Checks**:

```clojure
;; Check file exists before reading
(when-not (fs/exists? file-path)
  (throw (ex-info "File not found" {:path file-path})))

;; Check file is readable
(when-not (fs/readable? file-path)
  (throw (ex-info "File not readable" {:path file-path})))
```

**Safe File Operations**:

**Atomic Writes** (prevent partial writes):

```clojure
(defn safe-spit [path content]
  "Write to temp file, then atomic rename"
  (let [temp-path (str path ".tmp")]
    (try
      (spit temp-path content)
      (fs/move temp-path path {:replace-existing true})
      (catch Exception e
        (fs/delete-if-exists temp-path)
        (throw e)))))
```

**Backup Before Modification**:

```clojure
(defn migrate-with-backup [file-path]
  "Backup file before migration"
  (let [backup-path (str file-path ".backup")]
    (fs/copy file-path backup-path)
    (try
      (migrate-broken-metadata file-path)
      (catch Exception e
        ;; Restore from backup on failure
        (fs/copy backup-path file-path {:replace-existing true})
        (throw e)))))
```

**Error Message Safety**:

**No Sensitive Data in Error Messages**:

```clojure
;; GOOD: Include path for debugging but no file contents
(ex-info "Template not found"
         {:template-file template-file
          :phase next-ph
          :template-dir template-dir})

;; BAD: Don't include file contents in error
;; (ex-info "Parse failed" {:content file-content}) ;; AVOID
```

**Audit Logging**: N/A - local tool, no audit requirements

**Sensitive Data Handling**: N/A - RunNotes are user's own development notes, not PII

### Security Checklist

- [x] **Path traversal prevention**: Validate paths are within expected directories
- [x] **Input validation**: Validate phase names against allowed set
- [x] **Safe file operations**: Atomic writes, backup before modification
- [x] **Error message safety**: No file contents in error messages
- [ ] **File permission preservation**: Maintain original file permissions after modification
- [x] **Graceful degradation**: Clear error messages with recovery guidance

**Additional Safety Measures**:

1. **Version control**: Git provides backup/rollback (working in dedicated branch)
2. **Backup files**: Optional `.backup` files for extra safety (not required given git)
3. **Validation**: Use `runnote-validate` to verify output correctness

**Note**: Dry-run mode not needed - git provides sufficient safety net for this low-risk operation.

### 🚦 Human Review Gate - Security Considerations

**STOP**: AI must not proceed until human completes this review.

- [x] **Security considerations reviewed by human**
- [x] **Threat model appropriate**
- [x] **Authentication/authorization strategy clear**
- [x] **Input validation comprehensive**
- [x] **Audit logging sufficient**
- [x] **Sensitive data protection adequate**

**Human Notes**: Approved - very low risk scenario, git provides backup, working in dedicated branch

---

## 6. Performance Analysis

**Reference**: `doc/README-TECHNICAL-PLANNING-CLOJURE.md#performance`

### Performance Requirements

**From Research Phase**: Not performance-critical - human-in-loop tooling

**Performance Targets**:

- Latency: Not critical (user waits for tool to complete)
- Throughput: 18 files, expected < 1 second total
- Resource limits: Minimal - small files, bounded data
- Scalability: Not applicable - fixed dataset

### Performance Analysis

**Expected Performance**:

| Operation | Expected Time | Notes |
|-----------|--------------|-------|
| Fix `create-phase-file` | < 1ms | Simple string operations |
| Migrate single file | ~10-50ms | Read + regex + write |
| Migrate all 18 files | < 1 second | Sequential processing |

**Performance Characteristics**:

1. **File I/O dominates**: Most time spent in disk I/O, not CPU
2. **Small data**: Files are ~5-50KB each, fit in memory
3. **Simple operations**: String concatenation, regex matching - fast
4. **No optimization needed**: Sub-second completion is more than adequate

**Why No Optimization Required**:

- Human-in-loop tool (user expects to wait)
- One-time operation (migration runs once)
- Ongoing fix (transition) runs once per phase transition
- Sub-second performance acceptable for this use case

**Clojure Performance Considerations**:

**Simple, idiomatic code is sufficient**:

```clojure
;; No optimization needed - this is fast enough
(defn create-phase-file [...]
  (let [template (slurp template-file)]  ;; Simple file I/O
    (spit filename template)))            ;; Simple file I/O

;; Migration - sequential is fine for 18 files
(doseq [file affected-files]
  (let [content (slurp file)
        fixed (str/replace content pattern replacement)]
    (spit file fixed)))
```

**No advanced techniques needed**:
- ❌ No reflection issues (string/file operations)
- ❌ No transients needed (small data)
- ❌ No transducers needed (simple operations)
- ❌ No parallel processing needed (18 files, sequential is fast)
- ❌ No memoization needed (one-time operations)

### Performance Validation

**Acceptance Criteria**: Migration completes in < 5 seconds (very conservative)

**Validation Approach**: Manual observation during implementation - if noticeably slow (>1 second), investigate

**No Profiling or Benchmarking Required**: Performance is not a concern for this use case

### 🚦 Human Review Gate - Performance Analysis

**STOP**: AI must not proceed until human completes this review.

- [x] **Performance analysis reviewed by human**
- [x] **Performance targets realistic**
- [x] **Optimization strategy sound**
- [x] **Profiling plan clear**
- [x] **Premature optimization avoided**

**Human Notes**: Pre-approved - no performance concerns for this scenario

---

## 7. Language Best Practices

**Reference**: `doc/README-TECHNICAL-PLANNING-CLOJURE.md#best-practices`

### Clojure Idioms for This Feature

**Data Modeling**:

- [x] Use maps for error context (`ex-info` data)
- [x] No records needed (simple data)
- [x] No protocols needed (no polymorphism)
- [x] Prefer data over objects (already doing this)

**Namespace Organization**:

- [x] Editing existing `runnote.transition` namespace
- [x] Keep focused (single file, single responsibility)
- [x] Explicit requires (babashka.fs, clojure.string)
- [x] No circular dependencies (standalone tool)

**Error Handling**:

- [x] Use `ex-info` for rich error data with recovery guidance
- [x] Let exceptions bubble (appropriate for CLI tool)
- [x] Provide helpful error messages with actionable steps

**Example Error Handling**:

```clojure
(when-not (fs/exists? template-file)
  (throw (ex-info
          (str "Template not found: " template-file "\n\n"
               "Recovery steps:\n"
               "1. Check template directory configuration\n"
               "2. Verify template exists: " template-file "\n"
               "3. Run: runnote-init to setup templates\n"
               "4. Or create template manually in template/" next-ph ".md")
          {:template-file template-file
           :phase next-ph
           :template-dir template-dir})))
```

**Key Clojure Idioms Applied**:

1. **Destructuring** for clarity:
   ```clojure
   (let [{:keys [phase tags status date]} metadata]
     ;; Use extracted values
     )
   ```

2. **Threading macros** for readability (if needed):
   ```clojure
   (-> content
       (str/replace #"(?s)> \*\*Phase\*\*:.*?(?=\n##)" edn-metadata)
       (spit file-path))
   ```

3. **Idiomatic file I/O**:
   ```clojure
   (slurp file-path)  ;; Read
   (spit file-path content)  ;; Write
   ```

4. **Regex with named groups** (if complex):
   ```clojure
   (when-let [[_ phase] (re-find #"> \*\*Phase\*\*: (.+)" content)]
     ;; Use phase
     )
   ```

**State Management**:

- [x] No mutable state (pure functions only)
- [x] No atoms, refs, or agents needed
- [x] Stateless transformations (read → transform → write)

### Babashka-Specific Considerations

**This is a Babashka script** (not JVM Clojure):

**Use `babashka.fs` for file operations**:

```clojure
(require '[babashka.fs :as fs])

;; Idiomatic babashka file operations
(fs/exists? path)
(fs/readable? path)
(fs/copy source dest)
(fs/delete-if-exists path)
```

**Use `clojure.string` for string operations**:

```clojure
(require '[clojure.string :as str])

(str/replace-first content pattern replacement)
(str/split content #"\n")
(str/trim line)
```

**Keep it simple** - Babashka is optimized for scripting:
- Avoid complex abstractions
- Direct, imperative style is OK for scripts
- Prioritize clarity over cleverness

### Code Style Guidelines

**Function Signatures**:

```clojure
;; BEFORE (broken)
(defn create-phase-file
  [topic date current-phase context metadata runnotes-dir]
  ...)

;; AFTER (fixed) - add template-dir parameter
(defn create-phase-file
  [topic date current-phase context metadata template-dir runnotes-dir]
  ...)
```

**Docstrings** (update after fix):

```clojure
(defn create-phase-file
  "Create new RunNotes file for next phase.

  Parameters:
    topic - Topic name (string)
    date - Date string (YYYY-MM-DD)
    current-phase - Current phase name
    context - Context map from previous phase
    metadata - Metadata map with tags, etc.
    template-dir - Directory containing templates (from config)
    runnotes-dir - Directory for RunNotes files

  Returns: Path to created file

  Throws: ExceptionInfo if template not found (with recovery guidance)"
  [topic date current-phase context metadata template-dir runnotes-dir]
  ...)
```

**Comments** where helpful:

```clojure
;; Construct template path using template-dir from config
;; (NOT runnotes-dir - that was the bug)
template-file (str template-dir "/" next-ph ".md")
```

### Anti-Patterns to Avoid

**Avoid**:

- ❌ Hardcoded paths (use config)
- ❌ Silent failures (fail fast with clear errors)
- ❌ Swallowing exceptions (let them bubble)
- ❌ Incomplete error messages (provide recovery steps)
- ❌ Mixing concerns (keep file I/O separate from business logic where reasonable)

**Follow**:

- ✅ Use config system for paths
- ✅ Fail fast with helpful errors
- ✅ Let exceptions propagate to top level
- ✅ Provide actionable error messages
- ✅ Keep functions focused

### Polylith Considerations

**N/A**: This is a Babashka script, not a Polylith component. No Polylith considerations apply.

### 🚦 Human Review Gate - Language Best Practices

**STOP**: AI must not proceed until human completes this review.

- [x] **Language best practices reviewed by human**
- [x] **Clojure idioms appropriate**
- [x] **Polylith boundaries clear (if applicable)**
- [x] **Interface contracts well-defined**
- [x] **Anti-patterns avoided**

**Human Notes**: Approved - Babashka idioms and style guidelines are clear

---

## Implementation Strategy

### Phase Breakdown

**1. Fix `create-phase-file` Function** (Est: 30 min)

- [ ] Update function signature to add `template-dir` parameter
- [ ] Fix template path construction: `(str template-dir "/" next-ph ".md")`
- [ ] Remove fallback code (lines 170-175)
- [ ] Add fail-fast error with recovery guidance
- [ ] Update docstring
- [ ] Add clarifying comment about bug fix

**Success criteria**:
- Template path uses `template-dir` from config
- Helpful error message when template missing
- Function signature matches updated call site

**Technical focus**: Simple string operations, error handling with `ex-info`

---

**2. Update Call Site** (Est: 15 min)

- [ ] Find call site at line 289 in `runnote-transition`
- [ ] Pass `template-dir` from config to `create-phase-file`
- [ ] Verify config system provides `template-dir` (from `resolve-runnote-template-dir`)

**Success criteria**:
- Call site passes correct `template-dir` parameter
- Config resolution works correctly

**Technical focus**: Config integration

---

**3. Create Migration Script** (Est: 1-2 hours)

- [ ] Write `migrate-broken-metadata.clj` script
- [ ] Implement metadata extraction (phase, tags, dates)
- [ ] Implement EDN metadata reconstruction
- [ ] Add file list of 18 affected files
- [ ] Test on sample broken file
- [ ] Dry-run on all 18 files (verify output)
- [ ] Execute migration on all 18 files

**Success criteria**:
- All 18 files have valid EDN metadata
- `runnote-validate check` passes for all files
- No content loss (only metadata replaced)

**Technical focus**: Regex parsing, string manipulation, file I/O

---

**4. Testing & Validation** (Est: 30-45 min)

- [ ] Write unit test for template path resolution
- [ ] Write unit test for error handling
- [ ] Write test for metadata extraction
- [ ] Run integration test (full transition workflow)
- [ ] Validate all migrated files with `runnote-validate`
- [ ] Manual spot-check of migrated files

**Success criteria**:
- All tests pass
- All 18 files validate successfully
- Manual inspection confirms correctness

**Technical focus**: Test implementation, validation

---

**5. Documentation & Cleanup** (Est: 15 min)

- [ ] Update function docstrings
- [ ] Add comment explaining bug fix
- [ ] Clean up any debug output
- [ ] Verify no temporary files left behind

**Success criteria**:
- Code is well-documented
- No debug artifacts remain

**Technical focus**: Code documentation

---

**Total Estimated Time**: 3-4 hours

### Risk Mitigation Plan

| Risk | Probability | Impact | Mitigation | Technical Area |
|------|-------------|--------|------------|----------------|
| Migration loses data | Low | High | Test on sample first, validate all output, git provides backup | Algorithm - regex replacement |
| Template path still wrong | Low | Medium | Write regression test first, verify against working runnote-launch code | Data structures - path construction |
| Migration script has bugs | Medium | Medium | Test on sample file, dry-run before executing, validate output | Testing - comprehensive coverage |
| Regex fails on edge cases | Low | Medium | Review all 18 files for variations, test each pattern | Algorithm - pattern matching |
| Config system doesn't provide template-dir | Low | High | Verify config resolution before implementation | Integration - config system |

### Architecture Decisions

**No ADRs Required**: This is a bug fix, not an architectural decision.

**Rationale**:
- Restoring correct behavior (template path resolution)
- Not introducing new patterns or abstractions
- Following existing config system patterns
- No cross-cutting concerns or system-wide impact

**No ADR Creation Tasks**

## Phase Transition Checklist

Ready for Implementation when:

- [x] ADR searches completed and relevant decisions reviewed (N/A - bug fix)
- [x] No conflicts identified with existing ADRs (N/A)
- [x] **All 7 technical sections reviewed and human-approved**
  - [x] Data Structures
  - [x] Algorithm Selection
  - [x] Concurrency Strategy
  - [x] Testing Strategy
  - [x] Security Considerations
  - [x] Performance Analysis
  - [x] Language Best Practices
- [x] All human review gates checked off
- [x] Technical decisions documented and rationale clear
- [x] ADR creation tasks identified for implementation phase (None needed)
- [x] Test strategy defined and comprehensive
- [x] Performance targets established (sub-second execution)
- [x] Security considerations addressed (file safety, path validation)
- [x] Rollback plan established (git provides backup in dedicated branch)

**Next Phase**: Implementation-Detailed

**Recommendation**: Use `runnote-launch implementation-detailed TransitionMetadataBug` to track adherence to technical decisions made here.

**Total Planning Time**: ~45 minutes (with human review gates)

---

## Planning Summary

**Approved Technical Approach**:

1. **Fix**: Update `create-phase-file` to use `template-dir` parameter (fail-fast approach)
2. **Migration**: Sequential file processing with regex-based metadata replacement
3. **Testing**: Unit tests + integration tests + validation with `runnote-validate`
4. **Safety**: Git backup in dedicated branch, validation before/after

**Key Technical Decisions**:

| Section | Decision | Rationale |
|---------|----------|-----------|
| Data Structures | Simple strings, maps, vectors | Adequate for file manipulation |
| Algorithm | O(1) fix, O(n) sequential migration | Appropriate for 18 files |
| Concurrency | Single-threaded | File I/O safety, adequate performance |
| Testing | Unit + integration + validation | Comprehensive coverage without over-testing |
| Security | Path validation, safe file ops | Low risk, git provides backup |
| Performance | No optimization needed | Sub-second execution acceptable |
| Best Practices | Idiomatic Babashka, fail-fast errors | Clear, maintainable code |

**Estimated Implementation Time**: 3-4 hours

**Ready to implement!**
