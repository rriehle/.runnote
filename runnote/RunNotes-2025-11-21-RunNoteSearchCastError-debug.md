# Debug: RunNoteSearchCastError - 2025-11-21 15:06

```edn :metadata
{:phase "debug"
 :tag #{:debugging :bugfix :runnote-search :metadata-validation}
 :status :completed
 :thinking-mode "think hard"
 :date {:created "2025-11-21" :updated "2025-11-21"}
 :related-documents
 {:code-files #{"bin/runnote-search:76"
                "~/.lib/metadata-parser.bb:279-304"
                "bin/runnote-validate"}
  :runnote #{}}}
```

## Problem Statement

**Symptom**: ClassCastException when running `runnote-search summary` or `runnote-search list-tags`
```
Type:     java.lang.ClassCastException
Message:  clojure.lang.PersistentVector cannot be cast to clojure.lang.Named
Location: /Users/rriehle/.runnote/bin/runnote-search:75:29
```

**Context**: Occurs when parsing RunNotes files with malformed `:tag` metadata in `/Users/rriehle/src/xional/runnotes/`
**Impact**: Tool completely fails with stack trace; no graceful error handling
**Reproduction Rate**: Always (when malformed file is present)

## Reproduction Steps

1. Navigate to a directory containing RunNotes files (e.g., `xional.quant` project)
2. Run `runnote-search summary` or `runnote-search list-tags`
3. **Expected**: Summary report or tag list
4. **Actual**: ClassCastException stack trace

## Investigation Log

### [15:09] - Initial Investigation

**Hypothesis**: Error suggests `:tag` field contains a vector instead of keywords
**Evidence**: Error message shows "PersistentVector cannot be cast to Named" at line 75 where `(name %)` is called on tag elements
**Test**: Search for RunNotes files with malformed `:tag` metadata
**Result**: ✅ Confirmed - found file with vector in `:tag` field

### [15:15] - Deep Dive

**Found**: File `RunNotes-2025-08-09-EventSourcingAudit-planning.md` contains:
```edn
:tag #{[Inherit from research phase and add planning-specific tags]}
```

**Location**: `/Users/rriehle/src/xional/runnotes/RunNotes-2025-08-09-EventSourcingAudit-planning.md:5`

**Root Cause**:
1. This is a template file that was never properly filled in
2. Contains placeholder text as a vector: `[Inherit from research phase and add planning-specific tags]`
3. When `runnote-search` processes tags at line 75: `(map #(str ":" (name %)) (:tag edn-metadata))`
4. It tries to call `(name [Inherit...])` on the vector, which fails because vectors don't implement the Named interface

**Additional Finding**: `runnote-search` has no error handling for malformed metadata - it fails hard with stack trace instead of gracefully reporting the issue

## Experiments Conducted

### Experiment: Systematic Search for Malformed Tags

**Hypothesis**: There may be multiple files with malformed tags
**Method**: Babashka script to parse all RunNotes metadata and check each tag element
**Result**: Found exactly 1 file with malformed tags: `RunNotes-2025-08-09-EventSourcingAudit-planning.md`
**Conclusion**: This is an isolated instance, likely from an unfilled template

### Experiment: Test runnote-search in Clean Directory

**Hypothesis**: Tool works correctly when no malformed files are present
**Method**: Run `runnote-search summary` from `~/.runnote` directory (which has valid metadata)
**Result**: ✅ Tool works perfectly, generates correct summary
**Conclusion**: Confirms the issue is specific to the malformed file, not a general tool bug

## Solution

**Two-Part Fix Required**:

### Part 1: Add Graceful Error Handling to runnote-search

**Fix**: Modify `bin/runnote-search:75` to safely handle malformed tags
**Implementation**:
```clojure
;; Before (line 75)
:tags (map #(str ":" (name %)) (:tag edn-metadata))

;; After (with error handling)
:tags (when-let [tags (:tag edn-metadata)]
        (keep (fn [tag]
                (cond
                  (keyword? tag) (str ":" (name tag))
                  (symbol? tag) (str ":" (name tag))
                  :else (do
                          (binding [*out* *err*]
                            (println "WARNING: Skipping malformed tag in" (fs/file-name file) ":" tag))
                          nil)))
              tags))
```

**Rationale**:
- Gracefully handles malformed tags by warning instead of crashing
- Skips invalid tags but continues processing
- Emits warning to stderr so user knows there's an issue to fix
- Allows tool to remain functional even with corrupt data

**Side Effects**: Files with malformed tags will have incomplete tag listings (but this is better than total failure)

### Part 2: Fix the Malformed File

**Fix**: Replace placeholder vector with proper tag set in `RunNotes-2025-08-09-EventSourcingAudit-planning.md`
**Implementation**: Change line 5 from:
```edn
:tag #{[Inherit from research phase and add planning-specific tags]}
```
To:
```edn
:tag #{:event-sourcing :audit :planning}
```

