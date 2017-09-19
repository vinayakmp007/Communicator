/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 *
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
    public Element(int identifier, int inputport, long heartbeat_in_ms) throws UnknownHostException {
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

    public int getIdentifier() {
        return identifier;
    }

    public double getProbablityToGetSelected() {
        return probablitiy_to_get_selected;
    }

    public double getProbablityToGetTransmittedTo() {
        return probablitiy_to_get_transmitted_to;
    }

    public void setProbablityToGetSelected(double a) {
        probablitiy_to_get_selected = a;
    }

    public void setProbablityToGetTransmittedTo(double a) {
        this.probablitiy_to_get_transmitted_to = a;
    }

    public int getInputPort() {
        return input_port_no;
    }

    public int getTransmitterThreadPoolSize() {
        return send_threadpoolsize;
    }

    public void setTransmitterThreadPoolSize(int a) {
        send_threadpoolsize = a;
    }

    public int getReceiverThreadPoolSize() {
        return rcv_threadpoolsize;
    }

    public void setReceiverThreadPoolSize(int a) {
        rcv_threadpoolsize = a;
    }

    public void setWaitTillSendmillis(long a) {
        wait_till_send = a;
    }

    public void setWaitTillDeadmillis(long a) {
        wait_till_dead = a;
    }

    public long getWaitTillSendmillis() {
        return wait_till_send;
    }

    public long getWaitTillDeadmillis() {
        return wait_till_dead;
    }

    public long getHeartBeatInterval() {
        return heartbeat_in_ms;
    }

    public void setSendingIntervalmillis(long a) {
        sending_interval = a;
    }

    public long getSendingIntervalmillis() {
        return sending_interval;
    }

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
    }
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
