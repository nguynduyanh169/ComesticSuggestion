/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.crawler.mathoadaphan;

import anhnd.comestic.crawler.BaseCrawler;
import anhnd.comestic.dto.Model;
import anhnd.comestic.utils.ElementChecker;
import anhnd.comestic.utils.EscapseHTMLUtils;
import anhnd.comestic.utils.TextUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
public class MathoadaphanModelCrawler extends BaseCrawler {

    String url;
    String subCategoryName;

    public MathoadaphanModelCrawler(String url, String subCategoryName, ServletContext servletContext) {
        super(servletContext);
        this.url = url;
        this.subCategoryName = subCategoryName;
    }

    public Model getModel() {
        BufferedReader reader = null;
        Model model = null;
        try {
            reader = getBufferedReaderForURL(url);
            String document = getModelDocument(reader);
            model = stAXParserForModel(document);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (XMLStreamException ex) {
            Logger.getLogger(MathoadaphanModelCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return model;
    }

    private Model stAXParserForModel(String document) throws UnsupportedEncodingException, XMLStreamException {
        TextUtils textUtils = new TextUtils();
        Model model = null;
        String brand = "";
        String name = "";
        String category = "";
        double price = 0;
        String imageLink = "";
        String productLink = "";
        String origin = "";
        String volume = "";
        String validDoc = textUtils.refineHtml(document);

        imageLink = getProductImageLink(parseStringToXMLEventReader(validDoc));
        name = getProductName(parseStringToXMLEventReader(validDoc));
        price = getProductPrice(parseStringToXMLEventReader(validDoc));
        productLink = url;
        category = subCategoryName;
        origin = getOrigin(parseStringToXMLEventReader(validDoc));
        volume = getVolume(parseStringToXMLEventReader(validDoc));
        brand = getBrand(parseStringToXMLEventReader(validDoc));
        model = new Model(brand, name, category, price, imageLink, productLink, name + " " + price, origin, volume);
        return model;
    }

    private String getModelDocument(BufferedReader reader) throws IOException {
        String line = "";
        String document = "<doc>";
        boolean isFound = false;
        boolean isStart = false;
        while ((line = reader.readLine()) != null) {
            if (isStart && line.trim().contains("</main>")) {
                break;
            }
            if (isStart) {
                document += line.trim();
            }
            if (isFound && line.trim().contains("<main id=\"main\" class=\"site-main\" role=\"main\">")) {
                isStart = true;
            }
            if (line.trim().contains("<div id=\"primary\" class=\"content-area primary\">")) {
                isFound = true;
            }
        }
        document = document + "</doc>";
        return EscapseHTMLUtils.encodeHtml(document);
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
                if (ElementChecker.isElementWith(startElement, "img", "class", "wp-post-image")) {
                    Attribute attribute = startElement.getAttributeByName(new QName("src"));
                    imageLink = attribute.getValue();
                    return imageLink;
                }
            }
        }
        return imageLink;
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
                if (ElementChecker.isElementWith(startElement, "h1", "class", "product_title entry-title")) {
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

    private double getProductPrice(XMLEventReader eventReader) {
        XMLEvent event = null;
        double price = 0;
        while (eventReader.hasNext()) {
            try {
                event = (XMLEvent) eventReader.next();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "span", "class", "woocommerce-Price-amount amount")) {
                    XMLEvent value = (XMLEvent) eventReader.next();
                    String strPrice = value.asCharacters().getData();
                    strPrice = strPrice.replaceAll("[^0-9\\*]", "");
                    price = Double.parseDouble(strPrice);
                    return price;
                }
            }
        }
        return price;
    }

    private String getVolume(XMLEventReader eventReader) {
        XMLEvent event = null;
        String data = "";
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isCharacters()) {
                Characters characters = event.asCharacters();
                if (characters.getData().toLowerCase().contains("dung tích") || characters.getData().toLowerCase().contains("trọng lượng")) {
                    if (characters.getData() != null) {
                        data = characters.getData().substring(characters.getData().indexOf(":") + 1);
                        return data;
                    }
                }
            }
        }
        return data;
    }

    private String getOrigin(XMLEventReader eventReader) {
        XMLEvent event = null;
        String data = "";
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isCharacters()) {
                Characters characters = event.asCharacters();
                if (characters.getData().toLowerCase().contains("xuất xứ")) {
                    if (characters.getData() != null) {
                        data = characters.getData().substring(characters.getData().indexOf(":") + 1);
                        return data;
                    }
                }
            }
        }
        return data;
    }

    private String getBrand(XMLEventReader eventReader) {
        XMLEvent event = null;
        String brand = "";
        while (eventReader.hasNext()) {
            try {
                event = (XMLEvent) eventReader.next();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "span", "itemprop", "brand")) {
                    event = (XMLEvent) eventReader.next();
                    startElement = event.asStartElement();
                    if (ElementChecker.isElementWith(startElement, "a", "rel", "tag")) {
                        XMLEvent value = (XMLEvent) eventReader.next();
                        brand = value.asCharacters().getData();
                    }
                }
            }
        }
        return brand;
    }

}
