(ns clj-as-art.layers.equilibrium
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m])
  (:use [quil-layer.layer]))

;;;
;;; Equilibrium simulation
;;; http://nbeloglazov.com/2014/09/09/equilibrium.html
;;;

;;;
;;; Constants
;;;

(def speed 5)
(def eps 1)

;;;
;;; Setup
;;;

(defn choose-leaders
  "Randomly chooses 2 leaders for each point and assigns them to the point
  as :leaders. Algorithm chooses leaders in such way that every point is a
  leader for 2 other points."
  [points]
  (letfn [(delete-first [coll val]
            (let [[before after] (split-with #(= % val) coll)]
              (concat before (rest after))))

          (choose-leaders [cur leaders]
            (let [chosen (->> leaders
                              distinct
                              (remove #(= % cur))
                              (take 2))]
              [chosen (reduce delete-first leaders chosen)]))

          (process-point [state point]
            (let [[chosen leaders] (choose-leaders point (:leaders state))]
              (-> state
                  (update-in [:points point] assoc :leaders chosen)
                  (assoc :leaders leaders))))

          (process-all-points []
            (let [all (range (count points))
                  leaders (shuffle (concat all all))]
              (-> (reduce process-point
                          {:points points
                           :leaders leaders}
                          all)
                  :points)))

          (good? [points]
            (every? #(-> % :leaders count (= 2)) points))]
    (->> (repeatedly process-all-points)
         (filter good?)
         (first))))

(defn rand-point
  "Creates a random point with constant velocity."
  [size]
  (let [angle (rand q/TWO-PI)
        cur-speed (rand speed)]
    {:x (rand-int size)
     :y (rand-int size)
     :velocity ((juxt q/cos q/sin) cur-speed)
     :r (rand-int 255)
     :g (rand-int 255)
     :b (rand-int 255)}))

(defn generate-points
  "Generates n points with leaders assigned. size is size of the sketch."
  [n size]
  (-> (repeatedly n #(rand-point size))
      vec
      (choose-leaders)
      vec))


(defn- setup []
  {:running? false})

;; (defn- setup
;;   "Standard quil function which sets up a sketch and returns initial state."
;;   []
;;   (q/frame-rate 30)
;;   (let [size (q/width)
;;         n (int (q/map-range size
;;                             200 500
;;                             15 25))]
;;    {:points (generate-points n size)
;;     :running? true
;;     :dragging nil
;;     :n n
;;     :size size}))

;;;
;;; Update logic
;;;

(defn calc-move
  "Calculates the displacement vector for point p given it leaders p1 and p2."
  [p p1 p2]
  (let [a (- (:x p2) (:x p1))
        b (- (:y p2) (:y p1))
        mx (/ (+ (:x p1) (:x p2)) 2)
        my (/ (+ (:y p1) (:y p2)) 2)
        c (- (+ (* a mx) (* b my)))
        hyp (q/sqrt (+ (q/sq a) (q/sq b)))
        d (/ (+ (* a (:x p)) (* b (:y p)) c)
             hyp)
        angle (q/atan2 b a)
        angle (if (pos? d) (+ q/PI angle) angle)
        speed (min speed (Math/abs d))]
    (if (< speed eps)
      [0 0]
      [(* (q/cos angle) speed)
       (* (q/sin angle) speed)])))

(defn ensure-in-board
  "Ensures that the point is inside sketch boundaries.
  Uses toroidal board."
  [p size]
  (letfn [(update-coord [p coord]
            (update-in p [coord] #(-> % (+ size) (mod size))))]
    (reduce update-coord p [:x :y])))

(defn move
  "Moves the point towards equidistance with point leaders."
  [p state]
  (let [points (:points state)
        [l1 l2] (:leaders p)
        [dx dy] (calc-move p (points l1) (points l2))
        [vx vy] (:velocity p)]
    (-> p
        (update-in [:x] + dx vx)
        (update-in [:y] + dy vy)
        (ensure-in-board (:size state)))))

(defn update-points
  "Moves each point and returns updated vector of points."
  [points state]
  (reduce
   (fn [new-points ind]
     (update-in new-points [ind] move state))
   points
   (range (count points))))

(defn- update-state
  "Updates sketch state. If sketch is d then the state
  returned unmodified."
  [state]
  (if (:running? state)
    (update-in state [:points] update-points state)
    (let [size (q/width)
          n (int (q/map-range size
                              200 500
                              15 25))]
      {:points (generate-points n size)
       :running? true
       :dragging nil
       :n n
       :size size})))

;;;
;;; Draw
;;;

(defn draw-point [p]
  (do
    (q/color-mode :rgb)
    (q/stroke (:r p) (:g p) (:b p))
    (q/point (:x p) (:y p))
    (q/color-mode :hsb)))

(defn find-points
  "Finds all points in 10-pixel approximity of point (x,y)."
  [points x y]
  (for [ind (range (count points))
        :let [p (points ind)]
        :when (< (q/dist (:x p) (:y p) x y) 100)]
    ind))

(defn draw-equidistance
  "Highlights a point, it leaders and draws equidistance line."
  [points p]
  (let [[l1 l2] (:leaders p)
        p1 (points l1)
        p2 (points l2)
        a (* (q/height) (- (:y p1) (:y p2)))
        b (* (q/width) (- (:x p2) (:x p1)))
        mx (/ (+ (:x p1) (:x p2)) 2)
        my (/ (+ (:y p1) (:y p2)) 2)]
    (q/stroke 0 255 0)
    (draw-point p)
    (q/stroke 255 0 0)
    (draw-point p1)
    (draw-point p2)
    (q/stroke-weight 5) ;; CHANGE THIS to tweak line width!
    (q/line (+ mx a) (+ my b) (- mx a) (- my b))))

(defn- draw-state
  "Draws sketch state."
  [{:keys [points] :as state}]
  ;;(q/background 250)
  ;; Use different point size depending on
  ;; screen size.
  (let [weight (q/map-range (:size state)
                            200 500
                            20 40)] ;; CHANGE THIS to tweak circle size!
    (q/stroke-weight weight))
  (doseq [p points]
    (draw-point p))
  ;; (doseq [ind (find-points points (q/mouse-x) (q/mouse-y))]
  ;;   (draw-equidistance points (points ind))))
  (if (< (rand-int 30) 15)
    (doseq [ind (find-points points (rand-int (q/width)) (rand-int (q/width)))]
      (draw-equidistance points (points ind)))))

(defn regenerate-points
  "Regenerates points in the state map."
  [state]
  (assoc state :points
         (generate-points (:n state) (:size state))))

(defrecord EquilibriumLayer [state]
  Layer
  (setup-layer-state [this]
    (setup))
  (update-layer-state [this state]
    (update-state state))
  (draw-layer-state [this state]
    (draw-state state)))

;; (q/defsketch equilibrium
;;     :host "host"
;;     :size [500 500]
;;     :setup setup
;;     :update update-state
;;     :draw draw-state
;;     :mouse-pressed mouse-pressed
;;     :mouse-dragged mouse-dragged
;;     :key-pressed key-pressed
;;     :middleware [m/fun-mode])
