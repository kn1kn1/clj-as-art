(ns clj-as-art.synth.latchbell
  (:use [overtone.live]))

(def kbus-latchbell-rate (control-bus))
(defsynth mod-latchbell-rate []
  (out:kr kbus-latchbell-rate (+ 1.6 (* 0.03 (lf-noise0:kr (/ 1.0 10)))))
  )

(def kbus-latchbell-midi (control-bus))
(defsynth mod-latchbell-midi [rate 9]
  (let [latchrate (* rate (in:kr kbus-latchbell-rate))
        trigger (impulse rate)
        mul (max 0 (+ 10 (* 24 (lf-noise1:kr (/ 1.0 5)))))
        add (+ 60 (* 12 (lf-noise0:kr (/ 1.0 7))))
        ]
    (out:kr kbus-latchbell-midi (latch (+ add (* mul (lf-saw:kr latchrate))) trigger))))

(def kbus-latchbell-idx (control-bus))
(defsynth mod-latchbell-idx [rate 9]
  (let [latchrate (* rate (in:kr kbus-latchbell-rate))
        trigger (impulse rate)
    ]
    (out:kr kbus-latchbell-idx (latch (+ 6 (* 5 (lf-saw:kr latchrate))) trigger))))

(def kbus-latchbell-ratio (control-bus))
(defsynth mod-latchbell-ratio []
  (out:kr kbus-latchbell-ratio (+ 5.0 (* 2.0 (lf-noise1:kr (/ 1.0 10)))))
  )

(definst latchbell [rate 9 amp 0.1 time-scale-max 4.0 time-scale-min 0.5]
  (let [index (in:kr kbus-latchbell-idx)
        midinote (in:kr kbus-latchbell-midi)
        freq (midicps midinote)
        ratio (in:kr kbus-latchbell-ratio)
        add (/ (+ time-scale-max time-scale-min) 2)
        mul (/ (- time-scale-max time-scale-min) 2)
        dur (+ add (* mul (lf-noise1:kr rate)))
        env (env-gen (perc 0.0 (/ dur rate)) :action FREE)
        ]
    (* amp (* env (pm-osc [freq, (* 1.5 freq)] (* freq ratio) index)))
  ))

(defn init-latchbell-mod [rate]
  (do
    (mod-latchbell-rate)
    (mod-latchbell-midi :rate rate)
    (mod-latchbell-idx :rate rate)
    (mod-latchbell-ratio)
    ))

;;(volume (/ 40 127))

;;(init-latchbell-mod)
;;(latchbell :rate 10 :amp 0.2)
;;(stop)
;; (latchbell)

;; (inst-fx! latchbell fx-chorus)
;; (inst-fx! latchbell fx-distortion2)
;; (clear-fx latchbell

;;(mod-latchbell-rate)
;;(mod-latchbell-midi)
;;(mod-latchbell-idx)
;;(mod-latchbell-ratio)
