(ns wiring-calculator.core
  (:import (java.io FileNotFoundException)))

(defn get-input
  "Function definition taken from book Clojure for true and brave, returns lowered user input"
  ([] (get-input ""))
  ([default]
   (let [input (clojure.string/trim (read-line))]
     (if (empty? input)
       default
       (clojure.string/lower-case input)))))

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

(defn load-existing-project-names []
  (rest (map #(clojure.string/lower-case
                (clojure.string/replace-first (.getPath %)
                                              #"^housing-projects\\" ""))
             (file-seq (clojure.java.io/file "housing-projects")))))

(defn print-collection-of-projects [collection]
  (doseq [name (collection)]
    (when name
      (println name))))

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
