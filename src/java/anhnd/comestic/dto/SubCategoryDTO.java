/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.dto;

/**
 *
 * @author anhnd
 */
public class SubCategoryDTO {
    private String link;
    private String name;

    public SubCategoryDTO() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubCategoryDTO(String link, String name) {
        this.link = link;
        this.name = name;
    }
    
    
}
