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
@Table(name = "SkinType", catalog = "AppropicateCosmetic", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkinType.findAll", query = "SELECT s FROM SkinType s")
    , @NamedQuery(name = "SkinType.findBySkinTypeId", query = "SELECT s FROM SkinType s WHERE s.skinTypeId = :skinTypeId")
    , @NamedQuery(name = "SkinType.findBySkinTypeName", query = "SELECT s FROM SkinType s WHERE s.skinTypeName = :skinTypeName")})
public class SkinType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SkinTypeId", nullable = false, length = 50)
    private String skinTypeId;
    @Column(name = "SkinTypeName", length = 255)
    private String skinTypeName;
    @OneToMany(mappedBy = "skinTypeId")
    private Collection<Product> productCollection;
    @OneToMany(mappedBy = "skinTypeId")
    private Collection<Users> usersCollection;

    public SkinType() {
    }

    public SkinType(String skinTypeId) {
        this.skinTypeId = skinTypeId;
    }

    public String getSkinTypeId() {
        return skinTypeId;
    }

    public void setSkinTypeId(String skinTypeId) {
        this.skinTypeId = skinTypeId;
    }

    public String getSkinTypeName() {
        return skinTypeName;
    }

    public void setSkinTypeName(String skinTypeName) {
        this.skinTypeName = skinTypeName;
    }

    @XmlTransient
    public Collection<Product> getProductCollection() {
        return productCollection;
    }

    public void setProductCollection(Collection<Product> productCollection) {
        this.productCollection = productCollection;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (skinTypeId != null ? skinTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SkinType)) {
            return false;
        }
        SkinType other = (SkinType) object;
        if ((this.skinTypeId == null && other.skinTypeId != null) || (this.skinTypeId != null && !this.skinTypeId.equals(other.skinTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "anhnd.comestic.entity.SkinType[ skinTypeId=" + skinTypeId + " ]";
    }
    
}
