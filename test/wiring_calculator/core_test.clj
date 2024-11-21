(ns wiring-calculator.core-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer [facts => ]]
            [wiring-calculator.core :refer :all]))

;user wants to see available projects to check the layout of the construction site (apartment/warehouse etc.)
;only 3 projects will be used in early development
(facts "load-project-names-test" (load-existing-project-names) => '("project1.txt" "project2.txt" "project3.txt"))

;lightbulb tests
(facts "count-lightbulbs-p1" (count-bulbs (slurp (str "housing-projects/" "project1.txt"))) => 2)

(facts "count-lightbulbs-p2" (count-bulbs (slurp (str "housing-projects/" "project2.txt"))) => 0)

(facts "count-lightbulbs-p3" (count-bulbs (slurp (str "housing-projects/" "project3.txt"))) => 0)

;material pricing tests
;false material input (type map)
(facts "calculate-material-price-test1" (calculate-material-price :bulb "led" 5) => "The passed material doesn't exist. Try another input")

;!!!!!!!!!!!!!!!!!!!!!!
(facts "calculate-material-price-test2" (calculate-material-price :lightbulb "led" 5) => "The passed type doesn't exist. Try another input")
;would be better if it wasn't case sensitive, something to keep an eye for

;false material type input (type vector)
(facts "calculate-material-price-test3" (calculate-material-price :cable "single" 5) => "The passed type doesn't exist. Try another input")

;checking case price 0 for single map
(facts "calculate-material-price-test4" (calculate-material-price :lightbulb "LED" 0) => 0)

;'normal' case for map
(facts "calculate-material-price-test5" (calculate-material-price :lightbulb "LED" 6) => 42)

;case price 0 for vector
(facts "calculate-material-price-test6" (calculate-material-price :cable "3x2.5" 0) => 0.0)

;'normal' vector case
(facts "calculate-material-price-test7" (calculate-material-price :cable "3x2.5" 5) => 6.0)

;counting switches in project1
(facts "count-switches-test-1" (count-switches (slurp (str "housing-projects/" "project1.txt"))) => 2)

;counting switches in empty project (project 3)
(facts "count-switches-test-2" (count-switches (slurp (str "housing-projects/" "project3.txt"))) => 0)






