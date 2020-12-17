package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.firebaseapp.Model.Chat;
import com.example.firebaseapp.Model.Users;
import com.example.firebaseapp.firebaseApp.Adapter.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    TextView username;
    ImageView imageView;

    RecyclerView recyclerView;
    EditText msg_ediText;
    ImageButton sendBtn;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mchat;
    String userid;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // widget initializing

        imageView = findViewById(R.id.imageview_profile);
        username = findViewById(R.id.username);
        sendBtn = findViewById(R.id.btn_send);
        msg_ediText = findViewById(R.id.text_send);


        // RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //Toolbar
      //  Toolbar toolbar = findViewById(R.id.toolbar);
      // setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  toolbar.setNavigationOnClickListener(new View.OnClickListener() {
          //  @Override
          //  public void onClick(View v) {
           //    finish();
          //  }
       // });

   // }

  //  private void setSupportActionBar(Toolbar toolbar) {
        intent = getIntent();
      userid  = intent.getStringExtra("userid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                username.setText(users.getUsername());

                if (users.getImageURL().equals("default")){
                     imageView.setImageResource(R.mipmap.ic_launcher);

                }else{
                    Glide.with(MessageActivity.this).load(users.getImageURL()).into(imageView);
                }

              readMessages(firebaseUser.getUid(),userid, users.getImageURL());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       sendBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             String msg = msg_ediText.getText().toString();
             if (!msg.equals("")){
                 sendMessage(firebaseUser.getUid(), userid, msg);
             }  else {

             }

             msg_ediText.setText(" ");

           }
       });



    }

    private void sendMessage(String sender, String receiver, String message){
       DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

       reference.child("Chats").push().setValue(hashMap);


      // Adding User to chat fragment: Latest Chats with contact

      final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatsList").child(firebaseUser.getUid()).child(userid);

      chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              if (!snapshot.exists()){
                  chatRef.child("id").setValue(userid);
              }

          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });

    }

    private  void readMessages(final String myid, final String userid, final String imageurl){

         mchat = new ArrayList<>();
          databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
          databaseReference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  mchat.clear();
                  for (DataSnapshot snapshot1 : snapshot.getChildren()){
                     Chat chat = snapshot.getValue(Chat.class);
                     if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                      chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){

                        mchat.add(chat);
                      }

                  }

                  messageAdapter = new MessageAdapter(MessageActivity.this, mchat,imageurl);
                  recyclerView.setAdapter(messageAdapter);


              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });





    }


}