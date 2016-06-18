(ns clj-as-art.layers.background-layer
  (:require [quil.core :as q])
  (:use [quil-layer.layer]))

(def oneoff-fn (atom nil))
(def bg-alpha (atom nil))

(defn- setup [] {})
(defn- update-state [state] {})

(defn- draw-state [state]
  ;;(println (.backgroundColor (q/current-graphics)))
  (when-not (nil? @oneoff-fn)
    (do (@oneoff-fn)
        (reset! oneoff-fn nil)))

  (q/no-stroke)
  (q/color-mode :hsb)
  (let [alpha (if (nil? @bg-alpha) 255 @bg-alpha)]
    (q/fill (.backgroundColor (q/current-graphics)) alpha))
  (q/rect 0 0 (q/width) (q/height)))

(defrecord BackgroundLayer [state]
  Layer
  (setup-layer-state [this]
    (setup))
  (update-layer-state [this state]
    (update-state state))
  (draw-layer-state [this state]
    (draw-state state)))
