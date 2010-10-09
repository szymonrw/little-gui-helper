(ns little-gui-helper.styles
  "Operations on styles.

  CSS here has different syntax that anywhere else. It's because we want
  to write everything in Clojure.

  Syntax:
  ((list of things representing group or object ids)
     {prop1 val1 prop2 val2 ...}
   one-element
     {another map}
   ...)

  \"Things\" can be anything that can be a key in map.
  Instead of {} you can use () if you prefer.")

(defn- pair&correct-css
  "Pairs and corrects css."
  [css]
  {:pre [;; check if css contains pairs
	 (-> css count even?)
	 ;; check if style is a map or list with even elements:
	 (->> css rest (take-nth 2) 
	      (every? #(or (map? %)
			   (and (sequential? %) (-> % count even?)))))]}

  ;; list of [(id1 id2 id3) style]
  (->> (partition 2 css)
       (map (fn [[ids style]]
	      [(if (sequential? ids)
		 ids ;; ids is a proper list
		 (list ids)) ;; ids is not a list, wrap it in a list
	       (if (map? style)
		 style ;; style is a proper style
		 (apply array-map style))])))) ;; style is a list, make it a map

(defn- flat-styles
  "Flats styles"
  [css]
  (->> (for [[ids style] (pair&correct-css css)
	     i ids]
	 [i style]) ;; list of [id style]
       
       (reduce (fn [style-map [id style]]
		 (assoc style-map id (merge (style-map id) style)))
	       {})))
