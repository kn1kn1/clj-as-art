(ns clj-as-art.synth.grumbles
  (:use [overtone.live]))

;; Inspired by an example in an early chapter of the SuperCollider book
(definst grumble [freq 440 freq-mul 1 speed 10 attack 10 release 50 amp 0.7]
  (let [snd (mix (map #(* (lf-cub (* % freq-mul freq))
                          (max 0 (+ (lf-noise1:kr speed)
                                    (env-gen (perc attack release) :action FREE))))
                      [1 (/ 2 3) (/ 3 2) 2]))]
    (pan2 (* amp snd) (sin-osc:kr 16))))

;;(stop)
;; (grumble)
;; (grumble :freq-mul 0.5)
;; (grumble :freq-mul 0.75)
;; (grumble :freq-mul 1)
;; (grumble :freq-mul 1.5)
;; (grumble :freq-mul 2)
;; (ctl grumble :speed 3000)

;; (defn player [metro beat rates]
;;   (let [rates (if (empty? rates)
;;                 [5 4 3 3 2 2 1.5 1.5 0.75 0.75 0.5]
;;                 rates)
;;         freq (midi->hz (+ (note :C3) -1))
;;         dur (/ 60.0 (metro :bpm))
;;         attack (* dur 4) ; 1bar
;;         rel (* dur (* 4 7)) ; 7bars
;;         amp 0.2]
;;     (at (metro beat)
;;         (do
;;           (if (zero? (mod beat 8)) (grumble :freq freq :freq-mul 0.5 :attack attack :release rel :amp amp))
;;           (if (zero? (mod beat 16)) (grumble :freq freq :freq-mul 1 :attack attack :release rel :amp amp))
;;           (if (zero? (mod beat 48)) (grumble :freq freq :freq-mul (choose rates) :attack attack :release rel :amp amp))
;;           ))
;;     (apply-by (metro (inc beat)) #'player [sequencer-metro (inc beat) rates] )
;;     ))
;; ;; (player sequencer-metro (sequencer-metro) nil)
