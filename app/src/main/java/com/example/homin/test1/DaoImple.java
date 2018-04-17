package com.example.homin.test1;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.InputStream;
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
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private InputStream stream;
    private List<InputStream> pictureList;
    private List<String> pictrueUrlList;
    private List<String> myFriendList;


    public List<String> getMyFriendList() {
        return myFriendList;
    }

    public void setMyFriendList(List<String> myFriendList) {
        this.myFriendList = myFriendList;
    }

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



    // 이메일에서 특수문자 뺀 key값 구하기
    public String getFirebaseKey(String id){
        int b = id.indexOf("@");
        String key1 = id.substring(0,b);
        int d = id.indexOf(".");
        String key2 = id.substring(b + 1,d);
        String key3 = id.substring(d + 1,id.length());
        String key = key1+key2+key3;

        return key;
    }


}
