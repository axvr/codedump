#!/usr/local/bin/csi -ss


;;; Convert 24-bit colours to closest xterm-256 colour.
;;;
;;; Written by Alex Vear (2021-02).
;;; Public domain.  No rights reserved.
;;;
;;; Original but unfinished CHICKEN Scheme version of this colour converter,
;;; use the Clojure version instead.


(import (srfi-1))


(define colours
  '((16 . "000000") (17 . "00005F") (18 . "000087") (19 . "0000AF")
    (20 . "0000D7") (21 . "0000FF") (22 . "005F00") (23 . "005F5F")
    (24 . "005F87") (25 . "005FAF") (26 . "005FD7") (27 . "005FFF")
    (28 . "008700") (29 . "00875F") (30 . "008787") (31 . "0087AF")
    (32 . "0087D7") (33 . "0087FF") (34 . "00AF00") (35 . "00AF5F")
    (36 . "00AF87") (37 . "00AFAF") (38 . "00AFD7") (39 . "00AFFF")
    (40 . "00D700") (41 . "00D75F") (42 . "00D787") (43 . "00D7AF")
    (44 . "00D7D7") (45 . "00D7FF") (46 . "00FF00") (47 . "00FF5F")
    (48 . "00FF87") (49 . "00FFAF") (50 . "00FFD7") (51 . "00FFFF")
    (52 . "5F0000") (53 . "5F005F") (54 . "5F0087") (55 . "5F00AF")
    (56 . "5F00D7") (57 . "5F00FF") (58 . "5F5F00") (59 . "5F5F5F")
    (60 . "5F5F87") (61 . "5F5FAF") (62 . "5F5FD7") (63 . "5F5FFF")
    (64 . "5F8700") (65 . "5F875F") (66 . "5F8787") (67 . "5F87AF")
    (68 . "5F87D7") (69 . "5F87FF") (70 . "5FAF00") (71 . "5FAF5F")
    (72 . "5FAF87") (73 . "5FAFAF") (74 . "5FAFD7") (75 . "5FAFFF")
    (76 . "5FD700") (77 . "5FD75F") (78 . "5FD787") (79 . "5FD7AF")
    (80 . "5FD7D7") (81 . "5FD7FF") (82 . "5FFF00") (83 . "5FFF5F")
    (84 . "5FFF87") (85 . "5FFFAF") (86 . "5FFFD7") (87 . "5FFFFF")
    (88 . "870000") (89 . "87005F") (90 . "870087") (91 . "8700AF")
    (92 . "8700D7") (93 . "8700FF") (94 . "875F00") (95 . "875F5F")
    (96 . "875F87") (97 . "875FAF") (98 . "875FD7") (99 . "875FFF")
    (100 . "878700") (101 . "87875F") (102 . "878787") (103 . "8787AF")
    (104 . "8787D7") (105 . "8787FF") (106 . "87AF00") (107 . "87AF5F")
    (108 . "87AF87") (109 . "87AFAF") (110 . "87AFD7") (111 . "87AFFF")
    (112 . "87D700") (113 . "87D75F") (114 . "87D787") (115 . "87D7AF")
    (116 . "87D7D7") (117 . "87D7FF") (118 . "87FF00") (119 . "87FF5F")
    (120 . "87FF87") (121 . "87FFAF") (122 . "87FFD7") (123 . "87FFFF")
    (124 . "AF0000") (125 . "AF005F") (126 . "AF0087") (127 . "AF00AF")
    (128 . "AF00D7") (129 . "AF00FF") (130 . "AF5F00") (131 . "AF5F5F")
    (132 . "AF5F87") (133 . "AF5FAF") (134 . "AF5FD7") (135 . "AF5FFF")
    (136 . "AF8700") (137 . "AF875F") (138 . "AF8787") (139 . "AF87AF")
    (140 . "AF87D7") (141 . "AF87FF") (142 . "AFAF00") (143 . "AFAF5F")
    (144 . "AFAF87") (145 . "AFAFAF") (146 . "AFAFD7") (147 . "AFAFFF")
    (148 . "AFD700") (149 . "AFD75F") (150 . "AFD787") (151 . "AFD7AF")
    (152 . "AFD7D7") (153 . "AFD7FF") (154 . "AFFF00") (155 . "AFFF5F")
    (156 . "AFFF87") (157 . "AFFFAF") (158 . "AFFFD7") (159 . "AFFFFF")
    (160 . "D70000") (161 . "D7005F") (162 . "D70087") (163 . "D700AF")
    (164 . "D700D7") (165 . "D700FF") (166 . "D75F00") (167 . "D75F5F")
    (168 . "D75F87") (169 . "D75FAF") (170 . "D75FD7") (171 . "D75FFF")
    (172 . "D78700") (173 . "D7875F") (174 . "D78787") (175 . "D787AF")
    (176 . "D787D7") (177 . "D787FF") (178 . "D7AF00") (179 . "D7AF5F")
    (180 . "D7AF87") (181 . "D7AFAF") (182 . "D7AFD7") (183 . "D7AFFF")
    (184 . "D7D700") (185 . "D7D75F") (186 . "D7D787") (187 . "D7D7AF")
    (188 . "D7D7D7") (189 . "D7D7FF") (190 . "D7FF00") (191 . "D7FF5F")
    (192 . "D7FF87") (193 . "D7FFAF") (194 . "D7FFD7") (195 . "D7FFFF")
    (196 . "FF0000") (197 . "FF005F") (198 . "FF0087") (199 . "FF00AF")
    (200 . "FF00D7") (201 . "FF00FF") (202 . "FF5F00") (203 . "FF5F5F")
    (204 . "FF5F87") (205 . "FF5FAF") (206 . "FF5FD7") (207 . "FF5FFF")
    (208 . "FF8700") (209 . "FF875F") (210 . "FF8787") (211 . "FF87AF")
    (212 . "FF87D7") (213 . "FF87FF") (214 . "FFAF00") (215 . "FFAF5F")
    (216 . "FFAF87") (217 . "FFAFAF") (218 . "FFAFD7") (219 . "FFAFFF")
    (220 . "FFD700") (221 . "FFD75F") (222 . "FFD787") (223 . "FFD7AF")
    (224 . "FFD7D7") (225 . "FFD7FF") (226 . "FFFF00") (227 . "FFFF5F")
    (228 . "FFFF87") (229 . "FFFFAF") (230 . "FFFFD7") (231 . "FFFFFF")
    (232 . "080808") (233 . "121212") (234 . "1C1C1C") (235 . "262626")
    (236 . "303030") (237 . "3A3A3A") (238 . "444444") (239 . "4E4E4E")
    (240 . "585858") (241 . "626262") (242 . "6C6C6C") (243 . "767676")
    (244 . "808080") (245 . "8A8A8A") (246 . "949494") (247 . "9E9E9E")
    (248 . "A8A8A8") (249 . "B2B2B2") (250 . "BCBCBC") (251 . "C6C6C6")
    (252 . "D0D0D0") (253 . "DADADA") (254 . "E4E4E4") (255 . "EEEEEE")))


