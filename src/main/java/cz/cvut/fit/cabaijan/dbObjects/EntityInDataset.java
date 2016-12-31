package cz.cvut.fit.cabaijan.dbObjects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Janka on 9/12/2016.
 * EntityInDataset class uses for storing information about entity in dataset to database.
 * It is help table where are stored calculated information of entity in concrete dataset.
 */

@XmlRootElement
@DatabaseTable(tableName = "entityInDataset")
public class EntityInDataset implements Comparable<EntityInDataset> {
    /**
     * database id of entity in dataset
     */
    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;
    /**
     * entity to which entityInDataset belongs
     */
    @DatabaseField(foreign = true)
    private Entity entity;
    /**
     * datasetAnalysis to which entityInDataset belongs
      */
    @DatabaseField(foreign = true)
    private DatasetAnalysis datasetAnalysis;
    /**
     * count of triples which were calculated on the base of entity
     */
    @DatabaseField(defaultValue = "0")
    private int countEntities;

    /**
     * This is a constructor to initialize EntityInDataset object. It has to be empty because of ORMLite.
     */
    public EntityInDataset() {
    }

    /**
     * get id of entityInDataset
     * @return id of entityInDataset
     */
    public int getId() {
        return id;
    }

    /**
     * set id of entityInDataset
     * @param id a new entityInDataset id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * get entity to which entityInDataset belongs
     * @return entity to which entityInDataset belongs
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * set entity to which entityInDataset belongs
     * @param entity a new entityInDataset entity
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * get datasetAnalysis to which entityInDataset belongs
     * @return datasetAnalysis to which entityInDataset belongs
     */
    public DatasetAnalysis getDatasetAnalysis() {
        return datasetAnalysis;
    }

    /**
     * set datasetAnalysis to which entityInDataset belongs
     * @param datasetAnalysis datasetAnalysis to which entityInDataset belongs
     */
    public void setDatasetAnalysis(DatasetAnalysis datasetAnalysis) {
        this.datasetAnalysis = datasetAnalysis;
    }

    /**
     * get count of triples which were calculated on base of this entity
     * @return count of triples which were calculated on base of this entity
     */
    public int getCountEntities() {
        return countEntities;
    }

    /**
     * set count of triples which were calculated on base of this entity
     * @param countEntities a new entityIndataset count of triples belongs to entity
     */
    public void setCountEntities(int countEntities) {
        this.countEntities = countEntities;
    }

    /**
     * compare entities in dataset in list on the base of entityCount
     * @param o entityInDataset, which should be compared
     * @return compared value
     */
    @Override
    public int compareTo(EntityInDataset o) {
        int compareCount = ((EntityInDataset) o).getCountEntities();
        return compareCount - this.countEntities;
    }
}
