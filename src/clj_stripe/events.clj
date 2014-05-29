;   Copyright (c) 2014 Frozenlock. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns clj-stripe.events
    "Functions for Stripe events API"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn get-event
  "Creates a new get-event operation.
  Requires a event id as a string.
  Execute the operation using common/execute."
  [event-id]
  {:operation :get-event "id" event-id})

(defmethod execute :get-event 
  [op-data] 
  (util/get-request *stripe-token* (str api-root "/events/" (get op-data "id"))))


;; ;; untested

;; (defn get-all-events
;;   "Creates a new get-all-events operation.
;;   Execute the operation using common/execute."
;;   ([& extra-info] 
;;     (apply util/merge-maps {:operation :get-all-events} extra-info)))

;; (defmethod execute :get-all-events 
;;   [op-data] 
;;   (util/get-request *stripe-token* (str api-root "/events") op-data))
