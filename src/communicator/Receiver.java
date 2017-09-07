/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.*;

import com.sun.net.httpserver.HttpServer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author vinayak
 */
public class Receiver {

    int port_no;
    Element whomai;
    HttpServer server;                      //identifies the system and properties on which the service is runnig on
    HeartBeatHandler heartbthand;
    Map<Integer, Node> node_data;                    //data structers to store nodes
    NodePriorityQueue node_pqueue;                  //data structure to store nodes in the sorted order
    ExecutorService executor;
    
    public Receiver(Element m, Map<Integer, Node> t, NodePriorityQueue b) throws IOException {
        node_data = t;
        node_pqueue = b;
        whomai=m;
        port_no = m.getInputPort();
        setServer();
        heartbthand = new HeartBeatHandler(node_data, node_pqueue);
        executor = Executors.newFixedThreadPool(whomai.getReceiverThreadPoolSize());
        server.createContext("/heartbeat", heartbthand);
        server.setExecutor(null);
        startServer();
    }

    public final void setServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port_no), 0);

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
