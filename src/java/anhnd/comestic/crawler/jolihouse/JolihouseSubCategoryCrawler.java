/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.crawler.jolihouse;

import anhnd.comestic.crawler.BaseCrawler;
import anhnd.comestic.crawler.BaseThread;
import anhnd.comestic.dao.CategoryDAO;
import anhnd.comestic.dao.SubCategoryDAO;
import anhnd.comestic.entity.Category;
import anhnd.comestic.entity.SubCategory;
import anhnd.comestic.utils.ElementChecker;
import anhnd.comestic.utils.TextUtils;
import anhnd.comestic.utils.XMLChecker;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author anhnd
 */
public class JolihouseSubCategoryCrawler extends BaseCrawler implements Runnable {

    private String url;
    private String categoryName;
    private Category category;

    public JolihouseSubCategoryCrawler(ServletContext servletContext, String url, String categoryName) {
        super(servletContext);
        this.url = url;
        this.categoryName = categoryName;
    }

    private String getModelDocument(BufferedReader bufferedReader, String categoryName, String link) throws IOException {
        String line = "";
        String document = "<doc>";
        boolean isStart = false;
        boolean isFound = false;
        while ((line = bufferedReader.readLine()) != null) {
            if (isStart && line.contains("</ul>")) {
                break;
            }
            if (isStart) {
                document += line.trim();
            }
            if (isFound && line.contains("<ul class=\"item_small hidden-md hidden-sm hidden-xs\">")) {
                isStart = true;
            }
            if (line.contains("<a class=\"a-img\" href=\"" + link + "\"><span>" + categoryName + "</span><i class=\"fa fa-caret-down\"></i></a>")) {
                isFound = true;
            }
        }
        return document + "</doc>";
    }

    private Map<String, String> stAXParserForSubCategories(String document) throws UnsupportedEncodingException, XMLStreamException {
        String validDoc = TextUtils.refineHtml(document);
        XMLEventReader eventReader = parseStringToXMLEventReader(validDoc);
        Map<String, String> productHrefs = new HashMap<>();
        while (eventReader.hasNext()) {
            XMLEvent event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String tagName = startElement.getName().getLocalPart();
                if ("a".equals(tagName)) {
                    Attribute attributeHref = startElement.getAttributeByName(new QName("href"));
                    String link = "https://jolicosmetic.vn" + attributeHref.getValue();
                    XMLEvent value = (XMLEvent) eventReader.next();
                    String title = value.asCharacters().getData();
                    productHrefs.put(link, title);
                }
            }
        }
        
        return productHrefs;
    }

    @Override
    public void run() {
        CategoryDAO categoryDAO = new CategoryDAO();
        category = categoryDAO.getAndInsertIfNewCategory(categoryName);
        BufferedReader reader = null;
        try {
            reader = getBufferedReaderForURL("https://jolicosmetic.vn" + url);
            String document = getModelDocument(reader, categoryName, url);
            Map<String, String> data = new HashMap<>(); 
            data = stAXParserForSubCategories(document);
            for (Map.Entry<String, String> entry : data.entrySet()) {
                SubCategoryDAO subCategoryDAO = new SubCategoryDAO();
                SubCategory subCategory = subCategoryDAO.getAndInsertIfNewSubCategory(entry.getValue(), category);
                Thread thread = new Thread(new JolihousePageCrawler(entry.getKey(), subCategory, servletContext));
                thread.start();
            }
            synchronized (BaseThread.getInstance()) {
                while (BaseThread.isSuspended()) {
                    BaseThread.getInstance().wait();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(JolihouseCategoryCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(JolihouseSubCategoryCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(JolihouseSubCategoryCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
