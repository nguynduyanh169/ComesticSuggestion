/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.dao;

import anhnd.comestic.entity.Product;
import anhnd.comestic.entity.RecommendProduct;
import anhnd.comestic.entity.Users;
import anhnd.comestic.utils.DBUtils;
import anhnd.comestic.utils.TextUtils;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author anhnd
 */
public class RecommendProductDAO extends BaseDAO<RecommendProduct> {

    public RecommendProductDAO() {
    }

    private static RecommendProductDAO instance;
    private static final Object LOCK = new Object();

    public static RecommendProductDAO getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new RecommendProductDAO();
            }
        }
        return instance;
    }

    public synchronized RecommendProduct insertAndUpdateRecomand(Product product, Users user, double point) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            List<RecommendProduct> recommendProducts = em.createNativeQuery("SELECT * FROM RecommendProduct t WHERE t.productId = ? and t.userId= ? ", RecommendProduct.class)
                    .setParameter(1, product.getProductId())
                    .setParameter(2, user.getUserId())
                    .getResultList();
            if (recommendProducts.isEmpty()) {
                RecommendProduct recommendProduct = new RecommendProduct(TextUtils.getUUID());
                recommendProduct.setProductId(product);
                recommendProduct.setUserId(user);
                recommendProduct.setPoint(point);
                return create(recommendProduct);
            } else {
                recommendProducts.get(0).setPoint(point);
                return update(recommendProducts.get(0));
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

    public synchronized double caculateProductPoint(Product product, String categoryId, String origin, String brand) {
        double point = 0;
        if (product.getSubCategoryId().getCategoryId().getCategoryId().equals(categoryId)) {
            point = 1;
        }
        if (!product.getVolume().equals("")) {
            point = +0.2;
        }
        if (product.getBrand().equals(brand)) {
            point += 0.5;
        } else if (!product.getBrand().equals("")) {
            point += 0.2;
        }
        if (product.getOrigin().equals(origin)) {
            point += 0.5;
        } else if (!product.getOrigin().contains("")) {
            point += 0.2;
        }
        return point;
    }
    
    public synchronized boolean checkUserInRecommend(String userId) {
        EntityManager em = DBUtils.getEntityManager();
        boolean check = false;
        try {
            List<RecommendProduct> recommendProducts = em.createNamedQuery("RecommendProduct.findByUserId", RecommendProduct.class)
                    .setParameter("userId", userId)
                    .getResultList();
            if (recommendProducts != null && !recommendProducts.isEmpty()) {
                check = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return check;
    }
    
    
}
