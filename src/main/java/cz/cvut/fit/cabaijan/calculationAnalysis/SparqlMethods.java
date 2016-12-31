package cz.cvut.fit.cabaijan.calculationAnalysis;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.impl.LiteralImpl;
import cz.cvut.fit.cabaijan.dbObjects.*;
import cz.cvut.fit.cabaijan.logs.Logger;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Janka on 9/17/2016.
 * SparqlMethods class uses for execution of SPARQL queries and processing of results.
 * It is abstract and extended classes are one for querying on HDT files, and second one for querying on Sparql endpoint
 */
public abstract class SparqlMethods {
    /**
     * execute query and get results
     * @param query query for execution
     * @param datasetAnalysis datasetAnalysis for which queries are executed
     * @param model model on which queries are executed
     * @return results of querying
     */
    public abstract ResultSet executeQuery(String query, DatasetAnalysis datasetAnalysis, Model model);

    /**
     * execute query and get results where datasetAnalysis is not needed
     * @param query query for execution
     * @param model model on which queries are executed
     * @return query and get results
     */
    public ResultSet executeQuery(String query, Model model) {
        try {
            QueryExecution exec = QueryExecutionFactory.create(query, model);
            return exec.execSelect();

        } catch (Exception ex) {
            Logger.writeException(ex);
            return null;
        }

    }

    /**
     * get result from query execution in the form of one number
     * @param results resultSet for processing
     * @return result from query execution in the form of one number
     */
    public int getResultAsOneNumber(ResultSet results) {

        QuerySolution solution = results.nextSolution();
        String result = solution.toString();
        return Integer.parseInt(result.replaceAll("\\D+", ""));
    }

    /**
     * get result from query execution in the form of map of string and integer
     * @param results resultSet for processing
     * @return result from query execution in the form of map of string and integer
     */
    public Map<String, Integer> getResultAsMapStringInteger(ResultSet results) {
        Map<String, Integer> map = new HashMap<>();
        while (results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            RDFNode predicate = solution.get("p");
            RDFNode count = solution.get("n");
            map.put(predicate.toString(), ((LiteralImpl) count).getInt());
        }
        return map;
    }

    /**
     * get result from query execution in the form of map of string and string
     * @param results resultSet for processing
     * @param value1 subject of triple
     * @param value2 object of triple
     * @return result from query execution in the form of map of string and string
     */
    public Map<String, String> getResultAsMapStringString(ResultSet results, String value1, String value2) {
        Map<String, String> map = new HashMap<>();
        while (results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            RDFNode subject = solution.get(value1);
            RDFNode object = solution.get(value2);
            map.put(subject.toString(), object.toString());
        }
        return map;
    }

}
