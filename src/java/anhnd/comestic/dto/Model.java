/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.dto;

import java.io.Serializable;

/**
 *
 * @author anhnd
 */
public class Model implements Serializable{
    protected String brand;
    protected String name;
    protected String category;
    protected float price;
    protected String imageLink;
    protected String productLink;
    protected String detail;
    protected String origin;
    protected String volume;

    public Model(String brand, String name, String category, float price, String imageLink, String productLink, String detail, String origin, String volume) {
        this.brand = brand;
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageLink = imageLink;
        this.productLink = productLink;
        this.detail = detail;
        this.origin = origin;
        this.volume = volume;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Model{" + "brand=" + brand + ", name=" + name + ", category=" + category + ", price=" + price + ", imageLink=" + imageLink + ", productLink=" + productLink + ", detail=" + detail + ", origin=" + origin + ", volume=" + volume + '}';
    }
    
    
}
