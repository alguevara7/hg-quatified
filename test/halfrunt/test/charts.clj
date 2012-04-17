(ns halfrunt.test.charts
  (:use [halfrunt.charts]
        [clojure.test]
        [clj-time.core :only [date-time]]))

(defmacro with-private-fns [[ns fns] & tests]
  "Refers private fns from ns and runs tests in context."
  `(let ~(reduce #(conj %1 %2 `(ns-resolve '~ns '~%2)) [] fns)
     ~@tests))
  
(with-private-fns [halfrunt.charts [chart-to-url]]
  (deftest test-chart-to-url
    (is (= "https://chart.googleapis.com/chart?cht=p3&chs=250x100&chd=t:60,40&"
           (chart-to-url (array-map :type "p3" :size "250x100" :data "t:60,40"))) "TBD")))


(deftest test-x-values
    (is (= "19991212,19991213"
           (x-values (date-time 1999 12 12) (date-time 1999 12 13))) ""))

;;(view {:type "lc" :size "250x100" :data "t:60,40,30,20,20|6,41,32,25,27" :color "FF0000,00FF00" })