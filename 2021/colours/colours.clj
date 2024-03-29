;;;; Convert 24-bit colours to closest xterm-256 colour.
;;;;
;;;; Written by Alex Vear (2021-02-14).
;;;; Public domain.  No rights reserved.


(ns colours
  (:require [clojure.math.numeric-tower :as math]))


;; https://jonasjacek.github.io/colors/
(def xterm-colours
  {"000000" 16,  "00005F" 17,  "000087" 18,  "0000AF" 19
   "0000D7" 20,  "0000FF" 21,  "005F00" 22,  "005F5F" 23
   "005F87" 24,  "005FAF" 25,  "005FD7" 26,  "005FFF" 27
   "008700" 28,  "00875F" 29,  "008787" 30,  "0087AF" 31
   "0087D7" 32,  "0087FF" 33,  "00AF00" 34,  "00AF5F" 35
   "00AF87" 36,  "00AFAF" 37,  "00AFD7" 38,  "00AFFF" 39
   "00D700" 40,  "00D75F" 41,  "00D787" 42,  "00D7AF" 43
   "00D7D7" 44,  "00D7FF" 45,  "00FF00" 46,  "00FF5F" 47
   "00FF87" 48,  "00FFAF" 49,  "00FFD7" 50,  "00FFFF" 51
   "5F0000" 52,  "5F005F" 53,  "5F0087" 54,  "5F00AF" 55
   "5F00D7" 56,  "5F00FF" 57,  "5F5F00" 58,  "5F5F5F" 59
   "5F5F87" 60,  "5F5FAF" 61,  "5F5FD7" 62,  "5F5FFF" 63
   "5F8700" 64,  "5F875F" 65,  "5F8787" 66,  "5F87AF" 67
   "5F87D7" 68,  "5F87FF" 69,  "5FAF00" 70,  "5FAF5F" 71
   "5FAF87" 72,  "5FAFAF" 73,  "5FAFD7" 74,  "5FAFFF" 75
   "5FD700" 76,  "5FD75F" 77,  "5FD787" 78,  "5FD7AF" 79
   "5FD7D7" 80,  "5FD7FF" 81,  "5FFF00" 82,  "5FFF5F" 83
   "5FFF87" 84,  "5FFFAF" 85,  "5FFFD7" 86,  "5FFFFF" 87
   "870000" 88,  "87005F" 89,  "870087" 90,  "8700AF" 91
   "8700D7" 92,  "8700FF" 93,  "875F00" 94,  "875F5F" 95
   "875F87" 96,  "875FAF" 97,  "875FD7" 98,  "875FFF" 99
   "878700" 100, "87875F" 101, "878787" 102, "8787AF" 103
   "8787D7" 104, "8787FF" 105, "87AF00" 106, "87AF5F" 107
   "87AF87" 108, "87AFAF" 109, "87AFD7" 110, "87AFFF" 111
   "87D700" 112, "87D75F" 113, "87D787" 114, "87D7AF" 115
   "87D7D7" 116, "87D7FF" 117, "87FF00" 118, "87FF5F" 119
   "87FF87" 120, "87FFAF" 121, "87FFD7" 122, "87FFFF" 123
   "AF0000" 124, "AF005F" 125, "AF0087" 126, "AF00AF" 127
   "AF00D7" 128, "AF00FF" 129, "AF5F00" 130, "AF5F5F" 131
   "AF5F87" 132, "AF5FAF" 133, "AF5FD7" 134, "AF5FFF" 135
   "AF8700" 136, "AF875F" 137, "AF8787" 138, "AF87AF" 139
   "AF87D7" 140, "AF87FF" 141, "AFAF00" 142, "AFAF5F" 143
   "AFAF87" 144, "AFAFAF" 145, "AFAFD7" 146, "AFAFFF" 147
   "AFD700" 148, "AFD75F" 149, "AFD787" 150, "AFD7AF" 151
   "AFD7D7" 152, "AFD7FF" 153, "AFFF00" 154, "AFFF5F" 155
   "AFFF87" 156, "AFFFAF" 157, "AFFFD7" 158, "AFFFFF" 159
   "D70000" 160, "D7005F" 161, "D70087" 162, "D700AF" 163
   "D700D7" 164, "D700FF" 165, "D75F00" 166, "D75F5F" 167
   "D75F87" 168, "D75FAF" 169, "D75FD7" 170, "D75FFF" 171
   "D78700" 172, "D7875F" 173, "D78787" 174, "D787AF" 175
   "D787D7" 176, "D787FF" 177, "D7AF00" 178, "D7AF5F" 179
   "D7AF87" 180, "D7AFAF" 181, "D7AFD7" 182, "D7AFFF" 183
   "D7D700" 184, "D7D75F" 185, "D7D787" 186, "D7D7AF" 187
   "D7D7D7" 188, "D7D7FF" 189, "D7FF00" 190, "D7FF5F" 191
   "D7FF87" 192, "D7FFAF" 193, "D7FFD7" 194, "D7FFFF" 195
   "FF0000" 196, "FF005F" 197, "FF0087" 198, "FF00AF" 199
   "FF00D7" 200, "FF00FF" 201, "FF5F00" 202, "FF5F5F" 203
   "FF5F87" 204, "FF5FAF" 205, "FF5FD7" 206, "FF5FFF" 207
   "FF8700" 208, "FF875F" 209, "FF8787" 210, "FF87AF" 211
   "FF87D7" 212, "FF87FF" 213, "FFAF00" 214, "FFAF5F" 215
   "FFAF87" 216, "FFAFAF" 217, "FFAFD7" 218, "FFAFFF" 219
   "FFD700" 220, "FFD75F" 221, "FFD787" 222, "FFD7AF" 223
   "FFD7D7" 224, "FFD7FF" 225, "FFFF00" 226, "FFFF5F" 227
   "FFFF87" 228, "FFFFAF" 229, "FFFFD7" 230, "FFFFFF" 231
   "080808" 232, "121212" 233, "1C1C1C" 234, "262626" 235
   "303030" 236, "3A3A3A" 237, "444444" 238, "4E4E4E" 239
   "585858" 240, "626262" 241, "6C6C6C" 242, "767676" 243
   "808080" 244, "8A8A8A" 245, "949494" 246, "9E9E9E" 247
   "A8A8A8" 248, "B2B2B2" 249, "BCBCBC" 250, "C6C6C6" 251
   "D0D0D0" 252, "DADADA" 253, "E4E4E4" 254, "EEEEEE" 255})


