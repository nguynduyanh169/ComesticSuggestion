/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.crawler.mathoadaphan;

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
public class MathoadaphanThread extends BaseThread implements Runnable {

    private ServletContext context;

    public MathoadaphanThread(ServletContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        while (true) {
            try {
                MathoadaphanSubCategoryCrawler categoryCrawler = new MathoadaphanSubCategoryCrawler(context);
                Map<String, String> categories = categoryCrawler.getSubcategory("https://mathoadaphan.com/");
                categories.remove("https://mathoadaphan.com/danh-muc/cham-soc-da-mat/lam-sach-da-mat-cham-soc-da-mat/");
                categories.remove("https://mathoadaphan.com/danh-muc/cham-soc-da-mat/mat-na-cham-soc-da-mat/");
                for (Map.Entry<String, String> entry : categories.entrySet()) {
                    Thread t = new Thread(new MathoadaphanPageCrawler(context, entry.getKey(), entry.getValue()));
                    t.start();
                }
                MathoadaphanThread.sleep(TimeUnit.DAYS.toMillis(1));
                synchronized (BaseThread.getInstance()) {
                    while (BaseThread.isSuspended()) {
                        BaseThread.getInstance().wait();
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MathoadaphanThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
