/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.crawler.jolihouse;

import anhnd.comestic.crawler.BaseThread;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

/**
 *
 * @author anhnd
 */
public class JolihouseThread extends BaseThread implements Runnable {

    private ServletContext context;

    public JolihouseThread(ServletContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        while (true) {
            try {
                JolihouseCategoryCrawler categoryCrawler = new JolihouseCategoryCrawler(context);
                Map<String, String> categories = categoryCrawler.getCategories("https://jolicosmetic.vn/");
                for (Map.Entry<String, String> entry : categories.entrySet()) {
                    Thread subCategoryCrawler = new Thread(new JolihouseSubCategoryCrawler(context, entry.getKey(), entry.getValue()));
                    subCategoryCrawler.start();
                }
                JolihouseThread.sleep(TimeUnit.DAYS.toMillis(1));
                synchronized (BaseThread.getInstance()) {
                    while (BaseThread.isSuspended()) {
                        BaseThread.getInstance().wait();
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(JolihouseThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
