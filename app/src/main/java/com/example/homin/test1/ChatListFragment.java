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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
public class ChatListFragment extends Fragment {

    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == TIME_OUT){
                progressDialog.dismiss();
            }
        }
    };

    class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder>{


        @Override
        public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.chatlist_layout,parent,false);
            ListHolder holder = new ListHolder(itemView);

            return holder;
        }

        @Override
        public void onBindViewHolder(ListHolder holder, final int position) {
//            holder.iv.setImageResource(list.get(position).getImageId());
            final boolean check = true;
            holder.tv.setText(list.get(position) + " 님과의 대화창");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ChattingActivity.class);
                    intent.putExtra(FriendFragment.POSITION_KEY,list.get(position));
                    intent.putExtra("check",check);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ListHolder extends RecyclerView.ViewHolder{
            ImageView iv;
            TextView tv;

            public ListHolder(View itemView) {
                super(itemView);
                iv = itemView.findViewById(R.id.imageView_listLayout);
                tv = itemView.findViewById(R.id.textView_listLayout);
            }
        }
    }

    private RecyclerView recyclerView;
    private Context context;
    private List<String> list;
    private List<Chat> list2;
    private DatabaseReference reference;
    private String name;
    private String chatName;
    private ProgressDialog progressDialog;


    public ChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        reference = FirebaseDatabase.getInstance().getReference();
        recyclerView = getView().findViewById(R.id.recyclerView_ChatList);
        list2 = new ArrayList<>();
        list = new ArrayList<>();
        name = DaoImple.getInstance().getLoginId();
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        final ListAdapter adapter = new ListAdapter();
        recyclerView.setAdapter(adapter);


        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("로딩중...");
        progressDialog.show();
        mhandler.sendEmptyMessageDelayed(TIME_OUT,1000);

        reference.child("message").child(name+"Chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    Chat chat = data.getValue(Chat.class);
                   list2.add(chat);
                }
                String a = dataSnapshot.getKey();
                list.add(a);
                adapter.notifyDataSetChanged();
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
