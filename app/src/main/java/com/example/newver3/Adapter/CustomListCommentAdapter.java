package com.example.newver3.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.newver3.Firebase.Comment;
import com.example.newver3.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomListCommentAdapter extends BaseAdapter {

    Context context;
    List<Comment> listComment;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");

    int layoutID;

    public CustomListCommentAdapter(Context context, int layoutID, List<Comment> listComment) {
        this.context = context;
        this.listComment = listComment;

    }

    @Override
    public int getCount() {
        return listComment.size();
    }

    @Override
    public Object getItem(int position) {
        return listComment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    private class ViewHolder {
        ImageView imgHinh;
        TextView name, comment;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_comment, parent, false);
        Comment comment = (Comment) getItem(position);
        ViewHolder holder;
        holder = new ViewHolder();
        holder.imgHinh = convertView.findViewById(R.id.imageProfileComment);
        holder.name = convertView.findViewById(R.id.textName);
        holder.comment = convertView.findViewById(R.id.textContent);

        holder.comment.setText(comment.comment);
        holder.imgHinh.setImageResource(R.drawable.avatar);
        holder.name.setText(comment.name);

        Query allUser = myRef.child(comment.username).child("avatar");

        View finalConvertView = convertView;
        allUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.with(finalConvertView.getContext()).load(Uri.parse(snapshot.getValue(String.class))).into(holder.imgHinh);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        convertView = finalConvertView;
        convertView.setTag(holder);

        return convertView;
    }
}
