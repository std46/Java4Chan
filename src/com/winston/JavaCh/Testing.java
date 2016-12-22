package com.winston.JavaCh;

import java.util.LinkedList;
import com.winston.JavaCh.Board.specBoard;

//used for testing instead of building this each time
public class Testing {
	public static void main(String[] args){
		LinkedList<specBoard> myList =  (LinkedList<specBoard>) Board.allBoards();
		for(int i = 0; i < myList.size(); i++){
		   System.out.println(( myList.get(i)).getName() + " " + myList.get(i).getURL());
		}
	}
}
