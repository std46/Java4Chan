package com.winston.JavaCh;

import java.util.LinkedList;
import java.util.List;

import com.winston.JavaCh.Board.specBoard;

//used for testing instead of building this each time
public class Testing {
	public static void main(String[] args){
		specBoard my = new specBoard("g");
		
		List myList = Board.getAllThreadIds("g");
		for(int i = 0; i < myList.size(); i++){
		   System.out.println( myList.get(i));
		}
	}
}
