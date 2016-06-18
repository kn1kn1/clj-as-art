(ns clj-as-art.layers.text
  (:require [quil.core :as q])
  (:use [quil-layer.layer]))

(defn- setup []
  {:angle 0})

(defn- update-state [state]
  {:angle (+ (:angle state) 0.01)})

(defn- draw-state [state]
  (q/color-mode :rgb)
  (q/fill (+ 200 (* 25 (q/sin (:angle state)))))
  (q/text-size 300)
  ;;(q/text "clj as art" 10 320)
  (q/text "clj as art" 10 (- (q/height) 100))
  )

(defrecord TextLayer [state]
  Layer
  (setup-layer-state [this]
                     (setup))
  (update-layer-state [this state]
                      (update-state state))
  (draw-layer-state [this state]
                    (draw-state state)))
