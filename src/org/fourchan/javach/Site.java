package org.fourchan.javach;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Site {
	private static String boardsUrlStr = "https://a.4cdn.org/boards.json";
	
	//retrieved data for all boards
	static JSONObject AllBoardData = null;
	
	//returns List of Site objects given list of board names
	public static Object getBoards(Collection<String> list){
		if (null == list)
			return null;
		List<Board> myList = new LinkedList<>();
		for (String s : list){
		    Board board = new Board(s);
		    myList.add(board);
		}
		return myList;
	}

	//returns the 4chan api's JSON representation of all boards
	public static JSONObject allBoardsJSON(){
		if(AllBoardData == null) {
			AllBoardData = (JSONObject) JSONFetcher.vomit(boardsUrlStr);
		}
	    return AllBoardData;
	}



	//Returns list of all thread IDs given board name
	public static List<Integer> getAllThreadIds(String id){
	    
		LinkedList<Integer> threadIDs = new LinkedList<>();
		Board board = new Board(id);
        JSONArray jsonobj = (JSONArray) JSONFetcher.vomit(board.getURL() + "/threads.json");
        for(Object o: jsonobj) {
            JSONObject page = (JSONObject) o;
            JSONArray pageArray = (JSONArray) page.get("threads");
            for (Object t: pageArray) {
            	JSONObject thread = (JSONObject) t;
                threadIDs.add((int)(long) thread.get("no"));
            }
        }
        return threadIDs;
		
	}

}
