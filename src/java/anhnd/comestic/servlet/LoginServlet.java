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
import anhnd.comestic.entity.Users;
import java.io.IOException;
import java.io.StringReader;
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
public class LoginServlet extends HttpServlet {

    private static final String SUCCESS = "home.jsp";
    private static final String SURVEY_PAGE = "survey.jsp";
    private static final String CRAWL_PAGE = "crawler.html";

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
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            Users user = UserDAO.getInstance().checkUser(email, password);
            HttpSession session = request.getSession();
            int productCount = ProductDAO.getInstance().countAllProduct();
            if(productCount == 0){
                request.getRequestDispatcher(CRAWL_PAGE).forward(request, response);
            }
            else if (user != null) {
                boolean check = RecommendProductDAO.getInstance().checkUserInRecommend(user.getUserId());
                if (check == false) {
                    session.setAttribute("EMAIL", user.getEmail());
                    session.setAttribute("USERID", user.getUserId());
                    XmlDAO xmlDAO = new XmlDAO();
                    String categoryList = xmlDAO.getAllCategory();
                    String brandList = xmlDAO.getAllBrand();
                    String originList = xmlDAO.getAllOrigin();
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();

                    Document categoryDoc = db.parse(new InputSource(new StringReader(categoryList)));
                    Document brandDoc = db.parse(new InputSource(new StringReader(brandList)));
                    Document originDoc = db.parse(new InputSource(new StringReader(originList)));

                    session.setAttribute("CATEGORY", categoryDoc);
                    session.setAttribute("BRAND", brandDoc);
                    session.setAttribute("ORIGIN", originDoc);
                    request.getRequestDispatcher(SURVEY_PAGE).forward(request, response);
                } else {
                    XmlDAO xmlDAO = new XmlDAO();
                    String recommendProduct = xmlDAO.getRecommendProduct(user.getUserId(), "", 0, 6);
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
                    request.getRequestDispatcher(SUCCESS).forward(request, response);
                }
            }

        } catch (IOException | ServletException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
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
