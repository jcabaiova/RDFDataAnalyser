package cz.cvut.fit.cabaijan.charts.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Janka on 10/29/2016.
 * DatasetAnalysisInChart class uses for getting information for drawing chart, which are transformed to JSON object
 */
public class DatasetAnalysisInChart {
    /**
     * name of json element, it is domain or entity
     */
    private String name;
    /**
     * children of json element (this chart has two levels)
     */
    private List<DatasetAnalysisInChart> children;
    /**
     * size of json element, it is domain or entity
     */
    private int size;

    /**
     *This is a constructor to initialize DatasetAnalysisInChart object.
     */
    public DatasetAnalysisInChart() {
        this.children = new ArrayList<>();
        this.size = 0;
    }
    /**
     * get size of element, which is number triples in domain or in entity
     * @return size of element
     */
    public int getSize() {
        return size;
    }

    /**
     * set size of element, which is number triples in domain or in entity
     * @param size a new DatasetAnalysisInChart size of element
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * get name of element, which is name of domain or entity
      * @return name of element, which is name of domain or entity
     */
    public String getName() {
        return name;
    }

    /**
     * set name of element, which is name of domain or entity
     * @param name a new DatasetAnalysisInChart name of element
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * add DatasetAnalysisInChart object to children
     * @param datasetAnalysisInChart added object
     */
    public void addToChildren(DatasetAnalysisInChart datasetAnalysisInChart) {
        this.children.add(datasetAnalysisInChart);
    }

    /**
     * add size of DatasetAnalysisInChart which is connected with adding children
     * @param size size of added object
     */
    public void addToSize(int size) {
        this.size += size;
    }
    public List<DatasetAnalysisInChart> getChildren() {
        return children;
    }

    public void setChildren(List<DatasetAnalysisInChart> children) {
        this.children = children;
    }
}
