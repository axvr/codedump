(ns primes-attempt-7
  (:use clojure.repl
        clojure.pprint))

(set! *print-length* 50)

;; (range 1 100 1)
;; (cycle )
;; (iterate inc 1)

(range 0 100)

:all [1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25]
:one [1 1 1 1 1 1 1 1 1  1  1  1  1  1  1  1  1  1  1  1  1  1  1  1  1] ; 1
:two [0 1 0 1 0 1 0 1 0  1  0  1  0  1  0  1  0  1  0  1  0  1  0  1  0] ; 01
:thr [0 0 1 0 0 1 0 0 1  0  0  1  0  0  1  0  0  1  0  0  1  0  0  1  0] ; 001
:fou [0 0 0 1 0 0 0 1 0  0  0  1  0  0  0  1  0  0  0  1  0  0  0  1  0] ; 0001
:fiv [0 0 0 0 1 0 0 0 0  1  0  0  0  0  1  0  0  0  0  1  0  0  0  0  1] ; 00001

; Little endian.
:one  1       1 
:two  01      2
:thr  001     4
:fou  0001    8
:fiv  00001  16


:two [0 1 0 1 0 1 0 1 0  1  0  1  0  1  0  1  0  1  0  1  0  1  0  1  0] ; 01
:thr [0 0 1 0 0 1 0 0 1  0  0  1  0  0  1  0  0  1  0  0  1  0  0  1  0] ; 001

:two 01   si2
:thr 001  si3
2*3=6 ; get this many digits of each number.  (Repeats on 6th digit)
:two 010101
:thr 001001
:t|t 011101
[1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25]
[1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25]

[1 0 0 0 5 0 7 0 0  0 11  0 13  0  0  0 17  0 19  0  0  0 23  0 25]

01  ; 2
100 ; 4

(->bin 101110) ; = 46

(defn ->bin [b]
  (Integer/parseInt (str b) 2))
