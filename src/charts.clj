(ns charts
  (:use [c2.core :only [unify]]
        [cssgen :only [css rule]]
        [clojure.string :only [join]])
  (:require [c2.scale :as scale]))

;; from c2.core
(defn style
  "Convert map to CSS string. Optional :numeric-suffix added to numbers (defaults to 'px')."
  [m & {:keys [numeric-suffix]
        :or {numeric-suffix "px"}}]
  (join (for [[k v] m]
          (str (name k) ":"
               (if (number? v)
                 (str v numeric-suffix)
                 v)
               ";"))))

(defn box-width-line [y-position]
    [:line {:x1 0 :x2 100
            :y1 y-position :y2 y-position}])

(defn bars []
  (let [width 500, bar-height 20
      data {"A" 1, "B" 2, "C" 4, "D" 3}
      s (scale/linear :domain [0 (apply max (vals data))]
                      :range [0 width])]

  [:div#bars
   (unify data (fn [[label val]]
                 [:div {:style (style {:height bar-height
                                       :width (s val)
                                       :background-color "gray"})}
                  [:span {:style "color: white;"} label]
                  ]))]))

(defn line-chart
  "sequable of maps"
  [data fy]
  (let [height 200 width 400
        scale-y (scale/linear :domain [0 (apply max (map fy data))]
                              :range [0 height])
        scale-x (scale/linear :domain [0 (count data)]
                              :range [0 width])
        lala (unify data
                    (fn [datum]
                      [:line {:x1 0, :y1 0
                       :x2 0, :y2 (scale-y (fy datum))
                       :style "stroke:red;stroke-width:2"}]))]
    (seq [:div#much [:svg {:style (style {:display "block"
                                         :margin "auto"
                                         :height height
                                         :width width})}
                [:g
                 lala
                 ]]]))


  
)