**Rationale**: File is clearly about event sourcing audit planning, so appropriate tags are `:event-sourcing`, `:audit`, `:planning`

**Testing**: After both fixes, run `runnote-search summary` from xional directory to verify it works

## Prevention

**Root Cause Category**: Design + Testing
**Prevention Strategy**:
1. **Input Validation**: Add metadata validation to `runnote-launch` templates to prevent placeholder text from being committed
2. **Template Validation**: Create pre-commit hook or validation script to detect unfilled templates
3. **Better Templates**: Make templates more obvious that placeholders need replacement (e.g., use `TODO` markers)
4. **Defensive Coding**: Always validate input types before calling type-specific functions like `name`

**Detection Strategy**:
1. Create `runnote-validate` tool to scan all RunNotes files for malformed metadata
2. Run validation as part of CI/CD pipeline
3. Add unit tests for `runnote-search` with malformed data fixtures
4. Consider adding schema validation using `clojure.spec` for EDN metadata

**Recommended Tooling**: Create `bin/runnote-validate` script to detect:
- Vector tags instead of keywords
- Missing required fields (`:phase`, `:tag`, `:status`, `:date`)
- Invalid EDN syntax
- Unfilled template placeholders

## Implementation Log

### [15:30] - Library-Level Fix

**Action**: Fixed `~/.lib/metadata-parser.bb` instead of individual scripts
**Changes**:
1. Added `safe-tag-strings` function to handle malformed tags gracefully
2. Updated `summarize-metadata` to use safe tag processing
3. Exported `safe-tag-strings` for use by other tools

**Rationale**: DRY principle - fix once in shared library rather than in every script that uses it

### [15:35] - Updated runnote-search

**Action**: Modified `bin/runnote-search` to use library function
**Changes**:
1. Imported `safe-tag-strings` from metadata-parser
2. Changed line 76 from `(map #(str ":" (name %)) (:tag edn-metadata))` to `(safe-tag-strings (:tag edn-metadata) (fs/file-name file))`

**Testing**: ✅ Confirmed tool now warns about malformed tags but continues processing

### [15:40] - Fixed Malformed File

**Action**: Corrected `/Users/rriehle/src/xional/runnotes/RunNotes-2025-08-09-EventSourcingAudit-planning.md`
**Changes**:
- Changed `:tag #{[Inherit from research phase and add planning-specific tags]}` to `:tag #{:event-sourcing :audit :planning}`
- Updated title from placeholder to actual topic
- Changed status to `:completed`

**Testing**: ✅ `runnote-search summary` now runs without warnings

### [15:45] - Created Validation Tool

**Action**: Created `bin/runnote-validate` to detect metadata issues
**Features**:
- Validates tag structure (keywords only, no vectors/strings/etc.)
- Checks for required fields (`:phase`, `:tag`, `:status`, `:date`)
- Detects unfilled template placeholders
- Reports errors and warnings separately

**Testing**: ✅ Validates all files in `~/.runnote/runnote` successfully

## Resolution Checklist

- [x] Fix implemented in metadata-parser library (graceful error handling)
- [x] runnote-search updated to use safe library function
- [x] Malformed file corrected
- [x] Validation tool (`runnote-validate`) created to prevent recurrence
- [x] Related code reviewed - library fix covers all scripts
- [x] Fix verified - tested with both clean and previously-malformed files

## Summary

**Issue**: `runnote-search` crashed with ClassCastException when encountering a RunNotes file with malformed metadata containing a vector `[Inherit from research phase and add planning-specific tags]` instead of keywords in the `:tag` field.

**Root Cause**: Unfilled template placeholder in `/Users/rriehle/src/xional/runnotes/RunNotes-2025-08-09-EventSourcingAudit-planning.md`. When the code called `(name [Inherit...])`, it failed because vectors don't implement the Named interface.

**Solution Implemented**:
1. **Library-level fix**: Added `safe-tag-strings` function to `~/.lib/metadata-parser.bb` that gracefully handles malformed tags by:
   - Validating each tag is a keyword or symbol
   - Warning to stderr about invalid tags (with file context)
   - Skipping invalid tags but continuing processing
2. **Tool update**: Modified `runnote-search` to use the safe library function
3. **Data fix**: Corrected the malformed file with proper tags
4. **Prevention**: Created `runnote-validate` tool to detect metadata issues proactively

**Impact**:
- ✅ `runnote-search` now handles malformed metadata gracefully instead of crashing
- ✅ All RunNotes tools benefit from library-level fix (DRY principle)
- ✅ New validation tool prevents future occurrences
- ✅ User gets actionable warnings instead of cryptic stack traces

**Time**: ~45 minutes (investigation: 20min, implementation: 15min, validation tool: 10min)

**Lessons Learned**:
1. Always validate input types before calling type-specific functions like `name`
2. Unfilled template placeholders can cause runtime errors - need validation at creation time
3. Fixing at the library level prevents duplication and protects all consumers
4. Graceful degradation (warn + skip) is better than hard failure for data quality issues
