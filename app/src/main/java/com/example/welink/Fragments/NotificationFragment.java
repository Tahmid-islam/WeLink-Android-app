package com.example.welink.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.welink.MODEL.Notification;
import com.example.welink.R;
import com.example.welink.ViewHolder.CommentViewHolder;
import com.example.welink.ViewHolder.NotificationViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    private RecyclerView notifRecycle;
    private DatabaseReference notifRef;
    private FirebaseRecyclerOptions<Notification> options;
    private FirebaseRecyclerAdapter<Notification, NotificationViewHolder> adapter;
    public NotificationFragment(){
        
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_notification, container, false);
        notifRecycle = rootview.findViewById(R.id.notifRecycle);
        notifRecycle.setHasFixedSize(true);
        notifRecycle.setLayoutManager(new LinearLayoutManager(NotificationFragment.this.getContext()));
        notifRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
        notifRef.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<Notification>().setQuery(notifRef,Notification.class).build();

        adapter = new FirebaseRecyclerAdapter<Notification, NotificationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NotificationViewHolder holder, int i, @NonNull Notification notification) {
                holder.not_user_name.setText(notification.getName());
                holder.not_user_time.setText(notification.getTime());
            }

            @NonNull
            @Override
            public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new NotificationViewHolder(LayoutInflater.from(NotificationFragment.this.getContext()).inflate(R.layout.all_notification_layout, parent, false));
            }
        };
        notifRecycle.setAdapter(adapter);
        adapter.startListening();

        return rootview;
    }
}
