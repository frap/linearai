(ns build
  (:require [clojure.tools.build.api :as b]
            [org.corfield.build :as bb ]))

(def lib 'frap/rebel)
(def main 'frap.rebel )
(def src-dirs ["dev" "src"])
(def version (format "1.2.%s" (b/git-count-revs nil)))
(def snapshot "1.2.999-SNAPSHOT")

;;(def version (format "1.0.%s" (b/git-count-revs nil)))
;;
(defn ci "Run the CI pipeline of tests (and build the JAR)." [opts]
  (-> opts
      (assoc :lib lib :version (if (:snapshot opts) snapshot version))
      (test)
      (bb/clean)
      (bb/jar)))


(defn uber "Create the uberjar" [opts]
  (-> opts
      (assoc :lib lib :src-dirs src-dirs :main main )
      (bb/clean)
      (bb/uber)))

