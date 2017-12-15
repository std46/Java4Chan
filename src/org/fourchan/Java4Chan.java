package org.fourchan;

import org.fourchan.javach.JSONFetcher;

public class Java4Chan {

	public Java4Chan() {}
	
	public Java4Chan(String proxyUrl, int proxyPort) {
		JSONFetcher.setProxy(proxyUrl, proxyPort);
	}
}