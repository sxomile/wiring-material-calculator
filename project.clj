(defproject wiring-material-calculator "0.0.1-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [midje "1.10.9"]]
  :plugins [[lein-midje "3.2.1"]]
  :main ^:skip-aot wiring-calculator.core
)