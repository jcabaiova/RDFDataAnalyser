package cz.cvut.fit.cabaijan.export.excel;

import cz.cvut.fit.cabaijan.dbMethods.DbAccess;
import cz.cvut.fit.cabaijan.logs.Logger;
import cz.cvut.fit.cabaijan.dbObjects.Domain;
import cz.cvut.fit.cabaijan.dbObjects.Entity;
import cz.cvut.fit.cabaijan.export.objectToExport.DomainsToExport;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Janka on 9/28/2016.
 * ExportDomainsToExcel extends the class ExportToExcel and overrides the methods prepareDataForSheet1 and prepareDataForSheet2
 */
public class ExportDomainsToExcel extends ExportToExcel {
    /**
     * This is a constructor to initialize ExportDomainsToExcel object.
     */
    public ExportDomainsToExcel() {
        dbAccess = new DbAccess();
        pathDirectoryForExport = "/tmpFiles";
        pathToTemplate = this.getClass().getResource("/templatesExcel/domainsExportTemplate.xlsx").getFile().toString();
        nameForDataSheet = "Domains and entities";
        startNameForExportedFile = "DomainsList";
    }

    /**
     * create data for the first datasheet, which contains domains
     * @return map of string and object for writing
     */
    @Override
    public Map<String, Object[]> prepareDataForSheet1() {
        try {


            List<Domain> domainList = dbAccess.domainDao.queryForAll();
            Map<String, Object[]> data = new TreeMap<>();
            data.put("1", new Object[]{"Domain name", "Number of entities in domain"});
            int i = 2;
            if (domainList == null) return data;
            for (Domain d : domainList) {
                List<Entity> entityList = dbAccess.entityDao.queryForEq("domain_id", d.getId());
                data.put(String.valueOf(i), new Object[]{d.getName(), entityList.size()});
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

            List<DomainsToExport> domainsToExports = dbAccess.getDomainsToExport();
            Map<String, Object[]> data = new TreeMap<>();
            data.put("1", new Object[]{"Domain id", "Domain name", "Entity id", "Entity name", "Entity-group id", "Entity-group name"});
            if (domainsToExports == null) {
                return data;
            }
            int i = 1;
            for (DomainsToExport d : domainsToExports) {
                i++;
                data.put(String.valueOf(i), new Object[]{d.getIdDomain(), d.getNameDomain(), d.getIdEntity(), d.getPathEntity(), d.getIdEntityGroup(), d.getNameEntityGroup()});
            }
            return data;
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
    }
}
