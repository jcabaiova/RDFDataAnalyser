package cz.cvut.fit.cabaijan.dbObjects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Janka on 9/8/2016.
 * Domain class uses for storing information about domain to database
 */

@XmlRootElement
@DatabaseTable(tableName = "domain")
public class Domain {

    /**
     * database id of domain
     */
    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;
    /**
     * name of domain
     */
    @DatabaseField(canBeNull = false, unique = true)
    private String name;
    /**
     * path of domain in N-Triple format
     */
    @DatabaseField(canBeNull = false, unique = true)
    private String path;

    /**
     * This is a constructor to initialize Domain object. It hs to be empty because of ORMLite.
     */
    public Domain() {
    }

    /**
     * This is a parametrized constructor to initialize Domain object.
     * @param id id of domain
     * @param name name of domain
     * @param path path of domain
     */
    public Domain(int id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    /**
     * get domain id
     * @return domain id
     */
    public int getId() {
        return id;
    }

    /**
     * get domain name
     * @return domain name
     */
    public String getName() {
        return name;
    }

    /**
     * set domain name
     * @param name a new domain name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get domain path
     * @return domain path
     */
    public String getPath() {
        return path;
    }

    /**
     * set domain path
     * @param path a new domain path
     */
    public void setPath(String path) {
        this.path = path;
    }
}
