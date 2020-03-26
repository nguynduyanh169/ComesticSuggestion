/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.servlet;

import anhnd.comestic.dao.ProductDAO;
import anhnd.comestic.dao.RecommendProductDAO;
import anhnd.comestic.dao.UserDAO;
import anhnd.comestic.dao.XmlDAO;
import anhnd.comestic.entity.Product;
import anhnd.comestic.entity.Users;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author anhnd
 */
public class SurveyServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String categoryId = request.getParameter("categorySurvey");
            String brand = request.getParameter("brandSurvey");
            String origin = request.getParameter("originSurvey");
            String userid = request.getParameter("userid");
            Users user = UserDAO.getInstance().getUserById(userid);
            List<Product> products = ProductDAO.getInstance().getAll("Product.findAll");
            for (Product product : products) {
                double point = RecommendProductDAO.getInstance().caculateProductPoint(product, categoryId, origin, brand);
                RecommendProductDAO.getInstance().insertAndUpdateRecomand(product, user, point);
            }
            XmlDAO xmlDAO = new XmlDAO();
            HttpSession session = request.getSession();
            String recommendProduct = xmlDAO.getRecommendProduct(user.getUserId(), "" , 0, 6);
            String categoryList = xmlDAO.getAllCategory();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document recommendDoc = db.parse(new InputSource(new StringReader(recommendProduct)));
            Document categoryDoc = db.parse(new InputSource(new StringReader(categoryList)));
            session.setAttribute("CURRENTPOS", 0);
            session.setAttribute("USERID", user.getUserId());
            session.setAttribute("USERNAME", user.getFullname());
            session.setAttribute("RECOMMEND", recommendDoc);
            session.setAttribute("CATEGORY", categoryDoc);
            session.setAttribute("SEARCHVALUE", "");
            request.getRequestDispatcher("home.jsp").forward(request, response);
        } catch (Exception e) {
            Logger.getLogger(SurveyServlet.class.getName()).log(Level.SEVERE, null, e);

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
