import cz.cvut.fit.cabaijan.HelpfulMethods;
import cz.cvut.fit.cabaijan.dbMethods.DbAccess;
import cz.cvut.fit.cabaijan.dbObjects.Dataset;
import cz.cvut.fit.cabaijan.dbObjects.Domain;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static java.lang.System.in;
import static org.junit.Assert.*;

/**
 * Created by Janka on 11/27/2016.
 */

public class MyTest {

    @Before
    public void createTmpFile() {
        HelpfulMethods.createFile("ccc.txt");
    }

    @Test
    public void shouldGetRightExtensionOfFileTest() {
      String fileNameOk = "bootstrap.min.js";
        String fileNameBad = "abcdefgh";

        assertEquals("Extension of file"+fileNameOk+"must be js","js",HelpfulMethods.getExtensionOfFile(fileNameOk));
        assertEquals("Extension of file"+fileNameOk+"is none","",HelpfulMethods.getExtensionOfFile(fileNameBad));
    }

    @Test
    public void shouldMakeFileTest() {
        String newFile="aaa.txt";
        assertNotNull("File "+ newFile + "must be created",HelpfulMethods.createFile(newFile));
        assertNotNull("File "+ newFile + "is not created twice",HelpfulMethods.createFile(newFile));
        assertNull("File without filename must not be created",HelpfulMethods.createFile(""));
    }

    @Test
    public void shouldDeleteFileTest() {
        String deleteFile="ccc.txt";
        assertTrue("File "+deleteFile + "must be deleted",HelpfulMethods.deleteTmpFile(deleteFile));
        assertFalse("File "+deleteFile + "is not exist",HelpfulMethods.deleteTmpFile(deleteFile));
    }

    @Test
    public void shouldCheckUrlTest() {
        String url="https://www.google.cz/";
        String badUrl="aaa";
        assertEquals("Url " + url + "must be active",200,HelpfulMethods.checkUrl(url));
        assertEquals("Url " + url + "must be active",0,HelpfulMethods.checkUrl(badUrl));
    }

    @Test
    public void shouldMakeFileFromInputStreamTest() {
        ByteArrayInputStream testInputStream = new ByteArrayInputStream("My string".getBytes());
        String fileName="bbb.txt";
        //assertEquals("File from inputstream must be created","C:\\Users\\Janka\\Diplomka\\RDFDataAnalyser\\bbb.txt",HelpfulMethods.makeFileFromInputStream(testInputStream,fileName));

    }

    @Test
    public void shouldMakeFileAndWriteToItTest() {
        String fileName="ddd.txt";
        String content1="ddd";
        String content2="";
        assertTrue("",HelpfulMethods.makeFileAndWriteString(fileName,content1));
        assertTrue("",HelpfulMethods.makeFileAndWriteString(fileName,content2));
    }

    @After
    public void deleteTmpFiles() {
        HelpfulMethods.deleteTmpFile("aaa.txt");
        HelpfulMethods.deleteTmpFile("bbb.txt");
        HelpfulMethods.deleteTmpFile("ccc.txt");
        HelpfulMethods.deleteTmpFile("ddd.txt");

    }


}
