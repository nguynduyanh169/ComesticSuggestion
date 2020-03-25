/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.utils;

import anhnd.comestic.entity.Product;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 *
 * @author anhnd
 */
@XmlRegistry
public class ObjectFactory {
    private final static QName _Product_QNAME = new QName("", "product");

    
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ModelItem }
     * 
     */
    public Product createModelItem() {
        return new Product();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ModelItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "tblProduct")
    public JAXBElement<Product> createTblProduct(Product value) {
        return new JAXBElement<Product>(_Product_QNAME, Product.class, null, value);
    }
}
