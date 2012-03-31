(ns hg-quatified.test.core
  (:use [hg-quatified.core]
        [clojure.test]
        [clj-time.core :only [date-time]]))

(deftest data-for-one-day
  (is (= [{:date (date-time 1980 1 1) :principal "P1" :commits 1}
          {:date (date-time 1980 1 1) :principal "P2" :commits 3}]
         (make-dataset-from {(date-time 1980 1 1) {"P1" {:commits 1}
                                                   "P2" {:commits 3}}})) "No tests have been written."))

(deftest test-add-missing-commits
  (is (= [{:date (date-time 1980 1 1) :principal "P1" :node "1"}
          {:date (date-time 1980 1 1) :principal "P2" :node "2"}
          {:date (date-time 1980 1 2) :principal "P1" :node "3"}
          {:date (date-time 1980 1 2) :principal "P2" :node nil}]
         (normalize-changesets [{:date (date-time 1980 1 1) :principal "P1" :node "1"}
                                {:date (date-time 1980 1 1) :principal "P2" :node "2"}
                                {:date (date-time 1980 1 2) :principal "P1" :node "3"}])) "edge case"))