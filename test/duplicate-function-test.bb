#!/usr/bin/env bb

(ns ^:clj-kondo/ignore duplicate-function-test
  "Tests for RunNotes duplicate functions before refactoring.
   Validates current behavior of list-all-tags and extract-metadata."
  (:require [babashka.fs :as fs]
            [clojure.string :as str]))

;; Load test framework
(def lib-dir (str (System/getenv "HOME") "/.lib"))
(load-file (str lib-dir "/test/test-framework.bb"))
(load-file (str lib-dir "/metadata-parser.bb"))

;; Import test utilities
(def test-case (:test-case test-framework/exports))
(def test-suite (:test-suite test-framework/exports))
(def with-temp-dir (:with-temp-dir test-framework/exports))
(def create-test-file (:create-test-file test-framework/exports))
(def create-test-files (:create-test-files test-framework/exports))
(def assert-true (:assert-true test-framework/exports))
(def assert-equal (:assert-equal test-framework/exports))
(def assert-count (:assert-count test-framework/exports))
(def assert-contains (:assert-contains test-framework/exports))
(def assert-not-nil (:assert-not-nil test-framework/exports))
(def run-tests-and-exit (:run-tests-and-exit test-framework/exports))

;; Import metadata parser
(def extract-edn-metadata (:extract-edn-metadata metadata-parser/exports))

;; ============================================================================
;; Functions Under Test
;; ============================================================================

