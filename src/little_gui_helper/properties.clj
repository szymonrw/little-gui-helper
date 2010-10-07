(ns little-gui-helper.properties
  "Properties tools.

  Functions and macros to set object properties on compile time."
  (:require (clojure.contrib [string :as string])))

(defn setter-name
  "Generate setter method name for key. Accepts strings, keywords
  and symbols.

  Notation :nice-property is translated to NiceProperty (CamelCase)."

  [key]
  (->> key name
       (string/replace-by #"^\w|\-\w" #(-> %1 last Character/toUpperCase str))
       (str ".set") symbol))

(defmacro doprops
  "Generate code that set property k (symbol) with value v on object.

  Sample Usage:
  (doprops button Text \"Shiny button\")"
  
  [obj k v & kvs]
  {:pre [(-> kvs count even?)]}
  
  (concat `(doto ~obj)
	  (->> (partition 2 (concat [k v] kvs))
	       (map (fn [[key value]] (list (setter-name key) value))))))

  