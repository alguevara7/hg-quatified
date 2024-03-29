(ns server
  (:require [noir.server :as server]))

(server/load-views "src/views/")

(defn -main [& m]
  (let [mode (or (first m) :dev)
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (server/start port {:mode (keyword mode)
                        :ns 'views})))
