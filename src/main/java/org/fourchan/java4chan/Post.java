package org.fourchan.java4chan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

//represents a 4chan post

@Data
public class Post {
	@JsonProperty("no")
	private Number id;

	@JsonProperty("sticky")
	private boolean sticky;

	@JsonProperty("closed")
	private boolean closed;

	@JsonProperty("archived")
	private boolean archived;

	@JsonProperty("imagelimit")
	private boolean imagelimitArchieved;

	@JsonProperty("bumplimit")
	private boolean bumplimitArchieved;

	@JsonProperty("archived_on")
	private String timeArchived;

	@JsonProperty("now")
	private String time;

	@JsonProperty("name")
	private String posterName;

	@JsonProperty("sub")
	private String subject;

	@JsonProperty("com")
	private String body;

	@JsonProperty("tim")
	private Long timestampMillis;

	@JsonProperty("time")
	private Integer timestampSeconds;

	@JsonProperty("resto")
	private Integer resto;

	@JsonProperty("id")
	private String posterId;

	@JsonProperty("trip")
	private String tripCode;

	@JsonProperty("capcode")
	private String capCode;

	@JsonProperty("semantic_url")
	private String semanticUrl;

	@JsonProperty("replies")
	private Integer numReplies;

	@JsonProperty("images")
	private Integer numImages;

	@JsonProperty("unique_ips")
	private Integer uniqueIps;

	@JsonUnwrapped
	File file;

	@JsonIgnore
	private Thread thread; //reference to it's thread

	Post() {}

	public Post(Thread thread){
		this.thread = thread;
	}
	
	//File related methods
	public boolean hasFile(){
		return ((null != file)) && (null != file.getAttachmentName()) && (!file.getAttachmentName().isEmpty());
	}
	
	//URL related
	public String url(){
		return thread.url() + "#p" + id;
	}
	public String semanticUrl(){
		return thread.semanticUrl() + "#p" + id;
	}
	
	//misc accessors
	
	public Thread containingThread(){
		return thread;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(id);
		if ((null != subject) && (!subject.isEmpty())) {
			buf.append(" - ");
			String subj = subject.replaceAll("&#\\d+;", "");
			subj = subj.replaceAll("&\\w+;", "");
			buf.append(subj);
		}
		if ((null != body) && (!body.isEmpty())) {
			buf.append(" - ");
			String bd = body;
			bd = bd.replaceAll("<br>", "");
			bd = bd.replaceAll("&#\\d+;", "");
			bd = bd.replaceAll("&\\w+;", "");
			bd = bd.replaceAll("<span class=\"quote\">", "");
			bd = bd.replaceAll("</span>", "");
			int len = Math.min(bd.length(), 50);
			buf.append(bd.substring(0, len));
		}
		return buf.toString();
	}
}
