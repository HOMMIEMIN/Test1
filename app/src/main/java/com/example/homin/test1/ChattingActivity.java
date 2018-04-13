package com.example.homin.test1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChattingActivity extends AppCompatActivity {

    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder>{


        @Override
        public int getItemViewType(int position) {
            if(cList.get(position).getName().equals(myId)) {
                return 0;
            }else{
                return 1;
            }
        }

        @Override
        public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ChatHolder holder = null;


            switch (viewType) {
                case 0:
                    LayoutInflater inflater = LayoutInflater.from(ChattingActivity.this);
                    View itemView = inflater.inflate(R.layout.chat_layout, parent, false);
                    holder = new ChatHolder(itemView);
                break;
                case 1:
                    LayoutInflater inflater2 = LayoutInflater.from(ChattingActivity.this);
                    View itemView2 = inflater2.inflate(R.layout.chat_layout2, parent, false);
                    holder = new ChatHolder(itemView2);
                break;
            }


            return holder;
        }

        @Override
        public void onBindViewHolder(ChatAdapter.ChatHolder holder, int position) {

                holder.iv.setImageResource(cList.get(position).getImageId());
                holder.tv1.setText(cList.get(position).getName());
                holder.tv2.setText(cList.get(position).getChat());
                String id = holder.tv1.getText().toString();
            if(holder.getItemViewType() == 0) {
                holder.tv2.setBackground(getDrawable(R.drawable.ddd));
            }else{
                holder.tv2.setBackground(getDrawable(R.drawable.ddd1));
            }


        }

        @Override
        public int getItemCount() {
            return cList.size();
        }

        class ChatHolder extends RecyclerView.ViewHolder{
            ImageView iv;
            TextView tv1;
            TextView tv2;

            public ChatHolder(View itemView) {
                super(itemView);
                iv = itemView.findViewById(R.id.imageView_chatLaout);
                tv1 = itemView.findViewById(R.id.textView_chatLayout1);
                tv2 = itemView.findViewById(R.id.textView_chatLaout2);
            }
        }
    }

    private RecyclerView recyclerView;
    private EditText et;
    private Button btn;
    private List<Chat> cList;
    private List<Member> fList;
    private DatabaseReference reference;
    private int position1;
    private ChatAdapter adapter;
    private String myId;
    private String youId;
    private boolean check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        et = findViewById(R.id.editText_sends);
        btn = findViewById(R.id.button_sends);
        recyclerView = findViewById(R.id.recyclerView_chatting);

        if (cList != null) {
            Log.i("kaka", "cList: " + cList);
        } else {
            Log.i("kaka", "cList is null");
        }

        cList = new ArrayList<>();
//      cList = DaoImple.getInstance().getcList();
        fList = DaoImple.getInstance().getfList();
        reference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        String chatListName = intent.getStringExtra(FriendFragment.POSITION_KEY);
        check = intent.getBooleanExtra("check",false);

        recyclerView.hasFixedSize();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter();
        recyclerView.setAdapter(adapter);


        myId = DaoImple.getInstance().getLoginId();



        if(check){
            youId = chatListName;
            check = false;
        }else {
            int a = DaoImple.getInstance().getYouEmail().indexOf("@");
            youId = DaoImple.getInstance().getYouEmail().substring(0, a);
        }

        Log.i("kaka",youId);



        cList.clear();

        reference.child("message").child(myId+"Chat").child(youId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                cList.add(chat);
                adapter.notifyDataSetChanged();

               int a = cList.size();
               recyclerView.scrollToPosition(cList.size()-1);

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


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chat c = new Chat(myId,et.getText().toString(),DaoImple.getInstance().getLoginEmail(),R.drawable.p1);
                et.setText("");
                reference.child("message").child(youId+"Chat").child(myId).push().setValue(c);
                reference.child("message").child(myId+"Chat").child(youId).push().setValue(c);

            }
        });


    }

}
