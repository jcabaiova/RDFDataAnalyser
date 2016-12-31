package cz.cvut.fit.cabaijan.export.objectToExport;

/**
 * Created by Janka on 9/28/2016.
 * DomainsToExport class uses for getting data for export to the one object
 */
public class DomainsToExport {
    /**
     * id of domain
     */
    private Integer idDomain;
    /**
     * name of domain
     */
    private String nameDomain;
    /**
     * id of entity
     */
    private int idEntity;
    /**
     * path of entity
     */
    private String pathEntity;
    /**
     * id of entity group
     */
    private int idEntityGroup;
    /**
     * name of entity group
     */
    private String nameEntityGroup;

    /**
     * This is a constructor to initialize DomainsToExport object.
     * @param idDomain id of domain
     * @param nameDomain name of domain
     * @param idEntity id of entity
     * @param pathEntity path of entity
     * @param idEntityGroup id of entity group
     * @param nameEntityGroup name of entity group
     */
    public DomainsToExport(int idDomain, String nameDomain, int idEntity, String pathEntity, int idEntityGroup, String nameEntityGroup) {
        this.idDomain = idDomain;
        this.nameDomain = nameDomain;
        this.idEntity = idEntity;
        this.pathEntity = pathEntity;
        this.idEntityGroup = idEntityGroup;
        this.nameEntityGroup = nameEntityGroup;
    }

    /**
     * get id of domain
     * @return id of domain
     */
    public Integer getIdDomain() {
        return idDomain;
    }

    /**
     * set id of domain
     * @param idDomain a new DomainsToExport id of domain
     */
    public void setIdDomain(int idDomain) {
        this.idDomain = idDomain;
    }

    /**
     * get name of domain
     * @return name of domain
     */
    public String getNameDomain() {
        return nameDomain;
    }

    /**
     * set name of domain
     * @param nameDomain a new DomainsToExport name of domain
     */
    public void setNameDomain(String nameDomain) {
        this.nameDomain = nameDomain;
    }

    /**
     * get id of entity
     * @return id of entity
     */
    public int getIdEntity() {
        return idEntity;
    }

    /**
     * set id of entity
     * @param idEntity a new DomainsToExport id of entity
     */
    public void setIdEntity(int idEntity) {
        this.idEntity = idEntity;
    }

    /**
     * get path of entity
     * @return path of entity
     */
    public String getPathEntity() {
        return pathEntity;
    }

    /**
     * set path of entity
     * @param pathEntity a new DomainsToExport path of entity
     */
    public void setPathEntity(String pathEntity) {
        this.pathEntity = pathEntity;
    }

    /**
     * get id of entity group
     * @return id of entity group
     */
    public int getIdEntityGroup() {
        return idEntityGroup;
    }

    /**
     * set id of entity group
     * @param idEntityGroup a new DomainsToExport id of entity group
     */
    public void setIdEntityGroup(int idEntityGroup) {
        this.idEntityGroup = idEntityGroup;
    }

    /**
     * get name of entity group
     * @return name of entity group
     */
    public String getNameEntityGroup() {
        return nameEntityGroup;
    }

    /**
     * set name of entity group
     * @param nameEntityGroup a new DomainsToExport name of entity group
     */
    public void setNameEntityGroup(String nameEntityGroup) {
        this.nameEntityGroup = nameEntityGroup;
    }
}
