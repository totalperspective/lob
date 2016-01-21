(ns lob.link
  (:require [cemerick.url :as url]
            [medley.core :as m]
            [lob.message :as msg]))

(defprotocol Link
  (-open! [link])
  (-close! [link])
  (-closed? [link])
  (-publication [link id] "Return a publication on the link"))

(defprotocol Publication
  (-subscribe! [publication buffer-size callback] "Subscribe to a publication, returns a subscription"))

(defprotocol Subscription
  (-unsubscribe! [subscription] "Unsubscribe from a publication"))

(defprotocol Receiver
  (-receive! [receiver] "Recieve a message from the link returns nil if there is none"))

(defprotocol Sender
  (-send! [sender msg] "Send a message on the link, returns true if
succesful, false if link closed or nil if link is non-blocking and send
will block"))

(defn link? [link]
  (satisfies? Link link))

(defn open! [link]
  {:pre [(link? link)]}
  (-open! link))

(defn close! [link]
  {:pre [(link? link)]}
  (-close! link))

(defn closed? [link]
  {:pre [(link? link)]}
  (-closed? link))

(defn publication? [publication]
  (satisfies? Publication publication))

(defn subscribe!
  ([publication buffer-size]
   (subscribe! publication buffer-size nil))
  ([publication buffer-size callback]
   {:pre [(publication? publication)]}
   (-subscribe! publication buffer-size callback)))

(defn subscription? [subscription]
  (satisfies? Subscription subscription))

(defn unsubscribe! [subscription]
  {:pre [(subscription? subscription)]}
  (-unsubscribe! subscription))

(defn receiver? [receiver]
  (satisfies? Receiver receiver))

(defn receive! [receiver]
  {:pre [(receiver? receiver)]
   :post [(or (msg/message? %) (nil? %))]}
  (-receive! receiver))

(defn sender? [sender]
  (satisfies? Sender sender))

(defn send! [sender msg]
  {:pre [(sender? sender) (msg/message? msg)]
   :post [(or (nil? %) (m/boolean? %))]}
  (-send! sender msg))
