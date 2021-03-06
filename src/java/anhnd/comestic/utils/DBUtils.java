/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author anhnd
 */
public class DBUtils {

    public DBUtils() {
    }
    
    private static EntityManagerFactory emf;
    private static final Object LOCK = new Object();

    public static EntityManager getEntityManager() {
        synchronized (LOCK) {
            if (emf == null) {
                try {
                    emf = Persistence.createEntityManagerFactory("Comestic");
                } catch (Exception e) {
                    Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
        return emf.createEntityManager();
    }
    
    public static Connection getMyConnection() throws Exception {
        Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=AppropicateCosmetic", "sa", "1234567");
        return conn;
    }
    
}
