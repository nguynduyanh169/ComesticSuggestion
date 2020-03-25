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
import java.util.ArrayList;
import java.util.List;
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
public class JolihouseModelCrawler extends BaseCrawler {

    private String productUrl;
    private String categoryName;

    public JolihouseModelCrawler(String productUrl, ServletContext servletContext, String categoryName) {
        super(servletContext);
        this.productUrl = productUrl;
        this.categoryName = categoryName;
    }

    public Model getModel() {
        BufferedReader reader = null;
        Model model = null;
        try {
            reader = getBufferedReaderForURL(productUrl);
            String document = getModelDocument(reader);
            model = stAXParserForModel(document);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (XMLStreamException ex) {
            Logger.getLogger(JolihouseModelCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return model;
    }

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
        double price = 0;
        String imageLink = "";
        String productLink = "";
        String origin = "";
        String volume = "";
        String validDoc = textUtils.refineHtml(document);
        imageLink = getProductImageLink(parseStringToXMLEventReader(validDoc));
        name = getProductName(parseStringToXMLEventReader(validDoc));
        price = getProductPrice(parseStringToXMLEventReader(validDoc));
        ArrayList<String> brandAndOrigin = getBrandAndOrigin(parseStringToXMLEventReader(validDoc));
        if (brandAndOrigin.size() == 1) {
            brand = "";
            origin = brandAndOrigin.get(0).trim();
        } else if (brandAndOrigin.size() == 0) {
            brand = "";
            origin = "";
        } else {
            brand = brandAndOrigin.get(0).trim();
            origin = brandAndOrigin.get(1).trim();
        }
        productLink = productUrl;
        volume = getVolume(parseStringToXMLEventReader(validDoc)).trim();
        category = categoryName;
        model = new Model(brand, name, category, price, imageLink, productLink, name + " " + price, origin, volume);
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
                if (ElementChecker.isElementWith(startElement, "span", "class", "price product-price")) {
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
                if (ElementChecker.isElementWith(startElement, "a", "class", "large_image_url checkurl")) {
                    Attribute attribute = startElement.getAttributeByName(new QName("href"));
                    imageLink = attribute.getValue();
                    return imageLink;
                }
            }
        }
        return imageLink;
    }

    private ArrayList<String> getBrandAndOrigin(XMLEventReader eventReader) {
        XMLEvent event = null;
        ArrayList<String> list = new ArrayList<>();
        while (eventReader.hasNext()) {
            event = (XMLEvent) eventReader.next();
            if (event.isCharacters()) {
                Characters character = event.asCharacters();
                if (character.getData().toLowerCase().contains("xuất xứ")) {
                    String output = character.getData().substring(character.getData().indexOf(":") + 1);
                    String[] data = output.split(",");
                    if (data.length == 1) {
                        list.add(data[0]);
                    } else if (data.length == 0) {
                        list.add("");
                        list.add("");
                    } else {
                        list.add(data[0]);
                        list.add(data[1]);
                    }
                    return list;
                }
            }
        }
        return list;
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

}
