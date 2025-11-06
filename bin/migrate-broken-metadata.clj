#!/usr/bin/env bb

(ns migrate-broken-metadata
  "Migration script to fix broken metadata in RunNotes files.
   Converts markdown blockquote format to valid EDN metadata."
  (:require [clojure.string :as str]
            [babashka.fs :as fs]))

(def affected-files
  "List of 14 files with broken metadata (verified with grep scan)"
  ["RunNotes-2025-07-21-EventSourcingMigration-review.md"
   "RunNotes-2025-07-24-CommandBusDomainSeparation-research.md"
   "RunNotes-2025-08-02-PolyCheckErrors-research.md"
   "RunNotes-2025-08-09-ArchitecturalDecoupling-planning.md"
   "RunNotes-2025-08-09-CriticalEventStoreFixes-implementation.md"
   "RunNotes-2025-08-09-CriticalEventStoreFixes-planning.md"
   "RunNotes-2025-08-09-EventSourcingAudit-planning.md"
   "RunNotes-2025-08-09-EventSourcingAudit-research.md"
   "RunNotes-2025-08-09-ProductionHardening-planning.md"
   "RunNotes-2025-09-20-CriticalEventStoreFixes-review.md"
   "RunNotes-2025-09-22-QuantTrackA-MarketData-implementation.md"
   "RunNotes-2025-10-26-StateManagementBranchReconciliation-implementation.md"
   "RunNotes-2025-10-26-StateManagementBranchReconciliation-planning.md"
   "RunNotes-2025-10-26-StateManagementBranchReconciliation-research.md"])

(defn extract-field
  "Extract field value from broken metadata line"
  [content pattern]
  (when-let [match (re-find pattern content)]
    (str/trim (second match))))

(defn parse-tags
  "Parse tags from broken metadata format to EDN set"
  [tags-str]
  (if tags-str
    (let [tags (-> tags-str
                   (str/replace #"\s+" " ")
                   str/trim
                   (str/split #"\s+"))]
      (str "#{" (str/join " " tags) "}"))
    "#{}"))

(defn parse-date
  "Extract date from filename: RunNotes-YYYY-MM-DD-..."
  [filename]
  (let [parts (str/split filename #"-")]
    (str/join "-" (take 3 (drop 1 parts)))))

(defn extract-phase-name
  "Extract just the phase name from 'Phase: Planning (2 of 4)' format"
  [phase-str]
  (when phase-str
    (-> phase-str
        (str/replace #"\s*\(\d+ of \d+\)" "")
        str/trim
        str/lower-case)))

(defn migrate-file
  "Migrate a single file from broken metadata to valid EDN"
  [base-dir filename]
  (let [file-path (str base-dir "/" filename)
        content (slurp file-path)

        ;; Extract fields from broken metadata
        phase-raw (extract-field content #"> \*\*Phase\*\*:\s*(.+)")
        tags-raw (extract-field content #"> \*\*Tags\*\*:\s*(.+)")
        date (parse-date filename)

        ;; Process fields
        phase (extract-phase-name phase-raw)
        tags (parse-tags tags-raw)

        ;; Construct valid EDN metadata
        edn-metadata (str "```edn :metadata\n"
                         "{:phase \"" phase "\"\n"
                         " :tag " tags "\n"
                         " :status :active\n"
                         " :date {:created \"" date "\"}}\n"
                         "```\n")

        ;; Replace broken metadata section with valid EDN
        ;; Pattern matches from "> **Phase**:" to just before "## "
        fixed-content (str/replace-first content
                                        #"(?s)> \*\*Phase\*\*:.*?(?=\n##)"
                                        edn-metadata)]

    {:filename filename
     :phase phase
     :tags tags
     :date date
     :original-length (count content)
     :fixed-length (count fixed-content)
     :fixed-content fixed-content}))

(defn dry-run
  "Perform dry-run migration and show what would change"
  [base-dir]
  (println "=== DRY RUN: Migration Preview ===\n")
  (doseq [filename affected-files]
    (try
      (let [result (migrate-file base-dir filename)]
        (println (str "✓ " filename))
        (println (str "  Phase: " (:phase result)))
        (println (str "  Tags: " (:tags result)))
        (println (str "  Date: " (:date result)))
        (println))
      (catch Exception e
        (println (str "✗ " filename))
        (println (str "  Error: " (.getMessage e)))
        (println)))))

(defn execute-migration
  "Execute actual migration on all affected files"
  [base-dir]
  (println "=== EXECUTING MIGRATION ===\n")
  (let [results (atom {:success [] :failed []})]
    (doseq [filename affected-files]
      (try
        (let [file-path (str base-dir "/" filename)
              result (migrate-file base-dir filename)]
          ;; Write fixed content
          (spit file-path (:fixed-content result))
          (swap! results update :success conj filename)
          (println (str "✓ Migrated: " filename)))
        (catch Exception e
          (swap! results update :failed conj {:file filename :error (.getMessage e)})
          (println (str "✗ Failed: " filename " - " (.getMessage e))))))

    (println (str "\n=== MIGRATION COMPLETE ==="))
    (println (str "Success: " (count (:success @results))))
    (println (str "Failed: " (count (:failed @results))))

    (when (seq (:failed @results))
      (println "\nFailed files:")
      (doseq [failed (:failed @results)]
        (println (str "  - " (:file failed) ": " (:error failed)))))))

(defn -main [& args]
  (let [base-dir (or (first args) (str (System/getenv "HOME") "/src/xional/runnotes"))
        mode (or (second args) "dry-run")]

    (println (str "Base directory: " base-dir))
    (println (str "Mode: " mode))
    (println (str "Files to migrate: " (count affected-files) "\n"))

    (if (= mode "execute")
      (execute-migration base-dir)
      (dry-run base-dir))))

(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
