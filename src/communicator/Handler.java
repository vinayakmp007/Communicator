/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author vinayak
 */
public class Handler implements HttpHandler {

    Map<Integer, Node> node_data;
    NodePriorityQueue queue;
    Element element;

    public Handler(Element element, Map<Integer, Node> t, NodePriorityQueue b) {
        node_data = t;
        queue = b;
        this.element = element;
    }

    public static Map<String, String> queryToMap(String query) {             //found this code on the internet
        Map<String, String> result = new HashMap<>();             //splits the queries and makes a key value pair
        if (query.isEmpty()) {
            return result;
        }
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }

    @Override
    public void handle(HttpExchange con) throws IOException {
        String path = con.getRequestURI().getPath();
        switch (path) {                                    //TODO more improvements required like mapping to HttpHandler

            case "/heartbeat":
                HeartBeatHandler heartbthand = new HeartBeatHandler(element, node_data, queue);
                heartbthand.handle(con);

                break;
            case "/requestall":
                RequestAllHandler ra_handler = new RequestAllHandler(element, node_data, queue);
                ra_handler.handle(con);
            default:
                System.out.println(path);
        }
    }
}
