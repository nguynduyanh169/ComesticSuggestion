/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.crawler.jolihouse;

import anhnd.comestic.crawler.BaseCrawler;
import anhnd.comestic.dao.ProductDAO;
import anhnd.comestic.dto.Model;
import anhnd.comestic.entity.Product;
import anhnd.comestic.entity.SubCategory;
import anhnd.comestic.utils.TextUtils;
import anhnd.comestic.utils.XMLChecker;
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
public class JolihousePageCrawler extends BaseCrawler implements Runnable {

    private String url;
    private SubCategory subCategory;

    public JolihousePageCrawler(String url, SubCategory subCategory, ServletContext servletContext) {
        super(servletContext);
        this.url = url;
        this.subCategory = subCategory;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            reader = getBufferedReaderForURL(url);
            String line = "";
            String document = "<doc>";
            boolean isFound = false;
            boolean isStart = false;
            while ((line = reader.readLine()) != null) {
                if (isStart && line.trim().contains("<div class=\"text-right\">")) {
                    break;
                }
                if (isStart) {
                    if (!line.trim().contains("<a  title=\"Bỏ thích\" class=\"button_wh_40 btn_35_h iWishAdded "
                            + "iwishAddWrapper iWishHidden\" href=\"javascript:;\"") && !line.trim().contains("<a title=\"Yêu thích\" class=\"button_wh_40 btn_35_h iWishAdd"
                                    + " iwishAddWrapper\" href=\"javascript:;\"")) {
                        document += line.trim();
                    }
                }
                if (isFound && line.trim().contains("<div class=\"row\">")) {
                    isStart = true;
                }
                if (line.trim().contains("<section class=\"main_container collection margin-bottom-30 col-md-9 col-lg-9 col-lg-push-3 col-md-push-3\">")) {
                    isFound = true;
                }
            }
            document = document + "</doc>";
            Map<String, String> linkProducts = getProductHref(document);
            for (Map.Entry<String, String> entry : linkProducts.entrySet()) {
                JolihouseModelCrawler modelCrawler = new JolihouseModelCrawler(entry.getKey(), servletContext, subCategory.getSubCategoryName());
                Model model = modelCrawler.getModel();
                String origin = TextUtils.validateOrigin(model.getOrigin());
                String brand = TextUtils.validateBrand(model.getBrand());
                Product product = new Product(TextUtils.getUUID(), model.getName(), model.getPrice(), model.getImageLink(), model.getProductLink(), model.getDetail(), origin, model.getVolume(), brand, subCategory);
                ProductDAO productDAO = new ProductDAO();
                productDAO.getAndInsertIfNewProduct(product);
            }
        } catch (IOException ex) {
            Logger.getLogger(JolihousePageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(JolihousePageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private Map<String, String> getProductHref(String document) throws UnsupportedEncodingException, XMLStreamException {
        XMLChecker checker = new XMLChecker();
        String validDoc = checker.check(document);
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
                    Attribute attributeTitle = startElement.getAttributeByName(new QName("title"));
                    String title = attributeTitle.getValue();
                    productHrefs.put(link, title);
                }
            }
        }
        return productHrefs;
    }

}
