package cz.cvut.fit.cabaijan.export.objectToExport;

/**
 * Created by Janka on 9/28/2016.
 * AnalysisToExport class uses for getting data for export to the one object
 */
public class AnalysisToExport {
    /**
     * id of domain
     */
    private int idDomain;
    /**
     * name of domain
     */
    private String nameDomain;
    /**
     * count of triples covered by domain
     */
    private int countDomain;
    /**
     * id of entity
     */
    private int idEntity;
    /**
     * path of entity
     */
    private String pathEntity;
    /**
     * count of triples covered by entity
     */
    private int countEntities;
    /**
     * id of predicate
     */
    private int idPredicate;
    /**
     * name of predicate
     */
    private String namePredicate;
    /**
     * count of predicate
     */
    private int countPredicate;

    /**
     * This is a constructor to initialize AnalysisToExport object.
     * @param idDomain id of domain
     * @param nameDomain name of domain
     * @param countDomain count of triples covered by domain
     * @param idEntity id of entity
     * @param pathEntity path of entity
     * @param countEntities count of triples covered by entity
     * @param idPredicate id of predicate
     * @param namePredicate name of predicate
     * @param countPredicate count of predicate
     */
    public AnalysisToExport(int idDomain, String nameDomain, int countDomain, int idEntity, String pathEntity, int countEntities, int idPredicate, String namePredicate, int countPredicate) {
        this.idDomain = idDomain;
        this.nameDomain = nameDomain;
        this.countDomain = countDomain;
        this.idEntity = idEntity;
        this.pathEntity = pathEntity;
        this.countEntities = countEntities;
        this.idPredicate = idPredicate;
        this.namePredicate = namePredicate;
        this.countPredicate = countPredicate;
    }

    /**
     * get id of domain
     * @return id of domain
     */
    public int getIdDomain() {
        return idDomain;
    }

    /**
     * set id of domain
     * @param idDomain a new AnalysisToExport id of domain
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
     * @param nameDomain a new AnalysisToExport name of domain
     */
    public void setNameDomain(String nameDomain) {
        this.nameDomain = nameDomain;
    }

    /**
     * get count of triples covered by domain
     * @return count of triples covered by domain
     */
    public int getCountDomain() {
        return countDomain;
    }

    /**
     * set count of triples covered by domain
     * @param countDomain a new AnalysisToExport count of domain
     */
    public void setCountDomain(int countDomain) {
        this.countDomain = countDomain;
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
     * @param idEntity a new AnalysisToExport id of entity
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
     * @param pathEntity a new AnalysisToExport path of entity
     */
    public void setPathEntity(String pathEntity) {
        this.pathEntity = pathEntity;
    }

    /**
     * get count of triples covered by entities
     * @return count of triples covered by entities
     */
    public int getCountEntities() {
        return countEntities;
    }

    /**
     * set count of triples covered by entities
     * @param countEntities a new AnalysisToExport entity count
     */
    public void setCountEntities(int countEntities) {
        this.countEntities = countEntities;
    }

    /**
     * get id of predicate
     * @return id of predicate
     */
    public int getIdPredicate() {
        return idPredicate;
    }

    /**
     * set id of predicate
     * @param idPredicate a new AnalysisToExport id of predicate
     */
    public void setIdPredicate(int idPredicate) {
        this.idPredicate = idPredicate;
    }

    /**
     * get name of predicate
     * @return name of predicate
     */
    public String getNamePredicate() {
        return namePredicate;
    }

    /**
     * set name of predicate
     * @param namePredicate a new AnalysisToExport name of predicate
     */
    public void setNamePredicate(String namePredicate) {
        this.namePredicate = namePredicate;
    }

    /**
     * get count of triples covered by predicate in particular entity
     * @return count of triples covered by predicate in particular entity
     */
    public int getCountPredicate() {
        return countPredicate;
    }

    /**
     * set count of triples covered by predicate in particular entity
     * @param countPredicate a new AnalysisToExport count of predicate
     */
    public void setCountPredicate(int countPredicate) {
        this.countPredicate = countPredicate;
    }
}
