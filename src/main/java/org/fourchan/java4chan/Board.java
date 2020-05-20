package org.fourchan.java4chan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@ToString(of={"name", "title"})
public class Board {
    @JsonIgnore
    String protocol =   "https://";

    @JsonProperty("board")
    private String name; 	//the name. ex: g, fit, etc

    @JsonProperty("title")
    private String title;

    @JsonProperty("meta_description")
    private String metaDescription;

    @JsonProperty("ws_board")
    private boolean workSafe;

    @JsonProperty("is_archived")
    private boolean archived;

    @JsonProperty("spoilers")
    private boolean spoilers;

    @JsonProperty("custom_spoilers")
    private boolean customSpoilers;

    @JsonProperty("forced_anon")
    private boolean forcedAnon;

    @JsonProperty("user_ids")
    private boolean userIds;

    @JsonProperty("country_flags")
    private boolean countryFlags;

    @JsonProperty("webm_audio")
    private boolean webmAudio;

    @JsonProperty("code_tags")
    private boolean codeTags;

    @JsonProperty("oekaki")
    private boolean oekaki;

    @JsonProperty("sjis_tags")
    private boolean sjisTags;

    @JsonProperty("text_only")
    private boolean textOnly;

    @JsonProperty("require_subject")
    private boolean requireSubject;

    @JsonProperty("troll_flags")
    private boolean trollFlags;

    @JsonProperty("math_tags")
    private boolean mathTags;

    @JsonProperty("per_page")
    private int threadsPerPage;

    @JsonProperty("pages")
    private int pageCount;

    @JsonProperty("bump_limit")
    private int bumpLimit;

    @JsonProperty("cooldowns")
    private CoolDownSettings coolDownSettings;

    @JsonProperty("image_limit")
    private int imageLimit;

    @JsonProperty("max_filesize")
    private int maxFileSize;

    @JsonProperty("min_image_width")
    private int minImageWidth;

    @JsonProperty("min_image_height")
    private int minImageHeight;

    @JsonProperty("max_webm_filesize")
    private int maxSizeWebM;

    @JsonProperty("max_webm_duration")
    private int maxWebMDurationSeconds;

    @JsonProperty("max_comment_chars")
    private int maxCommentLength;

    @JsonIgnore
    HashMap<Number, Thread> cache = new HashMap<>();	//constant cache of threads

    Board() {}

    public boolean hasThread(int id) { //checks if thread currently exists on the board
        Thread t = Thread.fetch(this.name, id);
        if (t == null){
            return false;
        }
        if (t.isClosed()){
            return false;
        }
        return true;
    }

    //returns thread object, updates cache if needed
    public Thread getThread(int id, boolean updateCached){

        Thread cachedThread = cache.get(id);
        if(cachedThread == null){ //not cached

            Thread newThread = new Thread(name, id);
            cache.put(id, newThread);
            newThread.populate();

            return cache.get(id);
        }
        if(!updateCached){ //just return the cached thread if necessary
            return cachedThread;
        }
        Thread newThread = new Thread(name, id);
        newThread.populate();
        if(newThread.is404()){ //return null on a 404ed thread
            cache.remove(id); //remove thread from cache
            return null;
        }
        //if here, the cache needs to be updated and the thread is not closed

        cache.put(id, newThread);
        return newThread;
    }


    public List<Thread> getThreads(int pageNum){  //get all threads on specific page
        if(pageNum <= 0 || pageCount < pageNum) { //invalid page returns null
            return null;
        }
        return getThreads(getThreadsUrl(pageNum));
    }

    public List<Thread> fetchAllThreads(){	//list of all thread objects in board
        List<Thread> myList = getThreads(getThreadsUrl());
        myList.forEach((t) -> {
            cache.put(t.getID(), t);//adds thread to cache
        });

        return myList;
    }

    public List<Long> getAllThreadIds(){ //get list of all thread id's
        return fetchAllThreads().stream().map((t) -> t.getID()).collect(Collectors.toList());
    }

    public void refreshCache(){
        clearCache();
        fetchAllThreads();

    }

    public void clearCache(){
        cache = new HashMap<Number, Thread>();
    }

    public String getUrl() {
        StringBuffer buf = new StringBuffer();
        buf.append("protocol");
        buf.append("/boards");
        buf.append(workSafe ? "4channel.org" : "4chan.org");
        buf.append("/");
        buf.append(name);
        return buf.toString();
    }

    public String getThreadsUrl(int page) {
        StringBuffer buf = new StringBuffer();
        buf.append("https://a.4cdn.org/");
        buf.append(name);
        buf.append("/"+page+".json");
        return buf.toString();
    }

    public String getThreadsUrl() {
        StringBuffer buf = new StringBuffer();
        buf.append("https://a.4cdn.org/");
        buf.append(name);
        buf.append("/threads.json");
        return buf.toString();
    }

    private List<Thread> getThreads(String url){
        List<Thread> myList = new LinkedList<>();
        List<Map> jsonobj = (List<Map>) JSONFetcher.vomit(url);
        for(Map page: jsonobj) {
            List<Map> pageArray = (List<Map>) page.get("threads");
            for (Map thread: pageArray) {
                Number id = (Number) thread.get("no");
                Thread newThread = new Thread(name, id);
                newThread.populate();
                myList.add(newThread);
            }
        }
        return myList;
    }

    public static class CoolDownSettings {
        @JsonProperty("threads")
        private int threadCooldownSeconds;

        @JsonProperty("replies")
        private int replyCooldownSeconds;

        @JsonProperty("images")
        private int imageCooldownSeconds;
    }

}
