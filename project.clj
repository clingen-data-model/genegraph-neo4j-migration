(defproject genegraph-neo4j-migration "0.1.0-SNAPSHOT"
  :description "Code to migrate necessary content from Neo4j into genegraph events"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.neo4j.driver/neo4j-java-driver "4.1.0"]
                 [cheshire "5.10.0"]]
  :main ^:skip-aot genegraph-neo4j-migration.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
