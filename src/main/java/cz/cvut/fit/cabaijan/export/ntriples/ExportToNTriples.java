package cz.cvut.fit.cabaijan.export.ntriples;

import cz.cvut.fit.cabaijan.HelpfulMethods;
import cz.cvut.fit.cabaijan.dbMethods.DbAccess;
import cz.cvut.fit.cabaijan.dbObjects.DatasetAnalysis;
import cz.cvut.fit.cabaijan.logs.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by Janka on 12/26/2016.
 * ExportToNTriples class uses for exporting dataset analysis or domains to the file of the format N-Triples.
 * It is an abstract class, it contains one class for export of dataset analysis and one class for export of domains
 */

public abstract class ExportToNTriples {
    /**
     *  object DbAccess for getting access to database
     */
    public DbAccess dbAccess;
    /**
     * predicate which is used for domain definition
     */
    public String predicateForDomainDefinition;
    /**
     * predicate which is used for entity definition
     */
    public String predicateForEntityDefinition;
    /**
     * predicate which is used for make links between entities
     */
    public String predicateForLinking;
    /**
     * start name for the exported file
     */
    public String startNameForExportedFile;
    /**
     * path to the directory where exported file is stored
     */
    public String pathDirectoryForExport;
    /**
     * dataset analysis, for which the export is created
     */
    public DatasetAnalysis datasetAnalysis;

    /**
     * transform string to the N-Triple format
     * @param input input for transformation
     * @return transformed value
     */
    public String formatToNt(String input) {
        return "<" + input + ">";
    }

    /**
     * write data to the created file
     * @param bf  buffered writer
     * @return true if the file was written successfully
     */
    public abstract Boolean writeData(BufferedWriter bf);

    /**
     * create file and write to it prepare data by the help of method writeData
     * @return absolute path to the exported file
     */
    public String exportData() {
        try {
            HelpfulMethods.createDirectoryIfDoNotExist(pathDirectoryForExport);
            File file = File.createTempFile(startNameForExportedFile, ".nt", new File(pathDirectoryForExport));
            FileWriter fw = new FileWriter(file.getAbsolutePath(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            if (!writeData(bw)) return "";
            return file.getAbsolutePath();
        } catch (Exception e) {
            Logger.writeException(e);
            return "";
        }

    }
}
