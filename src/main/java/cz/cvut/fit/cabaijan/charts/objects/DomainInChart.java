package cz.cvut.fit.cabaijan.charts.objects;

/**
 * Created by Janka on 10/9/2016.
 * DomainInChart class uses for getting information for drawing chart, which are transformed to JSON object
 */
public class DomainInChart {
    /**
     * name of domain in chart
     */
    private String domainName;
    /**
     * count of entities which belongs to domain
     */
    private int countOfEntities;

    /**
     * This is a constructor to initialize DomainInChart object.
     * @param domainName name of domain in chart
     * @param countOfEntities count of entities which belongs to domain
     */
    public DomainInChart(String domainName, int countOfEntities) {
        this.domainName = domainName;
        this.countOfEntities = countOfEntities;
    }

    /**
     * get domain name in chart
     * @return domain name in chart
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * set domain name in chart
     * @param name a new DomainInChart name
     */
    public void setDomainName(String name) {
        this.domainName = name;
    }

    /**
     * get count of entities in domain in chart
     * @return count of entities in domain in chart
     */
    public int getCountOfEntities() {
        return countOfEntities;
    }

    /**
     * set count of entities in domain in chart
     * @param countOfEntities a new DomainInChart count of entities
     */
    public void setCountOfEntities(int countOfEntities) {
        this.countOfEntities = countOfEntities;
    }
}
