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
@Table(name = "Concern")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Concern.findAll", query = "SELECT c FROM Concern c")
    , @NamedQuery(name = "Concern.findByConcernId", query = "SELECT c FROM Concern c WHERE c.concernId = :concernId")
    , @NamedQuery(name = "Concern.findByConcernName", query = "SELECT c FROM Concern c WHERE c.concernName = :concernName")})
public class Concern implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ConcernId")
    private String concernId;
    @Column(name = "ConcernName")
    private String concernName;
    @OneToMany(mappedBy = "concernId")
    private Collection<Product> productCollection;
    @OneToMany(mappedBy = "concernId")
    private Collection<Users> usersCollection;

    public Concern() {
    }

    public Concern(String concernId) {
        this.concernId = concernId;
    }

    public String getConcernId() {
        return concernId;
    }

    public void setConcernId(String concernId) {
        this.concernId = concernId;
    }

    public String getConcernName() {
        return concernName;
    }

    public void setConcernName(String concernName) {
        this.concernName = concernName;
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
        hash += (concernId != null ? concernId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Concern)) {
            return false;
        }
        Concern other = (Concern) object;
        if ((this.concernId == null && other.concernId != null) || (this.concernId != null && !this.concernId.equals(other.concernId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "anhnd.comestic.entity.Concern[ concernId=" + concernId + " ]";
    }
    
}
