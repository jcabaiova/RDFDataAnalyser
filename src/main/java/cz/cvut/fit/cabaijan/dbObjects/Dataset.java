package cz.cvut.fit.cabaijan.dbObjects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import cz.cvut.fit.cabaijan.HelpfulMethods;
import cz.cvut.fit.cabaijan.logs.Logger;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * Created by Janka on 9/11/2016.
 * Dataset class uses for storing information about dataset to database
 */

@XmlRootElement
@DatabaseTable(tableName = "dataset")
public class Dataset {
    /**
     * id of dataset
     */
    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;
    /**
     * name of dataset
     */
    @DatabaseField(canBeNull = false, unique = true)
    private String name;
    /**
     * description about dataset
     */
    @DatabaseField
    private String description;
    /**
     * in case of hdt, it is filename. In case of SPARQL endpoint, it is path to it.
     */
    @DatabaseField(canBeNull = false, unique = true)
    private String fileName;
    /**
     * attribute which defines if dataset is SPARQL endpoint
     */
    @DatabaseField
    private Boolean isSparqlEndpoint;
    /**
     * attribute which defines if dataset is in HDT format
     */
    @DatabaseField
    private Boolean isHdt;
    /**
     * name of predicate which is used in particular dataset
     */
    @DatabaseField
    private String ontologyPredicate;

    /**
     * This is a constructor to initialize Dataset object. It hs to be empty because of ORMLite.
     */
    public Dataset() {
    }

    /**
     * get dataset id
     * @return dataset id
     */
    public int getId() {
        return id;
    }

    /**
     * get dataset name
     * @return dataset name
     */
    public String getName() {
        return name;
    }

    /**
     * set dataset name
     * @param name a new dataset name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get dataset description
     * @return dataset description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set dataset description
     * @param description a new dataset description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get if dataset is SPARQL endpoint
     * @return if dataset is SPARQL endpoint
     */
    public Boolean getSparqlEndpoint() {
        return isSparqlEndpoint;
    }

    /**
     * get dataset path, in case of HDT it is filename, in case of SPARQL endpoint it is path to it
     * @return dataset path, in case of HDT it is filename, in case of SPARQL endpoint it is path to it
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * set dataset path
     * @param fileName a new dataset path
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * set if dataset is SPARQL endpoint
     * @param sparqlEndpoint a new dataset identification if it is a SPARQL endpoint
     */
    public void setSparqlEndpoint(Boolean sparqlEndpoint) {
        isSparqlEndpoint = sparqlEndpoint;
    }

    /**
     * set file type, HDT or SPARQL endpoint
     * @param filename a new dataset filename
     */
    public void setFileType(String filename) {

        try {
            if (Objects.equals(filename, "")) {
                throw new Exception("FileName is empty");
            }
            String ext = HelpfulMethods.getExtensionOfFile(filename);
            if (ext.equals("hdt")) {
                this.isSparqlEndpoint = false;
                this.isHdt = true;
            } else {
                this.isSparqlEndpoint = true;
                this.isHdt = false;
            }
        } catch (Exception e) {
            Logger.writeException(e);
        }
    }

    /**
     * get if dataset is in HDT format
     * @return if dataset is in HDT format
     */
    public Boolean getHdt() {
        return isHdt;
    }

    /**
     * set if dataset is in HDT format
     * @param hdt a new dataset identification if dataset is in HDT format
     */
    public void setHdt(Boolean hdt) {
        isHdt = hdt;
    }

    /**
     * get ontology predicate of dataset
     * @return ontology predicate of dataset
     */
    public String getOntologyPredicate() {
        return ontologyPredicate;
    }

    /**
     * set ontology predicate of dataset
     * @param ontologyPredicate a new dataset ontology predicate
     */
    public void setOntologyPredicate(String ontologyPredicate) {
        this.ontologyPredicate = ontologyPredicate;
    }


}
