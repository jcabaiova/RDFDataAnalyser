package cz.cvut.fit.cabaijan.calculationAnalysis;

import cz.cvut.fit.cabaijan.dbObjects.Entity;

/**
 * Created by Janka on 9/17/2016.
 * Queries class contains static functions and it defines queries used for calculation.
 */
public class Queries {
    /**
     * get query for the calculation of occurrence of entity in dataset
     * @param entity entity which is used for calculation
     * @param ontologyPredicate ontology predicate of dataset
     * @return query for the calculation of occurrence of entity in dataset
     */
    public static String getQueryForCountOfEntities(Entity entity, String ontologyPredicate) {

     return "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                "select (count(distinct ?s) as ?n)" +
                "where {?s <"+ontologyPredicate+"> <" + entity.getPath()+ ">.}";
    }

    /**
     * get query for the calculation of occurrence of predicates in entity in dataset
     * @param entity entity which is used for calculation
     * @param ontologyPredicate ontology predicate of dataset
     * @return query for the calculation of occurrence of predicates in entity in dataset
     */
    public static String getQueryForCreationOfPredicates(Entity entity , String ontologyPredicate) {


        return "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "select distinct ?p (count(distinct ?s) as ?n) \n" +
                "where {?s <"+ontologyPredicate+"> <" + entity.getPath() + ">;\n" +
                "       ?p ?o.}\n"+
        "group by ?p\n";
    }

    /**
     * get query for the initialization of domains from import file of domains and entities
     * @return query for the initialization of domains from import file of domains and entities
     */
    public static String getQueryForInitiateDomains() {
        return "prefix purl: <http://purl.org/dc/terms/>\n" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"+
                "select ?subject ?object where {\n" +
                "  ?subject rdfs:label ?object.\n" +
                "}";
    }

    /**
     * get query for the initialization of entities from import file of domains and entities
     * @return query for the initialization of entities from import file of domains and entities
     */
    public static String getQueryForInitiateEntities() {
        return "prefix purl: <http://purl.org/dc/terms/>\n" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"+
                "select ?subject ?object where {\n" +
                "  ?subject purl:subject ?object.\n" +
                "}";
    }

    /**
     * get query for the initialization of links between entities from import file of domains and entities
     * @return query for the initialization of links between entities from import file of domains and entities
     */
    public static String getQueryForLinking() {
        return "prefix purl: <http://purl.org/dc/terms/>\n" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"+
                "prefix owl: <http://www.w3.org/2002/07/owl#>\n"+
                "select ?subject ?object where {\n" +
                "?subject owl:sameAs ?object.\n" +
                "}";
    }
}
