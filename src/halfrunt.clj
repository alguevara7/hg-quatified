(ns halfrunt
  (:use [clj-time.core :only [date-time]])
  (:require [charts]
            [hg]))

(defn filter-by
  "filters 'changesets' by 'principal'"
  [principal changesets]
  (filter (fn [{p :principal}] (= p principal)) changesets))

(defn -main [& args]
  (let [changesets (hg/changesets (date-time 2012 2 1) (date-time 2012 4 1))]
    (doseq [changeset (filter-by "AG" changesets)]
      (prn changeset))))

;(view (date-chart changesets (date-time 2012 2 1) (date-time 2012 4 1) :date :lines-changed :principal))

;; commits per day

;; files changed per day

;; changes per day

;; tests added per day

;; test modified per day

;; all the above per person (one chart for each person)

;; questiuons it will also answer ?
;;   are we working too much over the weekend ?