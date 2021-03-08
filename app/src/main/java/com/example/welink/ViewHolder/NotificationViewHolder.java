package com.example.welink.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welink.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
    public TextView not_user_name,not_user_time;
    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        not_user_name = itemView.findViewById(R.id.not_user_name);
        not_user_time = itemView.findViewById(R.id.not_user_time);
    }
}
