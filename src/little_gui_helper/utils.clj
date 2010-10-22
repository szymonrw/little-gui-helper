(ns little-gui-helper.utils
  "General utilities for Little GUI Helper.
  Name \"utils\" usually means that author didn't know where to put
  this stuff."
  (:require (clojure.contrib [string :as string])))

(defn camelCase-method
  "Translates a string in a form of \"nice-property\" to
  niceProperty (traditionally used in Java for method names."
  [s]
  (string/replace-by #"\-\w" #(-> %1 last Character/toUpperCase str) s))

(defn CamelCase
  "Like camelCase but first letter is always uppercase (traditional
  way of naming classes in Java."
  [s]
  (let [camel-cased (camelCase-method s)]
  (reduce str (-> camel-cased first Character/toUpperCase) (rest camel-cased))))
