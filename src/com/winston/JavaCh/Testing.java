package com.winston.JavaCh;

import java.util.LinkedList;
import java.util.List;

import com.winston.JavaCh.Board.specBoard;

//used for testing instead of building this each time
public class Testing {
	public static void main(String[] args){
		System.out.println(Board.cache());
		specBoard b = new specBoard("fit");
		List myList = b.getAllThreads();
		for(int i = 0; i < myList.size(); i++){
		   Thread t = (Thread) myList.get(i);
		   System.out.println(t.getID() + " " + t.getBoard());
		}
		System.out.println(Board.cache());
	}
}
