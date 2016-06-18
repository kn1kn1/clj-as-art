(ns clj-as-art.fn.markov-logistics
  (:use [overtone.live]
        [clj-as-art.synth fmchord grumbles laserbeam latchbell]))

(def logistics-scale (scale :C2 :minor-pentatonic (range 1 15)))
(def logistics-r 3.8)
(def chord-chain-dict {:I   [:ii :iii :IV :V :vi :vii]
                       :ii  [:V :vii]
                       :iii [:IV :vi]
                       :IV  [:ii :V :vii]
                       :V   [:vi :I]
                       :vi  [:ii :IV :V]
                       :vii [:I]
                       })
(def MAJORDEGREE {:I     :i
                  :II    :ii
                  :III   :iii
                  :IV    :iv
                  :V     :v
                  :VI    :vi
                  :VII   :vii
                  :_     nil})

(defn logistics-loop [metro beat x]
  (let [idx (Math/floor (* x (count logistics-scale)))
        note (nth logistics-scale idx)
        freq (midi->hz note)
        ;;dur (* 60 4/8 (/ 60.0 (metro :bpm)))
        dur (* 1.2 4/8 (/ 60.0 (metro :bpm)))
        amp (+ 3 (rand 1))
        next-beat (+ 2/8 beat)
        next-x (* logistics-r x (- 1 x))] ;; x = r * x * (1 - x)
    (if (< 0 (rand-int 3))
      (at (metro beat)
          (fmchord01 :freq freq :dur dur :amp amp)))
    (apply-by (metro next-beat) #'logistics-loop [metro next-beat next-x])
    ))
;;(inst-fx! fmchord01 fx-echo)

(defn markov-loop [metro beat dict deg]
  (let [mi (deg DEGREE)
        sc (if mi :minor :major)
        d (if mi deg (deg MAJORDEGREE))
        notes (chord-degree d :C3 sc)
        dur (+ 12/8 (* (rand) 12/8 (/ 60.0 (metro :bpm))))
        next-beat (+ 12/8 beat)
        next-deg (choose (deg dict))]
    (at (metro beat)
        (doseq [n notes]
          (let [freq (midi->hz n)]
            (fmchord01 :freq freq :dur dur :amp 1.0))))
    (apply-by (metro next-beat) #'markov-loop [metro next-beat dict next-deg])
    ))

(defn markov-pat-loop [metro beat dict deg]
  (let [mi (deg DEGREE)
        sc (if mi :minor :major)
        d (if mi deg (deg MAJORDEGREE))
        notes (chord-degree d :C3 sc)
        dur (* 6 (/ 60.0 (metro :bpm)))
        next-beat (+ 4 beat)
        next-deg (choose (deg dict))]
    (doseq [[idx n] (map-indexed vector (take 3 notes))]
      (at (metro (+ (* idx 1/2) beat))
          (let [freq (midi->hz n)]
            (fmchord01 :freq freq :dur dur :amp 1.0))
          ))
    (apply-by (metro next-beat) #'markov-pat-loop [metro next-beat dict next-deg])
    ))

;; (markov-pat-loop sequencer-metro (sequencer-metro) chord-chain-dict :I)
;; (chord-degree :i :c4 :major)
;; (stop)
