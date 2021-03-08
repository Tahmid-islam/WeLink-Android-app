package com.example.welink.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.welink.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {
    private Button postBtn;
    private FirebaseAuth mAuth;
    private String postImage, postName,currentUser_id,saveCurrentDate,saveCurrentTime,randomKey;
    private DatabaseReference userRef,postRef,notifRef;
    private EditText postDescription;
    FrameLayout mframeLayout;

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_post, container, false);

        postBtn = rootview.findViewById(R.id.postBtn);
        mframeLayout = (FrameLayout) rootview.findViewById(R.id.mainFrame);
        postDescription = rootview.findViewById(R.id.postDescription);
        mAuth = FirebaseAuth.getInstance();
        currentUser_id = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        notifRef = FirebaseDatabase.getInstance().getReference().child("Notifications");

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePost();
                saveNotif();
            }
        });

        return rootview;
    }

    private void saveNotif() {
        userRef.child(currentUser_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String snapName = dataSnapshot.child("name").getValue().toString();
                HashMap notification = new HashMap();
                notification.put("name", snapName);
                notification.put("time", saveCurrentTime);
                notifRef.child(currentUser_id + randomKey).updateChildren(notification).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            //do something
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void savePost() {
        userRef.child(currentUser_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String snapName = dataSnapshot.child("name").getValue().toString();
                    String snapImage = dataSnapshot.child("image").getValue().toString();
                    String desc = postDescription.getText().toString();
                    randomKey = saveCurrentDate + saveCurrentTime;
                    if(!TextUtils.isEmpty(desc)){
                        HashMap postMap = new HashMap();
                            postMap.put("uid",currentUser_id);
                            postMap.put("date",saveCurrentDate);
                            postMap.put("time",saveCurrentTime);
                            postMap.put("description",desc);
                            postMap.put("image",snapImage);
                            postMap.put("username",snapName);
                            postRef.child(currentUser_id + randomKey).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(PostFragment.this.getContext(), "New Post Updated", Toast.LENGTH_SHORT).show();
                                        // Create new fragment and transaction
                                        Fragment newFragment = new HomeFragment();
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.mainFrame, newFragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }else{
                                        Toast.makeText(PostFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    }else{
                        Toast.makeText(PostFragment.this.getContext(), "Post Field Empty", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
