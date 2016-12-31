package cz.cvut.fit.cabaijan.export.ntriples;

import cz.cvut.fit.cabaijan.dbMethods.DbAccess;
import cz.cvut.fit.cabaijan.dbObjects.Domain;
import cz.cvut.fit.cabaijan.dbObjects.Entity;
import cz.cvut.fit.cabaijan.dbObjects.EntityGroup;
import cz.cvut.fit.cabaijan.logs.Logger;

import java.io.*;
import java.util.List;

/**
 * Created by Janka on 10/15/2016.
 * ExportDomainsToNtriple extends the class ExportToNTriples and overrides the methods for writing data, which is writeData
 */
public class ExportDomainsToNtriple extends ExportToNTriples {
    /**
     * This is a constructor to initialize ExportAnalysisToExcel object.
     */
    public ExportDomainsToNtriple() {
        this.dbAccess = new DbAccess();
        this.predicateForDomainDefinition = "<http://www.w3.org/2000/01/rdf-schema#label>";
        this.predicateForEntityDefinition = "<http://purl.org/dc/terms/subject>";
        this.predicateForLinking = "<http://www.w3.org/2002/07/owl#sameAs>";
        this.startNameForExportedFile = "domainList";
        this.pathDirectoryForExport = "/tmpFiles";
        this.datasetAnalysis = null;
    }

    /**
     * write data to the created file
     * @param bw buffered writer
     * @return true if the file was written successfully
     */
    public Boolean writeData(BufferedWriter bw) {
        try {
            PrintWriter out = new PrintWriter(bw);
            List<Domain> domainList = dbAccess.getAllDomains();
            List<Entity> entityList = dbAccess.getAllEntities();
            List<EntityGroup> entityGroupList = dbAccess.getAllEntitiesGroup();
            if (domainList != null) {
                for (Domain domain : domainList) {
                    out.println(formatToNt(domain.getPath()) + " " + this.predicateForDomainDefinition + " \"" + domain.getName() + "\".");
                }
            }
            if (entityList != null) {
                for (Entity entity : entityList) {
                    Domain domain = dbAccess.getDomain(entity.getDomain().getId());
                    out.println(formatToNt(entity.getPath()) + " " + this.predicateForEntityDefinition + " " + formatToNt(domain.getPath()) + " .");
                }
            }
            if (entityGroupList != null) {
                for (EntityGroup entityGroup : entityGroupList) {
                    List<Entity> entityList1 = dbAccess.getEntities(entityGroup.getId());
                    if (entityList1.size() > 1) {
                        Entity entity = entityList1.get(0);
                        for (int i = 1; i < entityList1.size(); i++) {
                            out.println(formatToNt(entityList1.get(i).getPath()) + " " + this.predicateForLinking + " " + formatToNt(entity.getPath()) + " .");
                        }
                    }
                }
            }
            out.close();
            return true;
        } catch (Exception e) {
            Logger.writeException(e);
            return false;
        }
    }

}
