package javach;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONFetcher {

public static Object vomit(String url){
	JSONParser parser = new JSONParser();
	System.out.println("f");
	try {        
		URL oracle = new URL(url); // URL to Parse
		System.out.println("reach");
		URLConnection yc = oracle.openConnection();
		System.out.println("reach");
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
 	  	Object o = parser.parse(in);

 	  	in.close();
 	  	return o;
    
	} catch (FileNotFoundException e) {
		return null;
	} catch (IOException e) {
		return null;
	} catch (ParseException e) {
		return null;
	}  
	
	
}  



}