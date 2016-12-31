import cz.cvut.fit.cabaijan.dbMethods.DB;
import cz.cvut.fit.cabaijan.dbMethods.DbAccess;
import cz.cvut.fit.cabaijan.dbObjects.Domain;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Janka on 12/4/2016.
 */
public class DbAccessTest {

    DbAccess dbAccess;
    @Before
    public void initiateTestDb() {
 //      dbAccess = new DbAccess("jdbc:sqlite:C:\\Users\\Janka\\Diplomka\\RDFDataAnalyser\\db\\testDb.db");
    }

    @Test
    public void shouldCreateAndUpdateDomainTest() {
        Domain domain = new Domain();
        domain.setName("Sport and recreation9");
        Domain domainInDb1 = dbAccess.createDomain(domain);
        assertNotNull("Domain must be created",domainInDb1);
        Domain domainInDb2 = dbAccess.createDomain(domain);
        assertNull("Domain must not be created",domainInDb2);
        assertTrue("Domain name must be updated",dbAccess.updateNameOfDomain(domainInDb1.getId(),"aaaa"));

    }






}
