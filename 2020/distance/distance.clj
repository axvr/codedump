;;;; Public domain.  No rights reserved.

(ns euclidean-space
  (:require [clojure.math.numeric-tower :as math]))

(defn distance
  "Calculate Euclidean distance between points p and q in Euclidean space."
  [p q]
  (loop [i (dec (count p))
         d 0]
    (if (< i 0)
      (math/sqrt d)
      (recur (dec i)
             (+ d
                (math/expt (- (p i)
                              (q i))
                           2))))))

;; (distance [0 1]
;;           [1 2])

;; (distance [1 2 0]
;;           [3 2 1])

;; (distance [78 84 87 91 76]
;;           [92 83 91 79 89])

;; Edit 2021-02-14: created a more compact version of this function.
(defn distance2
  "Calculate Euclidean distance between points p and q in Euclidean space."
  [p q]
  (->> (map (fn [p1 q1]
              (math/expt (- p1 q1) 2))
            p q)
       (reduce +)
       math/sqrt))
