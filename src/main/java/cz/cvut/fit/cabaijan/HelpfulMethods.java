package cz.cvut.fit.cabaijan;

import cz.cvut.fit.cabaijan.dbMethods.DbAccess;
import cz.cvut.fit.cabaijan.logs.Logger;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * Created by Janka on 9/11/2016.
 * HelpfulMethods class contains helpful static methods
 */
public class HelpfulMethods {
    /**
     * get extension of the file
     * @param filename name of the file
     * @return extension of the file in string
     */
    public static String getExtensionOfFile(String filename) {
        String ext = "";
        int i = filename.lastIndexOf('.');
        if (i >= 0) {
            ext = filename.substring(i + 1);
        }
        return ext;

    }

    /**
     * delete files which are not longer needed
     * @param pathOfTmpFile path to the file which should be deleted
     * @return true, if removal od the file was successful
     */
    public static boolean deleteTmpFile(String pathOfTmpFile) {
        try {
            File file = new File(pathOfTmpFile);

            if (file.delete()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            Logger.writeException(e);
            return false;
        }
    }

    /**
     * check, if the url is right
     * @param name name of the url
     * @return response code, 202 if ok
     */
    public static int checkUrl(String name) {
        try {
            URL u = new URL(name);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            return huc.getResponseCode();
        } catch (MalformedURLException | ProtocolException e) {
            Logger.writeException(e);
        } catch (IOException e) {
            Logger.writeException(e);
        }
        return 0;
    }

    /**
     * create file from input stream
     * @param inputStream input stream to write to the file
     * @param filename name of the file
     * @return absolute path to the created file
     */
    public static String makeFileFromInputStream(InputStream inputStream, String filename) {
        File file;
        FileOutputStream fop = null;

        try {
            file = createFile(filename);
            fop = new FileOutputStream(file);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                fop.write(bytes, 0, read);
            }

            fop.flush();
            fop.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            Logger.writeException(e);
            return "";
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * create file if do not exist
     * @param filename name of the file
     * @return created file
     */
    public static File createFile(String filename) {
        File file;
        try {
            file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;

        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }

    }

    /**
     * create file and write string to it
     * @param filename name of the file
     * @param content content to write
     * @return true, if writing was successful
     */
    public static Boolean makeFileAndWriteString(String filename, String content) {
        FileOutputStream fop = null;
        File file;

        try {

            file = createFile(filename);
            fop = new FileOutputStream(file);
            byte[] contentInBytes = content.getBytes();
            fop.write(contentInBytes);
            fop.flush();
            fop.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * create directory if do not exist
     * @param relativePath relative path to directory
     */
    public static void createDirectoryIfDoNotExist(String relativePath) {
        File file = new File(relativePath);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
    }


}

