package cz.cvut.fit.cabaijan.calculationAnalysis;

import cz.cvut.fit.cabaijan.dbObjects.Dataset;
import cz.cvut.fit.cabaijan.dbObjects.DatasetAnalysis;
import cz.cvut.fit.cabaijan.dbObjects.Domain;

import java.util.List;

/**
 * Created by Janka on 9/13/2016.
 * CalculationHdt extends class Calculation. It overrides method createDbObject, because hdtmodel has to be initiated.
 * It is used only in case that dataset is in HDT format
 */
public class CalculationHdt extends Calculation {

    /**
     * This is a constructor to initialize CalculationHdt object.
     * @param datasetAnalysis datasetAnalysis for which calculation in running
     * @param dataset dataset of datasetAnalysis for which calculation in running
     */
    public CalculationHdt(DatasetAnalysis datasetAnalysis, Dataset dataset) {
        sparqlMethods=new SparqlHDT();
        this.datasetAnalysis = datasetAnalysis;
        this.dataset = dataset;
    }

    /**
     *
     * @param domainList list of domains on which the analysis should be calculated
     * @return true, if the calculation was successful
     */
    @Override
    public boolean createDbObjects(List<Domain> domainList) {
        hdtModel = new HdtModel();
        hdtModel.createModel(dataset);
        return super.createDbObjects(domainList);
    }
}
