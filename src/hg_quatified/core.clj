(ns hg-quatified.core
  (:use [clojure.java.io :only [file]]
        [clj-time.core :only [date-time year month day hour minute]]
        [clj-time.coerce :only [from-date]]
        [incanter core stats charts])
  (:import [com.aragost.javahg.commands LogCommand]
           [com.aragost.javahg Repository]))

(defn retrieve-code-stats1 [start-date end-date]
  ""
  {(date-time 2011 01 01) {"P1" {:commits 2 :added 10 :deleted 10}
                           "P2" {:commits 1 :added 5 :deleted 10}}
   (date-time 2011 01 02) {"P1" {:commits 7 :added 1 :deleted 5}
                           "P2" {:commits 9 :added 5 :deleted 1}}
   (date-time 2011 01 03) {"P1" {:commits 6 :added 7 :deleted 10}
                           "P2" {:commits 1 :added 5 :deleted 20}}
   (date-time 2011 01 04) {"P1" {:commits 1 :added 6 :deleted 22}
                           "P2" {:commits 2 :added 5 :deleted 17}}
   (date-time 2011 01 05) {"P1" {:commits 1 :added 11 :deleted 1}
                           "P2" {:commits 7 :added 5 :deleted 1}}
   (date-time 2011 01 06) {"P1" {:commits 1 :added 12 :deleted 0}
                           "P2" {:commits 2 :added 5 :deleted 1}}
   (date-time 2011 01 07) {"P1" {:commits 1 :added 22 :deleted 0}
                           "P2" {:commits 4 :added 5 :deleted 1}}
   (date-time 2011 01  8) {"P1" {:commits 1 :added 19 :deleted 0}
                           "P2" {:commits 3 :added 5 :deleted 1}}})

(defn to-hg-date [datetime]
  (str (year datetime) "-" (month datetime) "-" (day datetime) " " (hour datetime) ":" (minute datetime)))

(defn extract-principal [changeset]
  (re-find #"AG|KE|KA|AH|RC|KC" (.getMessage changeset)))

(defn normalize [date]
  (date-time (year date) (month date) (day date)))

(defn retrieve-changesets [start-date end-date]
  ""
  (let [repository (Repository/open (file "irm"))
        log (LogCommand/on repository)]
    (do (.date log (str (to-hg-date start-date) " to " (to-hg-date end-date)))
        (let [logs (.execute log (into-array String ["."]))]
          (map #(hash-map :principal (extract-principal %)
                          :node (.getNode %)
                          :commits 1
                          :date (normalize (from-date (.getDate (.getTimestamp %)))))
               (filter #(extract-principal %) logs))))))

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
        commits-per-day (sel commits :cols 2)]
    (scatter-plot (map #(.getMillis (.toInstant %)) dates)
                  commits-per-day
                  :group-by principals
                  :legend true)))


;; commits per day

;; files changed per day

;; changes per day

;; tests added per day

;; test modified per day

;; all the above per person (one chart for each person)