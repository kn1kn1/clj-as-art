(ns clj-as-art.fn.prob-beats
  (:use [overtone.live]))

(def kick-sample (sample "resources/kick.wav"))
(def hat-sample (sample "resources/hat.wav"))
(def openhh-sample (sample "resources/openhh.wav"))
(def sd-sample (sample "resources/sd.wav"))

(defn prob-sample
  ([sound prob] (prob-sample sound prob 1.0))
  ([sound prob amp]
   (if (< (rand-int 10) prob)
     (sound :amp amp))))

(defn prob-kick [prob]
  (prob-sample kick-sample prob))
(defn prob-hat [prob]
  (prob-sample hat-sample prob))
(defn prob-openhh [prob]
  (prob-sample openhh-sample prob))
(defn prob-sd [prob]
  (prob-sample sd-sample prob 0.4))

(comment
  (kick-sample)
  (sd-sample)
  (prob-kick 5)
  (stop)
  )
