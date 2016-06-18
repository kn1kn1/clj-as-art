(ns clj-as-art.console
  (:use [overtone.live]
        [clj-as-art.fn sequencer util ambi-rand prob-beats beats markov-logistics]
        [clj-as-art.synth fmchord grumbles laserbeam latchbell]))

;; console
(comment
  (start-live-fn-sequencer (sequencer-metro (next-beat (sequencer-metro) 0 (* 8 beat-per-pattern))) (* 1 beat-per-pattern) *fn-beats "fn-beats")
  (stop-live-sequencer "fn-beats")
  (reset! *fn-beats fn-beats)

  (let
      [fnrep 32]
    ;;(volume 0)
    ;;(fadein-master 0.6)
    ;;(swap! *fn-beats assoc rand-latchbell (vec (repeat (/ fnrep 1) 1)))
    (swap! *fn-beats assoc ambi-rand (vec (repeat (/ fnrep 1) 1)))
    (swap! *fn-beats assoc prob-kick (vec (repeat fnrep 0.6)))
    (swap! *fn-beats assoc prob-sd (vec (repeat fnrep 0.6)))
    (swap! *fn-beats assoc prob-hat (vec (repeat fnrep 0.4)))
    (swap! *fn-beats assoc prob-openhh (vec (repeat fnrep 0.4))))

  (do
    ;;(swap! *fn-beats assoc rand-latchbell [_])
    ;;(swap! *fn-beats assoc rand-laser [_])
    (swap! *fn-beats assoc ambi-rand (vec (repeat 16 1)))
    ;;(swap! *fn-beats assoc ambi-rand (take 64 (cycle [1 0 1 0 1 1])))
    ;;(swap! *fn-beats assoc ambi-rand (vec (repeat 1 _)))
    (swap! *fn-beats assoc prob-kick (take 32 (cycle [10 0 2 0 6 0 2 0 3 0 6 0 1.1 0 2 4])))
    (swap! *fn-beats assoc prob-sd (take 32 (cycle [0 0 1.1 0 1.1 0 6 0 6 0 1.1 0 1.1 0 6 0])))
    (swap! *fn-beats assoc prob-hat (take 32 (cycle [10 2 8 2 1 2])))
    (swap! *fn-beats assoc prob-openhh (take 32 (cycle [0 0 0 0 5 0 0 0 0 0 0 0 0 0 5 0]))))

  (do
    (swap! *fn-beats assoc rand-latchbell [_])
    (swap! *fn-beats assoc rand-laser [_])
    (swap! *fn-beats assoc ambi-rand (vec (repeat 16 1)))
    (swap! *fn-beats assoc prob-openhh [7])
    (swap! *fn-beats assoc prob-kick [9])
    (swap! *fn-beats assoc prob-hat (take 16 (cycle [10 2 8 2 1 2])))
    (swap! *fn-beats assoc prob-sd [[[0] [10 _ _ [8]] ] [0 10]])
    ;;(volume 0)
    ;;(fadein-master 0.6)
    )

  (let
      [fnrep 32]
    ;;(swap! *fn-beats assoc rand-latchbell (vec (repeat (/ fnrep 1) 1)))
    ;;(swap! *fn-beats assoc rand-laser (vec (repeat (/ fnrep 2) 1)))
    (swap! *fn-beats assoc ambi-rand (vec (repeat (/ fnrep 1) 1)))
    (swap! *fn-beats assoc prob-kick [_])
    (swap! *fn-beats assoc prob-sd [_])
    (swap! *fn-beats assoc prob-hat [_])
    (swap! *fn-beats assoc prob-openhh [_]))
)

(comment
  ;;(start-live-fn-sequencer (sequencer-metro (next-beat (sequencer-metro) 0 (* 8 beat-per-pattern))) (* 1 beat-per-pattern) *fn-beats "fn-beats")
  (fadeout-master 0.6)
  (do
    ;;(stop-live-sequencer "fn-beats")
    ;;(reset! *fn-beats fn-beats)
    (volume 0)
    (stop)

    (fadein-master 0.6)
    ;;(clear-fx fmchord01)
    ;;(logistics-loop sequencer-metro (sequencer-metro) 0.1)
    (markov-loop sequencer-metro (sequencer-metro) chord-chain-dict :I)
    ;;(markov-pat-loop sequencer-metro (sequencer-metro) chord-chain-dict :I)
    )
  (stop))


(comment
  (start-live-sequencer (sequencer-metro (next-beat (sequencer-metro) 0 (* 8 beat-per-pattern))) (* 1 beat-per-pattern) *beats "beats")
  (stop-live-sequencer "beats")

  (start-live-sequencer (sequencer-metro (next-beat (sequencer-metro) 0 (* 8 beat-per-pattern))) (* 16 beat-per-pattern) *beats16 "beats16")
  (stop-live-sequencer "beats16")

  (do
    (reset! *beats16 beats16)
    (swap! *beats assoc latchbell [a _ a _ a _ a _ a _ a _ a _ a _])
    (swap! *beats assoc kick [[1 _ _ _] [_ _ _ _] [_ 1 1 _] [_ _ _ _]])
    (swap! *beats assoc laserbeam [d c c c b c c d c c d c b c c d]))

  ()* (*)  (* 2 2)

  (do
    (reset! *beats16 beats16)
    (swap! *beats assoc latchbell [_])
    (swap! *beats assoc kick [_])
    (swap! *beats assoc laserbeam (take 16 (cycle [d c c]))))

  (do
    (reset! *beats16 beats16)
    ;;(swap! *beats assoc latchbell [_])
    (swap! *beats assoc kick [[1 _ _ [1 1]] [_ _ _ 1] [_ _ 1 _] [_ _ _ [1 1 1]]])
    (swap! *beats assoc laserbeam [d _ _ [d d] (vec (repeat 64 0.8)) _ _ d _ _ d _ b _ _ [d d d]]))

  (do
    (swap! *beats16 assoc laserbeam [_])
    (swap! *beats assoc latchbell [_])
    (swap! *beats assoc kick [_])
    (swap! *beats assoc laserbeam [_]))

  (swap! *beats assoc laserbeam (take 16 (cycle [d c c])))
  ;;(swap! *beats assoc laserbeam (vec (repeat 16 c)))
  (swap! *beats assoc kick [(vec (repeat 4 1))])

  (swap! *beats assoc laserbeam (take 16 (cycle [d b c [d d] c b [d d] c [c c c]])))
  (swap! *beats assoc laserbeam (take 16 (cycle [b [d d] [c c c c c] d [d d]])))
  (grumble )

  (swap! *beats assoc kick [1])
  (swap! *beats dissoc kick))
(print *beats)

(reset! *beats beats)
(stop)
