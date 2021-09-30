(ns frap.rebel
  (:require
   [aero.core :refer [read-config]]
   [clojure.java.io :as io]
   [juxt.clip.repl :refer [start stop reset set-init! system]]
   [clojure.pprint :as pp]
   [fipp.edn :as fipp]
   [rebel-readline.main]
   [rebel-readline.clojure.main]
   )
  (:require
   [rebel-readline.core :as core]
    [rebel-readline.clojure.line-reader :as clj-line-reader]
    [rebel-readline.jline-api :as api]
    [rebel-readline.tools :as tools]
    [rebel-readline.clojure.service.local :as clj-service]
    [io.aviso.ansi]
    [clojure.main])
  (:gen-class))

(in-ns 'rebel-readline.clojure.main)
(require '[clojure.pprint :as pp])
(require '[fipp.edn :as fipp])

(defn syntax-highlight-prn
  "Print a syntax highlighted clojure value.
  This printer respects the current color settings set in the
  service.
  The `rebel-readline.jline-api/*line-reader*` and
  `rebel-readline.jline-api/*service*` dynamic vars have to be set for
  this to work.
  See `rebel-readline.main` for an example of how this function is normally used"
  [x]
  (binding [*out* (.. api/*line-reader* getTerminal writer)]
    (try
      (println (api/->ansi (clj-line-reader/highlight-clj-str
                             (with-out-str (pp/pprint x)))))
      (catch java.lang.StackOverflowError e
        (println (pr-str x))))))

(defn syntax-highlight-pprint
  "Print a syntax highlighted clojure value.
  This printer respects the current color settings set in the
  service.
  The `rebel-readline.jline-api/*line-reader*` and
  `rebel-readline.jline-api/*service*` dynamic vars have to be set for
  this to work."
  [x]
  (binding [*out* (.. api/*line-reader* getTerminal writer)]
    (try
      (print (api/->ansi
               (clj-line-reader/highlight-clj-str
                 (with-out-str (pp/pprint x)))))
      (catch java.lang.StackOverflowError e
        (pp/pprint x)))))

(defn syntax-highlight-fipp
  "Print a syntax highlighted clojure value.
  This printer respects the current color settings set in the
  service.
  The `rebel-readline.jline-api/*line-reader*` and
  `rebel-readline.jline-api/*service*` dynamic vars have to be set for
  this to work."
  [x]
  (binding [*out* (.. api/*line-reader* getTerminal writer)]
    (try
      (print (api/->ansi
               (clj-line-reader/highlight-clj-str
                 (with-out-str (fipp/pprint x)))))
      (catch java.lang.StackOverflowError _
        (try
          (fipp/pprint x)
          ;; Just in case of
          ;; https://github.com/brandonbloom/fipp/issues/28
          (catch java.lang.StackOverflowError _
            (prn x)))))))

(in-ns 'frap.rebel)

(def system-config {:frap {:start 1}})

(defn -main
  [& args]
  (rebel-readline.core/ensure-terminal
    (rebel-readline.clojure.main/repl
      :init (fn []
              (try
                (println "[Frap] Loading Clojure code, please wait...")
                (require 'dev)
                (in-ns 'dev)
                (println (str
                           (io.aviso.ansi/yellow "[Frap] Now enter ")
                           (io.aviso.ansi/bold-yellow "code")
                           (io.aviso.ansi/yellow " to start playing with neanderthal!")))
                (catch Exception e
                  (if (= (.getMessage e)
                         "Could not locate dev__init.class, dev.clj or dev.cljc on classpath.")
                    (do
                      (println (io.aviso.ansi/red "[Frap] Failed to require dev. Falling back to `user`. "))
                      (println (io.aviso.ansi/bold-red "[Frap] Make sure to supply `-M:dev` when running `../bin/rebel`.")))

                    (do
                      (.printStackTrace e)
                      (println "[Frap] Failed to require dev, this usually means there was a syntax error. See exception above.")
                      (println "[Frap] Please correct it, and enter (fixed!) to resume development."))))))
      :print rebel-readline.clojure.main/syntax-highlight-pprint)
    ;; When the REPL stops, stop:
    ;; (let [system (start system-config)]
    ;;          (.addShutdownHook
    ;;            (Runtime/getRuntime)
    ;;            (new Thread #(stop system))
    ;;            ))
     (System/exit 0)))


;; (rebel-readline.main/-main)
