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

/**
 *
 * @author vinayak
 */
public class Sender {

    Node to;
    String service;

    public Sender(Node a, String serv) {
        this.to = a;
        this.service = serv;

    }

    public void connect() throws MalformedURLException, IOException {
        String urls = "http://" + to.getIPAddress() + ";" + to.getInputPort() + "/" + service;             //creates the url for connecting
        URL url = new URL(urls);

        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();                     // connetcs to the service
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("POST");

    }
}
