(ns little-gui-helper.events
  "Tools for dealing with defining events

  Events are are special properties with special syntax. For example this
  property:

  +mouse.mouse-up body

  will expand to the equivalent Java code:

  object.addMouseListener(
    new MouseListener() {
      void mouseUp(MouseEvent event) {
        body;
      }});"
  (:require (little-gui-helper [properties :as props])
	    (clojure.contrib [string :as string])))

(defn- event-spec?
  "Returns true if name of symbol begins with + and contains /"
  [s]
  (let [n (name s)]
    (and (-> n first (= \+))
	 (-> n (.contains ".")))))

(defn- listener&method-names
  "Returns pair of listener name and method name.

  Syntax
  +[listener name without 'Listener' part].[method name]

  +arm.widget-armed => [\"ArmListener\" \"widgetArmed\"]"
  
  [s]
  {:pre [(event-spec? s)]}
  
  (let [[listener method] (->> s name rest ;; cut \+ from begining
			       (reduce str)
			       (string/split #"\."))]
    [(-> listener props/CamelCase (str "Listener"))
     (-> method props/camelCase)]))

(defmacro event
  ""
  [[spec body]]
  )
