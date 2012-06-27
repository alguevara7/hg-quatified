(ns utils
  (:import [org.joda.time DateTime]))

(defn future-date-seq [dt]
  (iterate #(.plusDays % 1) dt))

(defn past-date-seq [dt]
  (iterate #(.minusDays % 1) dt))

(defn date-range [start-dt end-dt]
  (take-while #(or (.isBefore % end-dt) (.isEqual % end-dt))
              (future-date-seq start-dt)))
