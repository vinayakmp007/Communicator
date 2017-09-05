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
/**
 *
 * @author vinayak
 */
public  class Receiver {
    int port_no;
    HttpServer server;
    HeartBeatHandler heartbthand;
    Map<Integer,Node> node_data;
    NodePriorityQueue node_pqueue;
    public Receiver(int port, Map<Integer,Node> t,NodePriorityQueue b) throws IOException{
        node_data=t;
        node_pqueue=b;
        port_no=port;
        setServer();
        heartbthand=new HeartBeatHandler(node_data,node_pqueue);
        server.createContext("/heartbeat", heartbthand);
        server.setExecutor(null);
        startServer();
    }
    public final void setServer() throws IOException{
    server=HttpServer.create(new InetSocketAddress(port_no),0);
    
    }
     public final void startServer() throws IOException{
    server.start();
    
    }
     public final void stopServer() throws IOException{
    server.stop(port_no);
    
    }
     
    /* public final void unsetServer() throws IOException{
    
    
    }*/
}
