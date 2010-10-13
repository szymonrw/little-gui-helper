(ns little-gui-helper.utils
  "General utilities for Little GUI Helper.
  Name \"utils\" usually means that author didn't know where to put this stuff."
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
