package cz.cvut.fit.cabaijan.export.excel;

import cz.cvut.fit.cabaijan.dbMethods.DbAccess;
import cz.cvut.fit.cabaijan.logs.Logger;
import cz.cvut.fit.cabaijan.dbObjects.*;
import cz.cvut.fit.cabaijan.export.objectToExport.AnalysisToExport;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Janka on 9/28/2016.
 * ExportAnalysisToExcel extends the class ExportToExcel and overrides the methods prepareDataForSheet1 and prepareDataForSheet2
 */
public class ExportAnalysisToExcel extends ExportToExcel {

    /**
     * This is a constructor to initialize ExportAnalysisToExcel object.
     * @param da datasetAnalysis for which the export will be created
     */
    public ExportAnalysisToExcel(int da) {
        try {
            dbAccess = new DbAccess();
            pathDirectoryForExport = "/tmpFiles";
            pathToTemplate = this.getClass().getResource("/templatesExcel/datasetAnalysisExportTemplate.xlsx").getFile().toString();
            nameForDataSheet = "Dataset Analysis";
            datasetAnalysis = dbAccess.datasetAnalysisDao.queryForId(da);
            startNameForExportedFile = "DatasetAnalysis";
        } catch (Exception e) {
            Logger.writeException(e);
        }
    }

    /**
     * create data for the first datasheet, which contains domains
     * @return map of string and object for writing
     */
    @Override
    public Map<String, Object[]> prepareDataForSheet1() {
        try {
            List<DomainInDataset> domainInDatasets = dbAccess.domainInDatasetDao.queryForEq("datasetAnalysis_id", datasetAnalysis.getId());

            Map<String, Object[]> data = new TreeMap<>(Comparator.comparingInt(Integer::parseInt));
            data.put("1", new Object[]{datasetAnalysis.getName()});
            data.put("2", new Object[]{"Domain name", "Number of entities in domain in Dataset"});
            if (domainInDatasets == null) return data;
            int i = 3;
            for (DomainInDataset d : domainInDatasets) {
                Domain domain = dbAccess.domainDao.queryForId(d.getDomain().getId());
                data.put(String.valueOf(i), new Object[]{domain.getName(), d.getCountDomain()});
                i++;
            }
            return data;
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }

    /**
     * create data for the second datasheet, which contains detailed domain, entities and predicates
     * @return map of string and object for writing
     */
    @Override
    public Map<String, Object[]> prepareDataForSheet2() {

        try {

            List<AnalysisToExport> analysisToExports = dbAccess.getAnalysisToExport(datasetAnalysis);

            Map<String, Object[]> data = new TreeMap<>();
            data.put("1", new Object[]{"Domain id", "Domain name", "Domain count", "Entity id", "Entity name", "Entity count", "Predicate id", "Predicate name", "Predicate count"});
            if (analysisToExports == null) return data;
            int i = 1;
            for (AnalysisToExport a : analysisToExports) {
                i++;
                data.put(String.valueOf(i), new Object[]{a.getIdDomain(), a.getNameDomain(), a.getCountDomain(), a.getIdEntity(), a.getPathEntity(), a.getCountEntities(), a.getIdPredicate(), a.getNamePredicate(), a.getCountPredicate()});
            }
            return data;
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }


}
