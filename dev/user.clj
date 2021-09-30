(ns user
  (:require
   [clojure.tools.namespace.repl :refer :all]
   [clojure.java.classpath :refer [classpath-directories]]
   [juxt.clip.repl]
   [io.aviso.ansi :as ansi]))

;; Work around TNS-45.  This used to be fixed by using a forked version of tns,
;; but because it now comes in transitively, it cannot be compared.  This might
;; be fixed by TDEPS-17.
(let [edge-target? (fn [f]
                     ;; match target, target/dev target/prod, etc.
                     (re-matches #".*target(/\w+)?" (str f)))]
  (apply set-refresh-dirs
         (remove edge-target? (classpath-directories))))


(let [lock (Object.)]
  (defn dev
    "Call this to switch to the dev namespace."
    []
    (println (ansi/bold-cyan "[Frap] Loading Clojure code and REPL, please wait..."))
    (locking lock
      (require 'dev))
   ;; (when-not juxt.clip.repl/system
   ;;   (println (ansi/bold-yellow "[Frap] Starting up repl")))
    (in-ns 'dev)))

(defn fixed!
  "If, for some reason, the Clojure code in the project fails to
  compile - we still bring up a REPL to help debug the problem. Once
  the problem has been resolved you can call this function to continue
  development."
  []
  (refresh-all)
  (in-ns 'dev))
