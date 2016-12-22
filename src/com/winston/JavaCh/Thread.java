package com.winston.JavaCh;

public class Thread {
	//represents a 4chan thread
	String board = "";
	long id;
	boolean is404 = false;
	boolean isStickied = false;
    
	
	boolean updateNeeded = false; //always starts out not needing an update
	
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
	public void updateNeeded() {
	    updateNeeded = true;
	}
}
