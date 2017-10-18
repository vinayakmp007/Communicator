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
import java.util.*;

/**
 *This handles all the incoming HTTP request on the node
 * @author vinayak
 */
public class Handler implements HttpHandler {

    Map<Integer, Node> node_data;
    NodePriorityQueue queue;
    Element element;
/**
 * 
 * @param element Element object of the communicator
 * @param t Node table of the Communicator
 * @param b Node prioritQueue object of the Communicator
 */
    public Handler(Element element, Map<Integer, Node> t, NodePriorityQueue b) {
        node_data = t;
        queue = b;
        this.element = element;
    }
/**
 * Coverts Query pass along with request to map
 * @param query
 * @return 
 */
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
                break;
            default:
                System.out.println(path);
        }
    }
}
