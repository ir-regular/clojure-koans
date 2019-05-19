(ns koans.14-recursion
  (:require [koan-engine.core :refer :all]))

(defn is-even? [n]
  (if (= n 0)
    true
    (not (is-even? (dec n)))))

(defn is-even-bigint? [n]
  (loop [n   n
         acc true]
    (if (= n 0)
      acc
      (recur (dec n) (not acc)))))

(defn recursive-reverse [coll]
  (loop [head (first coll)
         body (next coll)
         result '()]
    (if (nil? head)
      result
      (recur (first body) (next body) (cons head result)))))

(defn factorial [n]
  (loop [n      n
         result 1]
    (if (<= n 1)
      result
      (recur (dec n) (* n result))))
  )
; also possible:
;
; (defn factorial [n] (apply * (range 1N (inc n))))
; (defn factorial [n] (reduce * (range 1N (inc n))))
;
; (Both work because * can take any number of arguments; the difference is that:
;   - apply uses functions of variable arity
;   - reduce uses functions that can take only 2 arguments
; Reduce is more optimised in general, however apply can have specific optimisations like for str)
;
; (In fact (*) for (> 2 arguments) calls (reduce) under the hood; see (source *))
;
; orrr you can also do this:
;
;(defn factorial
;  ([] (concat [1N] (factorial2 1N 1N))) ; because special case, 0! == 1
;  ([prev n]
;   (let [current (* prev n)]
;     (cons current (lazy-seq (factorial2 current (inc n)))))))
;
; or if you want to use the builtin iterate instead of lazy-seq:
;
;(defn factorial []
;  (cons 1N (map first (iterate (fn [[current n]] ([(* current (inc n)), (inc n)]) [1N 1N]))))
;
; and then
; (map #(str %1 " - " %2) (take 10 (factorial)) (range 10))

;#(take % (map first (iterate (fn [[f s]] [s, (+ f s)]) [0N, 1N])))

(meditations
  "Recursion ends with a base case"
  (= true (is-even? 0))

  "And starts by moving toward that base case"
  (= false (is-even? 1))

  "Having too many stack frames requires explicit tail calls with recur"
  (= false (is-even-bigint? 100003N))

  "Reversing directions is easy when you have not gone far"
  (= '(1) (recursive-reverse [1]))

  "Yet it becomes more difficult the more steps you take"
  (= '(6 5 4 3 2) (recursive-reverse [2 3 4 5 6]))

  "Simple things may appear simple."
  (= 1 (factorial 1))

  "They may require other simple steps."
  (= 2 (factorial 2))

  "Sometimes a slightly bigger step is necessary"
  (= 6 (factorial 3))

  "And eventually you must think harder"
  (= 24 (factorial 4))

  "You can even deal with very large numbers"
  (< 1000000000000000000000000N (factorial 1000N))

  "But what happens when the machine limits you?"
  (< 1000000000000000000000000N (factorial 100003N)))
