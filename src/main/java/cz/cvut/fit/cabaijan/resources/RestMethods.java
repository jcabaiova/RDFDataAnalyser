package cz.cvut.fit.cabaijan.resources;
import cz.cvut.fit.cabaijan.charts.objects.DatasetAnalysisInChart;
import cz.cvut.fit.cabaijan.charts.objects.DomainInChart;
import cz.cvut.fit.cabaijan.dbMethods.DB;
import cz.cvut.fit.cabaijan.dbObjects.*;
import cz.cvut.fit.cabaijan.importDomains.ImportDomains;
import cz.cvut.fit.cabaijan.logs.Logger;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;


import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.List;

/**
 * Created by Janka on 9/10/2016.
 * RestMethods class contains rest methods which are called from the server
 */

@Path("/restMethod")
public class RestMethods {
    /**
     * create new domain
     * @param domain domain filled by user
     * @return domain stored to database
     */
    @POST
    @Path("/createDomain")
    @Consumes(MediaType.APPLICATION_JSON)
    public Domain createDomain(Domain domain) {

        Domain domainToReturn = DB.getDbAccess().createDomain(domain);
        if (domainToReturn != null) {
            return domainToReturn;
        } else return null;
    }

    /**
     * update domain
     * @param domain_id id of domain to be updated
     * @param domain domain fields filled by the user
     * @return domain stored to database
     */
    @POST
    @Path("/updateDomain/{domain_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Domain updateDomain(@PathParam("domain_id") Integer domain_id, Domain domain) {


        Domain domain1 = DB.getDbAccess().updateDomain(domain_id, domain);
        if (domain1 != null) {
            return domain1;

        } else return null;
    }

    /**
     * import domains from the file
     * @param domainName content of the file
     * @return string of failed domains and entities
     */
    @POST
    @Path("/inportDomains2")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response importDomains2(String domainName) {

        ImportDomains importDomains = new ImportDomains();
        String output = "Domains and entitiex below couldnt be imported:" + importDomains.loadFileToModel(domainName);
        return Response.status(201).entity(output).build();

    }

    /**
     * update entity
     * @param entity_id id of entity to be updated
     * @param entityPath new value filled by the user for update
     * @return response with the result
     */
    @POST
    @Path("/updateEntity/{entity_id}")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response updateEntity(@PathParam("entity_id") Integer entity_id, String entityPath) {


        if (DB.getDbAccess().updatePathOfEntity(entity_id, entityPath)) {
            String result = "Entity saved : ";
            return Response.status(201).entity(result).build();
        } else return Response.status(409).build();
    }

    /**
     * delete domain
     * @param domain_id id of domain to be deleted
     * @return response with the result
     */
    @DELETE
    @Path("/deleteDomain/{domain_id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteDomain(@PathParam("domain_id") Integer domain_id) {

        if (DB.getDbAccess().deleteDomain(domain_id)) {
            String result = "Domain deleted : ";
            return Response.status(201).entity(result).build();
        } else return Response.status(409).build();
    }

    /**
     * delete entity
     * @param entity_id id of entity to be deleted
     * @return response with the result
     */
    @DELETE
    @Path("/deleteEntity/{entity_id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteEntity(@PathParam("entity_id") Integer entity_id) {

        if (DB.getDbAccess().deleteEntity(entity_id)) {
            String result = "Domain deleted : ";
            return Response.status(201).entity(result).build();
        } else return Response.status(409).build();
    }

    /**
     * delete tmp files in the path /tmpFiles
     * @return response with the result
     */
    @DELETE
    @Path("/deleteTmpFiles")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteEntity() {
        try {
            FileUtils.cleanDirectory(new File("/tmpFiles"));
            return Response.status(201).build();
        } catch (IOException e) {
            Logger.writeException(e);
            return Response.status(404).build();
        }


    }

    /**
     * create entity
     * @param entity entity filled by user to be stored
     * @param domain_id id of domain to which entity belongs
     * @param entityGroup_id id of entity group to which entity belongs
     * @return
     */
    @POST
    @Path("/createEntity/domain/{domain_id}/entityGroup/{entityGroup_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Entity createEntity(Entity entity, @PathParam("domain_id") Integer domain_id, @PathParam("entityGroup_id") Integer entityGroup_id) {
        Entity entityToReturn = DB.getDbAccess().createEntity(entity, entityGroup_id, domain_id);
        if (entityToReturn != null) {
            return entityToReturn;
        } else return null;
    }

    /**
     * get all domains
     * @return lis of domains
     */
    @GET
    @Path("/getDomains")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Domain> getDomains() {
        return DB.getDbAccess().getAllDomains();
    }

    /**
     * get all entities
     * @return list of entities
     */
    @GET
    @Path("/getEntities")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entity> getEntities() {
        return DB.getDbAccess().getAllEntities();
    }

