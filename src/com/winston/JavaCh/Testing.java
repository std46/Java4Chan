package com.winston.JavaCh;

import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;

import com.winston.JavaCh.Board.specBoard;

//used for testing instead of building this each time
public class Testing {
	public static void main(String[] args){
		
		specBoard b = new specBoard("fit");
		Thread stick = b.getThread(17018018);
		
		
		System.out.println(stick.isStickied);
		System.out.println(stick.isStickied());
		
		
	}
}
