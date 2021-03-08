package com.example.welink.Fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.welink.MODEL.User;
import com.example.welink.R;
import com.example.welink.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllUserFragment extends Fragment {
    private RecyclerView recyclerview;
    private DatabaseReference userRef;
    private ArrayList<User> users;
    private FirebaseRecyclerOptions<User> options;
    private FirebaseRecyclerAdapter<User, UserViewHolder> adapter;
    public AllUserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_all_user, container, false);

        recyclerview = (RecyclerView) rootview.findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(AllUserFragment.this.getContext()));

        users = new ArrayList<User>();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<User>().setQuery(userRef,User.class).build();

        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int i, @NonNull User user) {
                holder.userDisplayName.setText(user.getName());
                Picasso.with(AllUserFragment.this.getContext()).load(user.getImage()).fit().centerInside().into(holder.userDisplayImage);
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new UserViewHolder(LayoutInflater.from(AllUserFragment.this.getContext()).inflate(R.layout.row, parent, false));
            }
        };


        recyclerview.setAdapter(adapter);
        adapter.startListening();
        return rootview;
    }

}
