(ns sample-http-server.core
   (:require
        [clojure.string :as string]
        [ring.util.request :as ring_util_request]
        [compojure.core :refer :all]
        [org.httpkit.server :refer [run-server]]
        [clojure.java.jdbc :as sql]
        [clojure.data.json :as json])
  (:gen-class))

(def mysql-db {
      :subprotocol "mysql"
      :subname "//localhost:3306/workbonddb"
      :user "root"
      :password "root"})

; Utility
(defn create-json-body [args]
  {
    :status  200
    :headers {"Content-Type" "application/json"}
    :body args
  }
)

(defn getQueryParameter [queryString key]
  (def parameters (string/split queryString #"&"))
  (def params {"hh" 6})
  (loop [i 0]
    (def pp (string/split (get parameters i) #"="))
    (def kk (get pp 0))
    (def value (get pp 1))
    (assoc params :kk "value")
    (println params)
    (println "KEY " kk " VALUE " value)
    (if (= i (count parameters))
      (recur (inc i))
    )
  )
  (println (str params))
  (get params key)
)
; -----------------------

(defn getUsers [req]
  (def result (sql/query mysql-db
      ["select * from users"]))
  (def formatted (json/write-str result))
  (create-json-body formatted)
)

(defn getSport [queryString]
  (def param (getQueryParameter queryString "id"))
  (println param)
  (def result (sql/query mysql-db
    ["select * from sports where sportId = ?" param]))
  (def formatted (json/write-str result))
  (create-json-body formatted)
)

(defn getSports []
  (def result (sql/query mysql-db
    ["select * from sports"]))
  (def formatted (json/write-str result))
  (create-json-body formatted)
)

(defn onGetSports [req]
  (def queryString (req :query-string))
  (if (string/blank? queryString)
  (getSports)
  (getSport queryString))
)

(defroutes request-handlers
  (GET "/users" req (getUsers req))
  (GET "/sports" request (onGetSports request))
    ; (println (str request)))
  ; (GET "/sports/:id" [req] (getSport req))
  ; (POST "/sports" [req] (addSport req))
  ; (PUT "/sports" [req] (updteSport req))
  ; (DELETE "/sports/" [req] (deleteSport req))
)

(defn -main
  [& args]
  (run-server request-handlers {:port 8080})
  (println "Server is running.")
)
