/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;


import com.sun.net.httpserver.HttpServer;
/**
 *
 * @author vinayak
 */
public  class Receiver {
    int port_no;
    HttpServer server;
    HeartBeatHandler heartbthand;
    public Receiver(int port) throws IOException{
    
        port_no=port;
        setServer();
        heartbthand=new HeartBeatHandler();
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
