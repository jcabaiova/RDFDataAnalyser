package cz.cvut.fit.cabaijan.calculationAnalysis;

import cz.cvut.fit.cabaijan.dbMethods.DB;
import cz.cvut.fit.cabaijan.logs.Logger;
import cz.cvut.fit.cabaijan.dbObjects.*;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Janka on 9/13/2016.
 * Calculation class uses as the main class for calculation of dataset analysis. It is an abstract class.
 * It creates also object connected with dataset analysis, which are DatasetAnalysis, DomainInDataset, EntityInDataset, Predicate
 */
public abstract class Calculation {
    /**
     * hdtmodel of calculation, it is filled out only in case of dataset in HDT format
     */
    protected HdtModel hdtModel = null;
    /**
     * sparqlMethods of calculation for handling queries
     */
    protected SparqlMethods sparqlMethods;
    /**
     * datasetAnalysis which is used for calculation
     */
    protected DatasetAnalysis datasetAnalysis;
    /**
     * dataset, on which is calculation running
     */
    protected Dataset dataset;

    /**
     * method for final calculation of entityCount and domainCount in datasetAnalysis
     * @param datasetAnalysis datasetAnalysis for setting calculated variables
     * @return true if the calculation was successful
     */
    private boolean calculateTotalCountInDatasetAnalysis(DatasetAnalysis datasetAnalysis) {
        List<EntityInDataset> entityInDatasetList = DB.getDbAccess().getEntitiesInDatasetAnalysis(datasetAnalysis);
        List<DomainInDataset> domainInDatasetList = DB.getDbAccess().getDomainsInDatasetAnalysis(datasetAnalysis);
        if (entityInDatasetList == null || domainInDatasetList == null) {
            return false;
        } else {
            int totalEntityCountInDatasetAnalysis = 0;
            for (int i = 0; i < entityInDatasetList.size(); i++) {
                totalEntityCountInDatasetAnalysis += entityInDatasetList.get(i).getCountEntities();
            }
            datasetAnalysis.setEntityCount(totalEntityCountInDatasetAnalysis);
            datasetAnalysis.setDomainCount(domainInDatasetList.size());
            try {
                DB.getDbAccess().datasetAnalysisDao.update(datasetAnalysis);
                return true;
            } catch (Exception e) {
                Logger.writeException(e);
                return false;
            }
        }
    }

    /**
     * Main method for calculation of analysis. It contains creation of db objects, creation threds and running sparql queries.
     * @param domainList list of domains on which the analysis should be calculated
     * @return true, if the calculation was successful
     */
    public boolean createDbObjects(List<Domain> domainList) {
        try {
            DatasetAnalysis datasetAnalysisCreated = datasetAnalysis;
            DB.getDbAccess().createDatasetAnalysis(datasetAnalysisCreated, dataset);
            if (datasetAnalysis == null) return false;
            ExecutorService executor = Executors.newFixedThreadPool(4);
            //empty domains
            if (domainList==null) return true;
            for (int i = 0; i < domainList.size(); i++) {
                int countInDomain = 0;
                DomainInDataset domainInDatasetCreated = DB.getDbAccess().createDomainInDataset(datasetAnalysis, domainList.get(i));
                if (domainInDatasetCreated == null) return false;
                List<Entity> entityList = DB.getDbAccess().getEntitiesInDomain(domainList.get(i));
                if (entityList == null) return false;

                Set<Future<Integer>> setCountEntities = new HashSet<>();
                Set<Future<List<Predicate>>> setCreatePredicates = new HashSet<>();

                for (int j = 0; j < entityList.size(); j++) {
                    EntityInDataset entityInDataset = DB.getDbAccess().createEntityInDataset(datasetAnalysis, entityList.get(j));
                    if (entityInDataset == null) return false;
                    Callable<Integer> callableCountEntities = new EntityCalculationCallable(sparqlMethods, entityInDataset, hdtModel.getModel(), dataset);
                    setCountEntities.add(executor.submit(callableCountEntities));
                    if (!datasetAnalysis.getShortCalculationWithouPredicates()) {
                        if (entityInDataset.getEntity().getPath().equals("http://www.geonames.org/ontology#P.PPL")) {
                            continue;
                        }
                        Callable<List<Predicate>> callableCreatePredicates = new CreatePredicatesCallable(sparqlMethods, entityInDataset, hdtModel.getModel(), dataset);
                        setCreatePredicates.add(executor.submit(callableCreatePredicates));
                    }

                }
                for (Future<Integer> future : setCountEntities) {
                    countInDomain += future.get();
                }

                for (Future<List<Predicate>> future : setCreatePredicates) {
                    List<Predicate> predicates = future.get();
                    if (predicates == null) {
                        continue;
                    }
                    for (int k = 0; k < predicates.size(); k++) {
                        DB.getDbAccess().predicateDao.create(predicates.get(k));
                    }
                }

                domainInDatasetCreated.setCountDomain(countInDomain);
                DB.getDbAccess().domainInDatasetDao.update(domainInDatasetCreated);
            }
            if (calculateTotalCountInDatasetAnalysis(datasetAnalysisCreated)) {
                DB.getDbAccess().getDataForDatasetAnalysisChart(datasetAnalysisCreated.getId());
                return true;
            } else return false;
        } catch (SQLException e) {
            Logger.writeException(e);
            return false;
        } catch (InterruptedException e) {
            Logger.writeException(e);
            return false;
        } catch (ExecutionException e) {
            Logger.writeException(e);
            return false;
        } finally {
            hdtModel.closeModel();
        }
    }
}


