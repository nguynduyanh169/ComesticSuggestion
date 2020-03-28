/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.dao;

import anhnd.comestic.entity.Category;
import anhnd.comestic.entity.SubCategory;
import anhnd.comestic.utils.DBUtils;
import anhnd.comestic.utils.TextUtils;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author anhnd
 */
public class SubCategoryDAO extends BaseDAO<SubCategory>{

    public SubCategoryDAO() {
    }
    
    private static SubCategoryDAO instance;
    private static final Object LOCK = new Object();

    public static SubCategoryDAO getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new SubCategoryDAO();
            }
        }
        return instance;
    }
    
    public synchronized SubCategory getAndInsertIfNewSubCategory(String name, Category category) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            List<SubCategory> subCategories = em.createNamedQuery("SubCategory.findBySubCategoryName", SubCategory.class)
                    .setParameter("subCategoryName", name)
                    .getResultList();
            if (subCategories != null && !subCategories.isEmpty()) {
                return subCategories.get(0);
            }
            SubCategory subCategory = new SubCategory();
            subCategory.setSubCategoryId(TextUtils.getUUID());
            subCategory.setSubCategoryName(name);
            subCategory.setCategoryId(category);
            return create(subCategory);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
    
    public synchronized List<SubCategory> getSubCategoryByCategoryName(String categoryName) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            List<SubCategory> subCategories = em.createNamedQuery("SubCategory.findByCategoryName", SubCategory.class)
                    .setParameter("categoryName", categoryName)
                    .getResultList();
            if (subCategories != null && !subCategories.isEmpty()) {
                return subCategories;
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
