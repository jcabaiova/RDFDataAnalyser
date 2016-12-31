package cz.cvut.fit.cabaijan.dbMethods;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import cz.cvut.fit.cabaijan.HelpfulMethods;
import cz.cvut.fit.cabaijan.calculationAnalysis.Calculation;
import cz.cvut.fit.cabaijan.calculationAnalysis.CalculationHdt;
import cz.cvut.fit.cabaijan.calculationAnalysis.CalculationSparql;
import cz.cvut.fit.cabaijan.charts.objects.DatasetAnalysisInChart;
import cz.cvut.fit.cabaijan.charts.objects.DomainInChart;
import cz.cvut.fit.cabaijan.logs.Logger;
import cz.cvut.fit.cabaijan.dbObjects.*;
import cz.cvut.fit.cabaijan.export.objectToExport.AnalysisToExport;
import cz.cvut.fit.cabaijan.export.objectToExport.DomainsToExport;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;


/**
 * Created by Janka on 9/10/2016.
 * DbAccess class uses for access to database, getting, creates, updates and deletes database object
 */
public class DbAccess {
    /**
     * url for defining database connection
     */
    private String URL;
    /**
     * created connection to database
     */
    private ConnectionSource connectionSource;
    /**
     * DAO object for handling domains in database
     */
    public Dao<Domain, Integer> domainDao;
    /**
     DAO object for handling entity groups in database
     */
    private Dao<EntityGroup, Integer> entityGroupsDao;
    /**
     * DAO object for handling entities in database
     */
    public Dao<Entity, Integer> entityDao;
    /**
     * DAO object for handling datasets in database
     */
    private Dao<Dataset, Integer> datasetDao;
    /**
     * DAO object for handling dataset analysis in database
     */
    public Dao<DatasetAnalysis, Integer> datasetAnalysisDao;
    /**
     * DAO object for handling domains in dataset in database
     */
    public Dao<DomainInDataset, Integer> domainInDatasetDao;
    /**
     * DAO object for handling entities in dataset in database
     */
    public Dao<EntityInDataset, Integer> entityInDatasetDao;
    /**
     * DAO object for handling predicates in database
     */
    public Dao<Predicate, Integer> predicateDao;

    /**
     * This is a constructor to initialize DbAccess object.
     */
    public DbAccess() {

        try {
            createDatabase();
           // this.URL = "jdbc:sqlite:C:\\Users\\Janka\\Diplomka\\RDFDataAnalyser\\db\\rdfDataTest.db";
            this.URL = "jdbc:sqlite:"+this.getClass().getResource("/db/testToDelete.db").getFile().toString();
            this.connectionSource = createConnection();
            createTables();
            this.domainDao = DaoManager.createDao(connectionSource, Domain.class);
            this.entityGroupsDao = DaoManager.createDao(connectionSource, EntityGroup.class);
            this.entityDao = DaoManager.createDao(connectionSource, Entity.class);
            this.datasetDao = DaoManager.createDao(connectionSource, Dataset.class);
            this.datasetAnalysisDao = DaoManager.createDao(connectionSource, DatasetAnalysis.class);
            this.entityInDatasetDao = DaoManager.createDao(connectionSource, EntityInDataset.class);
            this.domainInDatasetDao = DaoManager.createDao(connectionSource, DomainInDataset.class);
            this.predicateDao = DaoManager.createDao(connectionSource, Predicate.class);
        } catch (SQLException e) {
            Logger.writeException(e);
        }
    }

