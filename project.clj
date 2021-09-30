(defproject neanderthal-hola "0.43.1"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [uncomplicate/neanderthal "0.43.3"]
                 [midje  "1.10.4"]
                 [criterium "0.4.6"]
                 ;;Optional. If bytedeco is not present, a system-wide MKL is used.
                 [org.bytedeco/mkl-platform-redist "2021.3-1.5.6"]
                 [net.mikera/core.matrix  "0.62.0"]
                 [com.taoensso/timbre "5.1.2"]
                 [io.pedestal/pedestal.log "0.5.9"]
                 [ch.qos.logback/logback-classic "1.2.6"]
                 [uncomplicate/clojurecl "0.15.1"]
                 [org.jocl/jocl "2.0.4"]
                 [com.bhauman/rebel-readline "0.1.4"]
                 [nrepl/nrepl  "0.8.3"]
                 [cider/cider-nrepl "0.26.0"]
                 ]
  ;; Nvidia doesn't ship CUDA for macOS; you have to add this to your project
  :exclusions [[org.jcuda/jcuda-natives :classifier "apple-x86_64"]
               [org.jcuda/jcublas-natives :classifier "apple-x86_64"]
               [org.bytedeco/mkl-platform-redist :classifier "windows-x86_64"]
               [org.bytedeco/mkl-platform-redist :classifier "windows-x86"]
             ;;  [org.bytedeco/mkl-platform-redist :classifier "macosx-x86_64"]
               ]

  :uberjar-name "neanderthal-standalone.jar"
  ;; If on Java 9+, you have to uncomment the following JVM option.
  :jvm-opts ^:replace ["-Dclojure.compiler.direct-linking=true"
  ;;                     "-XX:MaxDirectMemorySize=16g"
  ;;                     "--add-opens=java.base/jdk.internal.ref=ALL-UNNAMED"
  ;;                     "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED"
                       ]
  )
