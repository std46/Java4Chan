//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.fourchan;

import org.fourchan.java4chan.Board;
import org.fourchan.java4chan.JSONFetcher;
import org.fourchan.java4chan.Site;

import java.util.*;

public class Java4Chan {
	public Java4Chan() {
	}

	public Java4Chan(String proxyUrl, int proxyPort) {
		JSONFetcher.setProxy(proxyUrl, proxyPort);
	}

	public List<Board> getAllBoards() {
		return Site.getAllBoards();
	}

	public List<Board> getBoardsByName(Collection<String> names) {
		if (null == names) {
			return null;
		} else {
			List<Board> existingBoards = this.getAllBoards();
			List<Board> myList = new ArrayList(40);
			Iterator iter = existingBoards.iterator();

			while(iter.hasNext()) {
				Board b = (Board)iter.next();
				if (names.contains(b.getName())) {
					myList.add(b);
				}
			}

			return myList;
		}
	}

	public Board findBoard(String name) {
		if (null == name) {
			return null;
		} else {
			List<String> names = Arrays.asList(name);
			List<Board> myList = this.getBoardsByName(names);
			return myList.isEmpty() ? null : myList.get(0);
		}
	}
}
