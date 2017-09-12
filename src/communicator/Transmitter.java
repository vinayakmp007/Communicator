/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author vinayak
 */
public class Transmitter implements Runnable {

    Map<Integer, Node> node_data;                    //data structers to store nodes
    NodePriorityQueue node_pqueue;
    ExecutorService executor;
    Element elem;
    volatile int status;

    public Transmitter(Element elem, Map<Integer, Node> node, NodePriorityQueue que) {
        this.node_data = node;
        this.node_pqueue = que;
        this.elem = elem;
        executor = Executors.newFixedThreadPool(elem.getTransmitterThreadPoolSize());

    }

    public void run() {

        status = 1;                                                 //asumming someone changes it 

        while (status > 0) {

            NodePriorityQueue tem = new NodePriorityQueue(node_pqueue);               //creates a temporay queue

            Runnable worker;
            int i = 0;
            while (tem.hasNext()) {
                i++;

                Node t = (Node) tem.next();
                if (t.getTimestamp() < (System.currentTimeMillis()) - elem.getWaitTillSendmillis()) {
                    break;            // ignore if they are 35 second  old or more
                }
                if ((Math.random() < elem.getProbablityToGetTransmittedTo())) {
                    try {
                        worker = new Sender(elem, t, "heartbeat", node_pqueue, node_data);
                        executor.execute(worker);

                    } catch (Exception ex) {
                        Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, null, ex);
                        ex.printStackTrace();

                    }

                }

            }

            try {
                Thread.sleep(elem.heartbeat_in_ms);
            } catch (InterruptedException ex) {
                Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }
    }

}
