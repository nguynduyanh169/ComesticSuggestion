/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.crawler.jolihouse;

import anhnd.comestic.crawler.BaseCrawler;
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
public class JolihouseCategoryCrawler extends BaseCrawler{
    
    public JolihouseCategoryCrawler(ServletContext servletContext) {
        super(servletContext);
    }
    
    public Map<String, String> getCategories(String url) {
        BufferedReader reader = null;
        try {
            reader = getBufferedReaderForURL(url);
            String line = "";
            String document = "";
            boolean isStart = false;
            boolean isFound = false;
            while ((line = reader.readLine()) != null) {     
                if(isStart && line.contains("</ul><ul class=\"ct-mobile\">")) {
                    break;
                }
                if(isStart) {
                    document += line.trim();  
                }
                if(isFound && line.contains("</a>")) {
                    isStart = true;
                }
                if(line.contains("<a href=\"/cham-soc-da\">")) {
                    isFound = true;
                }
            }
            System.out.println(document);
            return stAXParserForCategories(document);
        } catch (IOException ex) {
            Logger.getLogger(JolihouseCategoryCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(JolihouseCategoryCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Map<String, String> stAXParserForCategories(String document) throws UnsupportedEncodingException, XMLStreamException {
        document = document.trim();
        XMLEventReader eventReader = parseStringToXMLEventReader(document);
        Map<String, String> categories = new HashMap<>();
        while (eventReader.hasNext()) {    
            System.out.println("XML" + eventReader.toString());
            XMLEvent event = (XMLEvent) eventReader.next();
            if(event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String tagName = startElement.getName().getLocalPart();
                if("a".equals(tagName)) {
                    Attribute attributeHref = startElement.getAttributeByName(new QName("href"));
                    String link = "https://jolicosmetic.vn" + attributeHref.getValue();
                    event = (XMLEvent) eventReader.next();
                    Characters characters = event.asCharacters();
                    categories.put(link, characters.getData());
                }
            }
        }
        return categories;
    }
    
}
