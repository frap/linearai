(ns prueba
  (:require [midje.sweet :refer [facts => truthy throws]]
            [criterium.core :refer [quick-bench with-progress-reporting]]
            [uncomplicate.commons.core :refer [with-release]]
        ;;    [uncomplicate.clojurecl.core
        ;;     :refer [with-default finish!]]
            [uncomplicate.neanderthal
             [core :refer [dim vctr?  asum axpy! axpy xpy zero ax  mv! mm! transfer! copy subvector]]
             [real :refer [entry dot]]
             [native :refer [dv dge fv fge]]

        ;;     [opencl :refer [with-default-engine clv clge]]
             ]
            [taoensso.timbre :as timbre
              :refer [log  trace  debug  info  warn  error  fatal  report
                      logf tracef debugf infof warnf errorf fatalf reportf
                      spy get-env]]
 ))

(info "hola Neanderthal Practice")
(facts
 "2.1 What is a Vector?"
 (let [v (dv 3.14159 2.718281828 -1.0 2.0)]
   (dim v) => 4
   (vctr? v ) => true

   (ifn? v) => true
   (v 0) => (entry v 0)
   (v 1) => (entry v 1)
   (v 2) => (entry v 2)
   (v 3) => (entry v 3)))

(def x (dv 1 2 3))
(def y (dv 10 20 30))
(dot x y)

;; Grokking deep learning
(defn w_sum [a b]
  (assert (count a) (count b))
  (map + a b)
  )

(def weights [0.1 0.2 -0.1])
(def neural_network [input weights]
  (w_sum input weights))

(def toes [8.5 9.5 9.9 9.0])
