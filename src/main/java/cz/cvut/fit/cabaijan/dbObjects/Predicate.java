package cz.cvut.fit.cabaijan.dbObjects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Janka on 9/12/2016.
 * Predicate class uses for storing information about predicate to database
 */

@XmlRootElement
@DatabaseTable(tableName = "predicate")
public class Predicate implements Comparable<Predicate> {
    /**
     * database id of predicate
     */
    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;
    /**
     * name of predicate
     */
    @DatabaseField()
    private String name;
    /**
     * number of occurence of predicate in concrete dataset
     */
    @DatabaseField(defaultValue = "0")
    private int countPredicates;
    /**
     * entityInDataset to which predicate belongs
     */
    @DatabaseField(foreign = true)
    private EntityInDataset entityInDataset;

    /**
     * This is a constructor to initialize Predicate object. It hs to be empty because of ORMLite.
     */
    public Predicate() {
    }

    /**
     * get predicate id
     * @return predicate id
     */
    public int getId() {
        return id;
    }

    /**
     * set predicate id
     * @param id a new predicate id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * get predicate name
     * @return predicate name
     */
    public String getName() {
        return name;
    }

    /**
     * set predicate name
     * @param name a new predicate name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get number of occurence of predicates in concrete dataset
     * @return number of occurence of predicates in concrete dataset
     */
    public int getCountPredicates() {
        return countPredicates;
    }

    /**
     * set number of occurence of predicates in concrete dataset
     * @param countPredicates a new predicate number of occurence
     */
    public void setCountPredicates(int countPredicates) {
        this.countPredicates = countPredicates;
    }

    /**
     * get entityInDataset to which predicate belongs
     * @return entityInDataset to which predicate belongs
     */
    public EntityInDataset getEntityInDataset() {
        return entityInDataset;
    }

    /**
     * set entityInDataset to which predicate belongs
     * @param entityInDataset a new predicate entityInDataset
     */
    public void setEntityInDataset(EntityInDataset entityInDataset) {
        this.entityInDataset = entityInDataset;
    }

    /**
     * compare predicates in list on the base of countPredicates
     * @param o predicate, which should be compared
     * @return compared value
     */
    @Override
    public int compareTo(Predicate o) {
        int compareCount = ((Predicate) o).getCountPredicates();
        return compareCount - this.countPredicates;
    }
}
