(ns bowling
  "The smallest bowling score calculator in Clojure?  7 lines.

  I created this solution very early in 2023 (or late 2022) and used it to
  teach recursion.  The problem is perfectly suited for recursive solutions,
  much more so than the classic fibonacci number example.

  This is an almost identical recreation from 3 years later (2026-07-06).  Only
  differences being parameter order and naming.  It is still so elegant and
  natural a solution that I was still able to one shot it after 3+ years.")

;; Public domain.  No rights reserved.

(defn score [rolls]
  (loop [turn 1, total 0, [r1 r2 r3 :as rs] rolls]
    (cond
      (= 10 turn)      (apply + total rs)
      (= 10 r1)        (recur (inc turn) (+ total r1 r2 r3) (next rs))
      (= 10 (+ r1 r2)) (recur (inc turn) (+ total r1 r2 r3) (nnext rs))
      :else            (recur (inc turn) (+ total r1 r2)    (nnext rs)))))

(assert (= 300 (score [10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10])))
(assert (= 133 (score [1 4, 4 5, 6 4, 5 5, 10, 0 1, 7 3, 6 4, 10, 2 8, 6])))
(assert (= 299 (score [10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 9])))
(assert (= 270 (score [10, 10, 10, 10, 10, 10, 10, 10, 10, 9 1 1])))
(assert (=   0 (score [0 0, 0 0, 0 0, 0 0, 0 0, 0 0, 0 0, 0 0, 0 0, 0 0])))
(assert (=  20 (score [1 1, 1 1, 1 1, 1 1, 1 1, 1 1, 1 1, 1 1, 1 1, 1 1])))
(assert (=  16 (score [5 5, 3 0, 0 0, 0 0, 0 0, 0 0, 0 0, 0 0, 0 0, 0 0])))
(assert (=  24 (score [10, 3 4, 0 0, 0 0, 0 0, 0 0, 0 0, 0 0, 0 0, 0 0])))
(assert (=  29 (score [0 10, 1 1, 1 1, 1 1, 1 1, 1 1, 1 1, 1 1, 1 1, 1 1])))
(assert (= 112 (score [4 2, 0 10, 6 1, 0 0, 10, 3 4, 10, 10, 8 1, 0 3])))
