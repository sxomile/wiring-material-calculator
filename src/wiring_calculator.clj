(ns wiring-calculator)

(defn load-project [filepath]
  (clojure.string/replace
    (slurp "housing-projects/project1.txt") "\r" ""))

(defn load-existing-project-names []
  (rest (map #(clojure.string/lower-case
                (clojure.string/replace-first (.getPath %) #"^housing-projects\\" ""))
             (file-seq (clojure.java.io/file "housing-projects")))))

(defn get-input
  "Function definition taken from book Clojure for true and brave, returns lowered user input"
  ([] (get-input ""))
  ([default]
   (let [input (clojure.string/trim (read-line))]
     (if (empty? input)
       default
       (clojure.string/lower-case input)))))

(defn print-collection [collection]
  (doseq [name (load-existing-project-names)]
    (when name
      (println name))))

(defn -main
  [& args]
  (print-collection load-existing-project-names)
  (get-input))
