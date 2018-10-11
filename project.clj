(defproject sample-http-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                [http-kit "2.2.0"]
                [compojure "1.5.1"]
                [org.clojure/java.jdbc "0.4.2"]
                [mysql/mysql-connector-java "5.1.18"]
                [org.clojure/data.json "0.2.6"]]
  :main ^:skip-aot sample-http-server.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
