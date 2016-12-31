package cz.cvut.fit.cabaijan.logs;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Janka on 9/10/2016.
 * Logger class uses as a writer of logs or exceptions o the file
 */
public class Logger {
    /**
     *  file writer
     */
    private static FileWriter fw =null;
    /**
     * buffered writer
     */
    private static BufferedWriter bw = null;
    /**
     * print writer
     */
    private static PrintWriter out = null;
    /**
     * name of the file for the logs
     */
    private static String filename = Logger.class.getResource("/logs/log.txt").getFile().toString();
    /**
     * name of the file for the exceptions
     */
    private static String filenameException = Logger.class.getResource("/logs/Exception.txt").getFile().toString();
    /**
     * date object
     */
    private static Date date;
    /**
     * dateFormat object
     */
    private static DateFormat dateFormat;

    /**
     * methods for writing exception to the file
     * @param ex exception to write
     */
    public static void writeException(Exception ex){
        writeToFile(filenameException, ex.toString());
    }

    /**
     * methods for writing log to the file
     * @param toLog log to write
     */
    public static void writeLog(String toLog) {
        writeToFile(filename, toLog);
    }

    /**
     * create file for logs or exceptions if it does not exist
     * @param filename
     */
    private static void CreateFileIfNotExist(String filename){
        File f = new File(filename);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * write data to the file
     * @param filename name of the file where to write data
     * @param message string for write
     */
    private static void writeToFile(String filename, String message){
        CreateFileIfNotExist(filename);
        try {
            fw = new FileWriter(filename, true);
            bw = new BufferedWriter(fw);
            date = new Date();
            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            out = new PrintWriter(bw);
            out.println(dateFormat.format(date).toString() + " " + message);
            out.close();
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        finally {
            if(out != null)
                out.close();
            try {
                if(bw != null)
                    bw.close();
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
            try {
                if(fw != null)
                    fw.close();
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }
    }
}

