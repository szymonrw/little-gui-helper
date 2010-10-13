(ns little-gui-helper.properties
  "Properties tools.

  Macros to generate code that sets object properties at compile time."
  
  (:require (little-gui-helper [utils :as utils]
			       [events :as events])))

(defn- setter-name
  "Generate setter method name for key. Accepts strings, keywords
  and symbols.

  Notation :nice-property is translated to NiceProperty (CamelCase)."

  [key]
  (->> key name
       utils/CamelCase
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
  (doprops button size ^unroll (300 200))

  Property can also be an event definition. See documentation for
  little-gui-helper.events/addevent."
  
  ([obj m]
     (concat `(doto ~obj)
	     (->> m (map (fn [[key value]]
			   (cond (events/event-spec? key)
				 `(events/addevent [~key ~value])
				 
				 (-> value meta :tag (= 'unroll))
				 `(~(setter-name key) ~@value)

				 true ;; else
				 (list (setter-name key) value)))))))
  
  ([obj k v & kvs]
     {:pre [(-> kvs count even?)]}
     `(doprops ~obj ~(apply array-map k v kvs))))

