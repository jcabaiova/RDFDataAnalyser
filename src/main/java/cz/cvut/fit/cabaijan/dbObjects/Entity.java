package cz.cvut.fit.cabaijan.dbObjects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Janka on 9/8/2016.
 * Entity class uses for storing information about entity to database
 */

@XmlRootElement
@DatabaseTable(tableName = "entity")
public class Entity {
    /**
     * database id of entity
     */
    @DatabaseField(generatedId = true)
    private int id;
    /**
     * name of entity
     */
    @DatabaseField(canBeNull = false)
    private String name;
    /**
     * path of entity, link in N-Triples format
     */
    @DatabaseField(canBeNull = false, unique = true)
    private String path;
    /**
     * entityGroup of entity, which defines links between entities
     */
    @DatabaseField(foreign = true)
    private EntityGroup entityGroup;
    /**
     * domain to which particular entity belongs
     */
    @DatabaseField(foreign = true)
    private Domain domain;

    /**
     * This is a constructor to initialize Entity object. It has to be empty because of ORMLite.
     */
    public Entity() {
    }

    /**
     * This is a parametrized constructor to initialize Entity object.
     * @param path path of entity
     * @param entityGroup entityGroup to which entity belongs
     * @param domain domain to which entity belongs
     */
    public Entity(String path, EntityGroup entityGroup, Domain domain) {

        this.path = path;
        this.entityGroup = entityGroup;
        this.domain = domain;
    }

    /**
     * get entity id
     * @return entity ide
     */
    public int getId() {
        return id;
    }

    /**
     * set entity id
     * @param id a new entity id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * get entity name
     * @return entity name
     */
    public String getName() {
        return name;
    }

    /**
     * set entity name
     * @param path a new entity name
     */
    public void setName(String path) {

        String[] splitted = path.split("/");
        this.name = splitted[splitted.length - 1];
    }

    /**
     * get entity path
     * @return entity path
     */
    public String getPath() {
        return path;
    }

    /**
     * set entity path
     * @param path a new entity path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * get entityGroup to which entity belongs
     * @return entityGroup to which entity belongs
     */
    public EntityGroup getEntityGroup() {
        return entityGroup;
    }

    /**
     * set entityGroup to which entity belongs
     * @param entityGroup a new entity entityGroup
     */
    public void setEntityGroup(EntityGroup entityGroup) {
        this.entityGroup = entityGroup;
    }

    /**
     * get domain to which entity belongs
     * @return domain to which entity belongs
     */
    public Domain getDomain() {
        return domain;
    }

    /**
     * set domain to which entity belongs
     * @param domain a new entity domain
     */
    public void setDomain(Domain domain) {
        this.domain = domain;
    }

}

