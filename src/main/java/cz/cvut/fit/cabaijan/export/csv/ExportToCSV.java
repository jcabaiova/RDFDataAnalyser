package cz.cvut.fit.cabaijan.export.csv;

import cz.cvut.fit.cabaijan.HelpfulMethods;
import cz.cvut.fit.cabaijan.dbMethods.DbAccess;
import cz.cvut.fit.cabaijan.logs.Logger;
import cz.cvut.fit.cabaijan.dbObjects.DatasetAnalysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created by Janka on 10/1/2016.
 * ExportToCSV class uses for exporting dataset analysis or domains to CSV.
 * It is an abstract class, it contains one class for export of dataset analysis and one class for export of domains
 */
public abstract class ExportToCSV {
    /**
     * object DbAccess for getting access to database
     */
    public DbAccess dbAccess;
    /**
     * default separator in CSV file
     */
    public Character default_separator;
    /**
     * directory path for the exported file
     */
    public String pathDirectoryForExport;
    /**
     * start name for the created file
     */
    public String startNameForExportedFile;
    /**
     * dataset analysis for which the export is created. Only in the case of the dataset analysis export.
     */
    public DatasetAnalysis datasetAnalysis;

    /**
     * write data for the particular export by the help of writer
     * @param writer writer
     */
    public abstract void writeData(Writer writer);

    /**
     * method for write one line
     * @param w writer
     * @param values values to write to the line
     */
    public void writeLine(Writer w, List<String> values) {
        writeLine(w, values, default_separator, ' ');
    }

    /**
     * method for replacing character "\"
     * @param value value in the line
     * @return replaced value
     */
    private String followCVSformat(String value) {

        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;

    }
    /**
     * method for write one line
     * @param w writer
     * @param values values to write to the line
     * @param separators char for specification of separator in CSV file
     * @param customQuote char for specification of cusom quote in CSV file
     */
    private void writeLine(Writer w, List<String> values, char separators, char customQuote) {

        try {

            boolean first = true;

            //default customQuote is empty

            if (separators == ' ') {
                separators = default_separator;
            }

            StringBuilder sb = new StringBuilder();
            for (String value : values) {
                if (!first) {
                    sb.append(separators);
                }
                if (' ' == ' ') {
                    sb.append(followCVSformat(value));
                } else {
                    sb.append(' ').append(followCVSformat(value)).append(' ');
                }

                first = false;
            }
            sb.append("\n");
            w.append(sb.toString());


        } catch (IOException e) {
            Logger.writeException(e);
        }
    }

    /**
     * creating file and write to it
     * @return absolute path to the new created CSV file
     */
    public String exportData() {
        try {
            HelpfulMethods.createDirectoryIfDoNotExist(pathDirectoryForExport);
            File file = File.createTempFile(startNameForExportedFile, ".csv", new File(pathDirectoryForExport));
            FileWriter writer = new FileWriter(file.getAbsolutePath());
            writeData(writer);
            writer.flush();
            writer.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            Logger.writeException(e);
        }
        return "";
    }
}
