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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 *Every unique nodes need an element object to configure it.
 * @author vinayak
 */
public class Element {                 //this class used by every individual system

    int identifier, input_port_no;                                                //ip might change here
    long heartbeat_in_ms;
    long wait_till_send;
    long wait_till_dead;
    long sending_interval;
    long maximum_time_sync_error;
    int send_threadpoolsize;
    int rcv_threadpoolsize;
    String myipaddr;
    double probablitiy_to_get_selected, probablitiy_to_get_transmitted_to;
    boolean dynamic_ip;
    /**
     * creates an element object
     * The Element object ris required to configure all the Communicator Object
     * <br>
     * default sender threadpool size is 5
     * <br>default receiver threadpool size is 5
     * <br>default probability a nodes gets selected is .6
     * <br>default probability a nodes gets transmitted is .6
     * 
     * @param identifier (unique)identfies nodes in the element
     * @param inputport port number for accepting input http connections
     * @param heartbeat_in_ms interval at which the node contacts other nodes in the network in milliseconds
     */
    public Element(int identifier, int inputport, long heartbeat_in_ms) {
        /*
arguments 
1)idenfifier 
2)input_port 
3)heartbeat interval 
4)*/
        this.identifier = identifier;
        this.input_port_no = inputport;
        this.heartbeat_in_ms = heartbeat_in_ms;
        send_threadpoolsize = 5;                   //default threadpool size is 5 
        wait_till_send = 3 * this.heartbeat_in_ms;                  //default value is 30 
        wait_till_dead = 5 * this.heartbeat_in_ms;                  //default value is 50
        rcv_threadpoolsize = 5;
        probablitiy_to_get_selected = .6;
        probablitiy_to_get_transmitted_to = .6;
        dynamic_ip=true;
    }
    /**
     * Returns the identifier of the element
     * @return 
     */
    public int getIdentifier() {
        return identifier;
    }
/**
 * Returns the probablity the element in the node list is selected that its details is transferred in the heart beat.
 * @return the probability between 0 t0 1
 */
    public double getProbabilityToGetSelected() {
        return probablitiy_to_get_selected;
    }
/**
 * Returns the probability the element will be selected by the current node from the node list for making transfering heartbeat to.
 * @return the probability between 0 t0 1
 */
    public double getProbabilityToGetTransmittedTo() {
        return probablitiy_to_get_transmitted_to;
    }
/**
 * Set the probablity the element in the node list is selected that its details is transferred in the heart beat.
 * @param a the probability between 0 t0 1
 */
    public void setProbabilityToGetSelected(double a) {
        probablitiy_to_get_selected = a;
    }
/**
 * Set the probability the element will be selected by the current node from the node list for making transfering heartbeat to.
 * @param a the probability between 0 t0 1
 */
    public void setProbabilityToGetTransmittedTo(double a) {
        this.probablitiy_to_get_transmitted_to = a;
    }
/**
 * Returns the input port used by the current Element object.
 * @return port number 
 */
    public int getInputPort() {
        return input_port_no;
    }
/**
 * Returns the threadpoolsize for sending heartbeat.
 * @return threadpool size
 */
    public int getTransmitterThreadPoolSize() {
        return send_threadpoolsize;
    }
    
    /**
     * Sets the threadpoolsize for sending heartbeat.
     * @param a 
     */

    public void setTransmitterThreadPoolSize(int a) {
        send_threadpoolsize = a;
    }
/**
 * Returns the threadpoolsize for receiving heartbeat.
 * @return threadpool size
 */
    public int getReceiverThreadPoolSize() {
        return rcv_threadpoolsize;
    }
    /**
     * Sets the threadpoolsize for receiving heartbeat.
     * @param a 
     */

    public void setReceiverThreadPoolSize(int a) {
        rcv_threadpoolsize = a;
    }
/**
 * Set time difference between current time and last known timestamp of the \nodes ater which the nodes should  be selected to transfer
 * the heartbeat.
 * @param a time in milli seconds
 */
    public void setWaitTillSendmillis(long a) {
        wait_till_send = a;
    }
/**
 * Set time difference between current time and last known timestamp of the nodes ater which the nodes should  be considered dead.
 * @param a time in milli seconds
 */
    public void setWaitTillDeadmillis(long a) {
        wait_till_dead = a;
    }
/**
 * Returns time difference between current time and last known timestamp of the \nodes ater which the nodes should  be selected to transfer
 * @return time in milli seconds
 */
    public long getWaitTillSendmillis() {
        return wait_till_send;
    }
    
/**
 * Returns time difference between current time and last known timestamp of the nodes ater which the nodes should  be considered dead.
 * @return time in milliseconds
 */
    public long getWaitTillDeadmillis() {
        return wait_till_dead;
    }
/**
 * Set the hearbeat interval.Hearbeat is the interval after which the node connects to remote nodes 
 * @return 
 */
    public long getHeartBeatInterval() {
        return heartbeat_in_ms;
    }
    /**
     * Set the  sending interval in millis
     * @param a 
     */

    public void setSendingIntervalmillis(long a) {
        sending_interval = a;
    }
    /**
     * Returns thw sending Interval in millis
     * @return 
     */

    public long getSendingIntervalmillis() {
        return sending_interval;
    }
/**
 * Returns the current hosts ip 
 * The returned ip is not loopback address id dyanmic ip is set
 * If Dynamic ip is set then the this methods returns the current IP address of the node
 * If dynamic IP is not set this function returns the IP address given in the element object
 * @return
 * @throws UnknownHostException
 * @throws SocketException 
 */
    public String getHostIP() throws UnknownHostException, SocketException {
        if(dynamic_ip)
        {
            myipaddr = getCurrentIp();
                return myipaddr;
        }
        return myipaddr;
    }
    public void setHostIP(String ip) throws UnknownHostException {
        dynamic_ip=false;
        myipaddr=ip;
    }/**
     * REturns the current IP of the node.This avoids loop back addresses
     * @return
     * @throws UnknownHostException
     * @throws SocketException 
     */
    public static String getCurrentIp() throws UnknownHostException, SocketException {                  //Copied from stack overflow
            
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                        .getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface ni = (NetworkInterface) networkInterfaces
                            .nextElement();
                    Enumeration<InetAddress> nias = ni.getInetAddresses();
                    while(nias.hasMoreElements()) {
                        InetAddress ia= (InetAddress) nias.nextElement();
                        if (!ia.isLinkLocalAddress() 
                         && !ia.isLoopbackAddress()
                         && ia instanceof Inet4Address) {
                            return ia.getHostAddress();
                        }
                    }
                }
            
            return InetAddress.getLocalHost().getHostAddress();
        }
    
}
