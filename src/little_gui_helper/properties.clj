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
  Two variants: with map m and with variable number of key and value pairs.

  Sample Usage:
  (doprops button text \"Shiny button\") or
  (doprops button {text \"Shiny button\"})

  If a setter method gets multiple parameters, as in
  JButton/setSize(width, height), put a list or vector with '&' as first
  element, for example:
  (doprops button size (& 300 200))"
  
  ([obj m]
     (concat `(doto ~obj)
	     (->> m (map (fn [[key value]]
			   (if (and (sequential? value)
				    (= '& (first value)))
			     `(~(setter-name key) ~@(rest value))
			     (list (setter-name key) value)))))))
  
  ([obj k v & kvs]
     {:pre [(-> kvs count even?)]}
     `(doprops ~obj ~(partition 2 (concat [k v] kvs)))))
  
  