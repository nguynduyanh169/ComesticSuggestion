/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.crawler;

/**
 *
 * @author anhnd
 */
public class BaseThread extends Thread{

   private final static Object LOCK = new Object();

    private static BaseThread instance;
    private static boolean suspended = false;

    protected BaseThread() {
    }

    public static boolean isSuspended() {
        return suspended;
    }

    public static void setSuspended(boolean suspended) {
        BaseThread.suspended = suspended;
    }

    public static BaseThread getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new BaseThread();
            }
        }
        return instance;
    }

    public void supendThread() {
        setSuspended(true);
        System.out.println("Suspended");
    }

    public void resumeThread() {
        synchronized (getInstance()) {
            setSuspended(false);
            getInstance().notifyAll();
        }
        System.out.println("Resume");
    }
}
