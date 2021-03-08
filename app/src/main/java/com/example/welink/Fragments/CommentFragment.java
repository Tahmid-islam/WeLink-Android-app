package com.example.welink.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.v4.app.INotificationSideChannel;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.welink.MODEL.comment;
import com.example.welink.MODEL.post;
import com.example.welink.R;
import com.example.welink.ViewHolder.CommentViewHolder;
import com.example.welink.ViewHolder.PostViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.zip.CheckedOutputStream;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.COMPANION_DEVICE_SERVICE;
import static android.content.Context.CONTEXT_IGNORE_SECURITY;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {
    private ImageButton post_comment_button;
    private RecyclerView commentList;
    private EditText commentInput;
    private String saveCurrentDate,saveCurrentTime,RandomKey,downloadUrl;
    private DatabaseReference userRef,postRef;
    private FirebaseAuth mAuth;
    private String cuurentUser,POSTKey;
    private FirebaseRecyclerOptions<comment> options;
    private FirebaseRecyclerAdapter<comment, CommentViewHolder> adapter;
    private DatabaseReference commentRef;
    private ImageButton selectFile;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private Uri fileUri;
    private Button uploadCv;

    public CommentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_comment, container, false);
        post_comment_button = rootview.findViewById(R.id.post_comment_button);
        commentList = rootview.findViewById(R.id.commentList);
        commentInput = rootview.findViewById(R.id.commentInput);
        selectFile = rootview.findViewById(R.id.selectFile);
        uploadCv = rootview.findViewById(R.id.uploadCv);


        storageReference = FirebaseStorage.getInstance().getReference().child("CVs");

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        commentList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        commentList.setLayoutManager(linearLayoutManager);
        mAuth = FirebaseAuth.getInstance();
        cuurentUser = mAuth.getCurrentUser().getUid();

        POSTKey = getArguments().getString("POSTKey");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(POSTKey).child("comments");
        mAuth = FirebaseAuth.getInstance();

        commentRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(POSTKey).child("comments");
        commentRef.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<comment>().setQuery(commentRef,comment.class).build();
        adapter = new FirebaseRecyclerAdapter<comment, CommentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder holder, int i, @NonNull comment comment) {
                holder.comment_user_name.setText(comment.getUsername());
                holder.comment_text.setText(comment.getComment());
                holder.comment_time.setText(comment.getTime());
                holder.comment_date.setText(comment.getDate());
            }

            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new CommentViewHolder(LayoutInflater.from(CommentFragment.this.getContext()).inflate(R.layout.all_comments_layout, parent, false));
            }
        };

        commentList.setAdapter(adapter);
        adapter.startListening();

        post_comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               validateComment();
            }
        });

        uploadCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CommentFragment.this.getContext(), "works", Toast.LENGTH_SHORT).show();
            }
        });

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return rootview;
    }

    private void validateComment() {
        userRef.child(cuurentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String username = dataSnapshot.child("name").getValue().toString();
                    Toast.makeText(CommentFragment.this.getContext(), username, Toast.LENGTH_SHORT).show();
                    String text = commentInput.getText().toString();
                    if(!TextUtils.isEmpty(text)){
                        final String randomKey = cuurentUser + saveCurrentDate + saveCurrentTime;
                        HashMap comment = new HashMap();
                        comment.put("uid", cuurentUser);
                        comment.put("comment", text);
                        comment.put("date",saveCurrentDate);
                        comment.put("time",saveCurrentTime);
                        comment.put("username", username);
                        postRef.child(randomKey).updateChildren(comment).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(CommentFragment.this.getContext(),"Comment Added!" , Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(CommentFragment.this.getContext(),"Something Went Wrong!" , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{
                        Toast.makeText(CommentFragment.this.getContext(), "Write Something to post", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
