package com.example.homin.test1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPwd;
    private Button btnLogin;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    private int num;
    private ProgressDialog progressDialog;
    private Thread loginThread;
    private DatabaseReference reference;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.editText);
        etPwd = findViewById(R.id.editText2);
        btnLogin = findViewById(R.id.button_login);
        btnSignUp = findViewById(R.id.button_signUp);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(LoginActivity.this);

                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("로그인 중...");
                progressDialog.show();

                clickLogin();



            }
        });
    }

    private void clickLogin() {
        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(),etPwd.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인 하세요.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else{
                            DaoImple.getInstance().setLoginEmail(etEmail.getText().toString());
                            int a = etEmail.getText().toString().indexOf("@");
                            String name = etEmail.getText().toString().substring(0,a);
                            DaoImple.getInstance().setLoginId(name);
                            DaoImple.getInstance().setLoginEmail(etEmail.getText().toString());
                            progressDialog.dismiss();

                            int c = etEmail.getText().toString().indexOf("@");
                            String key1 = etEmail.getText().toString().substring(0,c);

                            int b = etEmail.getText().toString().indexOf(".");
                            String key2 = etEmail.getText().toString().substring(c + 1,b);


                            String key3 = etEmail.getText().toString().substring(b + 1,etEmail.getText().toString().length());
                            key = key1+key2+key3;
                            DaoImple.getInstance().setKey(key);
                            Log.i("vv",key);

                            reference.child("Contact").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    if(dataSnapshot.getKey().equals(key)) {
                                        Contact c = dataSnapshot.getValue(Contact.class);
                                        Log.i("vv2","dd : " + c.getUserId());
                                        DaoImple.getInstance().setLoginEmail(c.getUserId());
                                        DaoImple.getInstance().setLoginId(c.getUserName());
                                        DaoImple.getInstance().setContact(c);
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
                            finish();
                        }
                    }
                });


    }

}
