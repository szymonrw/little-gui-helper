(ns little-gui-helper.properties
  "Properties tools.

  Functions and macros to set object properties on compile time.")

(defn setter-name
  "Generate setter name property key.
  Symbols are treated verbatim, they are just prefixed with '.set' and first
  is ensured to be upper case.

  TODO: Translate keywords from :camel-case-property -> .setCamelCaseProperty"
  [key]
  (->> key name (str ".set") symbol))

(defmacro doprops
  "Generate code that set property k (symbol) on object.
  
  sample usage:
  (doprops button Text \"Shiny button\" Width 100)"
  
  [obj k v & kvs]
  {:pre [(-> kvs count even?)]}
  
  (concat `(doto ~obj)
	  (->> (partition 2 (concat [k v] kvs))
	       (map (fn [[key value]] (list (setter-name key) value))))))

  