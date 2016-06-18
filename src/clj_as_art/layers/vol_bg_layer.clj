(ns clj-as-art.layers.vol-bg-layer
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clj-as-art.voltap :as voltap])
  (:use [overtone.live]
        [quil-layer.layer])
  )

;; (defonce sound-in-synth3000 (voltap/sound-in-vol-tap 3000))
(defonce sound-in-synth3000 (voltap/vol-tap [:after (foundation-monitor-group)] 3000))

(defn- setup []
  {:vol 0
   :bgcolour 0})

(defn- update-state [state]
  ;;(q/exit)
  ;;(q/frame-rate 30)
  (let
      [vol (voltap/monitor-vol)
       vol3000 (voltap/monitor-vol sound-in-synth3000)]
    {:vol vol
     :vol3000 vol3000}))

(defn- draw-state [state]
  (let [vol (:vol state)
        vol3000 (:vol3000 state)
        r (+ (* 20 100 vol) 100)
        g (+ (* 20 100 vol) 100)
        b (+ (* 20 100 vol3000) 100)]
    (q/background r g b)))

(defrecord VolBgLayer [state]
  Layer
  (setup-layer-state [this]
    (setup))
  (update-layer-state [this state]
    (update-state state))
  (draw-layer-state [this state]
    (draw-state state)))
