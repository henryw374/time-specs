(ns time-specs.core
  (:require [clojure.spec.alpha :as s]
    [cljc.java-time.temporal.temporal :as temporal]
    [cljc.java-time.temporal.chrono-unit :as chrono-unit]
    [cljc.java-time.duration :as duration]
    [clojure.spec.gen.alpha :as gen]
    #?@(:cljs
        [[java.time :refer [Clock ZoneId ZoneOffset Instant Duration Period DayOfWeek Month ZonedDateTime LocalTime
                            LocalDateTime LocalDate Year YearMonth OffsetDateTime OffsetTime]]
         [java.time.temporal :refer [ChronoUnit ChronoField Temporal TemporalAdjusters]]
         [cljs.java-time.extend-eq-and-compare]]))
  #?(:clj
     (:import
       [java.time Clock ZoneId ZoneOffset Instant Duration Period DayOfWeek Month ZonedDateTime LocalTime LocalDateTime LocalDate Year YearMonth ZoneId OffsetDateTime OffsetTime]
       [java.time.temporal ChronoUnit ChronoField Temporal TemporalAdjusters])))

; TemporalAmount
(defn period? [x] (instance? Period x))
(defn duration? [x] (instance? Duration x))

; Temporal
(defn day-of-week? [x] (instance? DayOfWeek x))
(defn month? [x] (instance? Month x))
(defn instant? [x] (instance? Instant x))
(defn local-date? [x] (instance? LocalDate x))
(defn local-time? [x] (instance? LocalTime x))
(defn zoned-date-time? [x] (instance? ZonedDateTime x))
(defn local-date-time? [x] (instance? LocalDateTime x))
(defn year? [x] (instance? Year x))
(defn year-month? [x] (instance? YearMonth x))
(defn zone-offset? [x] (instance? ZoneOffset x))
(defn offset-date-time? [x] (instance? OffsetDateTime x))
(defn offset-time? [x] (instance? OffsetTime x))
(defn temporal? [x] (instance? Temporal x))

;other
(defn zone-id? [x] (instance? ZoneId x))
(defn clock? [x] (instance? Clock x))

(defn temporal-in-range [start end t]
  (and (temporal? t)
    (or
      (= start t)
      (and (.isBefore start t) (.isBefore t end)))))


(defmacro temporal-in
  "Returns a spec that validates Temporals in the range from start
(inclusive) to end (exclusive).

 Start and end must be Temporal instances of the same type
"
  [start end]
  `(do
     (when-not (and
                 (= (type ~start) (type ~end))
                 (temporal? ~start)
                 (.isBefore ~start ~end))
       (throw #?(:clj (Exception. "invalid start and/or end")
                 :cljs (js/Error. "invalid start and/or end"))))
     (let [st# ~start
           et# ~end
           ;todo - local-date and bigger will need to do something else
           between# (duration/between ~start ~end)
           mkdate# (fn [d#] (temporal/plus ~start d# chrono-unit/nanos))]
       (s/spec (and #(instance? (type ~start) %) #(temporal-in-range ~start ~end %))
         :gen (fn []
                (gen/fmap mkdate#
                  (gen/large-integer* {:min 0 :max (duration/to-nanos between#)})))))))


(defmacro temporal-amount-in
  ([max-length])
  ([min-length max-length]))

