/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import communicator.*;
import communicator.Element;
import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vinayak
 */
public class Executor {
    
    public void testone() {
        
        try {
            Communicator b, c, d, e, f, g;
            
            b = new Communicator(new Element(1234, 8124, 30000));
            c = new Communicator(new Element(1235, 8125, 30000));
            d = new Communicator(new Element(1236, 8126, 30000));
            e = new Communicator(new Element(1237, 8127, 30000));
            f = new Communicator(new Element(1238, 8128, 30000));
            g = new Communicator(new Element(1239, 8129, 30000));
            
            b.start();
            d.start();
            c.start();
            e.start();
            f.start();
            g.start();
            
            (new Thread((new TableforPQ(b.getPQ())))).start();
            (new Thread((new TableforPQ(c.getPQ())))).start();
            (new Thread((new TableforPQ(d.getPQ())))).start();
            (new Thread((new TableforPQ(e.getPQ())))).start();
            (new Thread((new TableforPQ(f.getPQ())))).start();
            (new Thread((new TableforPQ(g.getPQ())))).start();

            /*9(new Thread((new TableforMap(b.getMap())))).start();
                   (new Thread((new TableforMap(c.getMap())))).start();
                   (new Thread((new TableforMap(d.getMap())))).start();*/
        } catch (Exception ex) {
            Logger.getLogger(Executor.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        
    }
    
    public static void test2() throws IOException, InterruptedException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        Communicator b, c;
        b = new Communicator(new Element(1234, 8124, 30000));
        c = new Communicator(new Element(1235, 8125, 30000));
        
        b.getElement().setPasswordAndKeyFile(Constants.PASSWORD_KEY, Constants.KEYFILE);
        
        c.getElement().setPasswordAndKeyFile(Constants.PASSWORD_KEY, Constants.KEYFILE);
        c.addNode(1234, System.currentTimeMillis(), 8124, InetAddress.getLocalHost().getHostAddress());
        b.start();
        c.start();
        
        (new Thread((new TableforPQ(b.getPQ())))).start();
        (new Thread((new TableforPQ(c.getPQ())))).start();
        
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TableforMap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TableforMap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TableforMap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TableforMap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        try {
            test2();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
}
