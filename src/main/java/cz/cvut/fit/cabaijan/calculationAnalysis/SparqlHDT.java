package cz.cvut.fit.cabaijan.calculationAnalysis;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import cz.cvut.fit.cabaijan.dbObjects.DatasetAnalysis;

/**
 * Created by Janka on 10/13/2016.
 * SparqlHDT class extends class SparqlMethods and overrides method executeQuery
 */
public class SparqlHDT extends SparqlMethods {
    /**
     * execute query and get results
     * @param query query for execution
     * @param datasetAnalysis datasetAnalysis for which queries are executed
     * @param model model on which queries are executed
     * @return results from the query execution
     */
    @Override
    public ResultSet executeQuery(String query, DatasetAnalysis datasetAnalysis, Model model) {

        return executeQuery(query,model);

    }
}
