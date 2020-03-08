/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.crawler.jolihouse;

import anhnd.comestic.crawler.BaseCrawler;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

/**
 *
 * @author anhnd
 */
public class JolihouseCrawler extends BaseCrawler{
    
    public JolihouseCrawler(ServletContext servletContext) {
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
                if(isStart && line.contains("</ul><ul class='ct-mobile")) {
                    break;
                }
                if(isStart) {
                    document += line.trim();
                }
                if(isFound && line.contains("<li class=\"level0 level-top parent level_ico\">")) {
                    isStart = true;
                }
                if(line.contains("<a href=\"/cham-soc-da\">")) {
                    isFound = true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(JolihouseCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
