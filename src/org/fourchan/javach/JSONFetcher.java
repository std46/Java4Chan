package org.fourchan.javach;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//Given a JSON endpoint returns either JSONObject or JSONArray
public class JSONFetcher {
	private static String proxyUrl;
	private static Integer proxyPort;

	public static void setProxy(String url, Integer port) {
		proxyUrl = url;
		proxyPort = port;
	}
	
	public static Object vomit (String url){
		JSONParser parser = new JSONParser();
		URLConnection yc = null;
		Object o = null;
		
		try {
			URL oracle = new URL(url); // URL to Parse
			if (null == proxyUrl) {
				yc = oracle.openConnection();
			} else {
				Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl, proxyPort));
				yc = oracle.openConnection(p);
			}
		} catch (IOException e) {
			return null;
		}

		try (BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()))) {
			o = parser.parse(in);
		}  catch (FileNotFoundException e) { //any exception results in a null return
			return null;					//may be changed as needed
		} catch (IOException e) {
			return null;
		} catch (ParseException e) {
			return null;
		}

		return o;
	}  
	
}