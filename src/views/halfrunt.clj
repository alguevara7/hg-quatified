(ns views.halfrunt
  (use [noir.core] 
       [hiccup.core] 
       [hiccup.page]
       [hiccup.form])
  (require [noir.validation :as validation]
           [clojure.string :as s]
           [noir.response :as resp]
           [views.common :as common]
           [charts]))

;; sixteen columns layout

(defpartial charts-page []
  (common/main-layout
   [:div {:class "row remove-bottom"}
    [:div {:class "three columns"}]
    [:div {:class "ten columns"}
     [:iaaa#a] [:iaaa#b]]
    ; [:div {:class "ten columns"} (charts/bars)]
    [:div {:class "three columns"}]]))

;(charts/line-chart [10 20 10 20 10 20 10 20 10 20] identity)
(defn log-info [message]
  (println *out*))

(defpage "/" {principal :principal}
  (charts-page))


