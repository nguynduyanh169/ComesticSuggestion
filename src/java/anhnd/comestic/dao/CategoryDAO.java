/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.dao;

import anhnd.comestic.entity.Category;
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
    
    public synchronized Category saveCategoryWhileCrawl(String categoryName) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setCategoryId(TextUtils.getUUID());
        return create(category);
    }
}
