package com.example.p.pocketdoc;

/**
 * Created by P on 12-04-2017.
 */

public class ForumData {

    public int fid;
    public int uid;
    public String question;
    public String description;

    public ForumData(String description, int fid, String question, int uid) {
        this.description = description;
        this.fid = fid;
        this.question = question;
        this.uid = uid;
    }
}
