(ns halfrunt.core
  (:use [clojure.java.io :only [file]]
        [clojure.string :only [split-lines]]
        [clj-time.core :only [date-time year month day hour minute]]
        [clj-time.coerce :only [from-date]]
        [incanter core stats charts])
  (:import [com.aragost.javahg.commands LogCommand DiffCommand]
           [com.aragost.javahg Repository]))

(defn to-hg-date [datetime]
  (str (year datetime) "-" (month datetime) "-" (day datetime) " " (hour datetime) ":" (minute datetime)))

(defn extract-principal [changeset]
  (re-find #"AG|KE|KA|AH|RC|KC" (.getMessage changeset)))

(defn normalize [date]
  (date-time (year date) (month date) (day date)))

(defn retrieve-lines-changed [repository revision]
  (let [diff (DiffCommand/on repository)]
    (.change diff (str revision))
    (- (count (filter #(re-find #"^[\+\-].*" %) (split-lines (.execute diff (into-array String [])))))
       2)))

(defn retrieve-changesets [start-date end-date]
  ""
  (let [repository (Repository/open (file "irm"))
        log (LogCommand/on repository)]
    (.date log (str (to-hg-date start-date) " to " (to-hg-date end-date)))
    (let [logs (.execute log (into-array String ["."]))]
          (map #(hash-map :principal (extract-principal %)
                          :node (.getNode %)
                          :revision (.getRevision %)
                          :commits 1
                          :lines-changed (retrieve-lines-changed repository (.getRevision %))
                          :date (normalize (from-date (.getDate (.getTimestamp %)))))
               (filter #(extract-principal %) logs)))))

(defn filter-by [principal changesets]
  (seq (filter (fn [{p :principal}] (= p principal)) changesets)))

(defn normalize-principal-changesets [date changesets principals]
  (map #(or (get changesets %)
            [{:date date :principal % :commits 0 :node nil}])
       principals))

(defn normalize-changesets [changesets]
  ""
  (for [[date changesets-by-date] (seq (group-by :date changesets))
        changesets-by-principal (normalize-principal-changesets date
                                                                (group-by :principal changesets-by-date)
                                                                (into #{} (keys (group-by :principal changesets))))
        changeset changesets-by-principal]
    changeset))

(defn commits-chart [changesets]
  ""
  (let [commits ($rollup sum :commits [:principal :date] (to-dataset (normalize-changesets changesets)))
        dates (sel commits :cols 0)
        principals (sel commits :cols 1)
        commits-per-day (sel commits :cols 2)
        dates (map #(.getMillis (.toInstant %)) dates)]
    (xy-plot dates
                  commits-per-day
                  :group-by principals
                  :legend true
                  :x-label "time"
                  :y-label "commits per day"
                  :density? false)))

(defn -main [& args]
  (let [changesets (retrieve-changesets (date-time 2012 1 1) (date-time 2012 4 1))]
    (view (commits-chart changesets))))

;; commits per day

;; files changed per day

;; changes per day

;; tests added per day

;; test modified per day

;; all the above per person (one chart for each person)

;; questiuons it will also answer ?
;;   are we working too much over the weekend ?
