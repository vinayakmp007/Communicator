/*
 * The MIT License
 *
 * Copyright 2017 vinayak.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package communicator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author vinayak
 */
public class RequestAllHandler implements HttpHandler  {

    Map<Integer, Node> node_data;
    NodePriorityQueue queue;
    Element element;
    InputStream data_inpstream;
    String req_body, from_id_str, from_id_ipaddr;
public RequestAllHandler(Element element, Map<Integer, Node> t, NodePriorityQueue b) {
        node_data = t;
        queue = b;
        this.element = element;
    }
    
    @Override
    public void handle(HttpExchange con) throws IOException {

         byte tm;
        char tmp;
        StringBuilder tembuf = new StringBuilder();
       JSONObject jsonobj;
             data_inpstream = con.getRequestBody();
            while ((tm = (byte) data_inpstream.read()) != -1) {
                tmp = (char) tm;
                tembuf.append(tmp);

            }
            req_body = tembuf.toString();
        
        String resp = "INVALID";
                                          //Write the response
        
        if (con.getRequestMethod().contentEquals("POST")) {
            jsonobj=makeHeader();
            jsonobj.put("NODES",mapToJSONArray(node_data));
            resp=jsonobj.toJSONString();
            
        } else {
            System.out.println(con.getRequestMethod());
        }

        byte[] response = resp.getBytes();
        con.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
        con.getResponseBody().write(response);
        con.getResponseBody().flush();
        con.getResponseBody().close();
        con.close();
    }
    public static JSONArray mapToJSONArray(Map<Integer,Node> nodm){
    String respon = null;
    JSONArray ja = new JSONArray();
    
    Map<Integer, Node> tm = new ConcurrentHashMap<>(nodm);
        for (Node t : tm.values()) {                                               //can also use jsonobject.putall
            JSONObject temp = new JSONObject();
                temp.put("ID", Integer.toString(t.getIdentifier()));
                temp.put("IPADDR", t.getIPAddress());
                temp.put("IPORT", Integer.toString(t.getInputPort()));
                temp.put("TIME", Long.toString(t.getTimestamp()));    
                ja.add(temp);
        }
        
        
    return ja;
    }
    private JSONObject makeHeader() throws UnknownHostException, SocketException{
    
        JSONObject json = new JSONObject();
        json.put("ID", Integer.toString(element.getIdentifier()));
        json.put("IPADDR", element.getHostIP());
        json.put("IPORT", Integer.toString(element.getInputPort()));
        json.put("TIME", Long.toString(System.currentTimeMillis()));
        return json;
    }
}
