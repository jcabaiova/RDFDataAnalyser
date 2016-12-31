package cz.cvut.fit.cabaijan.export.csv;

import cz.cvut.fit.cabaijan.dbMethods.DbAccess;
import cz.cvut.fit.cabaijan.export.objectToExport.AnalysisToExport;
import cz.cvut.fit.cabaijan.logs.Logger;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Janka on 10/1/2016.
 * ExportAnalysisToCSV extends the class ExportToCSV and overrides the method writeData
 */
public class ExportAnalysisToCSV extends ExportToCSV {
    /**
     * This is a constructor to initialize ExportAnalysisToCSV object.
     * @param datasetAnalysis datasetAnalysis for which the export will be created
     */
    public ExportAnalysisToCSV(int datasetAnalysis) {
        try {
            this.dbAccess = new DbAccess();
            this.default_separator = ',';
            this.pathDirectoryForExport = "/tmpFiles";
            this.startNameForExportedFile = "datasetAnalysis";
            this.datasetAnalysis = dbAccess.datasetAnalysisDao.queryForId(datasetAnalysis);
        } catch (Exception e) {
            Logger.writeException(e);
        }
    }

    /**
     * write data for dataset analysis by the help of writer
     * @param writer writer
     */
    @Override
    public void writeData(Writer writer) {
        writeLine(writer, Arrays.asList("Domain id", "Domain name", "Domain count", "Entity id", "Entity name", "Entity count", "Predicate id", "Predicate name", "Predicate count"));
        List<AnalysisToExport> analysisToExports = dbAccess.getAnalysisToExport(datasetAnalysis);
        for (AnalysisToExport analysis : analysisToExports) {
            List<String> list = new ArrayList<>();
            list.add(String.valueOf(analysis.getIdDomain()));
            list.add(analysis.getNameDomain());
            list.add(String.valueOf(analysis.getCountDomain()));
            list.add(String.valueOf(analysis.getIdEntity()));
            list.add(analysis.getPathEntity());
            list.add(String.valueOf(analysis.getCountEntities()));
            list.add(String.valueOf(analysis.getIdPredicate()));
            list.add(analysis.getNamePredicate());
            list.add(String.valueOf(analysis.getCountPredicate()));
            writeLine(writer, list);
        }
    }
}
