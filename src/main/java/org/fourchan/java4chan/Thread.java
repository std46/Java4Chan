package org.fourchan.java4chan;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fourchan.java4chan.exception.ThreadNotFoundException;

public class Thread { //represents a 4chan thread

	private LinkedList<Post> postCache = new LinkedList<>();	//cache of Post objects
	
	private String board = ""; //name of the board. ex: a, g, tg
	private long id; //the thread id
	private boolean is404 = false; 		//has the thread been closed?
	
	public Thread(String board, Number id){
	    this.id = id.longValue();
	    this.board = board;
	}
	
	public long getID(){
	    return id;
	}
	public String getBoard(){
	    return board;
	}
	
	public List<Post> getPosts(boolean refresh) {
		if (refresh) {
			refresh();
		} else if (postCache.isEmpty()) {
			populate();
		}
		return postCache;
	}
	
	//populate all the metadata of the thread (should be used only once for efficiency)
	public void populate(){
		List<Post> posts = fetchPosts();
		posts.forEach(this::addPost);
	}
	
	 //add all new posts needed, returns number of new posts, updates metadata
	public int refresh(){
		List<Post> posts = fetchPosts();
		if  (posts.size() == postCache.size()) { //no changes
			return 0;
		}

		AtomicInteger newPosts = new AtomicInteger();
		posts.forEach((p) -> {
			if (!this.contains(p)) {
				addPost(p);
				newPosts.getAndIncrement();
			}
		});

		return newPosts.get();
	}

	private List<Post> fetchPosts(){
		List<Post> retVal = new LinkedList<>();
		Map metadata = null;
		try {
			metadata = (Map) JSONFetcher.vomit("https://a.4cdn.org/" + board + "/thread/" + id + ".json");
		} catch (ThreadNotFoundException ex) {
			this.is404 = true;
			return retVal;
		}

		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, Object>> posts = mapper.convertValue(metadata.get("posts"), List.class);
		posts.forEach((b) -> {
			Post post = mapper.convertValue(b, Post.class);
			retVal.add(post);
		});
		return retVal;
	}

	public static Thread fetch(String board, Number id){
		Thread t = new Thread(board, id);
		t.populate();
		return t;
	}

	public Post getOriginalPost() {
		if (postCache.size() == 0)
			return null;
		return postCache.get(0);
	}

	public void addPost(Post p) {
		p.setThread(this);
		postCache.add(p);
	}

	public boolean contains(Post post) {
		Post foundPost = postCache.stream().filter((p) -> p.getId().equals(post.getId())).findFirst().orElse(null);
		return foundPost != null;
	}

	public boolean isClosed(){
		init();
		return getOriginalPost().isClosed();
	}

	public boolean isStickied(){
	    init();
	    return getOriginalPost().isSticky();
	}
	public long getId(){
		return getOriginalPost().getId().longValue();
	}

	public boolean is404(){
		init();
	    return is404;
	}
	public boolean isArchived(){
		init();
		return getOriginalPost().isArchived();
	}
	public boolean bumpLimit(){
		init();
		return getOriginalPost().isBumplimitArchieved();
	}
	public boolean imageLimit(){
	    init();
		return getOriginalPost().isImagelimitArchieved();
	}
	
	private void init() {
		if(postCache.isEmpty()){
			populate();
		}
	}

	
	//File related accessors
	public List<String> thumbUrls(){
		List<String> urls = new LinkedList<>();
		for(Post p: postCache){
			if(p.hasFile()){
				urls.add(p.getFile().thumbUrl());
			}
		}
		return urls;
	}
	
	public List<String> fileUrls(){
		List<String> urls = new LinkedList<>();
		for(Post p: postCache){
			if(p.hasFile()){
				urls.add(p.getFile().url());
			}
		}
		return urls;
	}
	
	public List<File> fileList(){
	    List<File> fileList = new LinkedList<>();
	    for(Post p : postCache){
	    	if(p.hasFile()){
	    		fileList.add(p.getFile());
	    	}
	    }
	    return fileList;
	}
	
	public List<String> filenames(){
		List<String> filenames = new LinkedList<>();
		for(Post p: postCache){
		    if(p.hasFile()){
		    	filenames.add(p.getFile().filename());
		    }
			
		}
		
		return filenames;
	}
	
	public List<String> thumbnames(){
		List<String> thumbnames = new LinkedList<>();
		for(Post p: postCache){
			if(p.hasFile()){
				thumbnames.add(p.getFile().thumbName());
			}
		}
		return thumbnames;
	}
	
	//URL accessors
	public String url(){
		return URL.threadURL("https://", this);
	}
	public String semanticUrl(){
		return url() + "/" + semanticSlug();
	}
	public String semanticSlug(){
		return getOriginalPost().getSemanticUrl();
	}

	@Override
	public String toString() {
		if (null == getOriginalPost())
			return null;
		return getOriginalPost().toString();
	}
}
