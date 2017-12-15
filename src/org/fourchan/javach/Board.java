package org.fourchan.javach;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Board {
    JSONObject metadata;	//will contain the board metadata
    private String name; 	//the name. ex: g, fit, etc
    String protocol =   "https://";
    private String url;

    HashMap<Integer, Thread> cache = new HashMap<>();	//constant cache of threads

    public Board(String name){
        this(true, name);
    }

    public Board(boolean https, String name){ //given protocol and name of board

        if(!https) { //swap protocols if necessary
            protocol = "http://";
        }
        this.name = name;
        this.url = URL.boardURL(protocol, name);

        JSONObject jsonObj = Site.AllBoardData; //get data for all boards
        JSONArray boards = (JSONArray) jsonObj.get("boards");

        for (Object o : boards) { //narrow it down to our board
            metadata = (JSONObject) o;
            if (metadata.get("board").equals(name)){
                break;
            }
        }
    }

    public boolean hasThread(int id) { //checks if thread currently exists on the board
        JSONObject value = (JSONObject)JSONFetcher.vomit(url + "/thread/" + id + ".json");
        if (value == null){
            return false;
        }
        if (value.containsKey("closed")){
            if ((int)value.get("closed") == 1){
                return false;
            }
        }
        return true;
    }

    //returns thread object, updates cache if needed
    public Thread getThread(int id, boolean updateCached){

        Thread cachedThread = cache.get(new Integer(id));
        if(cachedThread == null){ //not cached

            Thread newThread = new Thread(name, id);
            cache.put(id, newThread);

            return cache.get(id);
        }
        if(!updateCached){ //just return the cached thread if necessary
            return cachedThread;
        }
        Thread newThread = new Thread(name, id);
        if(newThread.is404()){ //return null on a 404ed thread
            cache.remove(new Integer(id)); //remove thread from cache
            return null;
        }
        //if here, the cache needs to be updated and the thread is not closed

        cache.put(new Integer(id), newThread);
        return newThread;
    }


    public List<Thread> getThreads(int page){  //get all threads on specific page
        if(page <= 0 || pageCount() < page) { //invalid page returns null
            return null;
        }
        List<Thread> myList = new LinkedList<>();
        JSONObject jsonobj = (JSONObject) JSONFetcher.vomit(url + "/" + page + ".json");

        JSONArray threads = (JSONArray) jsonobj.get("threads");
        for (Object t: threads) {
            JSONArray posts = (JSONArray) ((JSONObject) t).get("posts");
            myList.add(new Thread(name, (long)((JSONObject)posts.get(0)).get("no")));

        }
        return myList;
    }

    public List<Thread> getAllThreads(){	//list of all thread objects in board
        List<Thread> myList = new LinkedList<>();
        JSONArray jsonobj = (JSONArray) JSONFetcher.vomit(url + "/threads.json");
        for(Object o: jsonobj) {
            JSONObject page = (JSONObject) o;
            JSONArray pageArray = (JSONArray) page.get("threads");
            for (Object t: pageArray) {
                JSONObject thread = (JSONObject) t;

                Long id = (Long) thread.get("no");


                Thread newThread = new Thread(name, (long) thread.get("no"));
                myList.add(newThread);
                cache.put(new Integer((int) (long) id), newThread);//adds thread to cache



            }
        }
        return myList;

    }

    public List<Integer> getAllThreadIds(){ //get list of all thread id's
        LinkedList<Integer> threadIDs = new LinkedList<>();
        JSONArray jsonobj = (JSONArray) JSONFetcher.vomit(url + "/threads.json");
        for(Object o: jsonobj) {
            JSONObject page = (JSONObject) o;
            JSONArray pageArray = (JSONArray) page.get("threads");
            for (Object t: pageArray) {
                JSONObject thread = (JSONObject) t;
                threadIDs.add((int) (long) thread.get("no"));
            }
        }
        return threadIDs;
    }

    public String getName(){
        return this.name;
    }
    public String getURL(){
        return this.url;
    }



    public boolean isWorksafe(){ //is this nsfw
        //metadata should never be null
        return (long) metadata.get("ws_board") == 1;
    }
    public int pageCount(){ //how many pages does this board have
        return (int)(long) metadata.get("pages");
    }
    public int threadsPerPage(){  //max threads per page?
        return (int) (long) metadata.get("per_page");
    }
    public String title(){ //what is the title ex: Fitness
        return (String) metadata.get("title");
    }
    public String description(){ //ex:" /fit/ - Fitness is for exercise"

        return (String) metadata.get("meta_description");
    }

    public void refreshCache(){
        clearCache();
        getAllThreads();

    }

    public void clearCache(){
        cache = new HashMap<Integer, Thread>();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (null != this.name) {
            builder.append(this.name);
            builder.append(" ");
        }
        if (null != title()) {
            builder.append(title());
            builder.append(" ");
        }
        if (null != this.url) {
            builder.append(" (");
            builder.append(this.url);
            builder.append(")");
        }
        return builder.toString();
    }
}
