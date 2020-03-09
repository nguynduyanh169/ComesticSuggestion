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
public class JolihouseCategoryCrawler extends BaseCrawler {

    public JolihouseCategoryCrawler(ServletContext servletContext) {
        super(servletContext);
    }

    public Map<String, String> getCategories(String url) {
        BufferedReader reader = null;
        try {
            reader = getBufferedReaderForURL(url);
            String line = "";
            String document = "<doc>";
            boolean isStart = false;
            boolean isFound = false;
            while ((line = reader.readLine()) != null) {
                if (isStart && line.contains("<a href=\"/trang-diem\">Trang Điểm</a>")) {
                    break;
                }
                if (isStart && !line.contains("<li class=\"level1\">") && !line.contains("</li>") && !line.contains("</ul>") && !line.contains("<li class=\"level0 level-top parent level_ico\">")) {
                    document += line.trim();
                }
                if (isFound && line.contains("<li class=\"level1\">")) {
                    isStart = true;
                }
                if (line.contains("<a href=\"/cham-soc-da\">")) {
                    isFound = true;
                }
            }
            return stAXParserForCategories(document + "</doc>");
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
            XMLEvent event = (XMLEvent) eventReader.next();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String tagName = startElement.getName().getLocalPart();
                if ("a".equals(tagName)) {
                    Attribute attributeHref = startElement.getAttributeByName(new QName("href"));
                    String link = "https://jolicosmetic.vn" + attributeHref.getValue();
                    Attribute attributeTitle = startElement.getAttributeByName(new QName("title"));
                    event = (XMLEvent) eventReader.next();
                    if (event.isStartElement()) {
                        event = (XMLEvent) eventReader.next();
                        Characters characters = event.asCharacters();
                        categories.put(link, characters.getData());
                    }
                }
            }
        }
        return categories;
    }

}
