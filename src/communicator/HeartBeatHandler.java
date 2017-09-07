/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

import java.net.HttpURLConnection;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.util.*;
import java.io.*;
import java.lang.NullPointerException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author vinayak
 */
//TODO handle the triler part of the request body
public class HeartBeatHandler implements HttpHandler, Handler {

    Map<String, String> param = new HashMap<>();
    Map<String, String[]> req_headr = new HashMap<>();
    Map<Integer, Node> node_data;
    InputStream data_inpstream;
    String req_body, from_id_str, from_id_ipaddr;
    int req_bodysize, from_id_int, from_id_port;
    JSONArray trailer;
    long time, from_id_time;
    NodePriorityQueue queue;

    public HeartBeatHandler(Map<Integer, Node> t, NodePriorityQueue b) {
        node_data = t;
        queue = b;
    }

    @Override
    public void handle(HttpExchange con) throws IOException {

        // param=Handler.queryToMap(con.getRequestURI().getQuery());
        time = System.currentTimeMillis();
        data_inpstream = con.getRequestBody();

        byte tm, tmbuf[] = new byte[4096];
        char tmp;
        StringBuilder tembuf = new StringBuilder();

        while ((tm = (byte) data_inpstream.read()) != -1) {
            tmp = (char) tm;
            tembuf.append(tmp);

        }
        req_body = tembuf.toString();
        String resp = "OK";
        byte[] response = resp.getBytes();                                   //Write the response
        con.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
        con.getResponseBody().write(response);
        con.getResponseBody().flush();
        con.getResponseBody().close();
        if (con.getRequestMethod().contentEquals("POST")) {

            try {
                bodyToJSON();
                updateTable();
            } catch (Exception ex) {
                Logger.getLogger(HeartBeatHandler.class.getName()).log(Level.SEVERE, null, ex);
                System.out.print("parseerror");
            }

        } else {
            System.out.println(con.getRequestMethod());
        }

        con.close();
        //  throw new IOException("not post");
    }

    void bodyToJSON() throws ParseException {                          //converts rewuest body to json
        if (req_body.isEmpty()) {
            throw new NullPointerException("Request Body is empty");
        }

        JSONParser parser = new JSONParser();               //obtain data from JSON string
        JSONObject json = (JSONObject) parser.parse(req_body);
        from_id_str = (String) json.get("ID");
        from_id_int = Integer.parseInt(from_id_str);
        from_id_ipaddr = ((String) json.get("IPADDR"));
        from_id_port = Integer.parseInt(((String) json.get("IPORT")));
        from_id_time = Long.parseLong((String) json.get("TIME"));
        trailer = (JSONArray) json.get("TRAILER");                 //TODO parse the array

    }

    void updateTable() {                                     //TODO make this thread safe and take arguemnts rather than member functions

        if (node_data.containsKey(from_id_int)) {                                       //already an entry is present
            if (node_data.get(from_id_int).getTimestamp() < time) {                       //update only if timestamp is greater
                node_data.get(from_id_int).setTimestamp(time);                            //NOTE:here here the current system time is taken
                queue.update(node_data.get(from_id_int));
                if (node_data.get(from_id_int).getInputPort() != from_id_port) {          //changes port if the timestamp is new                     
                    node_data.get(from_id_int).setInputPort(from_id_port);

                }
                if (!(node_data.get(from_id_int).getIPAddress().contentEquals(from_id_ipaddr))) {          //changes ipaddress if the timestamp is new                     
                    node_data.get(from_id_int).setIPAddress(from_id_ipaddr);

                }

            }
        } else {                                                                          //if the entry is not present
            node_data.put(from_id_int, new Node(from_id_int, time, from_id_port, from_id_ipaddr));   //NOTE:here here the current system time is taken
            queue.update(node_data.get(from_id_int));                                   //TODO update or add

        }
        updateTableFromTrailer(trailer, from_id_time);                                  //no need to pass json array

    }

    void updateTableFromTrailer(JSONArray jsonarray, long sender_time) {

        for (int i = 0; i < jsonarray.size(); i++) {                    //TODO check correct elemnt is present or not
            JSONObject jsonobject = (JSONObject) jsonarray.get(i);

            String id = (String) jsonobject.get("ID");
            String ipadr = (String) jsonobject.get("IPADDR");
            String timest = (String) jsonobject.get("TIME");
            String port = (String) jsonobject.get("IPORT");

            updateTable(id, ipadr, timest, port, sender_time);
            //assuming sender_time and time to be timestamp of Sender and receiver at the instant 
            //we use to calcute the relative time of the other nodes to the receiver
        }

    }

    void updateTable(String id, String ipaddr_ob, String timest, String port, long sender_time) {                                     //TODO make this thread safe and take arguemnts rather than member functions
        //assuming sender_time and time to be timestamp of Sender and receiver at the instant 
        //we use to calcute the relative time of the other nodes to the receiver

        int id_ob = Integer.parseInt(id);
        int port_ob = Integer.parseInt(port);
        long time_ob = Long.parseLong(timest);
        time_ob = time - (sender_time - time_ob);                       //gets relative time for current system

        if (node_data.containsKey(id_ob)) {                                       //already an entry is present
            if (node_data.get(id_ob).getTimestamp() < time_ob) {                       //update only if timestamp is greater
                node_data.get(id_ob).setTimestamp(time_ob);
                queue.update(node_data.get(id_ob));
                if (node_data.get(id_ob).getInputPort() != port_ob) {          //changes port if the timestamp is new                     
                    node_data.get(id_ob).setInputPort(port_ob);

                }
                if (!(node_data.get(id_ob).getIPAddress().contentEquals(ipaddr_ob))) {          //changes ipaddress if the timestamp is new                     
                    node_data.get(id_ob).setIPAddress(ipaddr_ob);

                }

            }
        } else {                                                                          //if the entry is not present
            node_data.put(id_ob, new Node(id_ob, time_ob, port_ob, ipaddr_ob));
            queue.update(node_data.get(id_ob));

        }

    }
}
