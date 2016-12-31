package cz.cvut.fit.cabaijan.calculationAnalysis;

import com.hp.hpl.jena.rdf.model.Model;
import cz.cvut.fit.cabaijan.dbObjects.Dataset;
import cz.cvut.fit.cabaijan.logs.Logger;
import cz.cvut.fit.cabaijan.dbObjects.EntityInDataset;
import cz.cvut.fit.cabaijan.dbObjects.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Janka on 9/27/2016.
 * CreatePredicatesCallable class implement class Callable.
 * It uses for specification of function which is providing in threads.
 */
public class CreatePredicatesCallable implements Callable {
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
     * This is a parametrized constructor to initialize CreatePredicatesCallable object.
     * @param sparqlMethods sparqlMethods of calculation for handling DB objects
     * @param entityInDataset entityInDataset which is used for calculation of predicates
     * @param model model, which is used for calculation of predicates.
     * @param dataset dataset for which predicates are calculated
     */
    public CreatePredicatesCallable(SparqlMethods sparqlMethods, EntityInDataset entityInDataset, Model model, Dataset dataset) {
        this.entityInDataset = entityInDataset;
        this.sparqlMethods = sparqlMethods;
        this.model = model;
        this.dataset = dataset;
    }

    /**
     * this method is used for the calculation of occurrence of every predicate which is used with particular entity
     * @return list of predicates, which contains name and count of predicate.
     * @throws Exception
     */
    @Override
    public List<Predicate> call() throws Exception {
        try {
            Map<String, Integer> predicatesMap = sparqlMethods.getResultAsMapStringInteger(sparqlMethods.executeQuery(Queries.getQueryForCreationOfPredicates(entityInDataset.getEntity(), dataset.getOntologyPredicate()), entityInDataset.getDatasetAnalysis(), model));
            if (predicatesMap == null) {
                //if no predicates created
                return null;
            }
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : predicatesMap.entrySet()) {
                //Predicate predicate = DB.getDbAccess().createPredicate(entityInDataset,entry.getKey(),entry.getValue());
                Predicate predicate = new Predicate();
                predicate.setEntityInDataset(entityInDataset);
                predicate.setName(entry.getKey());
                predicate.setCountPredicates(entry.getValue());
                predicates.add(predicate);

            }
            return predicates;
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }

}
