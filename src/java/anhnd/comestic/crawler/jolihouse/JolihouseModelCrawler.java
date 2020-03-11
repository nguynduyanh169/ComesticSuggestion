/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.crawler.jolihouse;

import anhnd.comestic.crawler.BaseCrawler;
import anhnd.comestic.dto.Model;
import anhnd.comestic.utils.ElementChecker;
import anhnd.comestic.utils.EscapseHTMLUtils;
import anhnd.comestic.utils.TextUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
public class JolihouseModelCrawler extends BaseCrawler implements Runnable {

    private String productUrl;

    public JolihouseModelCrawler(String productUrl, ServletContext servletContext) {
        super(servletContext);
        this.productUrl = productUrl;
    }

//    public Model getModel() {
//        BufferedReader reader = null;
//        Model model = null;
//        try {
//            reader = getBufferedReaderForURL(productUrl);
//            String document = getModelDocument(reader);
//            return stAXParserForModel(document);
//        } catch (IOException | XMLStreamException ex) {
//            ex.printStackTrace();
//        }
//        return model;
//    }
    private String getModelDocument(BufferedReader reader) throws IOException {
        String line = "";
        String document = "<doc>";
        boolean isFound = false;
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {
            if (isStart && line.trim().contains("<div class=\"col-lg-3 col-md-3 col-sm-12 col-xs-12 hidden-xs hidden-sm hidden-md\">")) {
                break;
            }
            if (isStart) {
                document += line.trim();
            }
            if (isFound && line.trim().contains("<div class=\"rows\">")) {
                isStart = true;
            }
            if (line.trim().contains("<div class=\"col-lg-9 col-md-12 col-sm-12 col-xs-12\">")) {
                isFound = true;
            }
        }
        document = document + "</doc>";
        return EscapseHTMLUtils.encodeHtml(document);
    }

    public Model stAXParserForModel(String document) throws UnsupportedEncodingException, XMLStreamException {
        TextUtils textUtils = new TextUtils();
        Model model = null;
        String brand = "";
        String name = "";
        String category = "";
        float price = 0;
        String imageLink = "";
        String productLink = "";
        String detail = "";
        String origin = "";
        String volume = "";
        String validDoc = textUtils.refineHtml(document);
        XMLEventReader eventReader = parseStringToXMLEventReader(validDoc);
        name = getProductName(eventReader).trim();
        price = getProductPrice(eventReader);
        productLink = productUrl;
        imageLink = getProductImageLink(eventReader);
        System.out.println("Name: " + name);
        System.out.println("Price: " + price);
        System.out.println("ProductLink: " + productLink);
        System.out.println("ImageLink: " + imageLink);
        return model;
    }

    private String getProductName(XMLEventReader eventReader) {
        XMLEvent event = null;
        String name = "";
        while (eventReader.hasNext()) {
            try {
                event = (XMLEvent) eventReader.next();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "h1", "class", "title-product")) {
                    XMLEvent value = (XMLEvent) eventReader.next();
                    if (value.isStartElement()) {
                        value = (XMLEvent) eventReader.next();
                        name = value.asCharacters().getData();
                    }
                    name = value.asCharacters().getData();
                    return name;
                }
            }
        }
        return name;
    }

    private float getProductPrice(XMLEventReader eventReader) {
        XMLEvent event = null;
        float price = 0;
        while (eventReader.hasNext()) {
            try {
                event = (XMLEvent) eventReader.next();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "span", "class", "price product-price")) {
                    XMLEvent value = (XMLEvent) eventReader.next();
                    String strPrice = value.asCharacters().getData();
                    strPrice = strPrice.replaceAll("[^0-9\\*]", "");
                    price = Float.parseFloat(strPrice);
                    return price;
                }
            }
        }
        return price;
    }

    private String getProductImageLink(XMLEventReader eventReader) {
        XMLEvent event = null;
        String imageLink = "";
        while (eventReader.hasNext()) {
            try {
                event = (XMLEvent) eventReader.next();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "div", "class", "col_large_full large-image")) {
                    event = (XMLEvent) eventReader.next();
                    startElement = event.asStartElement();
                    String tag = startElement.getName().getLocalPart();
                    System.out.println(tag + "abc");
                    if ("a".equals(startElement.getName().getLocalPart())) {
                        Attribute imgAttr = startElement.getAttributeByName(new QName("href"));
                        imageLink = imgAttr.getValue();
                    }
                }
            }
        }
        return imageLink;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        Model model = null;
        try {
            reader = getBufferedReaderForURL(productUrl);
            String document = getModelDocument(reader);
            //System.out.println(document);
            model = stAXParserForModel(document);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (XMLStreamException ex) {
            Logger.getLogger(JolihouseModelCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
