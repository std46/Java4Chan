package com.winston.JavaCh;
import org.json.simple.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//board object represents boards
public class Board {
	
	static URL urlGenerator = new URL();
	
	public static Object getBoards(List<String> list){ //get specific boards
		
		List<specBoard> myList = new LinkedList<>();
		for (String s : list){
		    specBoard board = new specBoard(s);
		    myList.add(board);
		    
		}
		return myList;
	}
	
	public static Object allBoardsJSON(){ //returns the 4chan api's representation of all boards
	    return JSONFetcher.vomit("https://a.4cdn.org/boards.json");
	}
	
	public static List<specBoard> allBoards(){ //list of all boards
		
		LinkedList<specBoard> boardsList = new LinkedList<>();
		
		JSONObject allboards = (JSONObject) allBoardsJSON();
		JSONArray boards = (JSONArray) allboards.get("boards");
	    for (Object o : boards) {
	        JSONObject board = (JSONObject) o;
	        specBoard customBoard = new specBoard((String) board.get("board"));
	        boardsList.add(customBoard);
	    }
		
	    return boardsList;
	}
	
	public static List getAllThreadIds(String id){ //list of all threads in board
	    
		LinkedList threadIDs = new LinkedList();
		specBoard board = new specBoard(id);
        JSONArray jsonobj = (JSONArray) JSONFetcher.vomit(board.getURL() + "/threads.json");
        for(Object o: jsonobj) {
            JSONObject page = (JSONObject) o;
            JSONArray pageArray = (JSONArray) page.get("threads");
            for (Object t: pageArray) {
            	JSONObject thread = (JSONObject) t;
                threadIDs.add(thread.get("no"));
            }
        }
        return threadIDs;
		
	}
	public static class specBoard { //represents 4chan board
		
		String name; //the name. ex: g, fit, etc
		String protocol = "https://";
		String url;
		
		HashMap<Long, Thread> cache = new HashMap<>();
		
		public specBoard(String name){
		    this(true, name);	
		}
		
		public specBoard(boolean https, String name){
		    
			if(!https) { //swap protocols if necessary
			    protocol = "http://";
			}
		    this.name = name;
		    this.url = urlGenerator.boardURL(protocol, name);
		}
		
		public boolean hasThread(long id) { //checks if thread exists or no
		    JSONObject value = (JSONObject)JSONFetcher.vomit(url + "/thread/" + id + ".json");
		    if (value.containsKey("closed")){
		    	if ((int)value.get("closed") == 1){
		    		return false;
		    	}
		    }
		    return true;
		}
		
		public Thread getThread(long id){
		    return new Thread(name, id);
		}
		
		public List<Thread> getAllThreads(){//list of all thread objects
			List<Thread> myList = new LinkedList<>();
			JSONArray jsonobj = (JSONArray) JSONFetcher.vomit(url + "/threads.json");
	        for(Object o: jsonobj) {
	            JSONObject page = (JSONObject) o;
	            JSONArray pageArray = (JSONArray) page.get("threads");
	            for (Object t: pageArray) {
	            	JSONObject thread = (JSONObject) t;
	            	
	            	Long id = (Long) thread.get("no");
	            	
	            	
	            	Thread newThread = new Thread(name, (long) thread.get("no"));
		            myList.add(newThread);
		            cache.put(id, newThread);//adds thread to cache
	            	
	            	
	                
	            }
	        }
	        return myList;
			
		}
		
	    public List getAllThreadIds(){ //get list of all thread id's
	        LinkedList threadIDs = new LinkedList();
	        JSONArray jsonobj = (JSONArray) JSONFetcher.vomit(url + "/threads.json");
	        for(Object o: jsonobj) {
	            JSONObject page = (JSONObject) o;
	            JSONArray pageArray = (JSONArray) page.get("threads");
	            for (Object t: pageArray) {
	            	JSONObject thread = (JSONObject) t;
	                threadIDs.add(thread.get("no"));
	            }
	        }
	        return threadIDs;
	    }
		
		public String getName(){
			return this.name;
		}
		public String getURL(){
			return this.url;
		}
	}
}
