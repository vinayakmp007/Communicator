/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Iterator;
import java.io.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
/**
 *
 * @author vinayak
 */
public class Sender {

    Element element;
    Node to;
    String service;
    NodePriorityQueue queue;
    Map<Integer, Node> node_data;
    
    public Sender(Element el,Node a, String serv,NodePriorityQueue queu,Map<Integer,Node> nod_dat) throws IOException {
        this.queue=queu;
        this.node_data=nod_dat;
        this.to = a;
        this.element =el;
        this.service = serv;
        connect();

    }

    @SuppressWarnings("empty-statement")
    final void connect() throws MalformedURLException, IOException {
        String urls = "http://" + to.getIPAddress() + ":" + to.getInputPort() + "/" + service;             //creates the url for connecting
        URL url = new URL(urls);
        
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();                     // connects to the service
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("POST");
        httpCon.setUseCaches(false);
        httpCon.setDoInput(true);
        JSONObject json=new JSONObject();
        json.put("ID", Integer.toString(element.getIdentifier()));
        json.put("IPADDR",element.getHostIP());
        json.put("IPORT", Integer.toString(element.getInputPort()));
        json.put("TIME", Long.toString(System.currentTimeMillis()));
        httpCon.setRequestProperty("Accept", "application/json");
        httpCon.setRequestProperty("Content-Type", "application/json");
        
        json.put("TRAILER", makeTrailer());
        
        OutputStream os = httpCon.getOutputStream();
        try (OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8")) {
            osw.write(json.toString());
            osw.flush();
            osw.close();
        }
        Reader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "UTF-8"));

        for (int c; (c = in.read()) >= 0;);
            
        //System.out.println(json.toString()+"here");
        httpCon.connect();
        httpCon.disconnect();
        
    }

    public JSONArray  makeTrailer() throws UnknownHostException{
    
    JSONArray ja = new JSONArray();
    NodePriorityQueue tem=new NodePriorityQueue(queue);               //creates a temporay queue
    Iterator itr=tem.iterator();
    
    while(itr.hasNext()){
        
        JSONObject temp=new JSONObject();
        Node t=(Node)itr.next();
        if(t.getTimestamp()<(System.currentTimeMillis())-1000*35)break;            // ignore if they are 35 second  old or more
        temp.put("ID",Integer.toString(t.getIdentifier()));
        temp.put("IPADDR",t.getIPAddress());
        temp.put("IPORT", Integer.toString(t.getInputPort()));
        temp.put("TIME",Long.toString(t.getTimestamp())); 
        ja.add(temp);
    }
    
    
    return ja;
    }
}
