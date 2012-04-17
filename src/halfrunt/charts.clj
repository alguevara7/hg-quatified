(ns halfrunt.charts
  (:use [clojure.core]
        [clojure.java.io :only [as-url]]
        [clojure.string :only [join]]
        [halfrunt.date-utils :only [date-range]]
        [clj-time.core :only [year month day]])
  (:import [javax.swing JTable JScrollPane JFrame JLabel ImageIcon]
          [javax.imageio ImageIO]
          [java.net URL]))

(defn chart-to-url [chart]
  (apply str (cons "https://chart.googleapis.com/chart?"
                   (map (fn [[key value]] (cond (= :type key) (str "cht=" value "&")
                                                (= :size key) (str "chs=" value "&")
                                                (= :data key) (str "chd=t:" value "&")
                                                (= :colors key) (str "chco=" value "&")
                                                (= :fills key) (str "chm=" value "&")
                                                (= :legend-labels key) (str "chdl=" value "&")
                                                (= :visible-axes key (str "chxt=" value "&"))
                                                (= :axis-range key (str "chxr=" value "&"))                                                
                                                :else ""))
                        (seq chart)))))

(defn hello-chart []
  {:type "lxy0" :size "250x100" :data "10,20|60,40|10,20|30,50" :colors "00FF00,FF0000" :legend-labels "P1|P2"})

(defn view [chart] 
  (doto (JFrame. (:title chart))
    (.add (JScrollPane.
          (JLabel. (ImageIcon. (ImageIO/read (as-url (chart-to-url chart)))))))
    (.setSize 800 600) ;;FIXME get chart's size
    (.setVisible true)))

(defn x-values [start-dt end-dt]
  (join "," (map #(str (* (- (.getDayOfYear %) 74) 4)) (date-range start-dt end-dt))))

(defn lines-changed [principal dt data]
  (apply + (map #(:lines-changed %) (filter #(and (= principal (:principal %))
                                                  (= dt (:date %))) data))))

(defn spy [value]
  (println value)
  value)

(def *limit* 10000.0)
(defn y-values [start-dt end-dt principal data]
  (join ","
        (map (fn [date]
               (let [change-count (lines-changed principal date data)
                     limit (min *limit* change-count)]
                 (spy (if (= change-count 0)
                        0.0
                        (* (/ (min change-count limit) *limit*) 100.0)))))
             (date-range start-dt end-dt))))

(defn all-principals [data]
  (into #{} (spy (keys (group-by :principal data)))))

(defn to-date-chart-data [start-dt end-dt data]
  (let [principals #{"RC"}]
    (join "|" (map #(str (y-values start-dt end-dt % data)) principals))))

;(join "|" (map #(str (x-values start-dt end-dt) "|" (y-values start-dt end-dt % data)) principals))

(defn date-chart [data start-dt end-dt date-key value-key group-by-key]
  {:type "lc"
   :size "320x200"
   :data (to-date-chart-data start-dt end-dt data)
;   :colors "0000FF,0000FF,00FFFF,00FFFF,00FFFF,FFFFFF,00FF00"
;   :visible-axes "x, y"
;   :axis-range "0,0,100"
   })

;;TODO add sum by parameter (interesting but not needed)
(defn group-data [changesets]
  (mapcat (fn [[principal changesets]]
            (map #(hash-map :principal principal :date (first %) :lines-changed (second %))
                 (apply merge-with + (map #(hash-map (:date %) (:lines-changed %)) changesets))))
          (seq (group-by #(:principal %) changesets))))
