package javach;

//use this to build urls
public class URL {
	
	
	static String api = "a.4cdn.org/";
	static String html = "boards.4chan.org/"; //html subdomain
	static String image = "i.4cdn.org/"; //images
	static String thumb = "t.4cdn.org/"; //thumbnails
	static String stat = "s.4cdn.org/"; //static host (spoilers, flags, capcode icons, etc
	
	public String catalogURL(String board){ //returns url for catalog fetch
		return null;
	}
	
	public static String boardURL(String protocol, String name) {
		String url = protocol;
		url += api;
		url += name;
		
		return url;
	}
	
	public static String fileURL(String protocol, File file) {
		String url = protocol + image + file.post().thread.getBoard();
		url += "/";
		url += file.filename();
		return url;
	}
	
	public static String thumbURL(String protocol, File file) {
		String url = protocol + thumb + file.post().thread.getBoard();
		url += "/";
		url += file.thumbName();
		return url;
	}
	
	public static String threadURL(String protocol, Thread thread){
		String url = protocol + html + thread.getBoard() + "/thread/";
		url += thread.getID();
		return url;
	}
}
