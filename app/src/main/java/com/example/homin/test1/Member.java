package com.example.homin.test1;

import java.io.Serializable;

/**
 * Created by stu on 2018-03-26.
 */

public class Member implements Serializable{

    private String name;
    private String Email;
    private int imageId;
    private String chatName;


    public Member(){}

    public Member(String name, String email, int imageId, String chatName) {
        this.name = name;
        Email = email;
        this.imageId = imageId;
        this.chatName = chatName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }
}
