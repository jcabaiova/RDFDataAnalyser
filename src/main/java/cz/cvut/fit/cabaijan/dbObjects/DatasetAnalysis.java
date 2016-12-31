package cz.cvut.fit.cabaijan.dbObjects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Janka on 9/11/2016.
 * DatasetAnalysis class uses for storing information about calulated analysis of dataset to database
 */
@XmlRootElement
@DatabaseTable(tableName = "datasetAnalysis")
public class DatasetAnalysis {

    /**
     * database id of DatasetAnalysis
     */
    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;
    /**
     * name of DatasetAnalysis
     */
    @DatabaseField(canBeNull = false, unique = true)
    private String name;
    /**
     * description about DatasetAnalysis
     */
    @DatabaseField
    private String description;
    /**
     * specification of domains which will be used in calculation. It is filled only in case of creating new dataset analysis on already existed dataset.
     */
    @DatabaseField
    private String domainsFromInput;
    /**
     * number of domains used for calculation of dataset analysis
     */
    @DatabaseField
    private int domainCount;
    /**
     * number of triples in particular dataset calculated on the base of entities
     */
    @DatabaseField
    private int entityCount;
    /**
     * identification if calculated dataset analysis will be a short one, which means without calculation of predicates linked to particular entities
     */
    @DatabaseField
    private Boolean shortCalculationWithoutPredicates;
    /**
     * dataset of dataset analysis
     */
    @DatabaseField(foreign = true)
    private Dataset dataset;

    /**
     * This is a constructor to initialize DatasetAnalysis object. It hs to be empty because of ORMLite.
     */
    public DatasetAnalysis() {
    }

    /**
     * get dataset of dataset analysis
     * @return dataset of dataset analysis
     */
    public Dataset getDataset() {
        return dataset;
    }

    /**
     * set dataset to dataset analysis
     * @param dataset a new dataset analysis dataset
     */
    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    /**
     * get id of dataset analysis
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * get name of dataset analysis
     * @return name of dataset analysis
     */
    public String getName() {
        return name;
    }

    /**
     * set name of dataset analysis
     * @param name a new dataset analysis name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get description of dataset analysis
     * @return description of dataset analysis
     */
    public String getDescription() {
        return description;
    }

    /**
     * set description of dataset analysis
     * @param description a new dataset analysis description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get domains for calculation of dataset analysis
     * @return domains for calculation of dataset analysis
     */
    public String getDomainsFromInput() {
        return domainsFromInput;
    }

    /**
     * set domains for calculation of dataset analysis
     * @param domainsFromInput a new dataset analysis domains for calculation
     */
    public void setDomainsFromInput(String domainsFromInput) {
        this.domainsFromInput = domainsFromInput;
    }

    /**
     * get count of domains which were used for calculation of dataset analysis
     * @return count of domains which were used for calculation of dataset analysis
     */
    public int getDomainCount() {
        return domainCount;
    }

    /**
     * set count of domains which were used for calculation of dataset analysis
     * @param domainCount a new dataset analysis count of domains
     */
    public void setDomainCount(int domainCount) {
        this.domainCount = domainCount;
    }

    /**
     * get count of triples based on entities which were calculated
     * @return count of triples based on entities which were calculated
     */
    public int getEntityCount() {
        return entityCount;
    }

    /**
     * set count of triples based on entities from calculation
     * @param entityCount a new dataset analysis caount of calculated triples
     */
    public void setEntityCount(int entityCount) {
        this.entityCount = entityCount;
    }

    /**
     * get type of calculation of datasetAnalysis
     * @return type of calculation of datasetAnalysis
     */
    public Boolean getShortCalculationWithouPredicates() {
        return shortCalculationWithoutPredicates;
    }

    /**
     * set type of calculation of datasetAnalysis
     * @param shortCalculationWithouPredicates a new dataset analysis type of calculation
     */
    public void setShortCalculationWithouPredicates(Boolean shortCalculationWithouPredicates) {
        this.shortCalculationWithoutPredicates = shortCalculationWithouPredicates;
    }
}


