package com.example.welink.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welink.Interfaces.ItemClickListener;
import com.example.welink.R;

public class CommentViewHolder extends RecyclerView.ViewHolder implements ItemClickListener {
    public TextView comment_user_name,comment_date,comment_text,comment_time;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        comment_user_name = itemView.findViewById(R.id.comment_user_name);
        comment_date = itemView.findViewById(R.id.comment_date);
        comment_text = itemView.findViewById(R.id.comment_text);
        comment_time = itemView.findViewById(R.id.comment_time);

    }

    @Override
    public void onClick(View view, int position, boolean isLongClick) {

    }
}
