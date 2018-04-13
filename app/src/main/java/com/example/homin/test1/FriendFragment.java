package com.example.homin.test1;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.net.sip.SipErrorCode.TIME_OUT;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends Fragment {


    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == TIME_OUT){
                progressDialog.dismiss();
            }
        }
    };

    private Handler mHandle2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == TIME_OUT){
                p1.dismiss();
            }
        }
    };

    class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder>{

        private int viewType2;
        private boolean nameCheck;

        @Override
        public int getItemViewType(int position) {
            int a = 0;
            if(viewTypeSelect == 1){
                a = 1;
            }

                return a;
        }

        @Override
        public FriendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FriendHolder holder = null;
            Log.i("kaka","Holder : "+viewTypeSelect+"");
            if(viewType == 0) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View itemView1 = inflater.inflate(R.layout.friend_layout2, parent, false);
                holder = new FriendHolder(itemView1);
                viewType2 = viewType;
            }else{
                LayoutInflater inflater = LayoutInflater.from(context);
                View itemView2 = inflater.inflate(R.layout.friend_layout, parent, false);
                holder = new FriendHolder(itemView2);
                viewType2 = viewType;
            }


            return holder;
        }



        @Override
        public void onBindViewHolder(FriendHolder holder, final int position) {
            Log.i("kaka","뷰타입33 : " + holder.getItemViewType());
            if(holder.getItemViewType() == 0) {

                holder.iv.setImageResource(R.drawable.p1);
                holder.tv1.setText(friendList.get(position).getUserName());
            }else{
                holder.iv.setImageResource(R.drawable.p1);
                holder.tv1.setText(friendList.get(position).getUserName());

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ChattingActivity.class);
                    intent.putExtra(POSITION_KEY,position);
//                    DaoImple.getInstance().setYouEmail(list.get(position).getEmail());
                    size = 0;
                    startActivity(intent);
                }
            });
            if(holder.btn2 != null) {
                holder.btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int a = 0 ; a < list.size() ; a++){
                            // TODO : 수정
                            if(list2.get(position).getUserName().equals(list.get(position))){
                                nameCheck = true;
                            }
                    }
                        if(nameCheck){
                            Toast.makeText(context, "이미 추가 되어 있습니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            String id = list2.get(position).getUserId();
                            Contact yourContact = list2.get(position);
                            Log.i("vv1","너아이디 : " + yourContact.getUserName());
                            String yourKey = getKey(id);
                            List<String> yourList = yourContact.getWattingList();

                            if(yourList != null) {
                                yourList.add(DaoImple.getInstance().getContact().getUserId());
                                yourContact.setWattingList(yourList);
                                reference.child("Contact").child(yourKey).setValue(yourContact);
                                Toast.makeText(context, "친구 신청을 요청하였습니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                yourList = new ArrayList<>();
                                Log.i("dd2",DaoImple.getInstance().getContact().getUserId());
                                yourList.add(DaoImple.getInstance().getContact().getUserId());
                                yourContact.setWattingList(yourList);
                                reference.child("Contact").child(yourKey).setValue(yourContact);
                                Toast.makeText(context, "친구 신청을 요청하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });
            }

        }

        @Override
        public int getItemCount() {

            return size;
        }

        class FriendHolder extends RecyclerView.ViewHolder{
            ImageView iv;
            TextView tv1;
            TextView tv2;
            Button btn2;

            public FriendHolder(View itemView) {
                super(itemView);
                iv = itemView.findViewById(R.id.imageView_frindLayout);
                tv1 = itemView.findViewById(R.id.textView_FriendLayout1);
                tv2 = itemView.findViewById(R.id.textView_FriendLayou2);
                btn2 = itemView.findViewById(R.id.btn_addFriend);
            }
        }
    }


    private Context context;
    private RecyclerView recyclerView;
    private List<String> list;
    private List<Contact> friendList;
    private List<Contact> list2;
    private DatabaseReference reference;
    public static final String POSITION_KEY = "position_key";
    public static final String FRIEND_LIST_KEY = "list1_key";
    private ProgressDialog progressDialog;
    private Button btn;
    private EditText et;
    private int viewTypeSelect;
    private String loginName;
    private String loginEmail;
    private int size;
    private ProgressDialog p1;
    private String key;
    private Contact myContact;


    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView = getView().findViewById(R.id.recyclerView);
        btn = getView().findViewById(R.id.button_search);
        et = getView().findViewById(R.id.editText_search);
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        list.clear();
        reference = FirebaseDatabase.getInstance().getReference();
        loginName = DaoImple.getInstance().getLoginId();
        loginEmail = DaoImple.getInstance().getLoginEmail();
        key = DaoImple.getInstance().getKey();
        myContact = DaoImple.getInstance().getContact();
        friendList = new ArrayList<>();



        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        final FriendAdapter adapter = new FriendAdapter();
        recyclerView.setAdapter(adapter);
        if(list.size() == 0) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("로딩중...");
            progressDialog.show();
            mHandle.sendEmptyMessageDelayed(TIME_OUT, 1000);
        }



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list2.size() != 0) {
                    list2.clear();
                }
                if(list2.size() == 0) {
                    p1 = new ProgressDialog(context);
                    p1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    p1.setMessage("로딩중...");
                    p1.show();
                }
                mHandle2.sendEmptyMessageDelayed(TIME_OUT,1000);
                if(et.getText().toString().equals("")) {
                    Toast.makeText(context, "검색어를 입력 하세요", Toast.LENGTH_SHORT).show();
                    et.setText("");
                }else{
                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
                    reference2.child("Contact").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Contact c = dataSnapshot.getValue(Contact.class);
                            Log.i("vv1","cc : " + c.getUserName());
                            if (c.getUserName().equals(et.getText().toString())) {
                                viewTypeSelect = 1;
                                list2.add(c);
                                size = list2.size();
                                adapter.notifyDataSetChanged();
                                et.setText("");
                                }



                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }
        });




        reference.child("Contact").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                viewTypeSelect = 0;
                Contact dd = dataSnapshot.getValue(Contact.class);
                friendList.add(dd);
                if(dataSnapshot.getKey().equals(DaoImple.getInstance().getKey())) {
                    Contact c = dataSnapshot.getValue(Contact.class);
                    list = c.getFriendList();
                    if (list != null) {
                        size = list.size();
                    } else {
                        list = new ArrayList<>();
                        size = list.size();
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getKey(String id){
        int b = id.indexOf("@");
        String key1 = id.substring(0,b);
        int d = id.indexOf(".");
        String key2 = id.substring(b + 1,d);
        String key3 = id.substring(d + 1,id.length());
        String key = key1+key2+key3;

        return key;
    }
}
