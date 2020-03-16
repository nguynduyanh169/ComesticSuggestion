/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.dao;

import anhnd.comestic.entity.Category;
import java.util.List;
import javax.persistence.EntityManager;
import anhnd.comestic.utils.DBUtils;
import anhnd.comestic.utils.TextUtils;

/**
 *
 * @author anhnd
 */
public class CategoryDAO extends BaseDAO<Category> {

    public CategoryDAO() {
    }

    private static CategoryDAO instance;
    private static final Object LOCK = new Object();

    public static CategoryDAO getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new CategoryDAO();
            }
        }
        return instance;
    }
    
    public synchronized Category getAndInsertIfNewCategory(String name) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            List<Category> categorys = em.createNamedQuery("Category.findByCategoryName", Category.class)
                    .setParameter("categoryName", name)
                    .getResultList();
            if (categorys != null && !categorys.isEmpty()) {
                return categorys.get(0);
            }
            Category category = new Category();
            category.setCategoryId(TextUtils.getUUID());
            category.setCategoryName(name);
            return create(category);
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
