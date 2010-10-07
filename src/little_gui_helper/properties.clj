(ns little-gui-helper.properties)

(defn setter-name
  "Generate setter name from symbol or keyword"
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

  