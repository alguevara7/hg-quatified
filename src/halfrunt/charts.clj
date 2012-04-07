(ns halfrunt.charts
  (:use [clojure.core]
        [clojure.java.io :only [as-url]])
  (:import [javax.swing JTable JScrollPane JFrame JLabel ImageIcon]
          [javax.imageio ImageIO]
          [java.net URL]))

(defn- chart-to-url [chart]
  (apply str (cons "https://chart.googleapis.com/chart?"
                   (map (fn [[key value]] (cond (= :type key) (str "cht=" value "&")
                                                (= :size key) (str "chs=" value "&")
                                                (= :data key) (str "chd=" value "&")
                                                (= :colors key) (str "chco=" value "&")
                                                (= :fills key) (str "chm=" value "&") ;;FIXME abstract a bit
                                                :else ""))
                        (seq chart)))))


(defn hello-chart []
  {:type "p3" :size "250x100" :data "t:60,40"})

(defn view [chart] 
  (doto (JFrame. (:title chart))
    (.add (JScrollPane.
          (JLabel. (ImageIcon. (ImageIO/read (as-url (chart-to-url chart)))))))
    (.setSize 400 600) ;;FIXME get chart's size
    (.setVisible true)))

