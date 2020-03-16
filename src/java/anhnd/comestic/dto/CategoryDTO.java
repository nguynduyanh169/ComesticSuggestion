/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.dto;

import java.util.List;

/**
 *
 * @author anhnd
 */
public class CategoryDTO {
    String categoryName;
    List<SubCategoryDTO> subCategoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<SubCategoryDTO> getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(List<SubCategoryDTO> subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public CategoryDTO(String categoryName, List<SubCategoryDTO> subCategoryName) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" + "categoryName=" + categoryName + ", subCategoryName=" + subCategoryName + '}';
    }

    
    
    
}
