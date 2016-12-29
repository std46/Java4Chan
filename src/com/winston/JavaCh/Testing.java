package com.winston.JavaCh;

import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.winston.JavaCh.Board.specBoard;

//used for testing instead of building this each time
public class Testing {
	public static void main(String[] args){
		
		specBoard b = new specBoard("fit");
		
		Thread heh = b.getThread(39790358, true);
		
		List<File> postname = heh.fileList();
		System.out.println(postname.size());
		for(File f: postname){
			System.out.println(f.url());
		}
		
	}
}
