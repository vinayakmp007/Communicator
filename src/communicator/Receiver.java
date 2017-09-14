/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.util.*;

import com.sun.net.httpserver.HttpServer;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author vinayak
 */
public class Receiver {

    int port_no;
    Element elem;
    //HttpServer server;                      //identifies the system and properties on which the service is runnig on
    HttpServer server;
    HeartBeatHandler heartbthand;
    Map<Integer, Node> node_data;                    //data structers to store nodes
    NodePriorityQueue node_pqueue;                  //data structure to store nodes in the sorted order
    ExecutorService executor;

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

        server = HttpServer.create(new InetSocketAddress(port_no), 0);

        heartbthand = new HeartBeatHandler(elem,node_data, node_pqueue);
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
