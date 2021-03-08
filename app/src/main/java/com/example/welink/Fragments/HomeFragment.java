package com.example.welink.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.welink.MODEL.User;
import com.example.welink.MODEL.post;
import com.example.welink.R;
import com.example.welink.ViewHolder.PostViewHolder;
import com.example.welink.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView postrecycle;
    private DatabaseReference postRef;
    private FirebaseRecyclerOptions<post> options;
    private FirebaseRecyclerAdapter<post, PostViewHolder> adapter;
    private DatabaseReference commentRef;

    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_home, container, false);

        postrecycle = (RecyclerView) rootview.findViewById(R.id.postrecycle);
        postrecycle.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postrecycle.setLayoutManager(linearLayoutManager);

        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        postRef.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<post>().setQuery(postRef,post.class).build();

        adapter = new FirebaseRecyclerAdapter<post, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int i, @NonNull post post) {
                final String POSTKey = getRef(i).getKey();
                Picasso.with(HomeFragment.this.getContext()).load(post.getImage()).fit().centerInside().into(holder.post_profile_image);
                holder.post_user_name.setText(post.getUsername());
                holder.postedDate.setText(post.getDate());
                holder.postedTime.setText(post.getTime());
                holder.post_description.setText(post.getDescription());

                holder.commentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment newFragment = new CommentFragment();
                        Bundle args = new Bundle();
                        args.putString("POSTKey",POSTKey);
                        newFragment.setArguments(args);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.mainFrame, newFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new PostViewHolder(LayoutInflater.from(HomeFragment.this.getContext()).inflate(R.layout.all_post_layout, parent, false));
            }
        };
        postrecycle.setAdapter(adapter);
        adapter.startListening();

        return rootview;
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


}
