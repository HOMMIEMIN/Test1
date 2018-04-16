package com.example.homin.test1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stu on 2018-03-26.
 */

public class DaoImple {

    private static DaoImple instance = null;
    private String LoginEmail;
    private List<Chat> cList;
    private String LoginId;
    private String youEmail;
    private String key;
    private Contact contact;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static DaoImple getInstance(){
        if(instance == null){
            instance = new DaoImple();
        }
        return instance;
    }

    public String getYouEmail() {
        return youEmail;
    }

    public void setYouEmail(String youEmail) {
        this.youEmail = youEmail;
    }

    private DaoImple(){
        cList = new ArrayList<>();
    }



    public String getLoginEmail() {
        return LoginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        LoginEmail = loginEmail;
    }


    public List<Chat> getcList() {
        return cList;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    public void setcList(List<Chat> cList) {
        this.cList = cList;
    }
}
