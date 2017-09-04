/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communicator;
import java.util.*;
/**
 *
 * @author vinayak
 */
public interface Handler {
     public static Map<String, String> queryToMap(String query){             //found this code on the internet
    Map<String, String> result = new HashMap<>();             //splits the queries and makes a key value pair
    if(query.isEmpty())return result;
    for (String param : query.split("&")) {
        String pair[] = param.split("=");
        if (pair.length>1) {
            result.put(pair[0], pair[1]);
        }else{
            result.put(pair[0], "");
        }
    }
    return result;
  }
}
