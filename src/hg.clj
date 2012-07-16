(ns hg
  (:use [clojure.java.io :only [file]]
        [clj-time.core :only [year month day hour minute]]
        [clj-time.coerce :only [from-date]])
  (:require [clojure.string :as s])
  (:import [com.aragost.javahg.commands LogCommand DiffCommand]
           [com.aragost.javahg Repository]))


(defn- to-hg-date [datetime]
  (str (year datetime) "-" (month datetime) "-" (day datetime) " " (hour datetime) ":" (minute datetime)))

(defn- extract-principal [message]
  (when-let [principal-with-colon (re-find #"^\w*[:\s]" message)]
    (s/replace principal-with-colon #"[:\s]" "")))

(defn- retrieve-diff [repository revision]
  (let [diff (DiffCommand/on repository)]
    (.change diff (str revision))
    (.execute diff (into-array String []))))

(defn- calculate-lines-changed
  "takes a 'diff' and calculates the number of lines changed."
  [diff]
  (- (count (filter #(re-find #"^[\+\-].*" %) (s/split-lines diff)))
     2))

(defn changesets
  "retrieves changesets from a mercurial repository located in the folder <b>irm</b>, relative to the current folder."    
  [start-date end-date]
  (let [repository (Repository/open (file "irm"))
        log (LogCommand/on repository)]
    (.date log (str (to-hg-date start-date) " to " (to-hg-date end-date)))
    (let [logs (.execute log (into-array String ["."]))]
          (map #(hash-map :principal (extract-principal (.getMessage %))
                          :node (.getNode %)
                          :revision (.getRevision %)
                          :lines-changed (calculate-lines-changed (retrieve-diff repository (.getRevision %)))
                          :date (from-date (.getDate (.getTimestamp %))))
               (filter #(extract-principal %) logs)))))

