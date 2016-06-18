(ns clj-as-art.layers.vol-circles-layer
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clj-as-art.voltap :as voltap])
  (:use [overtone.live]
        [overtone.synth.stringed]
        [quil-layer.layer])
  )

;; (defonce sound-in-synth3000 (voltap/sound-in-vol-tap 3000))
(defonce sound-in-synth3000 (voltap/vol-tap [:after (foundation-monitor-group)] 3000))
(def circle-num 10)

(defn- setup []
  {:vol 0
   :bgcolour 0
   :radius 10})

(defn- update-state [state]
  ;;(q/exit)
  ;;(q/frame-rate 30)
  (let
      [vol (voltap/monitor-vol)
       vol3000 (voltap/monitor-vol sound-in-synth3000)]
    {:vol vol
     :bgcolour (* 10 (* 10 vol))
     :radius (+ 10 (* 10000 vol3000))}))

(defn- draw-state [state]
  (let [bgcolour (:bgcolour state)
        radius (:radius state)
        circle-num (* 5 (:vol state))
        r (+ (* 20 bgcolour) 120)
        g (+ (* 20 bgcolour) 30)
        b (+ (* 20 bgcolour) 60)
        w (q/width)
        h (q/height)
        fw (/ w 2)
        fh (/ h 2)]
    (q/background r g b)
    (dorun
     (for [i (range 0 circle-num)]
       (let [x (- (/ fw 2) (rand fw))
             y (- (/ fh 2) (rand fh))
             r (* (rand 2) radius)]
         (q/stroke-int (- 255 bgcolour))
         (q/no-fill)
         (q/with-translation [(/ w 2)
                              (/ h 2)]
           (q/ellipse x y r r)
           ))))))

(defrecord VolCirclesLayer [state]
  Layer
  (setup-layer-state [this]
    (setup))
  (update-layer-state [this state]
    (update-state state))
  (draw-layer-state [this state]
    (draw-state state)))

;; (q/defsketch voltap
;;   :title "You spin my circle right round"
;;   :size :fullscreen
;;  ; :size [500 500]
;;   :setup setup
;;   :update update-state
;;   :draw draw-state
;;   ; :features [:keep-on-top]
;;   ; Check quil wiki for more info about middlewares and particularly
;;   ; fun-mode.
;;   :middleware [m/fun-mode])
