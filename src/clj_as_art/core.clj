(ns clj-as-art.core
  (:require [quil.core :as q]
            [quil.middleware :as m])
  (:use [clj-as-art.sketch])
  (:use [quil-layer layer layers]
        [quil-layer.layers fadeout-layer]
        [clj-as-art.layers background-layer lines-layer text equilibrium]
        )
  (:use [overtone.live]
        [clj-as-art.fn sequencer util ambi-rand prob-beats beats markov-logistics]
        [clj-as-art.synth fmchord laserbeam latchbell rlpfsaw])
  )

;; 1
(do
  (do
    (def text-layer (->TextLayer (atom {})))
    (let [layer text-layer]
      (setup-layer layer)
      (add-layer layer)))
  (do
    (def lines-layer (->LinesLayer (atom {})))
    (let [layer lines-layer]
      (setup-layer layer)
      (add-layer layer)))

  ;;(stop)
  ;;(volume 0)
  (fadein-master 0 0.6 (/ 0.6 (/ 30000 200))) ;; rate 0.006 -- takes 100 * 0.2 sec
  (start-live-fn-sequencer (sequencer-metro (next-beat (sequencer-metro) 0 (* 1 beat-per-pattern))) (* 1 beat-per-pattern) *fn-beats "fn-beats")

  (let
      [fnrep 16]
    (reset! num-of-lines (+ fnrep (rand-int fnrep)))
    (swap! *fn-beats assoc ambi-rand (vec (repeat (/ fnrep 1) 1)))
    (swap! *fn-beats assoc prob-kick (vec (repeat fnrep 0.6)))
    (swap! *fn-beats assoc prob-sd (vec (repeat fnrep 0.6)))
    (swap! *fn-beats assoc prob-hat (vec (repeat fnrep 0.4)))
    (swap! *fn-beats assoc prob-openhh (vec (repeat fnrep 0.4))))
  )

;; 2
(let
    [fnrep 64]
  (reset! num-of-lines (+ fnrep (rand-int fnrep)))
  (swap! *fn-beats assoc ambi-rand (vec (repeat (/ fnrep 1) 1)))
  (swap! *fn-beats assoc prob-kick (vec (repeat fnrep 0.6)))
  (swap! *fn-beats assoc prob-sd (vec (repeat fnrep 0.6)))
  (swap! *fn-beats assoc prob-hat (vec (repeat fnrep 0.4)))
  (swap! *fn-beats assoc prob-openhh (vec (repeat fnrep 0.4))))

(do
  (reset! num-of-lines (+ 100 (rand-int 50)))
  ;;(grumble :freq-mul 0.5)
  ;;(ctl grumble :speed 3000)
  (swap! *fn-beats assoc ambi-rand (vec (repeat 16 1)))
  (swap! *fn-beats assoc prob-openhh [7])
  (swap! *fn-beats assoc prob-kick [9])
  (swap! *fn-beats assoc prob-hat (take 16 (cycle [10 2 8 2 1 2])))
  (swap! *fn-beats assoc prob-sd [[[0] [10 _ _ [8]]] [0 10]]))

(do
  ;;(swap! *fn-beats assoc ambi-rand [_])
  (swap! *fn-beats assoc ambi-rand (vec (repeat 16 1)))
  (swap! *fn-beats assoc prob-kick (take 32 (cycle [10 0 2 0 6 0 2 0 3 0 6 0 1.1 0 2 4])))
  (swap! *fn-beats assoc prob-sd (take 32 (cycle [0 0 1.1 0 1.1 0 6 0 6 0 1.1 0 1.1 0 6 0])))
  (swap! *fn-beats assoc prob-hat (take 32 (cycle [10 2 8 2 1 2])))
  (swap! *fn-beats assoc prob-openhh (take 32 (cycle [0 0 0 0 5 0 0 0 0 0 0 0 0 0 5 0]))))