    /**
     * create JDBC connection to database
     * @return connection source of database
     */
    private ConnectionSource createConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return new JdbcConnectionSource(URL);
        } catch (ClassNotFoundException | SQLException e) {
            Logger.writeException(e);
        }
        return null;
    }

    /**
     * create tables to database if they do not exists
     */
    private void createTables() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Domain.class);
            TableUtils.createTableIfNotExists(connectionSource, EntityGroup.class);
            TableUtils.createTableIfNotExists(connectionSource, Entity.class);
            TableUtils.createTableIfNotExists(connectionSource, Dataset.class);
            TableUtils.createTableIfNotExists(connectionSource, DatasetAnalysis.class);
            TableUtils.createTableIfNotExists(connectionSource, EntityInDataset.class);
            TableUtils.createTableIfNotExists(connectionSource, DomainInDataset.class);
            TableUtils.createTableIfNotExists(connectionSource, Predicate.class);

        } catch (SQLException e) {
            Logger.writeException(e);
        }
    }

    /**
     * create database if it do not exist
     */
    private void createDatabase() {

        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection(this.URL);
        } catch (ClassNotFoundException | SQLException e) {
            Logger.writeException(e);
        }
    }

    /**
     * create new domain to database
     * @param domain domain to store to database
     * @return domain stored in database
     */
    public Domain createDomain(Domain domain) {
        try {
            Domain domainToReturn = domain;
            domainDao.create(domainToReturn);
            return domainToReturn;
        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * create new entity to database
     * @param entity entity to store to database
     * @param entityGroup_id id of entityGroup to which entity should belong
     * @param domain_id id of domain to which entity should belong
     * @return entity stored in database
     */
    public Entity createEntity(Entity entity, Integer entityGroup_id, Integer domain_id) {
        try {
            if (domain_id == 0) {
                return null;
            }

            EntityGroup entityGroup;
            entity.setName(entity.getPath());
            entity.setDomain(domainDao.queryForId(domain_id));

            if (entityGroup_id == 0) {
                entityGroup = new EntityGroup();
                entityGroup.setName(entity.getName());
                entityGroupsDao.create(entityGroup);
            } else {
                entityGroup = entityGroupsDao.queryForId(entityGroup_id);
                entityGroup.setName(entity.getName());
                entityGroupsDao.update(entityGroup);
            }

            entity.setEntityGroup(entityGroup);
            entityDao.create(entity);
            return entity;

        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * create and update entityGroup on the base of imported entities
     * @param entity1 entity1 in triple according to which entity group will be created
     * @param entity2 entity2 in triple according to which entity group will be updated
     * @return updated entityGroup on the base of imported entities
     */
    public EntityGroup createEntityGroupForImport(Entity entity1, Entity entity2) {
        try {
            EntityGroup entityGroup = createEntityGroup(entity1);
            updateEntityGroupInEntity(entity2.getId(), entityGroup.getId());
            return entityGroup;
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * create new entity group to database
     * @param entity1 entity which belongs to entity group
     * @return stored new entity group
     */
    public EntityGroup createEntityGroup(Entity entity1) {
        try {
            EntityGroup entityGroup = new EntityGroup();
            entityGroup.setName(entity1.getName());
            entityGroupsDao.create(entityGroup);
            return entityGroup;
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * create new entity to database
     * @param entity entity to store to database
     * @param domain_id id of domain to which entity should belong
     * @return stored new entity
     */
    public Entity createEntity(Entity entity, Integer domain_id) {
        try {
            if (domain_id == 0) {
                return null;
            }
            entity.setName(entity.getPath());
            entity.setDomain(domainDao.queryForId(domain_id));
            entity.setEntityGroup(null);
            entityDao.create(entity);
            return entity;
        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * create new dataset to database
     * @param dataset dataset to store to database
     * @param inputStream input stream of dataset file, only in case of HDT dataset
     * @param shortCalculation identification, which calculation will be running with dataset creation
     * @return stored dataset
     */
    public Dataset createDataset(Dataset dataset, InputStream inputStream, Boolean shortCalculation) {
        try {
            dataset.setFileType(dataset.getFileName());
            if (dataset.getSparqlEndpoint()) {
                if (HelpfulMethods.checkUrl(dataset.getFileName()) != 200) {
                    Logger.writeLog("Bad URL");
                    return null;
                }
            } else if (dataset.getHdt()) {
                HelpfulMethods.makeFileFromInputStream(inputStream, this.getClass().getResource("/importedDatasets/").getFile().toString() + dataset.getFileName());
            }
            datasetDao.create(dataset);
            DatasetAnalysis datasetAnalysis = new DatasetAnalysis();
            datasetAnalysis.setName(dataset.getName() + "_default");
            datasetAnalysis.setDescription("Default dataset analysis created with importing dataset, contains all domains");
            datasetAnalysis.setShortCalculationWithouPredicates(shortCalculation);
            createDatasetAnalysis(dataset.getId(), datasetAnalysis, domainDao.queryForAll());
            return dataset;


        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }

    }

    /**
     * calculate new dataset analysis
     * @param dataset_id id of dataset to which dataset analysis belongs
     * @param datasetAnalysis dataset analysis to be stored in database
     * @param domainList list of domains according to which the analysis will be calculated
     * @return true, if the calculation of dataset analysis was successful
     */
    public boolean createDatasetAnalysis(int dataset_id, DatasetAnalysis datasetAnalysis, List<Domain> domainList) {

        Calculation c;
        Dataset dataset = getDataset(dataset_id);
        if (domainList == null) {
            domainList = makeDomainListForAnalysisFromInput(datasetAnalysis);
        }
        if (dataset != null) {
            if (dataset.getSparqlEndpoint()) {
                c = new CalculationSparql(datasetAnalysis, dataset);
                return c.createDbObjects(domainList);
            } else if (dataset.getHdt()) {
                c = new CalculationHdt(datasetAnalysis, dataset);
                return c.createDbObjects(domainList);

            } else return false;
        } else return false;
    }

    /**
     * create new dataset analysis to database without the calculation.
     * @param datasetAnalysis dataset analysis to be stored in database
     * @param dataset dataset to which dataset analysis belongs
     * @return stored dataset analysis
     */
    public DatasetAnalysis createDatasetAnalysis(DatasetAnalysis datasetAnalysis, Dataset dataset) {

        try {
            datasetAnalysis.setDataset(dataset);
            datasetAnalysisDao.create(datasetAnalysis);
            return datasetAnalysis;
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * create new domain in dataset to database
     * @param datasetAnalysis dataset analysis to which domain in dataset belongs
     * @param domain domain to which domain in dataset belongs
     * @return stored domain in dataset
     */
    public DomainInDataset createDomainInDataset(DatasetAnalysis datasetAnalysis, Domain domain) {
        try {
            DomainInDataset domainInDataset = new DomainInDataset();
            domainInDataset.setDatasetAnalysis(datasetAnalysis);
            domainInDataset.setDomain(domain);
            domainInDatasetDao.create(domainInDataset);
            return domainInDataset;
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }

    }

    /**
     * create new entity in dataset to database
     * @param datasetAnalysis dataset analysis to which entity in dataset belongs
     * @param entity entity to which entity in dataset belongs
     * @return stored entity in dataset
     */
    public EntityInDataset createEntityInDataset(DatasetAnalysis datasetAnalysis, Entity entity) {
        try {
            EntityInDataset entityInDataset = new EntityInDataset();
            entityInDataset.setDatasetAnalysis(datasetAnalysis);
            entityInDataset.setEntity(entity);
            DB.lock();
            entityInDatasetDao.create(entityInDataset);
            DB.unlock();
            return entityInDataset;

        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        } finally {
            DB.unlock();
        }
    }

    /**
     * make list of domains for the calculation from input string
     * @param datasetAnalysis datasetAnalysis of the calculation
     * @return list of domains which will be used for the calculation of particular dataset analysis
     */
    private List<Domain> makeDomainListForAnalysisFromInput(DatasetAnalysis datasetAnalysis) {
        List<Domain> list = new ArrayList<>();
        try {
            String[] domains = datasetAnalysis.getDomainsFromInput().split(";");
            for (String domain : domains) {
                Domain domainToList = domainDao.queryForEq("path", domain).get(0);
                list.add(domainToList);
            }
            return list;
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * update name of domain in database
     * @param domainId id of domain to update
     * @param domainName a new name of domain
     * @return true, if the update was successful
     */
    public boolean updateNameOfDomain(int domainId, String domainName) {
        try {
            Domain domain = domainDao.queryForId(domainId);
            domain.setName(domainName);
            domainDao.update(domain);
            return true;
        } catch (SQLException e) {
            Logger.writeException(e);
            return false;
        }
    }

    /**
     * update domain in database
     * @param domainId id of domain to update
     * @param domain a new domain according to which old domain will be updated
     * @return updated domain from database
     */
    public Domain updateDomain(int domainId, Domain domain) {
        try {
            Domain domainToUpdate = domainDao.queryForId(domainId);
            domainToUpdate.setName(domain.getName());
            domainToUpdate.setPath(domain.getPath());
            domainDao.update(domainToUpdate);
            return domainToUpdate;
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * update path of entity in database
     * @param entityId id of entity to update
     * @param entityPath a new path of entity
     * @return true, if the update was successful
     */
    public boolean updatePathOfEntity(int entityId, String entityPath) {
        try {
            Entity entity = entityDao.queryForId(entityId);
            entity.setPath(entityPath);
            entity.setName(entityPath);
            entityDao.update(entity);
            //update dataset analysis data for chart
            return true;
        } catch (SQLException e) {
            Logger.writeException(e);
            return false;
        }
    }

    /**
     * update entity group in entity in database
     * @param entityId id of entity to update
     * @param entityGroupId id of entityGroup to which the entity belongs
     * @return true, if the update was successful
     */
    public boolean updateEntityGroupInEntity(int entityId, int entityGroupId) {
        try {
            Entity entity = entityDao.queryForId(entityId);
            entity.setEntityGroup(entityGroupsDao.queryForId(entityGroupId));
            entityDao.update(entity);
            return true;
        } catch (SQLException e) {
            Logger.writeException(e);
            return false;
        }
    }

    /**
     * update name of entity group in database
     * @param entityGroupId id of entity group to be updated
     * @param name a new name of entity group
     * @return true, if the update was successful
     */
    public boolean updateNameOfEntityGroup(int entityGroupId, String name) {
        try {

            EntityGroup entityGroup = entityGroupsDao.queryForId(entityGroupId);
            entityGroup.setName(name);
            entityGroupsDao.update(entityGroup);
            return true;
        } catch (SQLException e) {
            Logger.writeException(e);
            return false;
        }
    }

    /**
     * update dataset in database
     * @param datasetId id of dataset to be updated
     * @param dataset a new dataset according to which an old dataset will be updated
     * @return updated dataset from the database
     */
    public Dataset updateDataset(int datasetId, Dataset dataset) {
        try {
            Dataset datasetToUpdate = datasetDao.queryForId(datasetId);
            datasetToUpdate.setName(dataset.getName());
            datasetToUpdate.setDescription(dataset.getDescription());
            datasetToUpdate.setOntologyPredicate(dataset.getOntologyPredicate());

            datasetDao.update(datasetToUpdate);
            return datasetToUpdate;
        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * update dataset analysis in database
     * @param datasetAnalysisId id of dataset analysis to be updated
     * @param datasetAnalysis a new dataset analysis according to which an old dataset analysis will be updated
     * @return updated dataset analysis from the database
     */
    public DatasetAnalysis updateDatasetAnalysis(int datasetAnalysisId, DatasetAnalysis datasetAnalysis) {
        try {
            DatasetAnalysis datasetAnalysisToUpdate = datasetAnalysisDao.queryForId(datasetAnalysisId);
            datasetAnalysisToUpdate.setName(datasetAnalysis.getName());
            datasetAnalysisToUpdate.setDescription(datasetAnalysis.getDescription());

            datasetAnalysisDao.update(datasetAnalysisToUpdate);
            return datasetAnalysisToUpdate;
        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * delete domain together with entities which belong to it from database.
     * @param domainId id of domain to be deleted
     * @return true, if the update was successful
     */
    public boolean deleteDomain(int domainId) {
        try {
            Domain domain = domainDao.queryForId(domainId);
            List<Entity> entityList = entityDao.queryForEq("domain_id", domainId);
            for (int i = 0; i < entityList.size(); i++) {

                EntityGroup entityGroup = entityGroupsDao.queryForId(entityList.get(i).getEntityGroup().getId());
                if (entityGroup != null) {
                    entityGroupsDao.delete(entityGroup);
                }

            }
            entityDao.delete(entityList);
            domainDao.delete(domain);
            return true;
        } catch (SQLException e) {
            Logger.writeException(e);
            return false;
        }
    }

    /**
     * delete dataset together with dataset analysis which belong to it from database.
     * @param datasetId id of dataset to be deleted
     * @return true, if the update was successful
     */
    public boolean deleteDataset(int datasetId) {
        try {
            Dataset dataset = datasetDao.queryForId(datasetId);
            List<DatasetAnalysis> datasetAnalysisList = datasetAnalysisDao.queryForEq("dataset_id", dataset.getId());
            for (DatasetAnalysis da : datasetAnalysisList) {
                deleteDatasetAnalysis(da.getId());
            }

            if (dataset.getHdt()) {
                HelpfulMethods.deleteTmpFile("C:\\Users\\Janka\\Diplomka\\RDFDataAnalyser\\importedDatasets\\" + dataset.getFileName());
            }
            datasetDao.delete(dataset);
            return true;
        } catch (SQLException e) {
            Logger.writeException(e);
            return false;
        }
    }

    /**
     * delete dataset analysis together with domain and entities in dataset analysis from database.
     * @param datasetAnalysisId id of dataset analysis to be deleted
     * @return true, if the update was successful
     */
    public boolean deleteDatasetAnalysis(int datasetAnalysisId) {
        try {
            DatasetAnalysis datasetAnalysis = datasetAnalysisDao.queryForId(datasetAnalysisId);
            List<DomainInDataset> domainInDatasets = domainInDatasetDao.queryForEq("datasetAnalysis_id", datasetAnalysis.getId());
            domainInDatasetDao.delete(domainInDatasets);
            List<EntityInDataset> entityInDatasets = entityInDatasetDao.queryForEq("datasetAnalysis_id", datasetAnalysis.getId());
            for (EntityInDataset entityInDataset : entityInDatasets) {
                List<Predicate> predicatesList = predicateDao.queryForEq("entityInDataset_id", entityInDataset.getId());
                predicateDao.delete(predicatesList);
            }
            entityInDatasetDao.delete(entityInDatasets);
            datasetAnalysisDao.delete(datasetAnalysis);
            return true;
        } catch (SQLException e) {
            Logger.writeException(e);
            return false;
        }
    }

    /**
     * delete entity from database.
     * @param entityId id of entity to be deleted
     * @return true, if the update was successful
     */
    public boolean deleteEntity(int entityId) {
        try {

            Entity entity = entityDao.queryForId(entityId);
            List<Entity> entityList = entityDao.queryForAll();
            int count = 0;
            for (Entity entity1 : entityList) {
               // List<EntityInDataset> entityInDataset = entityInDatasetDao.queryForEq("entity_id", entity1.getId());
               // entityInDatasetDao.delete(entityInDataset);
                if (entity1.getEntityGroup().getId() == entity.getEntityGroup().getId()) {
                    count++;
                }
            }
            if (count <= 1) {
                EntityGroup entityGroup = entityGroupsDao.queryForId(entity.getEntityGroup().getId());
                entityGroupsDao.delete(entityGroup);
            }
            entityDao.delete(entity);
            return true;
        } catch (SQLException e) {
            Logger.writeException(e);
            return false;
        }
    }

    /**
     * get domain from the database according to its id
     * @param domain_id id of domain to be get
     * @return domain from database
     */
    public Domain getDomain(Integer domain_id) {
        try {
            return domainDao.queryForId(domain_id);

        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }

    }

    /**
     * get domain from the database according to its name
     * @param name name of domain to be get
     * @return domain from database
     */
    public Domain getDomain(String name) {
        try {
            return domainDao.queryForEq("name", name).get(0);

        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get all domains from the database
     * @return list of domains from the database
     */
    public List<Domain> getAllDomains() {

        try {
            return domainDao.queryForAll();
        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get entities belongs to entity group
     * @param entityGroup_id id of entity group according to which list of entities will be get
     * @return list of entities belongs to partciular entity group from the database
     */
    public List<Entity> getEntities(int entityGroup_id) {
        try {
            return entityDao.queryForEq("entityGroup_id", entityGroup_id);
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get entity from the database according to its path
     * @param path path of entity according to which tha particular entity will be get
     * @return entity from the database
     */
    public Entity getEntity(String path) {
        try {
            return entityDao.queryForEq("path", path).get(0);
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get all entities from the database
     * @return list of all entities from the database
     */
    public List<Entity> getAllEntities() {
        try {
            return entityDao.queryForAll();
        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get all entity groups from the database
     * @return list of all entity groups from the database
     */
    public List<EntityGroup> getAllEntitiesGroup() {
        try {
            return entityGroupsDao.queryForAll();
        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get dataset from database according to ist name
     * @param name name of dataset according to which the particular dataset will be get
     * @return dataset from database
     */
    public Dataset getDataset(String name) {
        try {
            return datasetDao.queryForEq("name", name).get(0);

        } catch (SQLException e) {
            Logger.writeException(e);
        }
        return null;
    }

    /**
     * get dataset from database according to ist id
     * @param id id of dataset according to which the particular dataset will be get
     * @return dataset from database
     */
    private Dataset getDataset(int id) {
        try {
            return datasetDao.queryForId(id);

        } catch (SQLException e) {
            Logger.writeException(e);
        }
        return null;
    }

    /**
     * get all datasets from database
     * @return list of all datasets from database
     */
    public List<Dataset> getAllDatasets() {

        try {
            return datasetDao.queryForAll();
        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get dataset analysis in the particular dataset
     * @param datasetId id of dataset according to which dataset analysis will be get
     * @return list of dataset analysis belong to the particular dataset
     */
    public List<DatasetAnalysis> getDatasetsAnalysis(int datasetId) {
        try {
            return datasetAnalysisDao.queryForEq("dataset_id", datasetId);
        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get all dataset analysis from database
     * @return list of all dataset analysis
     */
    public List<DatasetAnalysis> getAllDatasetsAnalysis() {
        try {
            return datasetAnalysisDao.queryForAll();
        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get all entities in dataset belongs to the particular dataset analysis
     * @param datasetAnalysisId id of dataset analysis according to which entities in dataset will be get
     * @return list of entites in dataset belongs to the particular dataset analysis
     */
    public List<EntityInDataset> getEntitiesInDatasetAnalysis(int datasetAnalysisId) {
        try {
            return entityInDatasetDao.queryForEq("datasetAnalysis_id", datasetAnalysisId);
        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get entities in dataset belongs to the particular dataset analysis ane to the particular domain in dataset
     * @param datasetAnalysisId id of dataset analysis according to which entities in dataset will be get
     * @param domainInDatasetId id of domain in dataset according to which entities in dataset will be get
     * @return list of entites in dataset belongs to the particular dataset analysis and to the particular domain in dataset
     */
    public List<EntityInDataset> getEntitiesInDatasetAnalysisInDomain(int datasetAnalysisId, int domainInDatasetId) {
        try {
            List<EntityInDataset> list = new ArrayList<>();
            List<EntityInDataset> entityInDatasetList = entityInDatasetDao.queryForEq("datasetAnalysis_id", datasetAnalysisId);
            DomainInDataset domainInDataset = domainInDatasetDao.queryForId(domainInDatasetId);
            List<Entity> entities = entityDao.queryForEq("domain_id", domainInDataset.getDomain().getId());
            for (Entity entity : entities) {
                for (EntityInDataset entityInDataset : entityInDatasetList) {
                    if (entity.getId() == entityInDataset.getEntity().getId()) {
                        list.add(entityInDataset);
                    }
                }
            }
            Collections.sort(list);
            return list;

        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get all domains in dataset belongs to the particular dataset analysis
     * @param datasetAnalysisId id of dataset analysis according to which domains in dataset will be get
     * @return list of domains in dataset belongs to the particular dataset analysis
     */
    public List<DomainInDataset> getDomainsInDatasetAnalysis(int datasetAnalysisId) {
        try {
            return domainInDatasetDao.queryForEq("datasetAnalysis_id", datasetAnalysisId);
        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get all predicates belong to the particular entity in dataset
     * @param entityId id of entity in dataset according to which predicates will be get
     * @return
     */
    public List<Predicate> getPredicatesInEntity(int entityId) {
        try {
            List<Predicate> list = predicateDao.queryForEq("entityInDataset_id", entityId);
            Collections.sort(list);
            return list;

        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get entities belong to the particular domain
     * @param domain domain according to which entities will be get
     * @return list of entities which belong to the particular domain
     */
    public List<Entity> getEntitiesInDomain(Domain domain) {
        try {
            return entityDao.queryForEq("domain_id", domain.getId());
        } catch (SQLException e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get all entities in dataset belongs to the particular dataset analysis
     * @param datasetAnalysis dataset analysis according to which entities in dataset will be get
     * @return list of entites in dataset belongs to the particular dataset analysis
     */
    public List<EntityInDataset> getEntitiesInDatasetAnalysis(DatasetAnalysis datasetAnalysis) {
        try {
            return entityInDatasetDao.queryForEq("datasetAnalysis_id", datasetAnalysis.getId());
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get all domains in dataset belongs to the particular dataset analysis
     * @param datasetAnalysis dataset analysis according to which domains in dataset will be get
     * @return list of domains in dataset belongs to the particular dataset analysis
     */
    public List<DomainInDataset> getDomainsInDatasetAnalysis(DatasetAnalysis datasetAnalysis) {
        try {
            return domainInDatasetDao.queryForEq("datasetAnalysis_id", datasetAnalysis.getId());
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get all domains to export
     * @return list of all domains to export
     */
    public List<DomainsToExport> getDomainsToExport() {
        try {
            List<DomainsToExport> domainsToExports = new ArrayList<>();
            List<Entity> entityList = entityDao.queryForAll();
            for (Entity e : entityList) {
                Domain domain = domainDao.queryForId(e.getDomain().getId());
                EntityGroup entityGroup = entityGroupsDao.queryForId(e.getEntityGroup().getId());
                domainsToExports.add(new DomainsToExport(e.getDomain().getId(), domain.getName(), e.getId(), e.getPath(), e.getEntityGroup().getId(), entityGroup.getName()));

            }
            return domainsToExports;
        } catch (Exception e) {
            Logger.writeException(e);
        }
        return null;
    }

    /**
     * get all dataset analysis objects to export belong to the particular dataset analysis
     * @param datasetAnalysis datasetAnalysis for which the export is running
     * @return list of all dataset analysis objects to export belong to the particular dataset analysis
     */
    public List<AnalysisToExport> getAnalysisToExport(DatasetAnalysis datasetAnalysis) {
        try {
            List<AnalysisToExport> analysisToExports = new ArrayList<>();
            List<EntityInDataset> entityInDatasetList = entityInDatasetDao.queryForEq("datasetAnalysis_id", datasetAnalysis.getId());
            List<DomainInDataset> domainInDatasetList = domainInDatasetDao.queryForEq("datasetAnalysis_id", datasetAnalysis.getId());
            for (DomainInDataset d : domainInDatasetList) {
                Domain domain = domainDao.queryForId(d.getDomain().getId());
                for (EntityInDataset e : entityInDatasetList) {
                    if (getDomainIdFromEntityInDataset(e) == domain.getId()) {
                        Entity entity = entityDao.queryForId(e.getEntity().getId());
                        List<Predicate> predicates = predicateDao.queryForEq("entityInDataset_id", e.getId());
                        if (predicates.size() == 0) {
                            analysisToExports.add(new AnalysisToExport(domain.getId(), domain.getName(), d.getCountDomain(), entity.getId(), entity.getPath(), e.getCountEntities(), 0, "", 0));

                        } else {
                            for (Predicate p : predicates) {
                                analysisToExports.add(new AnalysisToExport(domain.getId(), domain.getName(), d.getCountDomain(), entity.getId(), entity.getPath(), e.getCountEntities(), p.getId(), p.getName(), p.getCountPredicates()));
                            }
                        }
                    }

                }

            }
            return analysisToExports;
        } catch (Exception e) {
            Logger.writeException(e);
        }
        return null;
    }

    /**
     * get entity groups of entities belong to the particular domain
     * @param domainId id of domain according to which list of entity groups will be get
     * @return
     */
    public List<EntityGroup> getEntitiesGroupInDomain(int domainId) {
        try {
            List<EntityGroup> entityGroupList = new ArrayList<>();
            List<Entity> entityList = entityDao.queryForEq("domain_id", domainId);
            for (Entity entity : entityList) {
                EntityGroup entityGroup = entityGroupsDao.queryForId(entity.getEntityGroup().getId());
                Boolean addEntityGroup = true;
                for (EntityGroup eg : entityGroupList) {
                    if (eg.getId() == entityGroup.getId()) {
                        addEntityGroup = false;
                        break;
                    }
                }
                if (addEntityGroup) {
                    entityGroupList.add(entityGroup);
                }
            }
            return entityGroupList;

        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get object for drawing domain chart
     * @return list of objects for drawing domain chart
     */
    public List<DomainInChart> getEntityNumberInDomains() {
        try {
            List<DomainInChart> domainInCharts = new ArrayList<>();
            List<Domain> domainList = domainDao.queryForAll();
            for (Domain domain : domainList) {
                List<Entity> entityList = entityDao.queryForEq("domain_id", domain.getId());
                domainInCharts.add(new DomainInChart(domain.getName(), entityList.size()));

            }
            return domainInCharts;
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get domain from the database according to its path
     * @param domainPath path of domain to be get
     * @return domain from database
     */
    public Domain getDomainAccordingPath(String domainPath) {
        try {
            List<Domain> domains = domainDao.queryForEq("path", domainPath);
            return domains.get(0);
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get entites which have null entity group. These entities can be created during the import
     * @return list of entities with null entity group
     */
    public List<Entity> getEntitiesWitNullEntityGroup() {
        try {
            List<Entity> entities = entityDao.queryForAll();
            List<Entity> entitiesToReturn = new ArrayList<>();

            for (int i = 0; i < entities.size(); i++) {
                EntityGroup entityGroup = entityGroupsDao.queryForId(entities.get(i).getEntityGroup().getId());
                if (entityGroup == null) {
                    entitiesToReturn.add(entities.get(i));
                }
            }
            return entitiesToReturn;
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * get id of domain to which entity in dataset belongs
     * @param entityInDataset entity in dataset for which domain is to be get
     * @return id of domain to which entity in dataset belongs
     */
    private int getDomainIdFromEntityInDataset(EntityInDataset entityInDataset) {
        try {
            Entity entity = entityDao.queryForId(entityInDataset.getEntity().getId());
            return entity.getDomain().getId();
        } catch (Exception e) {
            Logger.writeException(e);
            return 0;
        }
    }

    /**
     * get data for drawing the chart of the particular dataset analysis
     * @param datasetAnalysisId id of dataset analysis for which the chart will be drawn
     * @return object DatasetAnalysisInChart
     */
    public DatasetAnalysisInChart getDataForDatasetAnalysisChart(int datasetAnalysisId) {
        try {

            DatasetAnalysisInChart initialObject = new DatasetAnalysisInChart();
            initialObject.setName("root");
            List<Domain> domainList = domainDao.queryForAll();
            for (Domain domain : domainList) {
                DatasetAnalysisInChart domainInChart = new DatasetAnalysisInChart();
                domainInChart.setName(domain.getName());
                List<Entity> entityList = entityDao.queryForEq("domain_id", domain.getId());
                for (Entity entity : entityList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("entity_id",entity.getId());
                    map.put("datasetAnalysis_id",datasetAnalysisId);
                    List<EntityInDataset> list = entityInDatasetDao.queryForFieldValues(map);
                    if (list.size()==0 ) {
                        continue;
                    }else {
                        EntityInDataset entityInDataset = list.get(0);
                        DatasetAnalysisInChart entityInChart = new DatasetAnalysisInChart();
                        entityInChart.setName(entity.getPath());
                        entityInChart.setSize(entityInDataset.getCountEntities());
                        domainInChart.addToChildren(entityInChart);
                        domainInChart.addToSize(entityInChart.getSize());
                    }
                }
                initialObject.addToChildren(domainInChart);
                initialObject.addToSize(domainInChart.getSize());
            }

            return initialObject;
        }catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }

}






