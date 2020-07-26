(ns genegraph-neo4j-migration.core
  (:require [clojure.set :as set]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]])
  (:import [org.neo4j.driver AuthTokens Driver GraphDatabase Result])
  (:gen-class))

(def select-curations-query "match (d:Disease)<-[:has_object]-(a:GeneDiseaseAssertion)-[:has_subject]->(g:Gene), (moi:RDFClass)<-[:has_mode_of_inheritance]-(a)-[:has_predicate]->(score:RDFClass), (a)-[:wasAttributedto]->(gcep:Agent) where not a.gci_id is null return a.gci_id, g.iri, d.iri, score.iri, score.label, moi.iri, gcep.iri, a.score_string_gci, a.date, a.title, a.sopVersion limit 3")

(def curation-keys {"a.gci_id" :id
                    "g.iri" :gene
                    "d.iri" :disease
                    "score.iri" :score
                    "score.label" :score-label
                    "moi.iri" :moi
                    "gcep.iri" :gcep
                    "a.score_string_gci" :score-string
                    "a.date" :date
                    "a.title" :title
                    "a.sopVersion" :sop-version})



(defn create-driver! 
  []
  (GraphDatabase/driver (System/getenv "NEO4J_SERVER_PATH")
                        (AuthTokens/basic (System/getenv "NEO4J_USER")
                                          (System/getenv "NEO4J_PASS"))))


(defn write-curation [path content]
  (with-open [w (io/writer (str path "/" (:id content)))]
    (binding [*out* w]
      (pprint content))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (with-open [driver (create-driver!)
              session (.session driver)]
    (let [curations  (->> (.run session select-curations-query)
                          iterator-seq
                          (map #(.asMap %))
                          (map #(into {} %))
                          (map #(set/rename-keys % curation-keys)))]
      (doseq [c curations]
        (write-curation "data" c)))))
