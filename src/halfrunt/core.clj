(ns halfrunt.core
  (:use [clojure.java.io :only [file]]
        [clojure.string :only [split-lines]]
        [clj-time.core :only [date-time year month day hour minute]]
        [clj-time.coerce :only [from-date]]
        [halfrunt.charts :only [date-chart view]])
  (:import [com.aragost.javahg.commands LogCommand DiffCommand]
           [com.aragost.javahg Repository]))

(defn to-hg-date [datetime]
  (str (year datetime) "-" (month datetime) "-" (day datetime) " " (hour datetime) ":" (minute datetime)))

(defn extract-principal [changeset]
  (re-find #"AG|KE|KA|AH|RC|KC|MG" (.getMessage changeset)))

(defn retrieve-diff [repository revision]
  (let [diff (DiffCommand/on repository)]
    (.change diff (str revision))
    (.execute diff (into-array String []))))

(defn calculate-lines-changed
  "takes a 'diff' and calculates the number of lines changed."
  [diff]
  (- (count (filter #(re-find #"^[\+\-].*" %) (split-lines diff)))
     2))

(defn retrieve-changesets
  "retrieves changesets from a mercurial repository located in the folder <b>irm</b>, relative to the current folder."    
  [start-date end-date]
  (let [repository (Repository/open (file "irm"))
        log (LogCommand/on repository)]
    (.date log (str (to-hg-date start-date) " to " (to-hg-date end-date)))
    (let [logs (.execute log (into-array String ["."]))]
          (map #(hash-map :principal (extract-principal %)
                          :node (.getNode %)
                          :revision (.getRevision %)
                          :commits 1
                          :lines-changed (calculate-lines-changed (retrieve-diff repository (.getRevision %)))
                          :date (from-date (.getDate (.getTimestamp %))))
               (filter #(extract-principal %) logs)))))

(defn filter-by
  "filters 'changesets' by 'principal'"
  [principal changesets]
  (seq (filter (fn [{p :principal}] (= p principal)) changesets)))

(defn -main [& args]
  (let [changesets (retrieve-changesets (date-time 2012 2 1) (date-time 2012 4 1))]
    (view (date-chart changesets (date-time 2012 2 1) (date-time 2012 4 1) :date :lines-changed :principal))))

;; commits per day

;; files changed per day

;; changes per day

;; tests added per day

;; test modified per day

;; all the above per person (one chart for each person)

;; questiuons it will also answer ?
;;   are we working too much over the weekend ?

