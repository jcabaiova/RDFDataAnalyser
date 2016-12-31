package cz.cvut.fit.cabaijan.dbMethods;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Janka on 9/13/2016.
 * DB class uses for getting access to database and for handling database lock
 */
public class DB {
    /**
     *  object DbAccess for getting access to database
     */
    private static DbAccess dbAccess = null;
    /**
     * object ReentrantLock for handling locks of databse
     */
    private static ReentrantLock dbLock = null;

    /**
     * get dbAccess for database access
     * @return dbAccess for database access
     */
    public static DbAccess getDbAccess() {
        if (dbAccess == null) {
            dbAccess = new DbAccess();
        }
        return dbAccess;
    }

    /**
     * get dbLock
     * @return dbLock
     */
    private static ReentrantLock getDbLock() {
        if (dbLock == null) {
            dbLock = new ReentrantLock();
        }
        return dbLock;
    }

    /**
     * lock the database
     */
    public static void lock() {
        if (!getDbLock().isLocked())
            getDbLock().lock();
    }

    /**
     * unlock the database
     */
    public static void unlock() {
        if (getDbLock().isLocked())
            getDbLock().unlock();
    }
}



