package com.example.firebaseapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firebaseapp.Model.ChatList;
import com.example.firebaseapp.Model.Users;
import com.example.firebaseapp.R;
import com.example.firebaseapp.firebaseApp.Adapter.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
 // This class is for displaying Recent Chats with users

   private UserAdapter userAdapter;
   private List<Users> mUsers;

   FirebaseUser firebaseUser;
   DatabaseReference databaseReference;

   private List<ChatList> usersList;

   RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usersList.clear();

                // Loop for all users:
                for (DataSnapshot snapshot1 : snapshot.getChildren()){

                    ChatList chatList = snapshot.getValue(ChatList.class);

                    usersList.add(chatList);


                }

                chatList();
                // Getting all recent chats;
                mUsers = new ArrayList<>();
                databaseReference = FirebaseDatabase.getInstance().getReference("MyUsers");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mUsers.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){

                            Users users = snapshot.getValue(Users.class);
                            for (ChatList chatList : usersList){
                                if (users.getId().equals(chatList.getId())){
                                    mUsers.add(users);
                                }
                            }
                        }

                        userAdapter = new UserAdapter();
                        recyclerView.setAdapter(userAdapter);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });








            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void chatList() {







    }


}