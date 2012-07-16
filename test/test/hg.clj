(ns test.hg
  (:use [clojure.test]
        [hg]))

(defmacro with-private-fns [[ns fns] & tests]
  "Refers private fns from ns and runs tests in context."
  `(let ~(reduce #(conj %1 %2 `(ns-resolve '~ns '~%2)) [] fns)
     ~@tests))

(with-private-fns [hg [extract-principal]]
  (deftest test-extract-principal
    (is (= "AG" (extract-principal "AG: blah")) "")
    (is (= "AG" (extract-principal "AG  blah")) "")
    (is (= "OTHER" (extract-principal "OTHER: blah")) "")
    (is (= "OT1" (extract-principal "OT1: blah")) "")
    (is (= nil (extract-principal "OT-1: blah")) "")))





