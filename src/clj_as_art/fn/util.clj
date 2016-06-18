(ns clj-as-art.fn.util
  (:use [overtone.live]
        [mud.core :only (n-overtime!)]))

;; taken from mud.core.fadeout-master
(defn fadeout-master
  ([] (fadeout-master 1.0 0.0 1/64))
  ([current] (fadeout-master current 0.0 1/64))
  ([start end rate] (n-overtime! (foundation-output-group) :master-volume start end rate)))
(comment
  (fadeout-master (volume))
  (fadeout-master (volume) 0.0  1/64)
  (fadeout-master)
  )

;; taken from mud.core.fadeout-master
(defn fadein-master
  ([] (fadein-master 0.0 1.0 1/64))
  ([target] (fadein-master 0.0 target 1/64))
  ([start end rate] (n-overtime! (foundation-output-group) :master-volume start end rate)))
(comment
  (fadein-master 0.0 (volume) 1/64)
  (fadein-master)
  )
