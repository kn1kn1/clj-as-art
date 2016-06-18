(ns clj-as-art.fn.sequencer
  (:use [overtone.live]))

(def *sequencer-bpm (atom 128))
(def sequencer-metro (metronome @*sequencer-bpm))

(defn update-bpm
  [bpm]
  (reset! *sequencer-bpm bpm)
  (sequencer-metro :bpm bpm))

(defn dur-per-beat [] (/ 60.0 @*sequencer-bpm))
(defn dur-per-bar [] (* (dur-per-beat) 4))

(defn- flatten1
  "Takes a map and returns a seq of all the key val pairs:
        (flatten1 {:a 1 :b 2 :c 3}) ;=> (:b 2 :c 3 :a 1)"
  [m]
  (reduce (fn [r [arg val]] (cons arg (cons val r))) [] m))

(defn- flatten2
  "Takes a map and returns a seq of all the values:
        (flatten1 {:a 1 :b 2 :c 3}) ;=> (2 3 1)"
  [m]
  (reduce (fn [r [arg val]] (cons val r)) [] m))

(defn- normalise-beat-info
  [beat]
  (cond
    (= 0 beat)         nil
    (= 1 beat)         {}
    (map? beat)        beat
    (sequential? beat) beat
    :else              {:else beat}))

(defn- schedule-pattern
  [curr-t pat-dur sound pattern]
  {:pre [(sequential? pattern)]}
  (let [beat-sep-t (/ pat-dur (count pattern))]
    (doseq [[beat-info idx] (partition 2 (interleave pattern (range)))]
      (let [beat-t    (+ curr-t (* idx beat-sep-t))
            beat-info (normalise-beat-info beat-info)]
        (if (sequential? beat-info)
          (schedule-pattern beat-t beat-sep-t sound beat-info)
          (at beat-t (when beat-info (apply sound (flatten1 beat-info)))))))))

(defn- schedule-fn-pattern
  [curr-t pat-dur sound pattern]
  {:pre [(sequential? pattern)]}
  (let [beat-sep-t (/ pat-dur (count pattern))]
    (doseq [[beat-info idx] (partition 2 (interleave pattern (range)))]
      (let [beat-t    (+ curr-t (* idx beat-sep-t))
            beat-info (normalise-beat-info beat-info)]
        (if (sequential? beat-info)
          (schedule-fn-pattern beat-t beat-sep-t sound beat-info)
          (when beat-info (apply-at beat-t sound (vec (flatten2 beat-info)))))))))

(def live-sequencer-states {})
(def *live-sequencer-states (atom live-sequencer-states))

(defn- live-sequencer
  [curr-t beats-per-pat live-patterns uid]
  (let [running (@*live-sequencer-states uid)
        pat-dur (/ (* 1000 beats-per-pat 60.0) @*sequencer-bpm)]
    (when running
      (doseq [[sound pattern] @live-patterns]
        (schedule-pattern curr-t pat-dur sound pattern))
      (let [next-t (+ curr-t pat-dur)]
        (apply-by next-t #'live-sequencer [next-t beats-per-pat live-patterns uid]))
      )))

(defn- live-fn-sequencer
  [curr-t beats-per-pat live-patterns uid]
  (let [running (@*live-sequencer-states uid)
        pat-dur (/ (* 1000 beats-per-pat 60.0) @*sequencer-bpm)]
    (when running
      (doseq [[sound pattern] @live-patterns]
        (schedule-fn-pattern curr-t pat-dur sound pattern))
      (let [next-t (+ curr-t pat-dur)]
        (apply-by next-t #'live-fn-sequencer [next-t beats-per-pat live-patterns uid]))
      )))

(defn start-live-sequencer
  ([curr-t beats-per-pat live-patterns] (let [uid (trig-id)] (start-live-sequencer curr-t beats-per-pat live-patterns uid)))
  ([curr-t beats-per-pat live-patterns uid]
   (do
     (swap! *live-sequencer-states assoc uid true)
     (live-sequencer curr-t beats-per-pat live-patterns uid)
     uid
     )))

(defn start-live-fn-sequencer
  ([curr-t beats-per-pat live-patterns] (let [uid (trig-id)] (start-live-fn-sequencer curr-t beats-per-pat live-patterns uid)))
  ([curr-t beats-per-pat live-patterns uid]
   (do
     (swap! *live-sequencer-states assoc uid true)
     (live-fn-sequencer curr-t beats-per-pat live-patterns uid)
     uid
     )))

(defn stop-live-sequencer
  [uid]
  (do
    (swap! *live-sequencer-states dissoc uid)))

(defn next-beat [cur-beat beat beats]
  (let [mod-beat (mod cur-beat beats)
        d-beat   (- beat mod-beat)]
    (cond
      (<= d-beat 0) (+ cur-beat beats d-beat)
      (> d-beat 0) (+ cur-beat d-beat)
      )))

(comment
  (mod 1 4)
  (- 0 1)
  (+ 1 )
  (next-beat 0 0 4) ;; => 4
  (next-beat 1 0 4) ;; => 4
  (next-beat 2 0 4) ;; => 4
  (next-beat 3 0 4) ;; => 4
  (next-beat 4 0 4) ;; => 8

  (next-beat 0 2 4) ;; => 2
  (next-beat 1 2 4) ;; => 2
  (next-beat 2 2 4) ;; => 6
  (next-beat 3 2 4) ;; => 6
  (next-beat 4 2 4) ;; => 6
  (next-beat 5 2 4) ;; => 6
  (next-beat 6 2 4) ;; => 10
  )
