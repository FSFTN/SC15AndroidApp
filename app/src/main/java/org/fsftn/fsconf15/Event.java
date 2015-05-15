package org.fsftn.fsconf15;

/**
 * Created by suriya on 14/5/15.
 */
public class Event {

    public String day,title,content,timestamp,iurl;

    public Event(String params[]) {
        this.day = params[0];
        this.title = params[1];
        this.content = params[2];
        this.timestamp = params[3];
        this.iurl = params[4];
    }

    public Event(String text){

        this.day = this.content = this.timestamp = "";
        this.title = "          Scroll Down\n\n\n\n";
        this.iurl = "down_arrow";

    }

    public Event(){

        this.day = this.content = this.timestamp = this.title = "";
        this.iurl = "empty_pic";
    }
}
