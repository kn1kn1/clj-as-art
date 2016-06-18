(ns clj-as-art.ambi-piano
  (:use [overtone.live]
        [clj-as-art.fn util]))

;; https://twitter.com/kn1kn1/status/624227547660726272
;; loop{use_bpm 70;sample:ambi_piano,sustain:0.25,rate:pitch_to_ratio([-5,-2,0,3].choose)*[1,2,4].choose*[1,-1].choose,start:rand;sleep 0.25}

(def ambpf-b (load-sample "resources/ambi_piano.wav"))
(definst ambpf [start 0 rate 1.0 dur 0.25 amp 1]
  (let [frames (num-frames ambpf-b)
        trig (env-gen:kr (lin 0 dur 0))
        src (play-buf 1 ambpf-b rate trig (* frames start) 0 FREE)
        env (env-gen:kr (lin 0 dur 0))]
    (* src env amp)))
;;(ambpf (rand) (rand) 0.25)

(defn- exponential [x n]
  (reduce * (repeat n x)))
(defn- pitch-to-ratio [m]
  (exponential 2.0 (/ m 12.0)))
(defn ambpf-player [metro beat]
  (let [start (rand)
        rate (* (pitch-to-ratio (choose [-5 -3 0 2])) (choose [1 2 4]))
        dur (/ 60 (metro :bpm))
        next-beat (+ beat 1/2)]
    (at (metro beat) (ambpf start rate (/ dur 2)))
    (apply-by (metro next-beat) ambpf-player metro next-beat [])))

(do
  (volume 0)
  (fadein-master 0.6)
  ;;(fadein-master 0.0 0.6 1/64)
  (let [metro (metronome 140)]
    (ambpf-player metro (metro))))

(comment
  (volume)
  (fadeout-master 0.6)
  (stop)
  )
