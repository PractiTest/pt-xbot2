(ns pt-xbot2.utils
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]))

(defn load-config [file-name]
  (json/parse-stream (io/reader file-name) true))

(defn at-exit [f]
  (.addShutdownHook (Runtime/getRuntime) (Thread. f)))
