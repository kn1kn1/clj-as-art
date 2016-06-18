(ns clj-as-art.sketch
  (:require [quil.core :as q]
            [quil.middleware :as m])
  (:use [quil-layer layer layers]
        [quil-layer.layers fadeout-layer]
        ;;[clj-as-art.layers vol-circles-layer]
        [clj-as-art.layers background-layer lines-layer text equilibrium]
        )
  )

(defn- setup []
  ;; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ;; Set color mode to HSB (HSV) instead of default RGB.
  ;; (q/color-mode :rgb)
  (q/color-mode :hsb)
  ;; (q/background 128 255 255)
  (q/background 128)
  )

(defn- update-state [state]
  (update-layers)
  state)

(defn- draw-state [state]
  (draw-layers))

(comment
  (do
    (def bglayer (->BackgroundLayer (atom {})))
    (let [layer bglayer]
      (setup-layer layer)
      ;; (reset! oneoff-fn #(do (q/color-mode :hsb) (q/background 255 155 155)))
      ;; (reset! oneoff-fn #(do (q/color-mode :hsb) (q/background 100 100 100)))
      ;; (reset! bg-alpha 10)
      (add-layer layer)))
  (remove-layer bglayer)
  )

(defn add-layer-on-bglayer [layer]
  (let [
    bg-layer (first @layers)
    rest-layers (rest @layers)
    new-layers (into [] (concat [bg-layer] (concat [layer] rest-layers)))]
    (reset-layers new-layers)))

(defn start-eq-layer []
  (do
    (def eq-layer (->EquilibriumLayer (atom {})))
    (let [layer eq-layer]
      (setup-layer layer)
      (add-layer layer))))

(comment
  (do
    (def lines-layer (->LinesLayer (atom {})))
    (let [layer lines-layer]
      (setup-layer layer)
      (add-layer layer)))
   (reset! num-of-lines 5)
  (remove-layer lines-layer)
  )

(comment
  (do
    (def text-layer (->TextLayer (atom {})))
    (let [layer text-layer]
      (setup-layer layer)
      (add-layer layer)))
  (remove-layer text-layer)
  )

(comment
  (do
    (def eq-layer (->EquilibriumLayer (atom {})))
    (let [layer eq-layer]
      (setup-layer layer)
      (add-layer layer)))
  (remove-layer eq-layer)
  )

(comment
  (do
    (def vollayer (->VolCirclesLayer (atom {})))
    (let [layer vollayer]
      (setup-layer layer)
      (add-layer layer)))
  (remove-layer vollayer)
  )

(comment
  (do
    (def fadeoutlayer (->FadeoutLayer 5000 (atom {})))
    (let [layer fadeoutlayer]
      (setup-layer layer)
      (add-layer layer)))
  (println fadeoutlayer)
  (remove-layer fadeoutlayer)
  )

(q/defsketch clj-as-art
  :title "You spin my circle right round"
  :size :fullscreen
  ;; :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  ;; :features [:keep-on-top]
  ;; Check quil wiki for more info about middlewares and particularly
  ;; fun-mode.
  :middleware [m/fun-mode])

(do
  (def bglayer (->BackgroundLayer (atom {})))
  (let [layer bglayer]
    (setup-layer layer)
    ;; (reset! oneoff-fn #(do (q/color-mode :hsb) (q/background 255 155 155)))
    ;; (reset! oneoff-fn #(do (q/color-mode :hsb 100) (q/background 0 0 100)))
    ;; (reset! oneoff-fn #(do (q/color-mode :hsb) (q/background 100 100 100)))
    ;; (reset! bg-alpha 10)
    (add-layer layer)))

; (do
;   (def lines-layer (->LinesLayer (atom {})))
;   (let [layer lines-layer]
;     (setup-layer layer)
;     (add-layer layer)))

; (do
;   (def eq-layer (->EquilibriumLayer (atom {})))
;   (let [layer eq-layer]
;     (setup-layer layer)
;     (add-layer layer)))
