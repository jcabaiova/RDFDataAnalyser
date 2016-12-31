package cz.cvut.fit.cabaijan.export.ntriples;

import cz.cvut.fit.cabaijan.dbMethods.DbAccess;
import cz.cvut.fit.cabaijan.dbObjects.*;
import cz.cvut.fit.cabaijan.logs.Logger;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Janka on 12/26/2016.
 * ExportAnalysisToNTriples extends the class ExportToNTriples and overrides the methods for writing data, which is writeData
 */
public class ExportAnalysisToNTriples extends ExportToNTriples {
    /**
     * This is a constructor to initialize ExportAnalysisToExcel object.
     * @param id id of the datasetAnalysis for which the export will be created
     */
    public ExportAnalysisToNTriples(int id) {
        try {
            this.dbAccess = new DbAccess();
            this.predicateForDomainDefinition = "";
            this.predicateForEntityDefinition = "";
            this.predicateForLinking = "";
            this.startNameForExportedFile = "datasetAnalysis";
            this.pathDirectoryForExport = "/tmpFiles";
            this.datasetAnalysis = this.dbAccess.datasetAnalysisDao.queryForId(id);
        } catch (Exception e) {
            Logger.writeException(e);
        }
    }

    /**
     * write data to the created file
     * @param bw buffered writer
     * @return true if the file was written successfully
     */
    @Override
    public Boolean writeData(BufferedWriter bw) {
        try {
            PrintWriter out = new PrintWriter(bw);
            List<DomainInDataset> domainInDatasetsList = dbAccess.domainInDatasetDao.queryForEq("datasetAnalysis_id", datasetAnalysis.getId());
            List<EntityInDataset> entityInDatasetList = dbAccess.entityInDatasetDao.queryForEq("datasetAnalysis_id", datasetAnalysis.getId());
            if (domainInDatasetsList != null) {
                for (DomainInDataset domainInDataset : domainInDatasetsList) {
                    Domain domain = dbAccess.domainDao.queryForId(domainInDataset.getDomain().getId());
                    out.println(formatToNt(domain.getPath()) + " <http://rdfs.org/ns/void#triples> \"" + domainInDataset.getCountDomain() + "\"^^<http://www.w3.org/2001/XMLSchema#integer>.");
                }
            }
            if (entityInDatasetList != null) {
                for (EntityInDataset entityInDataset : entityInDatasetList) {
                    Entity entity = dbAccess.entityDao.queryForId(entityInDataset.getEntity().getId());
                    out.println(formatToNt(entity.getPath()) + " <http://rdfs.org/ns/void#triples> \"" + entityInDataset.getCountEntities() + "\"^^<http://www.w3.org/2001/XMLSchema#integer>.");
                }
            }
            out.close();
            return true;
        } catch (SQLException e) {
            Logger.writeException(e);
            return false;
        }
    }
}
