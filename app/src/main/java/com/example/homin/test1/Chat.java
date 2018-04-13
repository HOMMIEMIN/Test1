package com.example.homin.test1;

import java.io.Serializable;

/**
 * Created by stu on 2018-03-26.
 */

public class Chat implements Serializable{

    private String name;
    private String Chat;
    private String email;
    private int imageId;

    public Chat(){}

    public Chat(String name, String chat, String email, int imageId) {
        this.name = name;
        Chat = chat;
        this.email = email;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChat() {
        return Chat;
    }

    public void setChat(String chat) {
        Chat = chat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
