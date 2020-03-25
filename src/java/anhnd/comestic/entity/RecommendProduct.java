/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author anhnd
 */
@Entity
@Table(name = "RecommendProduct", catalog = "AppropicateCosmetic", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecommendProduct.findAll", query = "SELECT r FROM RecommendProduct r")
    , @NamedQuery(name = "RecommendProduct.findByRecommendId", query = "SELECT r FROM RecommendProduct r WHERE r.recommendId = :recommendId")
    , @NamedQuery(name = "RecommendProduct.findByPoint", query = "SELECT r FROM RecommendProduct r WHERE r.point = :point")
    , @NamedQuery(name = "RecommendProduct.findByUserId", query = "SELECT r FROM RecommendProduct r WHERE r.userId.userId = :userId")})
public class RecommendProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "RecommendId", nullable = false, length = 50)
    private String recommendId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Point", precision = 53)
    private Double point;
    @JoinColumn(name = "ProductId", referencedColumnName = "ProductId")
    @ManyToOne
    private Product productId;
    @JoinColumn(name = "UserId", referencedColumnName = "UserId")
    @ManyToOne
    private Users userId;

    public RecommendProduct() {
    }

    public RecommendProduct(String recommendId) {
        this.recommendId = recommendId;
    }

    public String getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(String recommendId) {
        this.recommendId = recommendId;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recommendId != null ? recommendId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecommendProduct)) {
            return false;
        }
        RecommendProduct other = (RecommendProduct) object;
        if ((this.recommendId == null && other.recommendId != null) || (this.recommendId != null && !this.recommendId.equals(other.recommendId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "anhnd.comestic.entity.RecommendProduct[ recommendId=" + recommendId + " ]";
    }

}
