(ns clj-as-art.voltap
  (:use [overtone.live]))

(defsynth vol-tap [freq 440.0 rq 1.0]
  (tap "system-vol" 60 (lag (abs (bpf (in:ar 0) freq rq)) 0.1)))
(defonce monitor-vol-synth440 (vol-tap [:after (foundation-monitor-group)] 440))
(defn monitor-vol
  ([] @(get-in monitor-vol-synth440 [:taps "system-vol"]))
  ([in-synth] @(get-in in-synth [:taps "system-vol"])))


(defsynth sound-in-vol-tap [freq 440.0 rq 1.0]
  (tap "sound-in-vol" 60 (lag (abs (bpf (sound-in:ar 0) freq rq)) 0.1)))
(defonce sound-in-synth440 (sound-in-vol-tap 440))
(defn sound-in-vol
  ([] @(get-in sound-in-synth440 [:taps "sound-in-vol"]))
  ([in-synth] @(get-in in-synth [:taps "sound-in-vol"])))

;;(let [vol (sound-in-vol)]
;;  (println vol))

;;(odoc bpf)
