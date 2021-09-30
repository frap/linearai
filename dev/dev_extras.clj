;; Copyright © 2016-2021, Tuatara Red.
(ns ^{:clojure.tools.namespace.repl/load false} dev-extras
  "This namespace provides all of the general-purpose dev utilities used from a
  `dev` namespace.  These vars are all available from `dev`."
  (:require
   [clojure.test :refer [run-tests]]
   [juxt.clip.repl :refer [start stop set-init! reset system]]
   [aero.core :refer [read-config]]
   [clojure.java.io :as io]
   io.aviso.ansi))

(when (try
        (Class/forName "org.slf4j.bridge.SLF4JBridgeHandler")
        (catch ClassNotFoundException _
          false))
  (eval
    `(do
       (org.slf4j.bridge.SLF4JBridgeHandler/removeHandlersForRootLogger)
       (org.slf4j.bridge.SLF4JBridgeHandler/install))))


(defn add-lib
  [& args]
  (apply (requiring-resolve 'clojure.tools.deps.alpha.repl/add-lib) args))

(defn set-init-from-config []
  (set-init! (fn [] (read-config (io/resource "config.edn")))))

(def system-config {:frap {:start 1}})

(defn- test-namespaces
  []
  (keep (fn [[ns vars]]
          (when (some (comp :test meta) vars) ns))
        (map (juxt identity (comp vals ns-publics))
             (all-ns))))

(defn test-all
  "Run all tests"
  []
  (apply run-tests (test-namespaces)))

(defn reset-and-test
  "Reset the system, and run all tests."
  []
  (reset)
  (time (test-all)))

(comment
  (start)
  (reset)
  (stop)
  system)