;; 3
(let
    [fnrep 64]
  (reset! num-of-lines (+ fnrep (rand-int fnrep)))
  (swap! *fn-beats assoc ambi-rand (vec (repeat (/ fnrep 1) 1)))
  (swap! *fn-beats assoc prob-kick [_])
  (swap! *fn-beats assoc prob-sd [_])
  (swap! *fn-beats assoc prob-hat [_])
  (swap! *fn-beats assoc prob-openhh [_]))

(let
    [t (next-beat (sequencer-metro) 0 (* 1 beat-per-pattern))
     ch (fn []
          ;;(stop-live-sequencer "fn-beats")
          ;;(stop)
          (markov-loop sequencer-metro (sequencer-metro) chord-chain-dict :I))]
  (apply-at (sequencer-metro t) ch []))

;; 4
(let
    [t (next-beat (sequencer-metro) 0 (* 1 beat-per-pattern))
     t2 (+ t (* 8 beat-per-pattern))
     ch (fn []
          ;;(stop-live-sequencer "fn-beats")
          (reset! *fn-beats fn-beats)
          (start-eq-layer)
          (remove-layer lines-layer)
          (reset! oneoff-fn #(do (q/color-mode :hsb) (q/background 255 155 155)))
          (logistics-loop sequencer-metro (sequencer-metro) 0.1)
          )]
  (start-live-sequencer (sequencer-metro t) (* 1 beat-per-pattern) *beats "beats")
  (swap! *beats assoc latchbell [a _ a _ a _ a _ a _ a _ a _ a _])
  (start-live-sequencer (sequencer-metro t2) (* 16 beat-per-pattern) *beats16 "beats16")
  (apply-at (sequencer-metro t2) ch []))

;; 5
(swap! *beats16 assoc laserbeam [_])

(do
  (swap! *beats16 assoc laserbeam [_])
  (swap! *beats assoc latchbell [_])
  (swap! *beats assoc kick [_])
  (swap! *beats assoc laserbeam [_]))

(do
  (reset! *beats16 beats16)
  (swap! *beats assoc latchbell [a _ a _ a _ a _ a _ a _ a _ a _])
  ;;(swap! *beats assoc rlpfsaw [[1 _ _ _] [_ _ _ _] [_ 1 1 _] [_ _ _ _]])
  (swap! *beats assoc kick [[1 _ _ _] [_ _ _ _] [_ 1 1 _] [_ _ _ _]])
  (swap! *beats assoc laserbeam [d c c c b c c d c c d c b c c d]))

(do
  (reset! *beats16 beats16)
  (swap! *beats assoc latchbell [_])
  (swap! *beats assoc rlpfsaw [_])
  (swap! *beats assoc kick [_])
  (swap! *beats assoc laserbeam (take 16 (cycle [d c c]))))

(swap! *beats assoc laserbeam (take 16 (cycle [d c])))
(swap! *beats assoc laserbeam (take 16 (cycle [d])))

(do
  ;;(reset! *beats16 beats16)
  (swap! *beats16 assoc laserbeam [_])
  ;;(swap! *beats assoc latchbell [_])
  (swap! *beats assoc rlpfsaw [_])
  (swap! *beats assoc kick [[1 _ _ [1 1]] [_ _ _ 1] [_ _ 1 _] [_ _ _ [1 1 1]]])
  (swap! *beats assoc laserbeam [d _ _ [d d] (vec (repeat 64 0.8)) _ _ d _ _ d _ b _ _ [d d d]]))


;; 6
(do
  (remove-layer eq-layer)
  (do
    (def fadeoutlayer (->FadeoutLayer 60000 (atom {})))
    (let [layer fadeoutlayer]
      (setup-layer layer)
      (add-layer layer)))
  (swap! *beats16 assoc laserbeam [_])
  (swap! *beats assoc latchbell [_])
  (swap! *beats assoc rlpfsaw [_])
  (swap! *beats assoc kick [_])
  (swap! *beats assoc laserbeam [_]))

;; (markov-loop sequencer-metro (sequencer-metro) chord-chain-dict :I)
;; (logistics-loop sequencer-metro (sequencer-metro) 0.1)

(do
  (fadeout-master 0.6 0 (/ 0.6 (/ 20000 200)))
  )