(define (keys alist)
  (fold (lambda (i acc)
          (cons (car i) acc))
        '()
        alist))


(define (vals alist)
  (fold (lambda (i acc)
          (cons (cdr i) acc))
        '()
        alist))


(define (some? x)
  (not (null? x)))


(define (hex-char->number char)
  (case char
    ((#\0) 0)
    ((#\1) 1)
    ((#\2) 2)
    ((#\3) 3)
    ((#\4) 4)
    ((#\5) 5)
    ((#\6) 6)
    ((#\7) 7)
    ((#\8) 8)
    ((#\9) 9)
    ((#\a #\A) 10)
    ((#\b #\B) 11)
    ((#\c #\C) 12)
    ((#\d #\D) 13)
    ((#\e #\E) 14)
    ((#\f #\F) 15)
    (else '())))


(define (hex->dec hex-str)
  ;; TODO handle decimals places and negative values.
  (define (calc so-far pow digits)
    (if (null? digits)
      so-far
      (calc (+ so-far
               (* (car digits)
                  (expt 16 pow)))
            (+ pow 1)
            (cdr digits))))
  (calc 0 0 (filter some?
                    (map hex-char->number
                         (reverse (string->list hex-str))))))


(define (hex-char? x)
  (some? (hex-char->number x)))


(define (batch lst width)
  (reverse
    (map
      reverse
      (fold (lambda (i acc)
              (if (some? acc)
                (let ((j (car acc)))
                  (if (< (length j) width)
                    (cons (cons i j)
                          (cdr acc))
                    (cons (list i)
                          acc)))
                (cons (list i) acc)))
            '()
            lst))))


(define (colour-hex->rgb-hex colour-hex)
  (let [(digits (filter hex-char? (string->list colour-hex)))]
    (map list->string (batch digits 2))))


(define (rgb->hsl r g b)
  (let* [(x-max (max r g b))
         (x-min (min r g b))
         (delta (- x-max x-min))
         (lightness (* (/ (+ x-max x-min)
                          2)
                       100))
         (saturation (if (= delta 0)
                       0
                       (* (/ delta
                             (- 1
                                (abs (- (* 2 lightness)
                                        1))))
                          100)))
         (hue (if (= delta 0)
                0
                (let [(h (* 60
                            (cond
                              [(= r x-max) (/ (- g b)
                                              delta)]
                              [(= g x-max) (+ 2
                                              (/ (- b r)
                                                 delta))]
                              [else (+ 4
                                       (/ (- r g)
                                          delta))])))]
                  (if (< h 0)
                    (+ h 360)
                    h))))]
    (list hue
          (round (* saturation 100))
          (round (* lightness 100)))))


(let* [(rgb (map
              (lambda (x) (/ x 255))
              (map
                hex->dec
                (colour-hex->rgb-hex "#ABCDEF"))))
       (r (first rgb))
       (g (second rgb))
       (b (third rgb))]
  (rgb->hsl r g b))


;; Example input:
;;
;;     $ ./colour.scm '#94BACA'
;;     #94BACA -> xxx
;;     $ ./colour.scm '94BACA'
;;     #94BACA -> xxx
;;     $ ./colour.scm '#94BACA' '#212121'
;;     #94BACA -> xxx
;;     #212121 -> yyy
;;
(define (main args)
  (if (null? args)
    (begin
      (display "Invalid arguments\n")
      1)
    (display args)))
