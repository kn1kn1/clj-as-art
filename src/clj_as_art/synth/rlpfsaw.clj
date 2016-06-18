(ns clj-as-art.synth.rlpfsaw
  (:use [overtone.live]))

(definst rlpfsaw [amp 1 dur 0.5]
  (let [amp-env (env-gen (perc 0.0001 dur 1 -4) :action FREE)]
    (pan2 (* amp (mix (rlpf (* amp-env (saw [99 100 101])) (mouse-x 1000 5000 EXP) (mouse-y 0.001 0.1 LIN)))))))
