/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.crawler.mathoadaphan;

import anhnd.comestic.crawler.BaseCrawler;
import anhnd.comestic.crawler.jolihouse.JolihouseCategoryCrawler;
import anhnd.comestic.utils.ElementChecker;
import anhnd.comestic.utils.TextUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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
public class MathoadaphanSubCategoryCrawler extends BaseCrawler {

    public MathoadaphanSubCategoryCrawler(ServletContext servletContext) {
        super(servletContext);
    }

    public Map<String, String> getSubcategory(String url) {
        BufferedReader reader = null;
        try {
            reader = getBufferedReaderForURL(url);
            String document = getDocument(reader);
            return stAXParserForCategories(document);
        } catch (IOException ex) {
            Logger.getLogger(JolihouseCategoryCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(MathoadaphanSubCategoryCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String getDocument(BufferedReader bufferedReader) throws IOException {
        String line = "";
        String document = "<doc><li>";
        boolean isStart = false;
        boolean isFound = false;
        while ((line = bufferedReader.readLine()) != null) {
            if (isStart && line.contains("li id=\"menu-item-39108\" class=\"menu-item menu-item-type-taxonomy menu-item-object-product_cat menu-item-has-children menu-item-39108\"><a href=\"https://mathoadaphan.com/danh-muc/trang-diem/\">Trang điểm</a><button class=\"ast-menu-toggle\" role=\"button\" aria-expanded=\"false\"><span class=\"screen-reader-text\">Menu Toggle</span></button>")) {
                break;
            }
            if (isStart) {
                document += line.trim();
            }
            if (isFound && line.contains("<li id=\"menu-item-39093\" class=\"menu-item menu-item-type-taxonomy menu-item-object-product_cat menu-item-has-children menu-item-39093\"><a href=\"https://mathoadaphan.com/danh-muc/cham-soc-da-mat/\">Chăm sóc da mặt</a><button class=\"ast-menu-toggle\" role=\"button\" aria-expanded=\"false\"><span class=\"screen-reader-text\">Menu Toggle</span></button>")) {
                isStart = true;
            }
            if (line.contains("<li id=\"menu-item-46188\" class=\"menu-item menu-item-type-custom menu-item-object-custom menu-item-46188\"><a href=\"https://mathoadaphan.com/khuyen-mai/\">KHUYẾN MÃI</a></li>")) {
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
        return category;
    }

    public Map<String, String> getCategory(XMLEventReader eventReader) {
        XMLEvent event = null;
        Map<String, String> subCategoryName = new HashMap<>();
        while (eventReader.hasNext()) {
            try {
                event = (XMLEvent) eventReader.next();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "a")) {
                    Attribute hrefAttr = startElement.getAttributeByName(new QName("href"));
                    String link = hrefAttr.getValue();
                    XMLEvent value = (XMLEvent) eventReader.next();
                    String name = value.asCharacters().getData();
                    subCategoryName.put(link, name);
                }
            }
        }
        return subCategoryName;
    }

}
