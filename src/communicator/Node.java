/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

/**
 *
 * @author vinayak
 */
public class Node {                         //this is used represent the other nodes(systems in the network) in memory

    int identifier;                        //uniquely identifies every system in the network which defined by every system Elemets class
    long milli_timestamp;
    int input_port;
    String ipaddress;

    public synchronized int getIdentifier() {
        return identifier;
    }

    public synchronized long getTimestamp() {
        return milli_timestamp;
    }

    public synchronized void setIdentifier(int i) {
        identifier = i;
    }

    public synchronized void setTimestamp(long i) {
        milli_timestamp = i;
    }

    public synchronized void setInputPort(int i) {
        input_port = i;
    }

    public synchronized void setIPAddress(String s) {
        ipaddress = s;

    }

    public String getIPAddress() {
        return ipaddress;

    }

    public int getInputPort() {
        return input_port;
    }

    public Node(int id, long timestmp, int port, String ipaddr) {
        identifier = id;
        milli_timestamp = timestmp;
        input_port = port;
        ipaddress = ipaddr;

    }
     public Node(Node node) {
      this.identifier=node.identifier;
      this.input_port=node.input_port;
      this.ipaddress=node.ipaddress;
      this.milli_timestamp=node.milli_timestamp;

    }

}
