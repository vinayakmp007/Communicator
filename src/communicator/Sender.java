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
import java.util.Iterator;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author vinayak
 */
public class Sender implements Runnable {

    Element element;
    Node to;
    String service;
    NodePriorityQueue queue;
    Map<Integer, Node> node_data;

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
    final void connect() throws MalformedURLException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        KeyStore ks = KeyStore.getInstance("JKS");

        ks.load(new FileInputStream(element.getKeyFile()), element.getPassword().toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, element.getPassword().toCharArray());
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, null);

        String urls = "https://" + to.getIPAddress() + ":" + to.getInputPort() + "/" + service;             //creates the url for connecting

        URL url = new URL(urls);
        try {
            HttpsURLConnection httpCon = (HttpsURLConnection) url.openConnection();
            // connects to the service
            httpCon.setSSLSocketFactory(sc.getSocketFactory());
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

            /*Trailer in the Json String*/
            json.put("TRAILER", makeTrailer());

            OutputStream os = httpCon.getOutputStream();
            try (OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8")) {
                osw.write(json.toString());
                osw.flush();
                osw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Reader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "UTF-8"));

            for (int c; (c = in.read()) >= 0;);

            httpCon.connect();
            httpCon.disconnect();
        } catch (ConnectException ec) {                   //Need more functinalities here

            ec.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JSONArray makeTrailer() throws UnknownHostException {   //this method makes trailer for from prioruty queue

        JSONArray ja = new JSONArray();
        NodePriorityQueue tem = new NodePriorityQueue(queue);               //creates a temporay queue

        while (tem.hasNext()) {

            JSONObject temp = new JSONObject();
            Node t = (Node) tem.next();
            if (t.getTimestamp() < (System.currentTimeMillis()) - element.wait_till_dead) {
                break;            // dead if they are  old 
            }
            temp.put("ID", Integer.toString(t.getIdentifier()));
            temp.put("IPADDR", t.getIPAddress());
            temp.put("IPORT", Integer.toString(t.getInputPort()));
            temp.put("TIME", Long.toString(t.getTimestamp()));
            ja.add(temp);
        }

        return ja;
    }
}
