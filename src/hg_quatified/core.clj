(ns hg-quatified.core
  (:use [clj-time.core :only [date-time]]
        [incanter core stats charts]))

(defn render-code-stats-chart [start-date end-date code-stats]
  "writes the chart to standard output"
  )

(defn retrieve-code-stats [start-date end-date]
  ""
  {(date-time 2011 01 01) {"AG" {:added 10 :deleted 10}
                           "KE" {:added 5 :deleted 10}}
   (date-time 2011 01 02) {"AG" {:added 1 :deleted 5}
                           "KE" {:added 5 :deleted 1}}
   (date-time 2011 01 03) {"AG" {:added 7 :deleted 10}
                           "KE" {:added 5 :deleted 20}}
   (date-time 2011 01 04) {"AG" {:added 6 :deleted 22}
                           "KE" {:added 5 :deleted 17}}
   (date-time 2011 01 05) {"AG" {:added 11 :deleted 1}
                           "KE" {:added 5 :deleted 1}}
   (date-time 2011 01 06) {"AG" {:added 12 :deleted 0}
                           "KE" {:added 5 :deleted 1}}
   (date-time 2011 01 07) {"AG" {:added 22 :deleted 0}
                           "KE" {:added 5 :deleted 1}}
   (date-time 2011 01  8) {"AG" {:added 19 :deleted 0}
                           "KE" {:added 5 :deleted 1}}})