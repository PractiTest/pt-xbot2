(defproject pt-xbot2 "2.0.1"
  :description "PractiTest xBot agent"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [compojure "1.1.5"]
                 [ring/ring-jetty-adapter "1.1.8"]
                 [cheshire "5.0.2"]]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler pt-xbot2.server/handler}
  :main pt-xbot2.core)
