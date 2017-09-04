/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.util.*;
import java.io.*;
import java.lang.NullPointerException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author vinayak
 */
public class HeartBeatHandler implements HttpHandler,Handler{
   
   Map<String,String> param =new HashMap<>();
   Map<String,String[]> req_headr=new HashMap<>();
   Map<Integer,Node> node_data;
   InputStream data_inpstream;
   String req_body,from_id_str;
   int req_bodysize,from_id_int;
   JSONObject trailer;
   long time;
   
   public HeartBeatHandler(Map<Integer,Node> t){
   node_data=t;
   }
   public void handle(HttpExchange con) throws IOException{
   
  // param=Handler.queryToMap(con.getRequestURI().getQuery());
    time = System.currentTimeMillis();
   data_inpstream=con.getRequestBody();
   
   byte tm,tmbuf[]=new byte[4096];
   char tmp;
  StringBuilder tembuf=new StringBuilder();
   
 while((tm= (byte) data_inpstream.read()) !=-1){
 tmp=(char) tm;
     tembuf.append(tmp);
  
 }
  req_body=tembuf.toString();
  
  if(con.getRequestMethod().contentEquals("POST")){
      System.out.println(req_body);
       try {
           bodyToJSON();
           updateTable();
       } catch (ParseException ex) {
           Logger.getLogger(HeartBeatHandler.class.getName()).log(Level.SEVERE, null, ex);
       }
      
  }
  else System.out.println(con.getRequestMethod());
 //  throw new IOException("not post");
   }
    void bodyToJSON() throws ParseException{
      if(req_body.isEmpty())throw new NullPointerException("Request Body is empty");
      
      
       JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(req_body);
        from_id_str = ((String) json.get("ID"));
        from_id_int=Integer.parseInt(from_id_str);
        trailer = (JSONObject) json.get("trailer");
        System.out.println(trailer);
        
        
          
   }
    
    void updateTable(){
    
    if(node_data.containsKey(from_id_int)){                                       //already an entry is present
            if(node_data.get(from_id_int).getTimestamp()<time){                       //update only if timestamp is greater
        node_data.get(from_id_int).setTimestamp(time);
        }
                }
        else {                                                                          //if the enter is not present
        node_data.put(from_id_int, new Node(from_id_int,time));
        
        
        }
    
    }
}
