(ns wiring-calculator.core-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer [facts => ]]
            [wiring-calculator.core :refer :all]))

;user wants to see available projects to check the layout of the construction site (apartment/warehouse etc.)

(facts "load-project-names-test" (load-existing-project-names) => '("project1.txt" "project2.txt" "project3.txt"))








