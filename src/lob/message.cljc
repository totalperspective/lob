(ns lob.message)

(defprotocol Message
  (-id [msg])
  (-source [msg])
  (-destination [msg])
  (-content-type [msg])
  (-headers [msg])
  (-content [msg]))

(defprotocol Ackable
  (-ack! [msg]))

(defprotocol Nackable
  (-nack! [msg]))

(defprotocol Replyable
  (-reply [msg id content opts]))

(defn message? [msg]
  (satisfies? Message msg))

(defn id [msg]
  {:pre [(message? msg)]}
  (-id msg))

(defn source [msg]
  {:pre [(message? msg)]}
  (-source msg))

(defn destination [msg]
  {:pre [(message? msg)]}
  (-destination msg))

(defn content-type [msg]
  {:pre [(message? msg)]}
  (-content-type msg))

(defn headers [msg]
  {:pre [(message? msg)]}
  (-headers msg))

(defn content [msg]
  {:pre [(message? msg)]}
  (-content msg))

(defn ackable? [msg]
  (satisfies? Ackable msg))

(defn ack! [msg]
  (when (ackable? msg)
    (-ack! msg)))

(defn nackable? [msg]
  (satisfies? Nackable msg))

(defn nack! [msg]
  (when (nackable? msg))
  (-nack! msg))

(defn replyable? [msg]
  (satisfies? Replyable msg))

(defn reply [msg id content opts]
  (when (replyable? msg)
    (-reply msg id content opts)))

(defrecord BasicMessage [id content content-type headers]
  (-id [msg]
    id)
  (-source [msg])
  (-destination [msg])
  (-content-type [msg]
    content-type)
  (-headers [msg]
    headers)
  (-content [msg]
    content))

(defn basic
  ([id content]
   (basic id content "text/plain"))
  ([id content content-type]
   (basic id content content-type {}))
  ([id content content-type headers]
   (->BasicMessage id content content-type headers)))
