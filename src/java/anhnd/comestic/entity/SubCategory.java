/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author anhnd
 */
@Entity
@Table(name = "SubCategory", catalog = "AppropicateCosmetic", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubCategory.findAll", query = "SELECT s FROM SubCategory s")
    , @NamedQuery(name = "SubCategory.findBySubCategoryId", query = "SELECT s FROM SubCategory s WHERE s.subCategoryId = :subCategoryId")
    , @NamedQuery(name = "SubCategory.findBySubCategoryName", query = "SELECT s FROM SubCategory s WHERE s.subCategoryName = :subCategoryName")})
public class SubCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SubCategoryId", nullable = false, length = 50)
    private String subCategoryId;
    @Column(name = "SubCategoryName", length = 255)
    private String subCategoryName;
    @JoinColumn(name = "CategoryId", referencedColumnName = "CategoryId")
    @ManyToOne
    private Category categoryId;
    @OneToMany(mappedBy = "subCategoryId")
    private Collection<Product> productCollection;

    public SubCategory() {
    }

    public SubCategory(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }

    @XmlTransient
    public Collection<Product> getProductCollection() {
        return productCollection;
    }

    public void setProductCollection(Collection<Product> productCollection) {
        this.productCollection = productCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subCategoryId != null ? subCategoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubCategory)) {
            return false;
        }
        SubCategory other = (SubCategory) object;
        if ((this.subCategoryId == null && other.subCategoryId != null) || (this.subCategoryId != null && !this.subCategoryId.equals(other.subCategoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "anhnd.comestic.entity.SubCategory[ subCategoryId=" + subCategoryId + " ]";
    }
    
}
