package com.winston.JavaCh;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Thread {
	
	private JSONArray posts;
	//represents a 4chan thread
	private String board = "";
	private long id;
	private boolean is404 = false;
	private boolean isStickied = false;
    private boolean archived = false;
    private boolean bumpLimit = false;
    private boolean imageLimit = false;
    
    int customSpoiler = 0;
    String topic;
    
	
	long lastUpdate = 0;
	
	public Thread(String board, long id){
	    this.id = id;
	    this.board = board;	    
	}
	
	public long getID(){
	    return id;
	}
	public String getBoard(){
	    return board;
	}
	
	private void populate(){
		JSONObject metadata = (JSONObject) JSONFetcher.vomit("https://a.4cdn.org/" + board + "/thread/" + id + ".json");
		posts = (JSONArray) metadata.get("posts");
		if(((JSONObject) posts.get(0)).containsKey("sticky")){
			
		    isStickied = true;
		}
		if(((JSONObject) posts.get(0)).containsKey("closed")){
			
		    is404 = true;
		}
		if(((JSONObject) posts.get(0)).containsKey("archived")){
			
		    archived = true;
		}
		if(((JSONObject) posts.get(0)).containsKey("imagelimit")){
			
		    imageLimit = true;
		}
		if(((JSONObject) posts.get(0)).containsKey("bumplimit")){
			
		    bumpLimit = true;
		}
	}
	public boolean isStickied(){
	    if(posts == null){
	        populate();
	    }
	    return isStickied;
	}
	public String board(){
	    return board;
	}
	public long ID(){
		return id;
	}
	public boolean is404(){
	    if(posts == null){
	        populate();
	    }
	    return is404;
	}
	public boolean isArchived(){
	    if(posts == null){
	        populate();
	    }
	    return archived;
	}
	public boolean bumpLimit(){
	    if(posts == null){
	        populate();
	    }
	    return bumpLimit;
	}
	public boolean imageLimit(){
	    if(posts == null){
	        populate();
	    }
	    return imageLimit;
	}
}
