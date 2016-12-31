package cz.cvut.fit.cabaijan.dbObjects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Janka on 9/8/2016.
 * EntityGroup class uses for storing information about entityGroup to database.
 * To one EntityGroup belongs entities, which are the same, only with different naming in different datasets
 */
@XmlRootElement
@DatabaseTable(tableName = "entityGroup")
public class EntityGroup {
    /**
     * database id of entity group
     */
    @DatabaseField(generatedId = true)
    private int id;
    /**
     * name of entity group. Name contains names of entities, which belong to it.
     */
    @DatabaseField(canBeNull = false)
    private String name;

    /**
     * This is a constructor to initialize EntityGroup object. It hs to be empty because of ORMLite.
     */
    public EntityGroup() {
    }

    /**
     * This is a parametrized constructor to initialize EntityGroup object.
     * @param id id of entity group
     * @param name name of entity group
     */
    public EntityGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * get id of entity group
     * @return id of entity group
     */
    public int getId() {
        return id;
    }

    /**
     * set id of entity group
     * @param id a new EntityGroup id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * get name of entityGroup
     * @return name of entityGroup
     */
    public String getName() {
        return name;
    }

    /**
     * set name of entityGroup
     * @param nameToAdd name of entityGroup to add.
     */
    public void setName(String nameToAdd) {
        if (this.name != null) {
            this.name += "," + nameToAdd;
        } else {
            this.name = nameToAdd;
        }

    }
}
