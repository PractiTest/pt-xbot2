; Parts of the code are based on clojure.java.shell package
; I couldn't use it as-is because it doesn't support timeouts

(ns pt-xbot2.scheduler
  (:use [clojure.java.io :only (as-file copy)])
  (import (java.util.concurrent Executors TimeUnit TimeoutException)
          (java.io StringWriter)))

(defonce scheduler (Executors/newSingleThreadScheduledExecutor))

(defn- parse-args [args]
  (let [[cmd opts] (split-with string? args)]
    [cmd (apply hash-map opts)]))

(defn- stream-to-string [in]
  (with-open [bout (StringWriter.)]
    (copy in bout :encoding "UTF-8")
    (.toString bout)))

(defn- exit-code [process timeout]
  (if timeout
    (try
      (.get (future (.waitFor process)) timeout TimeUnit/MILLISECONDS)
      (catch Exception e
        (if (or (instance? TimeoutException e)
                (instance? TimeoutException (.getCause e)))
          (do
            (.destroy process)
            :timeout)
          (throw e))))
    (.waitFor process)))

(defn sh [& args]
  (let [[cmd opts] (parse-args args)
        proc (.exec (Runtime/getRuntime) (into-array cmd) nil (as-file (:dir opts)))
        {:keys [in timeout]} opts]
    (if in
      (future
        (with-open [os (.getOutputStream proc)]
          (copy in os :encoding "UTF-8")))
      (.close (.getOutputStream proc)))
    (with-open [stdout (.getInputStream proc)
                stderr (.getErrorStream proc)]
      (let [out (future (stream-to-string stdout))
            err (future (stream-to-string stderr))]
        {:exit (exit-code proc timeout) :out @out :err @err}))))

(defn init-scheduler [config]
  (.scheduleWithFixedDelay scheduler
                           (fn []
                             (println "TestRunner is awake")
                             ; do something
                             (println "TestRunner finished, going to sleep"))
                           1
                           5
                           TimeUnit/SECONDS))
