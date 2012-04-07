(ns halfrunt.test.core
  (:use [halfrunt.core]
        [clojure.test]
        [clj-time.core :only [date-time]]))

(deftest test-add-missing-commits
  (is (= [{:date (date-time 1980 1 1) :principal "P1" :node "1" :commits 1}
          {:date (date-time 1980 1 1) :principal "P2" :node "2" :commits 1}
          {:date (date-time 1980 1 2) :principal "P1" :node "3" :commits 1}
          {:date (date-time 1980 1 2) :principal "P2" :node nil :commits 0}]
         (normalize-changesets [{:date (date-time 1980 1 1) :principal "P1" :node "1" :commits 1}
                                {:date (date-time 1980 1 1) :principal "P2" :node "2" :commits 1}
                                {:date (date-time 1980 1 2) :principal "P1" :node "3" :commits 1}])) "edge case"))


(deftest test-calculate-lines-changed
  (is (= 2 (calculate-lines-changed "+++\n---\n+line1\n+line2")) "lines added")
  (is (= 2 (calculate-lines-changed "+++\n---\n-line1\n-line2")) "lines removed")
  (is (= 2 (calculate-lines-changed "+++\n---\n-line1\n+line2")) "lines added and removed"))