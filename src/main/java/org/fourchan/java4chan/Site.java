package org.fourchan.java4chan;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;


public class Site {
	private static String boardsUrlStr = "https://a.4cdn.org/boards.json";
	
	//retrieved data for all boards
	static List<Board> boards = null;
	
	//returns List of Site objects given list of board names
	public static Object getBoards(Collection<String> list){
		if (null == list)
			return null;
		return getAllBoards().stream().filter((b) -> list.contains(b.getName())).collect(Collectors.toList());
	}

	//returns the 4chan api's JSON representation of all boards
	public static List<Board> getAllBoards(){
		if (boards == null) {
			boards = new ArrayList<>();
			Map boardMap = (Map) JSONFetcher.vomit(boardsUrlStr);
			ObjectMapper mapper = new ObjectMapper();
			List<Map<String, Object>> boards = mapper.convertValue(boardMap.get("boards"), List.class);
			boards.forEach((b) -> {
				Board board = mapper.convertValue(b, Board.class);
				Site.boards.add(board);
			});
		}
	    return boards;
	}

	public static Board getBoard(String id) {
		return getAllBoards().stream().filter((b) -> b.getName().equals(id)).findFirst().orElse(null);
	}

	//Returns list of all thread IDs given board name
	public static List<Long> getAllThreadIds(String id){
		Board board = getBoard(id);
        return board.getAllThreadIds();
	}
}
