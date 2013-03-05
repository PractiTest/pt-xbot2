(ns pt-xbot2.server
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [resources not-found]]
            [compojure.handler :refer [site]]
            [ring.adapter.jetty :refer [run-jetty]]))

(defroutes app-routes
  (GET "/pref" [] "<p>Preferences</p>")
  (resources "/")
  (not-found "Page not found"))

(def handler
  (site app-routes))

(defn start-server [server-config]
  (run-jetty handler {:port (:listening-port server-config)}))
