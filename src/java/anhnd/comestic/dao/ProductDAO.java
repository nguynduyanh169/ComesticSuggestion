/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.dao;

import anhnd.comestic.entity.Product;
import anhnd.comestic.utils.DBUtils;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author anhnd
 */
public class ProductDAO extends BaseDAO<Product> {

    public ProductDAO() {
    }

    private static ProductDAO instance;
    private static final Object LOCK = new Object();

    public static ProductDAO getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new ProductDAO();
            }
        }
        return instance;
    }

    public synchronized Product getAndInsertIfNewProduct(Product product) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            List<Product> products = em.createNamedQuery("Product.findByProductName", Product.class)
                    .setParameter("productName", product.getProductName())
                    .getResultList();
            if (products != null && !products.isEmpty()) {
                return products.get(0);
            }

            return create(product);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public synchronized Product getProductById(String productId) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            List<Product> products = em.createNamedQuery("Product.findByProductId", Product.class)
                    .setParameter("productId", productId)
                    .getResultList();
            if (products != null && !products.isEmpty()) {
                return products.get(0);
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
