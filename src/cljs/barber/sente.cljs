(ns barber.sente ; .cljs
  (:require-macros
   [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require
   ;; <other stuff>
   [taoensso.encore :as encore :refer-macros (have have?)]
   [cljs.core.async :as async :refer (<! >! put! chan)]
   [taoensso.sente  :as sente :refer (cb-success?)]
   [re-frame.core :refer [subscribe dispatch]]
   [ajax.core :as ajax :refer [GET POST]])) ; <--- Add this


(defn ->output! [& args]
  (.log js/console
        (str
          "Sente message: "
          (apply str args))))

(def ?csrf-token
  (when-let [el (.getElementById js/document "sente-csrf-token")]
    (.getAttribute el "data-csrf-token")))

(defn local-uri? [{:keys [uri]}]
  (not (re-find #"^\w+?://" uri)))


(defn default-headers [request]
  (if (local-uri? request)
    (-> request
        (update
          :headers
          #(merge
             {"Accept" "application/transit+json"
              "x-csrf-token" ?csrf-token}
             %)))
    request))


(defn load-interceptors! []
  (swap! ajax/default-interceptors
         conj
         (ajax/to-interceptor {:name "default headers"
                               :request default-headers})))

(load-interceptors!)



(let [;; For this example, select a random protocol:
      rand-chsk-type (if (>= (rand) 0.5) :ajax :auto)
      _ (->output! "Randomly selected chsk type: %s" rand-chsk-type)

      ;; Serializtion format, must use same val for client + server:
      packer :edn ; Default packer, a good choice in most cases
      ;; (sente-transit/get-transit-packer) ; Needs Transit dep

      {:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket-client!
        "/chsk" ; Must match server Ring routing URL
        ?csrf-token
        {:type   rand-chsk-type
         :packer packer})]

  (def chsk       chsk)
  (def ch-chsk    ch-recv) ; ChannelSocket's receive channel
  (def chsk-send! send-fn) ; ChannelSocket's send API fn
  (def chsk-state state))   ; Watchable, read-only atom


(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id) ; Dispatch on event-id

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg :keys [id ?data event]}]
  (-event-msg-handler ev-msg))

(defmethod -event-msg-handler
  :default ; Default/fallback case (no other matching handler)
  [{:as ev-msg :keys [event]}]
  (->output! "Unhandled event: %s" event))

(defmethod -event-msg-handler :chsk/state
  [{:as ev-msg :keys [?data]}]
  (let [[old-state-map new-state-map] (have vector? ?data)]
    (if (:first-open? new-state-map)
      (dispatch [:assoc-data-to-key :websocket? true])
      (->output! "Channel socket state change: %s"              new-state-map))))

(defmethod -event-msg-handler :chsk/recv
  [{:as ev-msg :keys [?data]}]
  (let [[the-key the-data] ?data]
    (case the-key
       :calendar/update (do
                          (.log js/console (str "Calendar update: " (:date the-data)))
                          (if (= (:date the-data) @(subscribe [:data :selected-date]))
                            (dispatch [:get-reservations-and-brakes (:date the-data)])))
       (->output! "Push event from server: %s" (first ?data)))))

(defmethod -event-msg-handler :chsk/handshake
  [{:as ev-msg :keys [?data]}]
  (let [[?uid ?csrf-token ?handshake-data] ?data]
    (->output! "Handshake: %s" ?data)))

(defonce router_ (atom nil))
(defn  stop-router! [] (when-let [stop-f @router_] (stop-f)))
(defn start-router! []
  (stop-router!)
  (reset! router_
    (sente/start-client-chsk-router!
      ch-chsk event-msg-handler)))

(defn start! []
  (if ?csrf-token
    (do (->output! "CSRF token detected in HTML, great!")
      (start-router!))
    (->output! "CSRF token NOT detected in HTML, default Sente config will reject requests")))
