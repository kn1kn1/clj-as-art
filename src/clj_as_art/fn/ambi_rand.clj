(ns clj-as-art.fn.ambi-rand
  (:use [overtone.live]))

(def ambi-piano-b (load-sample "resources/ambi_piano.wav"))
(def ambi-dark-woosh-b (load-sample "resources/ambi_dark_woosh.wav"))
(def ambi-glass-hum-b (load-sample "resources/ambi_glass_hum.wav"))
(def ambi-soft-buzz-b (load-sample "resources/ambi_soft_buzz.wav"))
(def ambi-swoosh-b (load-sample "resources/ambi_swoosh.wav"))
(definst play-buf-lin [buff ambi-piano-b frames 0 start 0 rate 1.0 attack 0 sustain 0.25 release 0 amp 3]
  (let [
        trig (env-gen:kr (lin attack sustain release))
        src (play-buf 1 buff rate trig (* frames start) 0 FREE)
        env (env-gen:kr (lin attack sustain release))]
    (* src env amp)))

(defn- exponential [x n]
  (reduce * (repeat n x)))
(defn- pitch-to-ratio [m]
  (exponential 2.0 (/ m 12.0)))
(defn ambi-rand []
  (let [dur 0.5
        ;;dur (/ 60 (metro :bpm))
        start (rand)
        rate (* (pitch-to-ratio (choose [-5 -3 0 2])) (choose [1/8 1/4 1/2 1]))
        buf (choose [ambi-piano-b
                     ambi-dark-woosh-b
                     ambi-glass-hum-b
                     ambi-soft-buzz-b
                     ambi-swoosh-b
                     ])]
    (play-buf-lin buf (num-frames buf) start rate 0 (/ dur (choose [1 2 3 4])) 0)))

(defn ambi-rand-loop [metro beat]
  (apply-at (metro beat) #'ambi-rand [])
  (apply-by (metro (+ beat 1/4)) #'ambi-rand-loop [metro (+ beat 1/4)]))

(comment
  (do
    (def metro (metronome 128))
    (ambi-rand-loop metro (metro)))
  (stop)
  )
