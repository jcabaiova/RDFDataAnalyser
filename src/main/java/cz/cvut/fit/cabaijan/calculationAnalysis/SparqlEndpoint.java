package cz.cvut.fit.cabaijan.calculationAnalysis;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import cz.cvut.fit.cabaijan.dbObjects.DatasetAnalysis;
import cz.cvut.fit.cabaijan.logs.Logger;

/**
 * Created by Janka on 10/13/2016.
 * SparqlEndpoint class extends class SparqlMethods and overrides method executeQuery
 */
public class SparqlEndpoint extends SparqlMethods {
    /**
     * execute query and get results
     * @param query query for execution
     * @param datasetAnalysis datasetAnalysis for which queries are executed
     * @param model model on which queries are executed
     * @return results from the query execution
     */
    @Override
    public ResultSet executeQuery(String query, DatasetAnalysis datasetAnalysis, Model model) {
        ResultSet rs;
        try {
            Query q = QueryFactory.create(query, Syntax.syntaxARQ);
            QueryExecution qexec = QueryExecutionFactory.createServiceRequest(datasetAnalysis.getDataset().getFileName(), q);
            //qexec.setTimeout(1000000);
            rs = qexec.execSelect();
            return rs;
        } catch (Exception ex) {
            Logger.writeException(ex);
            return null;
        }


    }
}
