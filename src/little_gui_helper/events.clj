(ns little-gui-helper.events
  "Tools for dealing with defining events

  Events are are special properties with special syntax. For example this
  property:

  +mouse.mouse-up body

  Will expand to the equivalent Java code:

  object.addMouseListener(
    new MouseListener() {
      void mouseUp(MouseEvent event) {
        body;
      }});"
  (:require (little-gui-helper [properties :as props])
	    (clojure.contrib [string :as string])))

(defn event-spec?
  "Returns true if name of symbol begins with + and contains ."
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

  (let [[listener method] (->> s name rest ;; cut '+' from begining
			       (reduce str)
			       (string/split #"\."))]
    [(-> listener props/CamelCase (str "Listener"))
     (-> method props/camelCase)]))

(defmacro addevent
  "Add event to obj given by the spec and body.

  Specification consists of + sign (interpret this as \"addXxxListener\")
  listener name (without \"Listener\" part), dot and a method name. For example:

  +mouse.mouse-clicked    ===>   MouseListener interface, mouseClicked method
     | 	 |     	     |
     |   '~~~~~~~~~~~'-- method to implement
     |
   interface name without \"Listener\"

  Notes:
   1. Referenced interface must be imported to the current namespace.
   2. Lispy notation (e.g. \"asdf-qwer\") is always translated to the CamelCase
      (\"AsdfQwer\").
   3. Inside body the event is binded to symbol event.

  Example:

  (addevent button [+mouse.mouse-clicked
                     (JOptionPane/showMessageDialog nil (str event))])
 
  Expands to code:

  (.addMouseListener button
    (reify MouseListener
      (mouseClicked [_ event]
        (JOptionPane/showMessageDialog nil (str event)))
      (mouseEntered [_ _] nil)
      (mouseExited [_ _] nil)
      (mousePressed [_ _] nil)
      (mouseReleased [_ _] nil)))

  Note that unreferenced methods are implemented with nil bodies."

  [obj [spec body]]
  (let [[listener method] (listener&method-names spec)
	listener-class (symbol listener)]
	`(~(symbol (str ".add" listener))
	  ~obj
	  (reify ~listener-class
		 ~(list (symbol method) ['_ 'event]
			body)

		 ;; Implement empty methods.
		 ;; Needed because reify would made them abstract.
		 ~@(for [m (->> listener-class resolve .getMethods
				(map #(.getName %))
				(filter #(not= % method))
				(map symbol))]
		     (list m ['_ '_] nil))))))
