package org.fourchan.java4chan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

//represents a file objects on a specific post
@Data
public class File {

	@JsonProperty("filename")
	private String attachmentName;

	@JsonProperty("ext")
	private String attachmentExtension;

	@JsonProperty("fsize")
	private Integer attachmentSize;

	@JsonProperty("w")
	private Integer width;

	@JsonProperty("h")
	private Integer height;

	@JsonProperty("tn_w")
	private Integer thumbnailWidth;

	@JsonProperty("tn_h")
	private Integer thumbnailHeight;

	@JsonProperty("md5")
	private String md5Hash;

	@JsonProperty("filedeleted")
	private boolean deleted;

	@JsonIgnore
	private Post post; //parent post

	File() {}

	public File(Post post){
		this.post = post;
	}

	public String filenameOriginal(){ //original filename 
		return attachmentName + attachmentExtension;
	}
	public String filename(){		//new filename
		return post.getTimestampMillis() + attachmentExtension;
	}
	
	public String url(){ //format: http(s)://i.4cdn.org/board/tim.ext
		return URL.fileURL("https://", this);
	}
	
	public String thumbName(){	//name of the thumbnail
		return post.getTimestampMillis() + "s.jpg";
	}
	public String thumbUrl(){ //url of the thumbnail
		return URL.thumbURL("https://", this);
		
	}
	
	public Post post(){ //note to self - possibly pointless method
		return post;
	}
}
