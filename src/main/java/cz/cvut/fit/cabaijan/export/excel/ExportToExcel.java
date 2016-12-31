package cz.cvut.fit.cabaijan.export.excel;

import cz.cvut.fit.cabaijan.HelpfulMethods;
import cz.cvut.fit.cabaijan.dbMethods.DbAccess;
import cz.cvut.fit.cabaijan.logs.Logger;
import cz.cvut.fit.cabaijan.dbObjects.DatasetAnalysis;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by Janka on 9/28/2016.
 * ExportToExcel class uses for exporting dataset analysis or domains to Excel.
 * It is an abstract class, it contains one class for export of dataset analysis and one class for export of domains
 */
public abstract class ExportToExcel {
    /**
     *  object DbAccess for getting access to database
     */
    public DbAccess dbAccess;
    /**
     * directory path for the exported file
     */
    public String pathDirectoryForExport;
    /**
     * path to excel template
     */
    public String pathToTemplate;
    /**
     * name for the particular datasheet in the Excel
     */
    public String nameForDataSheet;
    /**
     * dataset analysis for which the export is created
     */
    public DatasetAnalysis datasetAnalysis;
    /**
     * start name for the exported file
     */
    public String startNameForExportedFile;

    /**
     * create data for the first datasheet, which contains domains
     * @return map of string and object for writing
     */
    public abstract Map<String, Object[]> prepareDataForSheet1();

    /**
     * create data for the second datasheet, which contains detailed domain, entities and predicates
     * @return map of string and object for writing
     */
    public abstract Map<String, Object[]> prepareDataForSheet2();

    /**
     * export data to the excel file
     * @return path to the exported file
     */
    public String exportData() {
        try {
            Workbook workbook = new XSSFWorkbook(OPCPackage.open(new FileInputStream(pathToTemplate)));

            //Sheet with chart
            Sheet sh = workbook.getSheetAt(0);
            deleteDataFromTemplate(sh);
            Map<String, Object[]> data = prepareDataForSheet1();
            writeDataToSheet(sh, data);

            Sheet sheetNew = workbook.createSheet(nameForDataSheet);
            Map<String, Object[]> data2 = prepareDataForSheet2();
            if (!writeDataToSheet(sheetNew, data2)) return "";

            String path = writeWorkbook(workbook, pathDirectoryForExport);
            return path;

        } catch (Exception e) {
            Logger.writeException(e);
            return "";
        }

    }

    /**
     * Write data to the Excel sheet
     * @param sh sheet of the Excel
     * @param data data to write
     * @return true, if writing was successful
     */
    private Boolean writeDataToSheet(Sheet sh, Map<String, Object[]> data) {

        try {
            Set<String> keyset = data.keySet();
            int rownum = 0;
            for (String key : keyset) {
                Row row = sh.createRow(rownum++);
                Object[] objArr = data.get(key);
                int cellnum = 0;
                for (Object obj : objArr) {
                    Cell cell = row.createCell(cellnum++);

                    if (obj instanceof String)
                        cell.setCellValue((String) obj);
                    else if (obj instanceof Integer)
                        cell.setCellValue((Integer) obj);
                }
            }

            return true;
        } catch (Exception e) {
            Logger.writeException(e);
            return false;
        }
    }

    /**
     * create file for the export
     * @param workbook workbook to write
     * @param pathToFile path to the exported file
     * @return absolute path of the file
     */
    private String writeWorkbook(Workbook workbook, String pathToFile) {
        try {
            HelpfulMethods.createDirectoryIfDoNotExist(pathDirectoryForExport);
            File file = File.createTempFile(startNameForExportedFile, ".xlsx", new File(pathToFile));
            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            Logger.writeException(e);
            return "";
        }
    }

    /**
     * delete data from the pre-filled excel templae
     * @param sh excel sheet in which data will be deleted
     */
    private void deleteDataFromTemplate(Sheet sh) {
        int countActualRows = sh.getLastRowNum();
        for (int row = 0; row <= countActualRows; row++) {
            sh.removeRow(sh.getRow(row));
        }
    }
}
