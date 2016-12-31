package cz.cvut.fit.cabaijan.dbObjects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Janka on 9/12/2016.
 * DomainInDataset class uses for storing information about domain in dataset to database. It is help table where are stored calculated information
 * about concrete dataset
 */

@XmlRootElement
@DatabaseTable(tableName = "domainInDataset")
public class DomainInDataset {

    /**
     * domainInDataset id
     */
    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;
    /**
     * domainInDataset domain
     */
    @DatabaseField(foreign = true)
    private Domain domain;
    /**
     * domainInDataset datasetAnalysis
     */
    @DatabaseField(foreign = true)
    private DatasetAnalysis datasetAnalysis;
    /**
     * sum of triples in entities belongs to particular domain
     */
    @DatabaseField(defaultValue = "0")
    private int countDomain;

    /**
     * This is a constructor to initialize DomainInDataset object. It has to be empty because of ORMLite.
     */
    public DomainInDataset() {
    }

    /**
     * get domainInDataset id
     * @return domainInDataset id
     */
    public int getId() {
        return id;
    }

    /**
     * get domain of domainInDataset
     * @return domain of domainInDataset
     */
    public Domain getDomain() {
        return domain;
    }

    /**
     * domain of domainInDataset
     * @param domain domain of a new domainInDataset
     */
    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    /**
     * get dataset analysis of domainInDataset
     * @return dataset analysis of domainInDataset
     */
    public DatasetAnalysis getDatasetAnalysis() {
        return datasetAnalysis;
    }

    /**
     * set dataset analysis of domainInDataset
     * @param datasetAnalysis dataset analysis of a new domainInDataset
     */
    public void setDatasetAnalysis(DatasetAnalysis datasetAnalysis) {
        this.datasetAnalysis = datasetAnalysis;
    }

    /**
     * get domain count, which is a sum of triples in entities belongs to particular domain
     * @return domain count
     */
    public int getCountDomain() {
        return countDomain;
    }

    /**
     * set domain count, which is a sum of triples in entities belongs to particular domain
     * @param countDomain calculated count of domain in a new domainInDataset
     */
    public void setCountDomain(int countDomain) {
        this.countDomain = countDomain;
    }

}
