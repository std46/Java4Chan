package com.winston.JavaCh;
import org.json.simple.*;
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
	public static class specBoard { //represents 4chan board
		
		String name; //the name. ex: g, fit, etc
		String protocol = "https://";
		String url;
		
		
		public specBoard(String name){
		    this(true, name);	
		}
		
		public specBoard(boolean https, String name){
		    
			if(!https) {
			    protocol = "http://";
			}
		    this.name = name;
		    this.url = urlGenerator.boardURL(protocol, name);
		}
		
		public String getName(){
			return this.name;
		}
		public String getURL(){
			return this.url;
		}
	}
}
