package cz.cvut.fit.cabaijan.resources;

import cz.cvut.fit.cabaijan.export.csv.ExportAnalysisToCSV;
import cz.cvut.fit.cabaijan.export.csv.ExportDomainsToCSV;
import cz.cvut.fit.cabaijan.export.csv.ExportToCSV;
import cz.cvut.fit.cabaijan.export.excel.ExportAnalysisToExcel;
import cz.cvut.fit.cabaijan.export.excel.ExportDomainsToExcel;
import cz.cvut.fit.cabaijan.export.excel.ExportToExcel;
import cz.cvut.fit.cabaijan.export.ntriples.ExportAnalysisToNTriples;
import cz.cvut.fit.cabaijan.export.ntriples.ExportDomainsToNtriple;
import cz.cvut.fit.cabaijan.export.ntriples.ExportToNTriples;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.File;

/**
 * Created by Janka on 10/17/2016.
 * FileDownloadService class contains rest methods for file downloading which are called from the server
 */
@Path("/files")
public class FileDownloadService {
    /**
     * get domains in N-Triples format
     * @return response
     */
    @GET
    @Path("/nt")
    @Produces("text/plain")
    public Response getFileNt() {
        ExportDomainsToNtriple exportDomainsToNtriple = new ExportDomainsToNtriple();
        String filePath= exportDomainsToNtriple.exportData();
        File file = new File(filePath);

        Response.ResponseBuilder response = Response.ok(file);
        response.header("Content-Disposition","attachment; filename=\"domainList.nt\"");
        return response.build();

    }

    /**
     * get domains in CSV format
     * @return response
     */
    @GET
    @Path("/csv")
    @Produces("text/plain")
    public Response getFileCSV() {

        ExportToCSV exportToCSV = new ExportDomainsToCSV();
        String filePath= exportToCSV.exportData();
        File file = new File(filePath);
        Response.ResponseBuilder response = Response.ok(file);
        response.header("Content-Disposition","attachment; filename=\"domainList.csv\"");
        return response.build();

    }

    /**
     * get domains in XSLX format
     * @return response
     */
    @GET
    @Path("/xlsx")
    @Produces("text/plain")
    public Response getFileExcel() {
        ExportToExcel exportToExcel = new ExportDomainsToExcel();
        String filePath= exportToExcel.exportData();
        File file = new File(filePath);

        Response.ResponseBuilder response = Response.ok(file);
        response.header("Content-Disposition","attachment; filename=\"domainList.xlsx\"");
        return response.build();

    }

    /**
     * get dataset analysis in XSLX format
     * @param id id of dataset analysis
     * @return response
     */
    @GET
    @Path("/analysis/xlsx/{datasetAnalysis_id}")
    @Produces("text/plain")
    public Response getAnalysisFileExcel(@PathParam("datasetAnalysis_id") int id) {
        ExportToExcel exportToExcel = new ExportAnalysisToExcel(id);
        String filePath= exportToExcel.exportData();
        File file = new File(filePath);

        Response.ResponseBuilder response = Response.ok(file);
        response.header("Content-Disposition","attachment; filename=\"datasetAnalysis"+id+".xlsx\"");
        return response.build();

    }

    /**
     * get dataset analysis in CSV format
     * @param id id of dataset analysis
     * @return response
     */
    @GET
    @Path("/analysis/csv/{datasetAnalysis_id}")
    @Produces("text/plain")
    public Response getAnalysisFileCsv(@PathParam("datasetAnalysis_id") int id) {
        ExportToCSV exportToCSV = new ExportAnalysisToCSV(id);
        String filePath= exportToCSV.exportData();
        File file = new File(filePath);

        Response.ResponseBuilder response = Response.ok(file);
        response.header("Content-Disposition","attachment; filename=\"datasetAnalysis"+id+".csv\"");
        return response.build();

    }

    /**
     * get dataset analysis in N-Triples format
     * @param id id of dataset analysis
     * @return response
     */
    @GET
    @Path("/analysis/ntriples/{datasetAnalysis_id}")
    @Produces("text/plain")
    public Response getAnalysisFileNTriples(@PathParam("datasetAnalysis_id") int id) {
        ExportToNTriples exportToNTriples = new ExportAnalysisToNTriples(id);
        String filePath= exportToNTriples.exportData();
        File file = new File(filePath);
        Response.ResponseBuilder response = Response.ok(file);
        response.header("Content-Disposition","attachment; filename=\"datasetAnalysis"+id+".nt\"");
        return response.build();

    }

}