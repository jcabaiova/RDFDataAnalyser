package cz.cvut.fit.cabaijan.calculationAnalysis;

import com.hp.hpl.jena.rdf.model.Model;
import cz.cvut.fit.cabaijan.dbMethods.DB;
import cz.cvut.fit.cabaijan.dbObjects.Dataset;
import cz.cvut.fit.cabaijan.dbObjects.EntityInDataset;

import java.sql.SQLException;
import java.util.concurrent.Callable;

/**
 * Created by Janka on 9/27/2016.
 * EntityCalculationCallable class implements class Callable.
 * It uses for specification of calculated function which is providing in threads.
 */
public class EntityCalculationCallable implements Callable {
    /**
     * entityInDataset which is used for calculation of predicates
     */
    private final EntityInDataset entityInDataset;
    /**
     * sparqlMethods of calculation for handling DB objects
     */
    private final SparqlMethods sparqlMethods;
    /**
     * model, which is used for calculation of predicates.
     */
    private final Model model;
    /**
     * dataset for which predicates are calculated
     */
    private final Dataset dataset;

    /**
     * This is a parametrized constructor to initialize EntityCalculationCallable object.
     * @param sparqlMethods sparqlMethods of calculation for handling DB objects
     * @param entityInDataset entityInDataset which is used for calculation of predicates
     * @param model model, which is used for calculation of predicates.
     * @param dataset dataset for which predicates are calculated
     */
    public EntityCalculationCallable(SparqlMethods sparqlMethods, EntityInDataset entityInDataset, Model model, Dataset dataset) {
        this.entityInDataset = entityInDataset;
        this.sparqlMethods = sparqlMethods;
        this.model = model;
        this.dataset = dataset;
    }

    /**
     * this method is used for the calculation of occurrence of entity in particular dataset
     * @return count of triples
     * @throws Exception
     */
    @Override
    public Integer call() throws Exception {
        try {
            entityInDataset.setCountEntities(sparqlMethods.getResultAsOneNumber(sparqlMethods.executeQuery(Queries.getQueryForCountOfEntities(entityInDataset.getEntity(), dataset.getOntologyPredicate()), entityInDataset.getDatasetAnalysis(), model)));
            DB.getDbAccess().entityInDatasetDao.update(entityInDataset);
            return entityInDataset.getCountEntities();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
