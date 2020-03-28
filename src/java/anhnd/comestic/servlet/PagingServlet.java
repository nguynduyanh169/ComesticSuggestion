/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.servlet;

import anhnd.comestic.dao.XmlDAO;
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
public class PagingServlet extends HttpServlet {
    private static final String HOME_PAGE = "home.jsp";
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
            int nextPage = 0;
            String recommend = "";
            String searchValue = "";
            if (request.getParameter("action").equals("next")) {
                nextPage = Integer.valueOf(request.getParameter("curPage")) + 6;
            } else if (request.getParameter("action").equals("pre")) {
                nextPage = Integer.valueOf(request.getParameter("curPage")) - 6;
            }
            searchValue = request.getParameter("search");
            HttpSession session = request.getSession();
            Document categoryDoc = (Document) session.getAttribute("CATEGORY");
            String userid = (String) session.getAttribute("USERID");
            XmlDAO xmlDAO = new XmlDAO();
            if (nextPage < 0) {
                recommend = xmlDAO.getRecommendProduct(userid, searchValue, 0, 6);
            } else {
                recommend = xmlDAO.getRecommendProduct(userid, searchValue, nextPage, 6);
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document recommendDoc = db.parse(new InputSource(new StringReader(recommend)));
            session.setAttribute("CURRENTPOS", nextPage);
            session.setAttribute("USERID", userid);
            session.setAttribute("RECOMMEND", recommendDoc);
            session.setAttribute("CATEGORY", categoryDoc);
            //request.getRequestDispatcher("home.jsp").forward(request, response);
            response.sendRedirect(HOME_PAGE);
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
