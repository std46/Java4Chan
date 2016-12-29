package com.winston.JavaCh;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Thread { //represents a 4chan thread
	
	JSONArray posts; //array of posts
	private LinkedList<Post> postCache = new LinkedList<>();
	
	private String board = ""; //name of the board. ex: a, g, tg
	private long id; //the thread id
	private boolean is404 = false; 		//has the thread been closed?
	private boolean isStickied = false; //stickied?
    private boolean archived = false;	//archived?
    private boolean bumpLimit = false;  //has it hit the bumplimit
    private boolean imageLimit = false; //has it hit the imagelimit
    
    int customSpoiler = 0;
    
    Post OriginalPost;		//the OP of the thread
    
	
	long lastUpdate = 0;
	
	public Thread(String board, long id){
	    this.id = id;
	    this.board = board;	
	    
	    populate();
	}
	
	public long getID(){
	    return id;
	}
	public String getBoard(){
	    return board;
	}
	
	private void populate(){ //populate all the metadata of the thread
		JSONObject metadata = (JSONObject) JSONFetcher.vomit("https://a.4cdn.org/" + board + "/thread/" + id + ".json");
		posts = (JSONArray) metadata.get("posts");
		
		for(Object o: posts){ //add posts to cache
		    Post p = new Post(this, (JSONObject) o);
		    postCache.add(p);
		}
		
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
		OriginalPost = new Post(this, (JSONObject) posts.get(0));
	}
	
	public int refresh(){ //add all new posts needed, returns number of new posts
		JSONObject metadata = (JSONObject) JSONFetcher.vomit("https://a.4cdn.org/" + board + "/thread/" + id + ".json");
		posts = (JSONArray) metadata.get("posts");
		if(posts.size() == postCache.size()) { //no changes
			return 0;
		}
		
		int newPosts = 0;
		for(newPosts = 0; postCache.size() < posts.size(); newPosts++) { //adds all posts
			int i = posts.size();
			postCache.add(new Post(this, (JSONObject) posts.get(i))); //add new post
		}
		
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
		
		return newPosts;
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
	
	public List<String> fileUrls(){
		List<String> urls = new LinkedList<>();
		for(Post p: postCache){
			if(p.hasFile()){
				urls.add(p.getFile().url());
			}
		}
		return urls;
	}
	
	public List<File> fileList(){
	    List<File> fileList = new LinkedList<>();
	    for(Post p : postCache){
	    	if(p.hasFile()){
	    		fileList.add(p.getFile());
	    	}
	    }
	    return fileList;
	}
	
	public List<String> filenames(){
		
		List<String> filenames = new LinkedList<>();
		for(Post p: postCache){
		    if(p.hasFile()){
		    	filenames.add(p.getFile().filename());
		    }
			
		}
		
		return filenames;
	}
}
