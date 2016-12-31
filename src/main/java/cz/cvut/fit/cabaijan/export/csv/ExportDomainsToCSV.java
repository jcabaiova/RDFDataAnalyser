package cz.cvut.fit.cabaijan.export.csv;

import cz.cvut.fit.cabaijan.dbMethods.DbAccess;
import cz.cvut.fit.cabaijan.export.objectToExport.DomainsToExport;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Janka on 10/1/2016.
 * ExportDomainsToCSV extends the class ExportToCSV and overrides the method writeData
 */
public class ExportDomainsToCSV extends ExportToCSV {
    /**
     * This is a constructor to initialize ExportDomainsToCSV object.
     */
    public ExportDomainsToCSV() {
        dbAccess = new DbAccess();
        default_separator = ',';
        pathDirectoryForExport = "/tmpFiles";
        startNameForExportedFile = "domainList";
    }

    /**
     * write data for the domain export by the help of writer
     * @param writer writer
     */
    @Override
    public void writeData(Writer writer) {
        writeLine(writer, Arrays.asList("Domain id", "Domain name", "Entity id", "Entity name", "Entity-group id", "Entity-group name"));
        List<DomainsToExport> domainsToExports = dbAccess.getDomainsToExport();
        for (DomainsToExport d : domainsToExports) {
            List<String> list = new ArrayList<>();
            list.add(String.valueOf(d.getIdDomain()));
            list.add(d.getNameDomain());
            list.add(String.valueOf(d.getIdEntity()));
            list.add(d.getPathEntity());
            list.add(String.valueOf(d.getIdEntityGroup()));
            list.add(d.getNameEntityGroup());
            writeLine(writer, list);
        }
    }

}
