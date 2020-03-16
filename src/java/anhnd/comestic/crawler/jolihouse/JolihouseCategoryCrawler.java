/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.crawler.jolihouse;

import anhnd.comestic.crawler.BaseCrawler;
import anhnd.comestic.dto.CategoryDTO;
import anhnd.comestic.dto.SubCategoryDTO;
import anhnd.comestic.entity.Category;
import anhnd.comestic.utils.ElementChecker;
import anhnd.comestic.utils.TextUtils;
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
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author anhnd
 */
public class JolihouseCategoryCrawler extends BaseCrawler {

    public JolihouseCategoryCrawler(ServletContext servletContext) {
        super(servletContext);
    }

    public Map<String,String> getCategories(String url) {
        BufferedReader reader = null;
        try {
            reader = getBufferedReaderForURL(url);
            String document = getModelDocument(reader);
            return stAXParserForCategories(document);
        } catch (IOException ex) {
            Logger.getLogger(JolihouseCategoryCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(JolihouseCategoryCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String getModelDocument(BufferedReader bufferedReader) throws IOException {
        String line = "";
        String document = "<doc>";
        boolean isStart = false;
        boolean isFound = false;
        while ((line = bufferedReader.readLine()) != null) {
            if (isStart && line.contains("</nav>")) {
                break;
            }
            if (isStart) {
                document += line.trim();
            }
            if (isFound && line.contains("<nav class=\"header-nav\">")) {
                isStart = true;
            }
            if (line.contains("<div class=\"container relative\">")) {
                isFound = true;
            }
        }
        return document + "</doc>";
    }

    public Map<String, String> stAXParserForCategories(String document) throws UnsupportedEncodingException, XMLStreamException {
        document = document.trim();
        Map<String, String> category = new HashMap<>();
        String validDoc = TextUtils.refineHtml(document);
        category = getCategory(parseStringToXMLEventReader(validDoc));
        category.remove("/", "Trang chủ");
        return category;
    }

    public Map<String, String> getCategory(XMLEventReader eventReader) {
        XMLEvent event = null;
        Map<String, String> categoryName = new HashMap<>();
        while (eventReader.hasNext()) {
            try {
                event = (XMLEvent) eventReader.next();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "a", "class", "a-img")) {
                    Attribute hrefAttr = startElement.getAttributeByName(new QName("href"));
                    String link = hrefAttr.getValue();
                    event = (XMLEvent) eventReader.next();
                    startElement = event.asStartElement();
                    if("span".equals(startElement.getName().getLocalPart())){
                        XMLEvent value = (XMLEvent) eventReader.next();
                        String categoryN = value.asCharacters().getData();
                        categoryName.put(link,categoryN);
                    }
                }
            }
        }
        return categoryName;
    }

    

}
