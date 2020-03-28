/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.crawler.mathoadaphan;

import anhnd.comestic.crawler.BaseCrawler;
import anhnd.comestic.dao.ProductDAO;
import anhnd.comestic.dao.SubCategoryDAO;
import anhnd.comestic.dto.Model;
import anhnd.comestic.entity.Product;
import anhnd.comestic.entity.SubCategory;
import anhnd.comestic.utils.ElementChecker;
import anhnd.comestic.utils.TextUtils;
import anhnd.comestic.utils.XMLChecker;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
public class MathoadaphanPageCrawler extends BaseCrawler implements Runnable {

    private String url;
    private String subCategoryName;

    public MathoadaphanPageCrawler(ServletContext servletContext, String url, String subCategoryName) {
        super(servletContext);
        this.url = url;
        this.subCategoryName = subCategoryName;
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
                if (isStart && line.trim().contains("</ul>")) {
                    break;
                }
                if (isStart) {
                    document += line.trim();
                }
                if (isFound && line.trim().contains("<ul class=\"products columns-4\">")) {
                    isStart = true;
                }
                if (line.trim().contains("<div style=\"clear:both;\"></div>")) {
                    isFound = true;
                }
            }
            document = document + "</ul></doc>";
            List<String> list = getProductHref(document);
            SubCategory subCategoryForProduct = null;
            List<SubCategory> subCategoryList = SubCategoryDAO.getInstance().getSubCategoryByCategoryName("Chăm Sóc Da");
            for (SubCategory subCategory : subCategoryList) {
                if (TextUtils.similarity(subCategory.getSubCategoryName(), subCategoryName) >= 0.3) {
                    subCategoryForProduct = subCategory;
                }
            }
            for (String link : list) {
                MathoadaphanModelCrawler modelCrawler = new MathoadaphanModelCrawler(link, subCategoryName, servletContext);
                Model model = modelCrawler.getModel();
                String origin = TextUtils.validateOrigin(model.getOrigin());
                String brand = TextUtils.validateBrand(model.getBrand());
                Product product = new Product(TextUtils.getUUID(), model.getName(), model.getPrice(), model.getImageLink(), model.getProductLink(), model.getDetail(), origin, model.getVolume(), brand, subCategoryForProduct);
                ProductDAO.getInstance().getAndInsertIfNewProduct(product);
            }

        } catch (IOException ex) {
            Logger.getLogger(MathoadaphanPageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(MathoadaphanPageCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private List<String> getProductHref(String document) throws UnsupportedEncodingException, XMLStreamException {
        XMLChecker checker = new XMLChecker();
        String validDoc = checker.check(document);
        XMLEventReader eventReader = parseStringToXMLEventReader(validDoc);
        XMLEvent event = null;
        List<String> productHrefs = new ArrayList<String>();
        while (eventReader.hasNext()) {
            try {
                event = (XMLEvent) eventReader.next();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (ElementChecker.isElementWith(startElement, "a", "class", "ast-loop-product__link")) {
                    Attribute hrefAttr = startElement.getAttributeByName(new QName("href"));
                    String link = hrefAttr.getValue();
                    productHrefs.add(link);
                }
            }
        }
        return productHrefs;
    }

}
