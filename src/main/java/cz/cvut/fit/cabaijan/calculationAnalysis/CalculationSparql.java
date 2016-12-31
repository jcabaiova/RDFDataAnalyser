package cz.cvut.fit.cabaijan.calculationAnalysis;


import cz.cvut.fit.cabaijan.dbObjects.Dataset;
import cz.cvut.fit.cabaijan.dbObjects.DatasetAnalysis;
/**
 * Created by Janka on 9/13/2016.
 * CalculationSparql extends class Calculation. It is used only in case that dataset is Sparql endpoint.
 */
public class CalculationSparql extends Calculation {
    /**
     * This is a constructor to initialize CalculationSparql object.
     * @param datasetAnalysis datasetAnalysis for which calculation in running
     * @param dataset dataset of datasetAnalysis for which calculation in running
     */
    public CalculationSparql(DatasetAnalysis datasetAnalysis, Dataset dataset) {
        hdtModel = new HdtModel();
        sparqlMethods = new SparqlEndpoint();
        this.datasetAnalysis = datasetAnalysis;
        this.dataset = dataset;
    }
}