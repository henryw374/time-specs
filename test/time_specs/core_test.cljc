(ns time-specs.core-test
  (:require [clojure.test :refer [deftest is testing]]
    [clojure.spec.alpha :as s]
    [clojure.spec.gen.alpha :as gen]
    [time-specs.core :as c]
    [cljc.java-time.local-date :as ld]
    [cljc.java-time.local-date-time :as ldt]))


(deftest ldt-in 
  (let [ldt-year (time-specs.core/temporal-in
                   (ldt/now)
                   (ldt/plus-years (ldt/now) 1))]
    (is (c/local-date-time? (gen/generate (s/gen ldt-year))))
    (is (s/valid? ldt-year (ldt/now)))
    (is (not (s/valid? ldt-year (ldt/plus-years (ldt/now) 2))))))

(deftest ld-in
  (let [ld-year (time-specs.core/temporal-in
                   (ld/now)
                   (ld/plus-years (ld/now) 1))]
    (is (c/local-date? (gen/generate (s/gen ld-year))))
    (is (s/valid? ld-year (ld/now)))
    (is (not (s/valid? ld-year (ld/plus-years (ld/now) 2))))))




(comment

  

  (c/temporal-in-range
    (ld/now)
    (ld/plus-years (ld/now) 1)
    (ld/plus-months (ld/now) 1)
    )

  (c/temporal? (ld/now)))

