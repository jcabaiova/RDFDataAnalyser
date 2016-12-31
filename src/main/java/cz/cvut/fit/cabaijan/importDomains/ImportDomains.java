package cz.cvut.fit.cabaijan.importDomains;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import cz.cvut.fit.cabaijan.calculationAnalysis.Queries;
import cz.cvut.fit.cabaijan.calculationAnalysis.SparqlEndpoint;
import cz.cvut.fit.cabaijan.calculationAnalysis.SparqlMethods;
import cz.cvut.fit.cabaijan.dbMethods.DbAccess;
import cz.cvut.fit.cabaijan.dbObjects.Domain;
import cz.cvut.fit.cabaijan.dbObjects.Entity;
import cz.cvut.fit.cabaijan.dbObjects.EntityGroup;
import cz.cvut.fit.cabaijan.logs.Logger;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Janka on 10/11/2016.
 * ImportDomains class uses for importing domains and entities from the file of the format N-Triples
 */
public class ImportDomains {
    /**
     * start name of tmp file imported from the user
     */
    private final String startNameOfTmpFile;
    /**
     * path to the tmp file imported from the user
     */
    private final String pathOfTmpFile;
    /**
     * string with the path of domains which were not created
     */
    private String domainFailedString;
    /**
     * string with the path of entities which were not created
     */
    private String entityFailedString;
    /**
     * sparqlMethods of calculation for handling queries
     */
    private final SparqlMethods sparqlMethods;
    /**
     * object DbAccess for getting access to database
     */
    private final DbAccess dbAccess;
    /**
     * model for the calculation
     */
    private final Model model;

    /**
     * This is a constructor to initialize ImportDomains object
     */
    public ImportDomains() {
        this.startNameOfTmpFile = "uploadDomains";
        this.pathOfTmpFile= "/tmpFiles";
        this.domainFailedString="";
        this.entityFailedString="";
        this.sparqlMethods = new SparqlEndpoint();
        this.dbAccess = new DbAccess();
        this.model = ModelFactory.createDefaultModel();
    }

    /**
     * method for creation domains from the import file
     */
    private void createDomains() {
        String query = Queries.getQueryForInitiateDomains();
        Map<String, String> map = sparqlMethods.getResultAsMapStringString(sparqlMethods.executeQuery(query,model),"subject","object");
        if (map!=null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Domain domain = new Domain(0, entry.getValue(), entry.getKey());
                if (dbAccess.createDomain(domain) == null) {
                    domainFailedString += entry.getValue() + "\n";
                    continue;
                }
            }
        }
    }

    /**
     * method for the creation entities from the import file
     */
    private void createEntities() {
        Map<String, String> mapEntities= sparqlMethods.getResultAsMapStringString(sparqlMethods.executeQuery(Queries.getQueryForInitiateEntities(),model),"subject","object");
        if (mapEntities!=null) {
            for (Map.Entry<String, String> entry : mapEntities.entrySet()) {
                Entity entity = new Entity();
                entity.setPath(entry.getKey());
                Domain domain = dbAccess.getDomainAccordingPath(entry.getValue());
                if (domain == null) {
                    entityFailedString += entry.getKey() + "\n";
                    continue;
                }
                if (dbAccess.createEntity(entity, domain.getId())==null) {
                    entityFailedString += entry.getKey() + "\n";
                    continue;
                }
            }
        }

    }

    /**
     * method for the creation links between entities from the import file
     */
    private void createLinks() {
        Map<String,String> mapLinking = sparqlMethods.getResultAsMapStringString(sparqlMethods.executeQuery(Queries.getQueryForLinking(),model),"subject","object");
        if (mapLinking!=null) {
            for (Map.Entry<String, String> entry : mapLinking.entrySet()) {
                Entity entityKey = dbAccess.getEntity(entry.getKey());
                Entity entityValue = dbAccess.getEntity(entry.getValue());
                if (entityKey==null && entityValue==null) {
                    entityFailedString+= entry.getKey() + "\n";
                    entityFailedString+= entry.getValue() + "\n";
                    continue;
                }else if (entityKey==null) {
                    int entityGroup=entityValue.getEntityGroup().getId();
                    Entity entity = new Entity();
                    entity.setPath(entry.getKey().toString());
                    entityKey=dbAccess.createEntity(entity,entityGroup,entityValue.getDomain().getId());
                }else if (entityValue==null) {
                    Entity entity = new Entity();
                    entity.setPath(entry.getValue().toString());
                    entityValue=dbAccess.createEntity(entity,entityKey.getEntityGroup().getId(),entityKey.getDomain().getId());
                }

                if (entityKey.getEntityGroup().getId() == 0 && entityValue.getEntityGroup().getId() == 0) {
                    EntityGroup entityGroup = dbAccess.createEntityGroupForImport(entityKey, entityValue);
                    dbAccess.updateEntityGroupInEntity(entityKey.getId(), entityGroup.getId());
                    dbAccess.updateEntityGroupInEntity(entityValue.getId(), entityGroup.getId());
                } else if (entityKey.getEntityGroup().getId() == 0) {
                    dbAccess.updateNameOfEntityGroup(entityValue.getEntityGroup().getId(),entityKey.getName());
                    dbAccess.updateEntityGroupInEntity(entityKey.getId(), entityValue.getEntityGroup().getId());

                } else if (entityValue.getEntityGroup().getId() == 0) {
                    dbAccess.updateNameOfEntityGroup(entityKey.getEntityGroup().getId(),entityValue.getName());
                    dbAccess.updateEntityGroupInEntity(entityValue.getId(), entityKey.getEntityGroup().getId());
                } else {

                }
            }
        }
    }

    /**
     * method for the creation entity groups from the import file
     */
    private void createEntitiesGroups() {
        List<Entity> entitiesWithNullEntityGroup = dbAccess.getEntitiesWitNullEntityGroup();
        if (entitiesWithNullEntityGroup!=null) {
            for (int i = 0; i < entitiesWithNullEntityGroup.size(); i++) {
                EntityGroup entityGroup = dbAccess.createEntityGroup(entitiesWithNullEntityGroup.get(i));
                dbAccess.updateEntityGroupInEntity(entitiesWithNullEntityGroup.get(i).getId(), entityGroup.getId());
            }
        }
    }

    /**
     * import file from the user and process it
     * @param text string contain of the file
     * @return string with the failed items
     */
    public String loadFileToModel(String text) {

        model.read(new ByteArrayInputStream(text.getBytes()),"","N-TRIPLES");
        createDomains();
        createEntities();
        createLinks();
        createEntitiesGroups();
        return domainFailedString + "\n" + entityFailedString;
    }
    }

