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

(defn error [code message]
  (def body {code message})
  (json/write-str body)
)

(defn getSportById [id]
  (def result (sql/query mysql-db
    ["select * from sports where sportId = ?" id]))
  (json/write-str result)
)
; -----------------------

(defn getUsers [req]
  (def result (sql/query mysql-db
      ["select * from users"]))
  (def formatted (json/write-str result))
  (create-json-body formatted)
)

(defn getSport [queryString]
  (def parameters (string/split queryString #"="))
  (if (= (get parameters 0) "id")
  (getSportById (get parameters 1))
  (create-json-body (error "invalidParameter" "Invalid parameter key")))
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
  ; (POST "/sports" [req] (addSport req))
  ; (PUT "/sports" [req] (updteSport req))
  ; (DELETE "/sports/" [req] (deleteSport req))
)

(defn -main
  [& args]
  (run-server request-handlers {:port 8080})
  (println "Server is running.")
)
