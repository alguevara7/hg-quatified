(ns hg-quatified.test.core
  (:use [hg-quatified.core]
        [clojure.test]
        [clj-time.core :only [date-time]]))

(deftest data-for-one-day
  (is (= [{:date (date-time 1980 1 1) :principal "P1" :commits 1}
          {:date (date-time 1980 1 1) :principal "P2" :commits 3}]
         (make-dataset-from {(date-time 1980 1 1) {"P1" {:commits 1}
                                                   "P2" {:commits 3}}})) "No tests have been written."))
