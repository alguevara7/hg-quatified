(ns halfrunt.test.date-utils
  (:use [halfrunt.date-utils]
        [clj-time.core :only [date-time]]
        [clojure.test]))

(deftest test-date-range
    (is (= []
           (date-range (date-time 2000 1 1) (date-time 1999 1 1))) "empty range because end-date is before start-date")
    (is (= [(date-time 2000 1 1)]
           (date-range (date-time 2000 1 1) (date-time 2000 1 1))) "range with one item because start-dt = end-dt")
    (is (= [(date-time 2000 1 1) (date-time 2000 1 2) (date-time 2000 1 3)]
           (date-range (date-time 2000 1 1) (date-time 2000 1 3))) ""))