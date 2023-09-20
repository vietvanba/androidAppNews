package com.example.newver3.UsersActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.newver3.Adapter.CustomListViewAdapter;
import com.example.newver3.Database.Item;
import com.example.newver3.R;
import com.example.newver3.Activity.ReaderActivity;

import java.util.List;

public class LikedAndCommentedActivity extends AppCompatActivity {
    ListView listView;
    ImageView back2;
    CustomListViewAdapter customListViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_and_commented);
        listView=findViewById(R.id.likedandcommentedListView);
        back2=findViewById(R.id.back2);

        Intent intent = getIntent();
        List<Item> Listitem= (List<Item>) intent.getSerializableExtra("linkBaiBao");
        customListViewAdapter = new CustomListViewAdapter(this,R.layout.activity_listview,Listitem,"");
        listView.setAdapter(customListViewAdapter);

        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) customListViewAdapter.getItem(position);
                Intent intent = new Intent(LikedAndCommentedActivity.this, ReaderActivity.class);
                intent.putExtra("linkBaiBao", item);
                startActivity(intent);
            }

        });
    }
}