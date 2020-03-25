/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.utils;

import anhnd.comestic.entity.Product;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.servlet.ServletContext;
import org.w3c.dom.Document;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author anhnd
 */
public class JAXBUtils {
    
    private static final String XSD_FILE = "/WEB-INF/model.xsd";
    
    public static Product unmarshall(Document document, String path) throws JAXBException, SAXException {
        JAXBContext jAXBContext = JAXBContext.newInstance(Product.class);
        Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(path));
        unmarshaller.setSchema(schema);
        unmarshaller.setEventHandler(new ValidationEventHandler() {
            @Override
            public boolean handleEvent(ValidationEvent event) {
                if(event.getSeverity() == ValidationEvent.FATAL_ERROR || event.getSeverity() == ValidationEvent.ERROR) {
                    return false;
                }
                return true;
            }
        });
        DOMSource dom = new DOMSource(document);
        Product product = (Product) unmarshaller.unmarshal(dom);
        return product;
    }
    
    public static Document marshall(Product product) throws JAXBException, ParserConfigurationException, SAXException, IOException {
        JAXBContext jc = JAXBContext.newInstance(Product.class);
        Marshaller mar = jc.createMarshaller();
        StringWriter sw = new StringWriter();
        mar.marshal(product, sw);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new InputSource(new StringReader(sw.toString())));
        return doc;
    }

    public static Product validateProduct(Product product, ServletContext context) {
        try {
            String realPath = context.getRealPath("/");
            String filePath = realPath + XSD_FILE;
            Document document = marshall(product);
            return unmarshall(document, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
