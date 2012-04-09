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

(defn y-values [start-dt end-dt principal data]
  (join "," (map #(lines-changed principal % data) (date-range start-dt end-dt))))

(defn to-date-chart-data [start-dt end-dt data]
  (let [principals (into #{} (keys (group-by :principal data)))]
    (join "|" (map #(str (y-values start-dt end-dt % data)) principals))))

;(join "|" (map #(str (x-values start-dt end-dt) "|" (y-values start-dt end-dt % data)) principals))

(defn date-chart [data start-dt end-dt date-key value-key group-by-key]
  {:type "lc"
   :size "320x200"
   :data (to-date-chart-data start-dt end-dt data)
   ;:colors "00000F,0000FF,000FFF,00FFFF,0FFFFF,FFFFFF,00FF00"
   })

;;TODO add sum by parameter
(defn group-data [changesets]
  (mapcat (fn [[principal changesets]]
            (map #(hash-map :principal principal :date (first %) :lines-changed (second %))
                 (apply merge-with + (map #(hash-map (:date %) (:lines-changed %)) changesets))))
          (seq (group-by #(:principal %) changesets))))
