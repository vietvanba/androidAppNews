package com.example.newver3.CommentAndLike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.newver3.Adapter.CustomListCommentAdapter;
import com.example.newver3.Firebase.Comment;
import com.example.newver3.Database.Item;
import com.example.newver3.MainActivity;
import com.example.newver3.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    TextInputEditText ed_cmt;
    Button btn_cmt;
    TextView textNull;
    ImageButton close;
    ListView listView;
    List<Comment> listComment = new ArrayList<>();
    CustomListCommentAdapter customListCommentAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("news");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ed_cmt=findViewById(R.id.text_cmt);
        btn_cmt=findViewById(R.id.btn_cmt);
        textNull=findViewById(R.id.text_null_cmt);
        listView=findViewById(R.id.commentListView);
        close = findViewById(R.id.ic_close);
        Intent intent = getIntent();
        Item itemBaiBao= (Item) intent.getSerializableExtra("BaiBao");
        close.setOnClickListener(v -> finish());
        myRef.child("comment").child(itemBaiBao.getUrlNotRegex()).child("item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()==null)
                {
                    myRef.child("comment").child(itemBaiBao.getUrlNotRegex()).child("item").setValue(itemBaiBao);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child("comment").child(itemBaiBao.getUrlNotRegex()).child("listComment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot item: snapshot.getChildren()) {

                    listComment.add(item.getValue(Comment.class));

                }
                if(listComment.size()!=0)
                {
                    textNull.setText("");
                }
                customListCommentAdapter= new CustomListCommentAdapter(getApplicationContext(),R.layout.activity_list_comment,listComment);
                listView.setAdapter(customListCommentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btn_cmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ed_cmt.getText().toString()!=null)
                {
                    SimpleDateFormat dateVN =new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
                    String timeNow=dateVN.format(new Date().getTime());
                    Comment comment= new Comment(MainActivity.user.username,ed_cmt.getText().toString(),MainActivity.user.name,timeNow);
                    myRef.child("comment").child(itemBaiBao.getUrlNotRegex()).child("listComment").child(myRef.push().getKey()).setValue(comment);
                }
                ed_cmt.setText("");
            }
        });

    }
}