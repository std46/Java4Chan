package org.fourchan;

import org.fourchan.javach.Board;
import org.fourchan.javach.JSONFetcher;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static org.fourchan.javach.Site.allBoardsJSON;

public class Java4Chan {

	public Java4Chan() {}
	
	public Java4Chan(String proxyUrl, int proxyPort) {
		JSONFetcher.setProxy(proxyUrl, proxyPort);
	}

	//returns List of all boards
	public List<Board> getAllBoards() {
		LinkedList<Board> boardsList = new LinkedList<>();
		JSONObject allBoards = allBoardsJSON();
		if (null == allBoards) {
			return null;
		}
		JSONArray boards = (JSONArray) allBoards.get("boards");
		for (Object o : boards) {
			JSONObject board = (JSONObject) o;
			Board customBoard = new Board((String) board.get("board"));
			boardsList.add(customBoard);
		}

		return boardsList;
	}
}