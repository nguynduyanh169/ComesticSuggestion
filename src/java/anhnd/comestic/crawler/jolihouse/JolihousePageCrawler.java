/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.crawler.jolihouse;

import anhnd.comestic.crawler.BaseCrawler;
import anhnd.comestic.entity.Category;
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
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author anhnd
 */
public class JolihousePageCrawler extends BaseCrawler implements Runnable {

    private String url;
    private String categoryName;
    protected Category category = null;

    public JolihousePageCrawler(String url, String categoryName, ServletContext servletContext) {
        super(servletContext);
        this.url = url;
        this.categoryName = categoryName;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            reader = getBufferedReaderForURL(url);
            String line = "";
            String document = "";
            boolean isFound = false;
            boolean isStart = false;
            System.out.println(url);
            while ((line = reader.readLine()) != null) {
                if (isStart && line.trim().contains("<span class='hidden-sm hidden-xs call-count'>")) {
                    break;
                }
                if (isStart) {
                    document += line.trim();
                }
                if (isFound && line.trim().contains("<div class=\"product-box\">")) {
                    System.out.println();
                    isStart = true;
                }
                if (line.trim().contains("<div class=\"product-box\">")) {
                    isFound = true;
                }              
            }
            System.out.println(document + "doc");
            Map<String, String> linkProducts = getProductHref(document);
            for (Map.Entry<String, String> entry : linkProducts.entrySet()) {
                System.out.println("Product Key: " + entry.getKey() + " Product Value: " + entry.getValue()); 
           }
        } catch (IOException ex) {
            Logger.getLogger(JolihousePageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(JolihousePageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private Map<String, String> getProductHref(String document) throws UnsupportedEncodingException, XMLStreamException {
        document = document.trim();
        XMLEventReader eventReader = parseStringToXMLEventReader(document);
        Map<String, String> productHrefs = new HashMap<>();
        while (eventReader.hasNext()) {
            XMLEvent event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String tagName = startElement.getName().getLocalPart();
                if ("a".equals(tagName)) {
                    Attribute attributeHref = startElement.getAttributeByName(new QName("href"));
                    String link = "https://jolicosmetic.vn" + attributeHref.getValue();
                    Attribute attributeTitle = startElement.getAttributeByName(new QName("title"));
                    String title = attributeTitle.getValue();
                    productHrefs.put(link, title);
                }
            }
        }
        return productHrefs;
    }

}