    /**
     * get all entity groups
     * @return list of all entity groups
     */
    @GET
    @Path("/getEntitiesGroup")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EntityGroup> getEntitiesGroup() {
        return DB.getDbAccess().getAllEntitiesGroup();
    }

    /**
     * get domain according to its name
     * @param domain_name name of domain
     * @return domain
     */
    @GET
    @Path("/getDomain/{domain_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Domain getDomainAccordingName(@PathParam("domain_name") String domain_name) {
        return DB.getDbAccess().getDomain(domain_name);
    }

    /**
     * create new dataset
     * @param responseAsync asynchronous operation
     * @param uploadedInputStream input stream to upload, only in case of HDT dataset
     * @param fileDetail only in case of HDT dataset
     * @param name name of dataset
     * @param description description of dataset
     * @param sparqlPath path to SPARQL endpoint, only in case of SPARQL endpoint dataset
     * @param ontologyPredicate ontology predicate of dataset
     * @param shortCalculation type of the initial calculation of dataset analysis
     */
    @POST
    @Path("/createDataset")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void createDataset(@Suspended AsyncResponse responseAsync, @FormDataParam("file") InputStream uploadedInputStream,
                              @FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("nameDataset") String name, @FormDataParam("descriptionDataset") String description, @FormDataParam("sparql") String sparqlPath,
                              @FormDataParam("ontologyPredicate") String ontologyPredicate, @FormDataParam("shortCalculation") Boolean shortCalculation) {

        new Thread() {
            public void run() {
                Dataset dataset = new Dataset();
                dataset.setName(name);
                dataset.setDescription(description);
                dataset.setOntologyPredicate(ontologyPredicate);
                if (sparqlPath.length() > 0) {
                    dataset.setFileName(sparqlPath);
                } else if (uploadedInputStream != null) {
                    dataset.setFileName(fileDetail.getFileName());
                }
                Dataset datasetToReturn = DB.getDbAccess().createDataset(dataset, uploadedInputStream, shortCalculation);
                if (datasetToReturn != null) {
                    Response response = Response.ok(datasetToReturn).build();
                    responseAsync.resume(response);
                } else {
                    Response response = Response.status(404).build();
                    responseAsync.resume(response);
                }

            }
        }.start();
    }


    /**
     * get dataset according to its name
     * @param dataset_name name of dataset for searching
     * @return dataset
     */
    @GET
    @Path("/getDataset/{dataset_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Dataset getDatasetAccordingName(@PathParam("dataset_name") String dataset_name) {
        return DB.getDbAccess().getDataset(dataset_name);
    }

    /**
     * get all datasets
     * @return list of all datasets
     */
    @GET
    @Path("/getDatasets")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dataset> getDatasets() {
        return DB.getDbAccess().getAllDatasets();
    }

    /**
     * get all dataset analysis in the particular dataset
     * @param dataset_id id of dataset for searching dataset analysis
     * @return list of dataset analysis
     */
    @GET
    @Path("/getDatasetAnalysis/{dataset_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DatasetAnalysis> getDatasetAnalysis(@PathParam("dataset_id") int dataset_id) {
        return DB.getDbAccess().getDatasetsAnalysis(dataset_id);
    }

    /**
     * get all dataset analysis
     * @return list of all dataset analysis
     */
    @GET
    @Path("/getDatasetAnalysis")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DatasetAnalysis> getAllDatasetAnalysis() {
        return DB.getDbAccess().getAllDatasetsAnalysis();
    }

    /**
     * get entities in dataset in the particular dataset analysis
     * @param datasetAnalysis_id id of dataset analysis for searching
     * @return list of entities in dataset
     */
    @GET
    @Path("/getEntitiesInDatasetAnalysis/{datasetAnalysis_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EntityInDataset> getEntitiesInDatasetAnalysis(@PathParam("datasetAnalysis_id") int datasetAnalysis_id) {
        return DB.getDbAccess().getEntitiesInDatasetAnalysis(datasetAnalysis_id);
    }

    /**
     * get entities in dataset in the particular dataset analysis ane in the particular domain
     * @param datasetAnalysis_id id of dataset analysis for searching
     * @param domain_id id of domain for searching
     * @return list of entities in dataset
     */
    @GET
    @Path("/getEntitiesInDatasetAnalysis/{datasetAnalysis_id}/{domain_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EntityInDataset> getEntitiesInDatasetAnalysis(@PathParam("datasetAnalysis_id") int datasetAnalysis_id, @PathParam("domain_id") int domain_id) {
        return DB.getDbAccess().getEntitiesInDatasetAnalysisInDomain(datasetAnalysis_id, domain_id);
    }

    /**
     * get domains in dataset in the particular dataset analysis
     * @param datasetAnalysis_id id of dataset analysis for searching
     * @return list of domains in dataset
     */
    @GET
    @Path("/getDomainsInDatasetAnalysis/{datasetAnalysis_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DomainInDataset> getDomainsInDatasetAnalysis(@PathParam("datasetAnalysis_id") int datasetAnalysis_id) {
        return DB.getDbAccess().getDomainsInDatasetAnalysis(datasetAnalysis_id);
    }

