(ns little-gui-helper.properties
  "Properties tools.

  Macros to generate code that sets object properties at compile time."
  
  (:require (clojure.contrib [string :as string])))

(defn camelCase
  "Translates a string in a form of \"nice-property\" to niceProperty"
  [s]
  (string/replace-by #"\-\w" #(-> %1 last Character/toUpperCase str) s))

(defn CamelCase
  "Like camelCase but first letter is always uppercase."
  [s]
  (let [camel-cased (camelCase s)]
  (reduce str (-> camel-cased first Character/toUpperCase) (rest camel-cased))))
  
(defn- setter-name
  "Generate setter method name for key. Accepts strings, keywords
  and symbols.

  Notation :nice-property is translated to NiceProperty (CamelCase)."

  [key]
  (->> key name
       CamelCase
       (str ".set")
       symbol))

(defmacro doprops
  "Generate code that set property k (symbol) with value v on object.
  Two variants: with map m and with variable number of key and value pairs.

  Sample Usage:
  (doprops button text \"Shiny button\") or
  (doprops button {text \"Shiny button\"})

  If a setter method gets multiple parameters, as in
  JButton/setSize(width, height), put a list or vector and annotate it with
  ^unroll, for example:
  (doprops button size ^unroll (300 200))"
  
  ([obj m]
     (concat `(doto ~obj)
	     (->> m (map (fn [[key value]]
			   (if (-> value meta :tag (= 'unroll))
			     `(~(setter-name key) ~@value)
			     (list (setter-name key) value)))))))
  
  ([obj k v & kvs]
     {:pre [(-> kvs count even?)]}
     `(doprops ~obj ~(apply array-map k v kvs))))

