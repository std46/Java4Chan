package com.winston.JavaCh;

//use this to build urls
public class URL {
	
	String api = "a.4cdn.org/";
	String html = "boards.4chan.org"; //html subdomain
	String image = "i.4cdn.org"; //images
	String thumb = "t.4cdn.org"; //thumbnails
	String stat = "s.4cdn.org"; //static host (spoilers, flags, capcode icons, etc
	
	public String catalogURL(String board){ //returns url for catalog fetch
		return null;
	}
	
	public String boardURL(String protocol, String name) {
		String url = protocol;
		url += api;
		url += name;
		
		return url;
	}
}
