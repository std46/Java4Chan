package org.fourchan.java4chan;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.fourchan.java4chan.exception.ThreadNotFoundException;

//Given a JSON endpoint returns either JSONObject or JSONArray
public class JSONFetcher {
	private static String proxyUrl;
	private static Integer proxyPort;
	private static long lastFetchTime = -1L;

	public static void setProxy(String url, Integer port) {
		proxyUrl = url;
		proxyPort = port;
	}

	public static Object vomit (String url){
		block();
		ObjectMapper parser = new ObjectMapper();
		URLConnection yc = null;
		
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
			return parser.readValue(in, Object.class);
		} catch (IOException e) {
			if (FileNotFoundException.class.isAssignableFrom(e.getClass())) {
				throw new ThreadNotFoundException(e);
			}//any exception results in a null return
			return null;					//may be changed as needed
		}
	}

	@SneakyThrows
	private static synchronized void block() {
		long curTime = System.currentTimeMillis();
		long delta = (curTime - lastFetchTime) / 1000;
		if (delta < 60) {
			java.lang.Thread.sleep(60 - delta);
		}
		lastFetchTime = curTime;
	}
	
}