/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.dao;

import anhnd.comestic.entity.Users;
import anhnd.comestic.utils.DBUtils;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author anhnd
 */
public class UserDAO extends BaseDAO<Users> {

    public UserDAO() {
    }

    private static UserDAO instance;
    private static final Object LOCK = new Object();

    public static UserDAO getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new UserDAO();
            }
        }
        return instance;
    }
    
    public synchronized Users checkUser(String email, String password) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            List<Users> listUser = em.createNativeQuery("select * from Users where Email=? and Password=?", Users.class)
                    .setParameter(1, email)
                    .setParameter(2, password)
                    .getResultList();
            if (!listUser.isEmpty()) {
                return listUser.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
    
    public synchronized Users getUserById(String userId) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            List<Users> listuser = em.createNativeQuery("select * from Users where UserId =?", Users.class)
                    .setParameter(1, userId)
                    .getResultList();
            if (!listuser.isEmpty()) {
                return listuser.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
    
}