    /**
     * get predicates in entity in dataset
     * @param entity_id id of entity in dataset for searching predicates
     * @return list of predicates
     */
    @GET
    @Path("/getPredicatesInEntityInDataset/{entity_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Predicate> getPredicatesInEntity(@PathParam("entity_id") int entity_id) {
        return DB.getDbAccess().getPredicatesInEntity(entity_id);
    }

    /**
     * update dataset
     * @param dataset_id id of updated dataset
     * @param dataset filled information to update about dataset
     * @return response with the result
     */
    @POST
    @Path("/updateDataset/{dataset_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDataset(@PathParam("dataset_id") Integer dataset_id, Dataset dataset) {


        Dataset datasetToReturn = DB.getDbAccess().updateDataset(dataset_id, dataset);
        if (datasetToReturn != null) {
            return Response.ok(datasetToReturn).build();
        } else return Response.status(409).build();
    }

    /**
     * update dataset analysis
     * @param datasetAnalysis_id id of updated dataset analysis
     * @param datasetAnalysis filled information to update about dataset analysis
     * @return response with the result
     */
    @POST
    @Path("/updateDatasetAnalysis/{datasetAnalysis_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDatasetAnalysis(@PathParam("datasetAnalysis_id") Integer datasetAnalysis_id, DatasetAnalysis datasetAnalysis) {


        DatasetAnalysis datasetAnalysisToReturn = DB.getDbAccess().updateDatasetAnalysis(datasetAnalysis_id, datasetAnalysis);
        if (datasetAnalysisToReturn != null) {
            return Response.ok(datasetAnalysisToReturn).build();
        } else return Response.status(409).build();
    }

    /**
     * delete dataset
     * @param dataset_id id of dataset to be deleted
     * @return response with the result
     */
    @DELETE
    @Path("/deleteDataset/{dataset_id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteDataset(@PathParam("dataset_id") Integer dataset_id) {

        if (DB.getDbAccess().deleteDataset(dataset_id)) {
            String result = "Dataset deleted : ";
            return Response.status(201).entity(result).build();
        } else return Response.status(409).build();
    }

    /**
     * delete dataset analysis
     * @param datasetAnalysis_id id of dataset analysis to be deleted
     * @return response with the result
     */
    @DELETE
    @Path("/deleteDatasetAnalysis/{datasetAnalysis_id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteDatasetAnalysis(@PathParam("datasetAnalysis_id") Integer datasetAnalysis_id) {

        if (DB.getDbAccess().deleteDatasetAnalysis(datasetAnalysis_id)) {
            String result = "Dataset deleted : ";
            return Response.status(201).entity(result).build();
        } else return Response.status(409).build();
    }

    /**
     * create dataset analysis
     * @param dataset_id id of dataset to which new dataset analysis belongs
     * @param datasetAnalysis new dataset analysis to be stored to database
     * @param responseAsync async response
     */
    @POST
    @Path("/createDatasetAnalysis/{dataset_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createDatasetAnalysis(@PathParam("dataset_id") int dataset_id, DatasetAnalysis datasetAnalysis, @Suspended AsyncResponse responseAsync) {

        new Thread() {
            public void run() {
                DB.getDbAccess().createDatasetAnalysis(dataset_id, datasetAnalysis, null);
                String result = "dd";
                Response response = Response.ok(result).build();
                responseAsync.resume(response);
            }
        }.start();

    }

    /**
     * get entity groups belong to the particlar domain
     * @param domain_id id of domain for searching
     * @return list of entity groups
     */
    @GET
    @Path("/getEntityGroupsAccordingToDomain/{domain_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EntityGroup> getEntitiesGroupByDomain(@PathParam("domain_id") Integer domain_id) {
        return DB.getDbAccess().getEntitiesGroupInDomain(domain_id);
    }

    /**
     * get data for domain chart
     * @return list of domainsInChart
     */
    @GET
    @Path("/getDataForDomainChart")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DomainInChart> getDataForDomainChart() {
        List<DomainInChart> domainsToExports = DB.getDbAccess().getEntityNumberInDomains();
        return domainsToExports;
    }

    /**
     * get data for dataset analysis chart
     * @param datasetAnalysis_id id of dataset analysis for the searching
     * @return list of datasetAnalysisInChart
     */
    @GET
    @Path("/getDataForDatasetAnalysisChart/{datasetAnalysis_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DatasetAnalysisInChart getDataForDatasetAnalysisChart(@PathParam("datasetAnalysis_id") Integer datasetAnalysis_id) {
        DatasetAnalysisInChart toReturn = DB.getDbAccess().getDataForDatasetAnalysisChart(datasetAnalysis_id);
        return toReturn;
    }

}