(defn extract-metadata
  "Copy of extract-metadata from runnote-search:62"
  [file]
  (let [content (slurp (str file))
        lines (str/split-lines content)
        title (first lines)
        edn-metadata (extract-edn-metadata content)]
    (if edn-metadata
      ;; New EDN format
      {:file (fs/file-name file)
       :path file
       :title (str/trim (str/replace title #"^#+" ""))
       :phase (:phase edn-metadata)
       :tags (map #(str ":" (name %)) (:tag edn-metadata))
       :state (when-let [s (:status edn-metadata)]
                (name s))
       :date (let [parts (str/split (fs/file-name file) #"-")]
               (str/join "-" (take 3 (drop 1 parts))))}
      ;; Fall back to old format
      (let [extract-field (fn [field-name]
                            (some #(when (str/includes? % (str "> **" field-name "**:"))
                                     (str/trim (second (str/split % #":"))))
                                  lines))]
        {:file (fs/file-name file)
         :path file
         :title (str/trim (str/replace title #"^#+" ""))
         :phase (extract-field "Phase")
         :tags []
         :state (extract-field "Current State")
         :date (let [parts (str/split (fs/file-name file) #"-")]
                 (str/join "-" (take 3 (drop 1 parts))))}))))

(defn list-all-tags
  "Copy of list-all-tags from runnote-search:137"
  [runnote-dir]
  (let [files (fs/glob runnote-dir "RunNotes-*.md")
        all-tags (mapcat :tags (map extract-metadata files))]
    (sort (distinct all-tags))))

;; ============================================================================
;; Test Data
;; ============================================================================

(def sample-runnote-edn
  "# Database Migration Planning

```edn :metadata
{:phase \"planning\"
 :tag #{:database :migration :architecture}
 :status :active
 :thinking-mode \"think hard\"
 :date {:created \"2025-01-15\"}}
```

## Context
Planning database migration strategy.
")

(def sample-runnote-legacy
  "# API Implementation

> **Phase**: implementation
> **Tags**: :api :backend :rest
> **Current State**: 🟢 Active
> **Date**: 2025-01-16

## Progress
Working on REST API implementation.
")

;; ============================================================================
;; Test Cases for extract-metadata
;; ============================================================================

(defn test-extract-metadata-edn-format []
  (with-temp-dir
    (fn [temp-dir]
      (let [file-path (create-test-file temp-dir "RunNotes-2025-01-15-DbMigration-planning.md" sample-runnote-edn)
            result (extract-metadata file-path)]
        (assert-not-nil result "Should return metadata map")
        (assert-equal "planning" (:phase result) "Should extract phase")
        (assert-count 3 (:tags result) "Should extract 3 tags")
        (assert-contains (:tags result) ":database" "Should contain :database tag")
        (assert-contains (:tags result) ":migration" "Should contain :migration tag")
        (assert-contains (:tags result) ":architecture" "Should contain :architecture tag")
        (assert-equal "active" (:state result) "Should extract status")))))

(defn test-extract-metadata-date-from-filename []
  (with-temp-dir
    (fn [temp-dir]
      (let [file-path (create-test-file temp-dir "RunNotes-2025-01-15-Topic-phase.md" sample-runnote-edn)
            result (extract-metadata file-path)]
        (assert-equal "2025-01-15" (:date result) "Should extract date from filename")))))

(defn test-extract-metadata-title []
  (with-temp-dir
    (fn [temp-dir]
      (let [file-path (create-test-file temp-dir "RunNotes-2025-01-15-Test.md"
                                        "# My Test RunNote\n\n```edn :metadata\n{:phase \"research\" :tag #{:test}}\n```")
            result (extract-metadata file-path)]
        (assert-equal "My Test RunNote" (:title result) "Should extract title without #")))))

(defn test-extract-metadata-no-metadata []
  (with-temp-dir
    (fn [temp-dir]
      (let [file-path (create-test-file temp-dir "RunNotes-2025-01-15-NoMeta.md"
                                        "# Title\n\nNo metadata block here.")
            result (extract-metadata file-path)]
        (assert-not-nil result "Should still return map")
        (assert-equal "Title" (:title result) "Should extract title")
        (assert-equal [] (:tags result) "Should have empty tags")))))

;; ============================================================================
;; Test Cases for list-all-tags
;; ============================================================================

(defn test-list-all-tags-empty-dir []
  (with-temp-dir
    (fn [temp-dir]
      (let [result (list-all-tags (str temp-dir))]
        (assert-equal [] result "Empty directory should return empty list")))))

(defn test-list-all-tags-single-file []
  (with-temp-dir
    (fn [temp-dir]
      (create-test-file temp-dir "RunNotes-2025-01-15-Test-planning.md" sample-runnote-edn)
      (let [result (list-all-tags (str temp-dir))]
        (assert-count 3 result "Should find 3 unique tags")
        (assert-contains result ":database" "Should contain :database")
        (assert-contains result ":migration" "Should contain :migration")
        (assert-contains result ":architecture" "Should contain :architecture")))))

(defn test-list-all-tags-multiple-files []
  (with-temp-dir
    (fn [temp-dir]
      (create-test-file temp-dir "RunNotes-2025-01-15-DB-planning.md" sample-runnote-edn)
      (create-test-file temp-dir "RunNotes-2025-01-16-API-impl.md"
                        "# API\n\n```edn :metadata\n{:phase \"implementation\" :tag #{:api :backend}}\n```")
      (let [result (list-all-tags (str temp-dir))]
        (assert-count 5 result "Should find 5 unique tags across files")
        (assert-contains result ":database" "Should contain :database")
        (assert-contains result ":api" "Should contain :api")
        (assert-contains result ":backend" "Should contain :backend")))))

(defn test-list-all-tags-deduplicated []
  (with-temp-dir
    (fn [temp-dir]
      (create-test-file temp-dir "RunNotes-2025-01-15-A.md"
                        "# A\n\n```edn :metadata\n{:phase \"research\" :tag #{:database :api}}\n```")
      (create-test-file temp-dir "RunNotes-2025-01-16-B.md"
                        "# B\n\n```edn :metadata\n{:phase \"planning\" :tag #{:database :security}}\n```")
      (let [result (list-all-tags (str temp-dir))]
        (assert-count 3 result "Should deduplicate tags")
        (assert-contains result ":database" "Should contain :database once")
        (assert-contains result ":api" "Should contain :api")
        (assert-contains result ":security" "Should contain :security")))))

(defn test-list-all-tags-sorted []
  (with-temp-dir
    (fn [temp-dir]
      (create-test-file temp-dir "RunNotes-2025-01-15-Test.md"
                        "# Test\n\n```edn :metadata\n{:phase \"research\" :tag #{:zulu :alpha :mike}}\n```")
      (let [result (list-all-tags (str temp-dir))]
        (assert-equal [":alpha" ":mike" ":zulu"] result "Should be sorted alphabetically")))))

(defn test-list-all-tags-no-matching-files []
  (with-temp-dir
    (fn [temp-dir]
      (create-test-file temp-dir "notes.txt" "Not a RunNote")
      (create-test-file temp-dir "README.md" "# README")
      (let [result (list-all-tags (str temp-dir))]
        (assert-equal [] result "Should return empty list for non-RunNotes files")))))

;; ============================================================================
;; Test Runner
;; ============================================================================

(defn run-all-tests []
  (println "\n🧪 Testing RunNotes Duplicate Functions")
  (println "=========================================")

  (test-suite "\n📄 extract-metadata Tests"
              [["EDN format" test-extract-metadata-edn-format]
               ["Date from filename" test-extract-metadata-date-from-filename]
               ["Title extraction" test-extract-metadata-title]
               ["No metadata block" test-extract-metadata-no-metadata]])

  (test-suite "\n🏷️  list-all-tags Tests"
              [["Empty directory" test-list-all-tags-empty-dir]
               ["Single file" test-list-all-tags-single-file]
               ["Multiple files" test-list-all-tags-multiple-files]
               ["Deduplication" test-list-all-tags-deduplicated]
               ["Sorted output" test-list-all-tags-sorted]
               ["No matching files" test-list-all-tags-no-matching-files]])

  (run-tests-and-exit))

;; ============================================================================
;; Main
;; ============================================================================

(when (= *file* (System/getProperty "babashka.file"))
  (run-all-tests))
