(ns clj-as-art.layers.lines-layer
  (:require [quil.core :as q])
  (:use [quil-layer.layer]))

(def num-of-lines (atom 100))

(defn- setup [] {})
(defn- update-state [state] {})

(defn- draw-state [state]
  (let [w (/ (q/height) @num-of-lines)
        r (/ w 3)]
    ;;(q/smooth)
    (q/color-mode :rgb)
    ;;    (q/background 200)
    ;;(q/fill 220)
    (q/stroke 220)
    (q/stroke-weight w)

    (if (= (mod (q/frame-count) 1) 0)
      (dorun
       (for [i (range 0 @num-of-lines)]
         (let [top 2
               left 2
               x left
               y (+ (* i (/ (q/height) @num-of-lines)) (q/random (- r) r))
               x2 (- (q/width) left)
               y2 (+ (* i (/ (q/height) @num-of-lines)) (q/random (- r) r))]
           (q/line x y x2 y2))
         ))))
  )

(defrecord LinesLayer [state]
  Layer
  (setup-layer-state [this]
    (setup))
  (update-layer-state [this state]
    (update-state state))
  (draw-layer-state [this state]
    (draw-state state)))
