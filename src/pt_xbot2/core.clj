(ns pt-xbot2.core
  (:require [pt-xbot2.server :as server]
            [pt-xbot2.utils :as utils]
            [pt-xbot2.ui :as ui])
  (:gen-class))

(defn -main
  ([] (-main "etc/config.cfg"))
  ([config-file]
     (let [config (utils/load-config config-file)]
       (utils/at-exit (fn []
                        (println "Shutting down...")))
       (ui/init-icon (:icon config))
       (server/start-server (:server config)))))
