(ns views.common
  (use [noir.core]
       [hiccup.core]
       [hiccup.page]
       [hiccup.element]
       [hiccup.util]))

;; links and includes
(def all-include-tags {:jquery (include-js "http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js")
                       :base-css (include-css "css/base.css")
                       :skeleton-css (include-css "css/skeleton.css")
                       :layout-css (include-css "css/layout.css")})

(def all-javascript-tags {:set-date-function
   (javascript-tag (str "function nada() {"
                        "}"))})

;; helper partials
(defpartial build-head [include-tags javascript-tags]
  [:head
   [:title "Halfrunt, your personal code care specialist."]
   [:meta {:name "description" :content "Halfrunt, your personal code care specialist."}]
   [:meta {:charset "utf-8"}]
   [:meta {:name "author" :content "alexguev@gmail.com"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1, maximum-scale=1"}]
   [:link {:type "shortcut icon" :href (to-uri "img/favicon.ico")}]
   [:link {:type "apple-touch-icon" :href (to-uri "img/apple-touch-icon.png")}]
   [:link {:type "apple-touch-icon" :sizes "72x72" :href (to-uri "img/apple-touch-icon-72x72.png")}]
   [:link {:type "apple-touch-icon" :sizes "114x114" :href (to-uri "img/apple-touch-icon-114x114.png")}]
   (map #(get all-include-tags %) include-tags)
   (map #(get all-javascript-tags %) javascript-tags)])


;; layouts
(defpartial main-layout [& content]
  (html
   (build-head [:jquery :base-css :skeleton-css :layout-css] [:set-date-function])
   [:body {:onload "setDate()"}
    [:div.container
     [:div {:class "sixteen columns"}
      [:h3 {:class "remove-bottom" :style "margin-top: 0px"} (link-to "./" "Halfrunt")]
      [:h6 "Your personal code care specialist"]]
     [:div content]]]))