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
public class Node {
int identifier;
long milli_timestamp;
public int getIdentifier(){
return identifier;
}
public long getTimestamp(){
return milli_timestamp;
}
public void setIdentifier(int i){
identifier=i;
}
public void setTimestamp(long i){
milli_timestamp=i;
}
public Node(int i,long j){
identifier=i;
milli_timestamp=j;

}
}