(defn decode-hex-char
  "Convert hexadecimal character to its decimal equivalent."
  [char]
  (case char
    \0 0
    \1 1
    \2 2
    \3 3
    \4 4
    \5 5
    \6 6
    \7 7
    \8 8
    \9 9
    (\A \a) 10
    (\B \b) 11
    (\C \c) 12
    (\D \d) 13
    (\E \e) 14
    (\F \f) 15
    nil))


(defn hex->dec
  "Merge hexadecimal digits into a single decimal value."
  [digits]
  (->> digits
       reverse
       (map-indexed (fn [idx dgt]
                      (* dgt (math/expt 16 idx))))
       (reduce +)))


;;; TODO: use CIELAB colour space instead of HSL.
(defn srgb->hsl
  "Convert sRGB values to HSL."
  ([hex]
   (->> hex
        (keep decode-hex-char)
        (partition 2)
        (map hex->dec)
        (map #(/ % 255))  ; Normalise from 0-255 -> 0-1.
        (apply srgb->hsl)))
  ([r g b]
   (let [x-max (max r g b)
         x-min (min r g b)
         chroma (- x-max x-min)
         lightness (/ (+ x-max x-min) 2)
         saturation (if (or (= lightness 0)
                            (= lightness 1))
                      0
                      (/ chroma
                         (- 1
                            (math/abs
                              (- (* 2 lightness)
                                 1)))))
         hue (if (= chroma 0)
               0
               (let [h (* 60
                          (cond
                            (= r x-max) (/ (- g b) chroma)
                            (= g x-max) (+ 2
                                           (/ (- b r)
                                              chroma))
                            :else (+ 4
                                     (/ (- r g)
                                        chroma))))]
                 (if (< h 0) (+ h 360) h)))]
     {:h hue
      :s (* saturation 100)
      :l (* lightness 100)})))


(defn euclidean-distance
  "Calculate Euclidean distance between points p and q in Euclidean space."
  [p q]
  (->> (map (fn [p1 q1]
              (math/expt (- p1 q1) 2))
            p q)
       (reduce +)
       math/sqrt))


(defn apply-weights [{:keys [h s l]}]
  {:h (* h 10)
   :s (* s 7)
   :l (* l 1)})


(defn find-closest [colour table]
  (let [base (-> colour srgb->hsl apply-weights vals)]
    (first
      (sort-by
        :dist
        (map (fn [[rgb code]]
               {:dist (->> rgb
                           srgb->hsl
                           apply-weights
                           vals
                           (euclidean-distance base))
                :code code
                :hex rgb})
             table)))))


(comment

  (use 'clojure.pprint)

  (pprint (find-closest "#88766F" xterm-colours))  ; FIXME: inaccurate result.
  (pprint (find-closest "#998B70" xterm-colours))
  (pprint (find-closest "#EAB56B" xterm-colours))
  (pprint (find-closest "#A74F4F" xterm-colours))

  (pprint (find-closest "#858CA6" xterm-colours))
  (pprint (find-closest "#94BACA" xterm-colours))
  (pprint (find-closest "#96A8A1" xterm-colours))
  (pprint (find-closest "#679D80" xterm-colours))

  (pprint (find-closest "#C9C9C9" xterm-colours))
  (pprint (find-closest "#666967" xterm-colours))  ; FIXME: inaccurate result.

  (pprint (find-closest "#222222" xterm-colours))
  (pprint (find-closest "#2A2A2A" xterm-colours))

  (pprint (find-closest "#343434" xterm-colours))
  (pprint (find-closest "#1A1A1A" xterm-colours))

  (pprint (find-closest "#4A4A4A" xterm-colours))

  )
