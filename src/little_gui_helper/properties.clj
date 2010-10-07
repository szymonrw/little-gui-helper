(ns little-gui-helper.properties
  "Properties tools.

  Functions and macros to set object properties on compile time.")

(defn setter-name
  "Generate setter name property key.
  Symbols are treated verbatim, they are just prefixed with '.set' and first
  is ensured to be upper case."

  [key]
  (->> key name (str ".set") symbol))

(defmacro doprops
  "Generate code that set property k (symbol) with value v on object.

  Sample Usage:
  (doprops button Text \"Shiny button\")"
  
  [obj k v & kvs]
  {:pre [(-> kvs count even?)]}
  
  (concat `(doto ~obj)
	  (->> (partition 2 (concat [k v] kvs))
	       (map (fn [[key value]] (list (setter-name key) value))))))

  