package com.winston.JavaCh;
import org.json.simple.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//board object represents boards
public class Board {
	
	static JSONObject metadata;
	static URL urlGenerator = new URL();
	static boolean cache = true; //default setting
	static int refresh = 300; //default time for cache to refresh an item in seconds 
	
	public static Object getBoards(List<String> list){ //get specific boards
		
		List<specBoard> myList = new LinkedList<>();
		for (String s : list){
		    specBoard board = new specBoard(s);
		    myList.add(board);
		    
		}
		return myList;
	}
	
	public static Object allBoardsJSON(){ //returns the 4chan api's representation of all boards
		if(metadata == null) {
			metadata = (JSONObject) JSONFetcher.vomit("https://a.4cdn.org/boards.json");
		}
	    return metadata;
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
	    
		LinkedList<Long> threadIDs = new LinkedList<>();
		specBoard board = new specBoard(id);
        JSONArray jsonobj = (JSONArray) JSONFetcher.vomit(board.getURL() + "/threads.json");
        for(Object o: jsonobj) {
            JSONObject page = (JSONObject) o;
            JSONArray pageArray = (JSONArray) page.get("threads");
            for (Object t: pageArray) {
            	JSONObject thread = (JSONObject) t;
                threadIDs.add((Long) thread.get("no"));
            }
        }
        return threadIDs;
		
	}
	
	public static void cache(boolean decision) { //should cache be used? default is true
		cache = decision;
	}
	
	public static class specBoard { //represents 4chan board
		
		private String name; //the name. ex: g, fit, etc
		String protocol = "https://";
		private String url;
		
		private String title; //Ex: Technology, Fitness
		private Boolean isWorksafe; //is the board "safe for work"
		private int pageCount = 0;
		private int threadsPerPage = 0;
		private String description; //Ex: /g/ is for hardware and software
		
		long lastUpdate = 0;
		
		HashMap<Integer, Thread> cache = new HashMap<>();
		
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
		
		public Thread getThread(int id, boolean updateCached){
			
			Thread cachedThread = cache.get(new Integer(id));
			if(cachedThread == null){ //not cached
				System.out.println("heyhey " + id);
			    Thread newThread = new Thread(name, id);
			    cache.put(id, newThread);
			    System.out.println(cache.containsKey(new Integer(id)));
			    System.out.println(cache);
			    return cache.get(id);
			}
			if(!updateCached){ //just return the cached thread if necessary
				return cachedThread;
			}
			Thread newThread = new Thread(name, id);
			if(newThread.is404()){ //return null on a 404ed thread
				cache.remove(new Integer(id)); //remove thread from cache
				return null;
			}
			//if here, the cache needs to be updated and the thread is not closed
			
			cache.put(new Integer(id), newThread);
			return newThread;
		}
		
		public List<Thread> getThreads(){
		    return getThreads(1);
		}
		public List<Thread> getThreads(int page){
			if(page <= 0 || pageCount() < page) { //invalid page
			    return null;
			}
			List<Thread> myList = new LinkedList<>();
			JSONObject jsonobj = (JSONObject) JSONFetcher.vomit(url + "/" + page + ".json");
			
			JSONArray threads = (JSONArray) jsonobj.get("threads");
			for (Object t: threads) {
				JSONArray posts = (JSONArray) ((JSONObject) t).get("posts");
				myList.add(new Thread(name, (long)((JSONObject)posts.get(0)).get("no")));
				
			}
			return myList;
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
		            cache.put(new Integer((int) (long) id), newThread);//adds thread to cache
	            	
	            	
	                
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
		
		public long populate(){ //populate all the fields
			if(metadata == null) {
				metadata = (JSONObject) JSONFetcher.vomit("https://a.4cdn.org/boards.json");
			}
		    JSONObject jsonObj = metadata; //get data for all boards
		    JSONArray boards = (JSONArray) jsonObj.get("boards");
		    JSONObject board = null;
		    
		    for (Object o : boards) { //narrow it down to our board
		        board = (JSONObject) o;
		        if (board.get("board").equals(name)){
		            break;
		        }
		    }
		    
		    isWorksafe = ((long)board.get("ws_board") == 1) ? true: false;
		    pageCount = (int) (long) board.get("pages");
		    threadsPerPage = (int) (long) board.get("per_page");
		    title = (String) board.get("title");
		    description = (String) board.get("meta_description");
		    
		    return lastUpdate;
		}
		
		public boolean isWorksafe(){
		    if(isWorksafe == null) {
		        populate();
		    }
		    return isWorksafe;
		}
		public int pageCount(){
		    if(pageCount == 0) {
		        populate();
		    }
		    return pageCount;
		}
		public int threadsPerPage(){
		    if(threadsPerPage == 0) {
		        populate();
		    }
		    return threadsPerPage;
		}
		public String title(){
		    if(title == null) {
		        populate();
		    }
		    return title;
		}
		public String description(){
		    if(description == null) {
		        populate();
		    }
		    return description;
		}
	}
}
