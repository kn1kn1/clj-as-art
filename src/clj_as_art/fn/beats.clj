(ns clj-as-art.fn.beats
  (:use [overtone.live]
        [clj-as-art.fn sequencer util ambi-rand prob-beats]
        [clj-as-art.synth fmchord grumbles laserbeam latchbell]))


(def kick (sample "resources/kick.wav"))
;; (kick)

;; metro
(sequencer-metro :bpm)
(def beat-per-pattern 4)
(defn dur-per-pattern [] (* (dur-per-beat) beat-per-pattern))

;; fn-beats
(def _ 0)
(defn rand-laser []
  (let [dur (/ 60.0 (sequencer-metro :bpm))]
    (laserbeam :pan (- (rand 2.0) 1.0) :freq (+ (rand-int 1000) 100) :dur 0.25)))

(def latchbell-arg (atom {:rate 100 :amp 0.4 :max 5}))
(init-latchbell-mod (:rate @latchbell-arg))
(defn rand-latchbell []
  (let [dur (/ 60.0 (sequencer-metro :bpm))]
    (latchbell :rate (:rate @latchbell-arg) :amp (:amp @latchbell-arg) :time-scale-max (:max @latchbell-arg))))

(def fn-beats {ambi-rand [_]
               ;;ambi-rand (vec (repeat 16 1))
               rand-laser [_]
               ;;rand-laser (vec (repeat 16 1))
               ;;rand-latchbell (vec (repeat 16 1))
               })
(def *fn-beats (atom fn-beats))

;; beats
(def beats {;;laserbeam [1 _ _ _ _ _ _ 1 _ _ 1 _ _ _ _ 1]
            ;;latchbell [1 _ 1 _ 1 _ 1 _ 1 _ 1 _ 1 _ _ 1]
            })
;; (def beats {laserbeam [1 _ _ _ _ _ _ 1 _ _ 1 _ _ _ _ 1]})
(def *beats (atom beats))

(def a {:rate 2 :amp 0.1 :time-scale-max 5})
(def a {:rate 10 :amp 0.1 :time-scale-max 10})
(def a {:rate 100 :amp 0.4 :time-scale-max 5})
(def b {:pan -1 :freq 1000})
(def c {:freq 25000 :amp 0.2})
(def d {:amp 5.0 :dur 0.25})
(def g {:freq-mul 1})

;; beats16
(def beats16 {laserbeam [
                         [[d _ d _] [b _ _ _] [_ d 1 _] [[b _] [1 1 1]]]
                         [[d 1 _ d] [b 1 _ d] [d _ d _] [[b 0.5 0.5] (vec (repeat 3 0.5))]]
                         [[d 1 _ d] [b 1 _ _] [d 1 _ d] [b 1 _ _]]
                         [[d _ d _] [b _ _ _] [_ _ d _] [[b _] [1 1 1]]]
                         [[d _ d _] [b _ _ _] [_ d 1 _] [[b _] [1 1 1]]]
                         [[d 1 _ d] [b 1 _ [1 1 1]] [d _ d _] [[b 0.5 0.5] (vec (repeat 3 0.5))]]
                         [[d 1 _ d] [b 1 _ _] [d 1 _ d] [b 1 _ _]]
                         [[d _ d _] [b _ _ _] [_ _ d _] [[b _] [1 1 1]]]
                         [[d _ d _] [b _ _ _] [_ d 1 _] [[b _] [1 1 1]]]
                         [[d 1 _ d] [b 1 _ [1 1 1]] [d _ d _] [[b 0.5 0.5] (vec (repeat 3 0.5))]]
                         [[d 1 _ d] [b 1 _ _] [d 1 _ d] [b 1 _ _]]
                         [[d _ d _] [b _ _ _] [_ _ d _] [[b _] [1 1 1]]]
                         [[d _ d _] [b _ _ _] [_ d 1 _] [[b _] [1 1 1]]]
                         [[d 1 _ d] [b 1 _ [1 1 1]] [d _ d _] [[b 0.5 0.5] (vec (repeat 3 0.5))]]
                         [[d 1 _ d] [b 1 _ _] [d 1 _ d] [b 1 _ _]]
                         [[d _ d _] [b _ _ _] [_ _ d _] [[b _] [1 1 1]]]
                         ]})
(def *beats16 (atom beats16))
(reset! *beats16 beats16)
