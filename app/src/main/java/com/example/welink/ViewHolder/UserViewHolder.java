package com.example.welink.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welink.R;

public class UserViewHolder extends RecyclerView.ViewHolder {
    public ImageView userDisplayImage;
    public TextView userDisplayName;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        userDisplayImage = itemView.findViewById(R.id.userDisplayImage);
        userDisplayName = itemView.findViewById(R.id.userDisplayName);

    }
}
