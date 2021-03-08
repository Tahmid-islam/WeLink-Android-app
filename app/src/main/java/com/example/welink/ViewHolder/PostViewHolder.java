package com.example.welink.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welink.Interfaces.ItemClickListener;
import com.example.welink.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostViewHolder extends RecyclerView.ViewHolder implements ItemClickListener {
    public CircleImageView post_profile_image;
    public TextView post_user_name,postedDate,postedTime,post_description;
    public Button commentBtn;
    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        post_profile_image = itemView.findViewById(R.id.post_profile_image);
        post_user_name = itemView.findViewById(R.id.post_user_name);
        postedDate = itemView.findViewById(R.id.postedDate);
        postedTime = itemView.findViewById(R.id.postedTime);
        post_description = itemView.findViewById(R.id.post_description);
        commentBtn = (Button) itemView.findViewById(R.id.commentBtn);
    }

    @Override
    public void onClick(View view, int position, boolean isLongClick) {

    }
}
