/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.dao;

import anhnd.comestic.utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author anhnd
 */
public class XmlDAO {

    public String getRecommendProduct(String userid, String search, int nextPage, int numProduct) throws Exception {
        Connection con = null;
        PreparedStatement stm = null;
        String result = "";
        try {
            con = DBUtils.getMyConnection();
            if (con != null) {
                String sql = "select CAST(( select p.productId,p.productName,p.imageLink,p.origin,p.price from Product p, RecommendProduct r "
                        + "where p.productId = r.productId and r.userId = ? and p.productName like N'%" + search + "%'"
                        + " order by r.point desc offset ? rows fetch next ? rows ONLY for XML Path('product'), Root('products')) as NVARCHAR(max) ) AS XmlData";
                stm = con.prepareStatement(sql);
                stm.setString(1, userid);
                stm.setInt(2, nextPage);
                stm.setInt(3, numProduct);
                try (ResultSet rs = stm.executeQuery()) {
                    if (rs.next()) {
                        result += rs.getString("XmlData");
                    }
                }
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public String getProductById(String productId) throws Exception {
        Connection con = null;
        PreparedStatement stm = null;
        String result = "";
        try {
            con = DBUtils.getMyConnection();
            if (con != null) {
                String sql = "select CAST((select p.productId, p.productName, p.imageLink, p.price , p.brand, p.origin, p.volume from Product p where p.productId = ?"
                        + " for XML Path('product')) as NVARCHAR(max) ) AS XmlData";
                stm = con.prepareStatement(sql);
                stm.setString(1, productId);
                try (ResultSet rs = stm.executeQuery()) {
                    if (rs.next()) {
                        result += rs.getString("XmlData");
                    }
                }
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public String getAllCategory() throws Exception {
        Connection con = null;
        PreparedStatement stm = null;
        String result = "";
        try {
            con = DBUtils.getMyConnection();
            if (con != null) {
                String sql = "select CAST(( select c.categoryId, c.categoryName"
                        + " from Category c"
                        + " for XML Path('category'), Root('categories')) as NVARCHAR(max) ) AS XmlData";
                stm = con.prepareStatement(sql);
                try (ResultSet rs = stm.executeQuery()) {
                    if (rs.next()) {
                        result += rs.getString("XmlData");
                    }
                }
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public String getAllBrand() throws Exception {
        Connection con = null;
        PreparedStatement stm = null;
        String result = "";
        try {
            con = DBUtils.getMyConnection();
            if (con != null) {
                String sql = "Select CAST((select distinct p.brand from Product p "
                        + "for XML path('brand'), root('brands')) as nvarchar(max)) as xmldata";
                stm = con.prepareStatement(sql);
                try (ResultSet rs = stm.executeQuery()) {
                    if (rs.next()) {
                        result += rs.getString("XmlData");
                    }
                }
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public String getAllOrigin() throws Exception {
        Connection con = null;
        PreparedStatement stm = null;
        String result = "";
        try {
            con = DBUtils.getMyConnection();
            if (con != null) {
                String sql = "Select CAST((select distinct p.origin from "
                        + "Product p for XML path('origin'), root('origins')) as nvarchar(max)) as xmldata";
                stm = con.prepareStatement(sql);
                try (ResultSet rs = stm.executeQuery()) {
                    if (rs.next()) {
                        result += rs.getString("XmlData");
                    }
                }
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public String getAllProduct(int nextPage, int numProduct) throws Exception {
        Connection con = null;
        PreparedStatement stm = null;
        String result = "";
        try {
            con = DBUtils.getMyConnection();
            if (con != null) {
                String sql = "Select CAST((select p.productId,p.imageLink, p.productName, p.price, p.brand from Product p"
                        + " order by p.price desc offset ? rows fetch next ? rows ONLY"
                        + " for XML path('product'), root('products')) as nvarchar(max)) as XmlData";
                stm = con.prepareStatement(sql);
                stm.setInt(1, nextPage);
                stm.setInt(2, numProduct);
                try (ResultSet rs = stm.executeQuery()) {
                    if (rs.next()) {
                        result += rs.getString("XmlData");
                    }
                }
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

}
