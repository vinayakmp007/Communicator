/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.Map;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author vinayak
 */
public class Sender implements Runnable {

    Element element;
    Node to;
    String service, reqbody;
    NodePriorityQueue queue;
    Map<Integer, Node> node_data;
    long remote_time, time;
    String remote_ip;
    int remote_port, remote_id;

    public Sender(Element el, Node a, String serv, NodePriorityQueue queu, Map<Integer, Node> nod_dat) throws IOException {
        //This is used send information to individual system in JSON format
        this.queue = queu;
        this.node_data = nod_dat;
        this.to = a;
        this.element = el;
        this.service = serv;

    }

    @Override
    public void run() {

        try {
            connect();
        } catch (Exception ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();

        }
    }

    @SuppressWarnings("empty-statement")
    final void connect() throws MalformedURLException, IOException{

        String urls = "http://" + to.getIPAddress() + ":" + to.getInputPort() + "/" + service;             //creates the url for connecting
        time = System.currentTimeMillis();                                                             //the current time when the connection is made
        URL url = new URL(urls);
        try {

            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            // connects to the service

            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("POST");
            httpCon.setUseCaches(false);
            httpCon.setDoInput(true);

            /*Header in the JSON String*/
            JSONObject json = new JSONObject();
            json.put("ID", Integer.toString(element.getIdentifier()));
            json.put("IPADDR", element.getHostIP());
            json.put("IPORT", Integer.toString(element.getInputPort()));
            json.put("TIME", Long.toString(System.currentTimeMillis()));
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-Type", "application/json");

            switch (service) {

                case "heartbeat":
                    /*Trailer in the Json String*/
                    json.put("TRAILER", makeTrailer());
                    break;
                case "requestall":
                    break;

            }

            OutputStream os = httpCon.getOutputStream();

            try (OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8")) {
                //System.out.println(json);
                osw.write(json.toString());
                osw.flush();
                osw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Reader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "UTF-8"));

            byte tm, tmbuf[] = new byte[4096];
            char tmp;
            StringBuilder tembuf = new StringBuilder();
            while ((tm = (byte) in.read()) != -1) {
                tmp = (char) tm;
                tembuf.append(tmp);

            }

            switch (service) {

                case "heartbeat":
                    /*Trailer in the Json String*/

                    break;
                case "requestall":
                    //System.out.println(tembuf);
                    JSONParser parser = new JSONParser();               //obtain data from JSON string
                    JSONObject reqob = (JSONObject) parser.parse(tembuf.toString());
                    JSONArray ja = (JSONArray) reqob.get("NODES");
                    remote_ip = (String) reqob.get("IPADDR");
                    remote_id = Integer.parseInt((String) reqob.get("ID"));
                    remote_port = Integer.parseInt((String) reqob.get("IPORT"));
                    remote_time = Long.parseLong((String) reqob.get("TIME"));

                    for (int i = 0; i < ja.size(); i++) {                    //TODO check correct elemnt is present or not .This looop adds all elemnts to the table
                        JSONObject jsonobject = (JSONObject) ja.get(i);

                        String id = (String) jsonobject.get("ID");
                        String ipadr = (String) jsonobject.get("IPADDR");
                        String timest = (String) jsonobject.get("TIME");
                        String port = (String) jsonobject.get("IPORT");
                        updateTable(id,ipadr,port,timest);
                    }

                    break;

            }

            httpCon.disconnect();
        } catch (ConnectException ec) {
        } catch (IOException | ParseException | NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    public JSONArray makeTrailer() throws UnknownHostException {   //this method makes trailer for from prioruty queue

        JSONArray ja = new JSONArray();
        NodePriorityQueue tem = new NodePriorityQueue(queue);               //creates a temporay queue

        while (tem.hasNext()) {

            JSONObject temp = new JSONObject();
            Node t = (Node) tem.next();
            if (t.getTimestamp() < (System.currentTimeMillis() - element.getWaitTillDeadmillis())) {
                break;            // rest are dead if they are  tooo old since it is ordered queue
            }
            if ((Math.random() < element.getProbabilityToGetSelected())) {              //randomy select wheither the details of node should be transmitted
                temp.put("ID", Integer.toString(t.getIdentifier()));
                temp.put("IPADDR", t.getIPAddress());
                temp.put("IPORT", Integer.toString(t.getInputPort()));
                temp.put("TIME", Long.toString(t.getTimestamp()));
                ja.add(temp);
            }
        }

        return ja;
    }

    private void updateTable(String id, String ipaddr, String port, String timestmp) //used update table and queue
    {
        long timest = Long.parseLong(timestmp), obtime;
        int idn = Integer.parseInt(id), port_ob = Integer.parseInt(port);
        timest = time - (remote_time - timest);

        if (idn != element.getIdentifier()) {                            //checks whether itself is found inside the trailer
            if (node_data.containsKey(idn)) {                                       //already an entry is present
                if (node_data.get(idn).getTimestamp() < timest) {                       //update only if timestamp is greater
                    node_data.get(idn).setTimestamp(timest);
                    queue.update(node_data.get(idn));
                    if (node_data.get(idn).getInputPort() != port_ob) {          //changes port if the timestamp is new                     
                        node_data.get(idn).setInputPort(port_ob);

                    }
                    if (!(node_data.get(idn).getIPAddress().contentEquals(ipaddr))) {          //changes ipaddress if the timestamp is new                     
                        node_data.get(idn).setIPAddress(ipaddr);

                    }

                }
            } else {                                                                          //if the entry is not present
                node_data.put(idn, new Node(idn, timest, port_ob, ipaddr));
                queue.update(node_data.get(idn));

            }

        } else {   //the part where the node finds about itself inside the trailer yet to implement

        }

    }
}
