(defproject halfrunt "0.0.1-SNAPSHOT"
  :description "your person code doctor"
  :plugins [[lein-swank "1.4.4"]
            [lein-marginalia "0.7.1"]
            [lein-light "0.0.4"]]
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [clj-time "0.4.3"]
                 [hiccup "1.0.0"]
                 [noir "1.3.0-beta3" :exclude [org.clojure/clojure]]
                 [cssgen "0.2.6"]
                 [com.keminglabs/c2 "0.1.0"]
                 [com.aragost.javahg/javahg "0.2"]])

