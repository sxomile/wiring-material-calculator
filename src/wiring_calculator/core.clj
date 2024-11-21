(ns wiring-calculator.core
  (:import (java.io FileNotFoundException)))

;defining basic material (lightbulb, switch, socket, fuse box, cables)
;price in USD
;cable price defined per meter of length, other material defined by single piece
(def materials
  {:lightbulb {:price 7 :type "LED"}
   :socket    {:price 1 :type "single-phase"}
   :cable     [{:price 1 :type "3x1.5"}
               {:price 1.2 :type "3x2.5"}
               {:price 3 :type "5x4"}]
   :switch    [{:price 2 :type "mono pole"}
               {:price 2 :type "toggle"}
               {:price 2 :type "cross"}]
   :fuse-box  {:price 6 :type "on wall 230.5 x 175.5 x 99.5"}})

;User wants to know the number of bulbs so he can calculate total price
(defn count-bulbs [project]
  (count (filter #(= % \x) project)))

;User needs number of switches to calculate the price
;Will be modified, currently only assuming mono pole switches
(defn count-switches [project]
  (count (filter #(= % \/) project)))

;calculates price of distinct material type
;quantity of cables is measured in meters
;other material is measured in pieces
(defn calculate-material-price [material type quantity]
  (let [material-data (get materials material)]
    (cond
      (nil? material-data)
      "The passed material doesn't exist. Try another input"

      ;CORRECTED EXPLANATION:
      ;EVERY single material is defined as a map
      (map? material-data)
      (if (= (clojure.string/lower-case type) ((clojure.string/lower-case (get material-data :type "")))
        (* (:price material-data) quantity))
        "The passed type doesn't exist. Try another input")

      ;materials that have multiple kinds are in form of vectors of maps
      (vector? material-data)
      (let [item (some #(when (= (clojure.string/lower-case type)
                                 (clojure.string/lower-case (get % :type ""))) %) material-data)]
        ;the issue was that i was trying to lower :type directly, and not the value inside of it
        ;now i am using get to access that value
        ;seems better now, but tests are still failing (2 more than supposed)
        (if item
          (* (:price item) quantity)
          "The passed type doesn't exist. Try another input")))))

;retrieves user's input, multi-purpose
(defn get-input
  "Function implementation taken from book Clojure for true and brave, returns lowered user input"
  ([] (get-input ""))
  ([default]
   (let [input (clojure.string/trim (read-line))]
     (if (empty? input)
       default
       (clojure.string/lower-case input)))))

;loads housing project from txt file
;user wants to load the project so he can have insight in how the recommended material fits into site
(defn load-project [filename]
  (try
    (clojure.string/replace
      (slurp (str "housing-projects/" filename))
      "\r" "")
    (catch FileNotFoundException e
      (println (str "File not found: " filename "\nTry again or type -giveup to finish"))
      (let [filename (get-input)]
        (if (= filename "-giveup")
          (println "Goodbye!")
          (load-project filename))))
    (catch Exception e
      (str "An error occurred: " (.getMessage e)))))

;loads names of projects from directory
(defn load-existing-project-names []
  (rest (map #(clojure.string/lower-case
                (clojure.string/replace-first (.getPath %)
                                              #"^housing-projects\\" ""))
             (file-seq (clojure.java.io/file "housing-projects")))))

;prints project names, important for user interaction
(defn print-collection-of-projects [collection]
  (doseq [name (collection)]
    (when name
      (println name))))

;restarts prompt if user wants to keep working
(defn prompt-restart []
  (loop []
    (println "\nWould you like to load another project? (y-yes, n-no)")
    (let [choice (get-input)]
      (cond
        (= choice "y") true
        (= choice "n") (do (println "Goodbye!") false)
        :else (do
                (println "Invalid input. Please enter 'y' for yes or 'n' for no.")
                (recur))))))

(defn -main
  [& args]
  (loop []
    (print-collection-of-projects load-existing-project-names)
    (print (load-project (get-input)))
    (when (prompt-restart)
      (recur))))
