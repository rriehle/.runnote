#!/usr/bin/env bb

(ns ^:clj-kondo/ignore runnote-core
  "Core library for RunNotes tools.
   Provides shared file discovery and configuration functions.

   Delegates to config-core.bb for configuration handling.
   Provides recursive file discovery by default."
  (:require [babashka.fs :as fs]
            [clojure.string :as str]))

;; Load the generalized config-core library
(def lib-dir (str (System/getenv "HOME") "/.lib"))
(load-file (str lib-dir "/config-core.bb"))

;; Import functions from config-core
(def discover-project-root config-core/discover-project-root)
(def load-edn-file config-core/load-edn-file)
(def deep-merge config-core/deep-merge)
(def merge-configs config-core/merge-configs)
(def expand-path config-core/expand-path)
(def get-config-value config-core/get-config-value)

;; ============================================================================
;; Configuration Wrappers
;; ============================================================================

(defn load-config
  "Load and merge RunNotes configuration.
   Delegates to config-core/load-config with :runnote system type."
  ([]
   (load-config nil))
  ([project-root-override]
   (config-core/load-config :runnote project-root-override)))

(defn resolve-runnote-dir
  "Resolve RunNotes directory path from config."
  [config-result]
  (config-core/resolve-runnote-dir config-result))

(defn resolve-template-dir
  "Resolve RunNotes template directory path from config."
  [config-result]
  (config-core/resolve-runnote-template-dir config-result))

;; ============================================================================
;; RunNote-Specific File Discovery
;; ============================================================================

(defn find-runnote-files
  "Find all RunNote markdown files in the configured directory.

   Recursively scans subdirectories by default (unlike ADR tools which are opt-in).

   Args:
     runnote-dir: Directory to search (string or Path)
     options: Optional map with keys:
       :recursive - true (default) to recurse into subdirs, false for single dir
       :pattern - filename glob pattern (default: \"RunNotes-*.md\")

   Returns:
     Sequence of file paths, or nil if directory doesn't exist

   Examples:
     (find-runnote-files \"/path/to/runnotes\")
     ;; => finds all RunNotes-*.md recursively

     (find-runnote-files dir {:pattern \"RunNotes-*-planning.md\"})
     ;; => finds only planning phase files recursively

     (find-runnote-files dir {:recursive false})
     ;; => finds only in top-level directory"
  ([runnote-dir]
   (find-runnote-files runnote-dir {}))
  ([runnote-dir {:keys [recursive pattern]
                 :or {recursive true
                      pattern "RunNotes-*.md"}}]
   (when (fs/exists? runnote-dir)
     (let [;; {,**/} matches both top-level (empty) and any subdirectory depth
           glob-pattern (if recursive
                          (str "{,**/}" pattern)
                          pattern)]
       (fs/glob runnote-dir glob-pattern)))))

;; ============================================================================
;; Export for use by other scripts
;; ============================================================================

(def exports
  "Exported functions for use by RunNote tools"
  {:discover-project-root discover-project-root
   :load-config load-config
   :resolve-runnote-dir resolve-runnote-dir
   :resolve-template-dir resolve-template-dir
   :get-config-value get-config-value
   :expand-path expand-path
   :find-runnote-files find-runnote-files})
