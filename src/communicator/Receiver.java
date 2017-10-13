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
/**
 * Creates a Receiver object
 * @param m Element object
 * @param t Map for id to Node
 * @param b  NodePriority object
 */
    public Receiver(Element m, Map<Integer, Node> t, NodePriorityQueue b) {
        node_data = t;
        node_pqueue = b;
        elem = m;
        port_no = m.getInputPort();

    }
/**
 * Starts the server
 * <br>
 * This function starts reciever server at port specified in Element object
 * @throws IOException 
 */
    public void start() throws IOException{
        setServer();

        startServer();
    }
/**
 * Configures the server
 * <br>
 * Configures the threadpool and request handlers
 * @throws IOException 
 */
    public final void setServer() throws IOException {

        server = HttpServer.create(new InetSocketAddress(port_no), 0);
        
        heartbthand = new HeartBeatHandler(elem,node_data, node_pqueue);
        executor = Executors.newFixedThreadPool(elem.getReceiverThreadPoolSize());
        
        server.createContext("/heartbeat", heartbthand);
        server.setExecutor(executor);

    }
/**
 * Starts the server
 * <br>
 * The server starts receiving at the port.The setServer() method should be invoked before this method is invoked
 * @throws IOException 
 */
    public final void startServer() throws IOException {
        server.start();

    }
/**
 * This stops the server from receiving information from other nodes
 * @throws IOException 
 */
    public final void stopServer() throws IOException {
        server.stop(port_no);

    }

    /* public final void unsetServer() throws IOException{
    
    
    }*/
}
