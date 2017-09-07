/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

import java.io.IOException;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.util.*;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import com.sun.net.httpserver.HttpsServer;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;

/**
 *
 * @author vinayak
 */
public class Receiver {

    int port_no;
    Element elem;
    //HttpServer server;                      //identifies the system and properties on which the service is runnig on
    HttpsServer server;
    HeartBeatHandler heartbthand;
    Map<Integer, Node> node_data;                    //data structers to store nodes
    NodePriorityQueue node_pqueue;                  //data structure to store nodes in the sorted order
    ExecutorService executor;
    KeyStore ks;
    KeyManagerFactory kmf;
    TrustManagerFactory tmf;

    public Receiver(Element m, Map<Integer, Node> t, NodePriorityQueue b) {
        node_data = t;
        node_pqueue = b;
        elem = m;
        port_no = m.getInputPort();

    }

    public void start() throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        setServer();

        startServer();
    }

    public final void setServer() throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {

        server = HttpsServer.create(new InetSocketAddress(port_no), 0);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        //System.out.println(elem.getKeyFile());
        ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(elem.getKeyFile()), elem.getPassword().toCharArray());

        // setup the key manager factory           copied fromstack overflow
        kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, elem.getPassword().toCharArray());

        // setup the trust manager factory
        tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        // setup the HTTPS context and parameters                  Thanks to Stack Overflow
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            @Override
            public void configure(HttpsParameters params) {
                try {
                    // initialise the SSL context
                    SSLContext c = SSLContext.getDefault();
                    SSLEngine engine = c.createSSLEngine();
                    params.setNeedClientAuth(false);
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols());

                    // get the default parameters
                    SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
                    params.setSSLParameters(defaultSSLParameters);

                } catch (Exception ex) {
                    System.out.println("Unable to create https port");
                    ex.printStackTrace();

                }
            }
        });

        heartbthand = new HeartBeatHandler(node_data, node_pqueue);
        executor = Executors.newFixedThreadPool(elem.getReceiverThreadPoolSize());
        server.createContext("/heartbeat", heartbthand);
        server.setExecutor(executor);

    }

    public final void startServer() throws IOException {
        server.start();

    }

    public final void stopServer() throws IOException {
        server.stop(port_no);

    }

    /* public final void unsetServer() throws IOException{
    
    
    }*/
}
