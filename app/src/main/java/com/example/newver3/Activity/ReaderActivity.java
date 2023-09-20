package com.example.newver3.Activity;


import android.app.ProgressDialog;
import android.content.Intent;


import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import androidx.appcompat.app.AppCompatActivity;


import com.example.newver3.CommentAndLike.CommentActivity;
import com.example.newver3.Database.AppDatabase;
import com.example.newver3.Database.BaiBao;
import com.example.newver3.Database.Item;
import com.example.newver3.MainActivity;
import com.example.newver3.R;
import com.example.newver3.UsersActivity.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webviewtopdf.PdfView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ReaderActivity extends AppCompatActivity {


    TextView tvComment, tvLike, tvDownload;
    ImageView imComment, imLike, imDownload, imNguon, imBack, imShare;
    LinearLayout layoutComment, layoutLike, layoutDownload;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("news/like");
    WebView webView;
    AppDatabase db;

    boolean like = false;
    boolean enable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }
        setEvent();
        setControl();
    }

    private void setControl() {
        //Lấy Item Bài báo đã được chuyển từ ListView qua
        Intent intent = getIntent();
        Item item = (Item) intent.getSerializableExtra("linkBaiBao");

        //Khởi tạo database ROOM
        db = AppDatabase.getInMemoryDatabase(getApplicationContext());

        //Animation rotate của Nút Share
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);

        //Sự kiện bấm nút share
        imShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imShare.startAnimation(animation);//Chạy animation cho nút Share
                shareIt(item);
            }
        });
        //sự kiện Back quay lại
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        //Check User đã like bài báo này trong Firebase chưa
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (MainActivity.user != null) {

                    for (DataSnapshot i : snapshot.child(MainActivity.user.username).getChildren()) {
                        if (i.getKey().equals(item.getUrlNotRegex())) {
                            like = true;
                            setLike(like);
                            break;
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Check xem Đã Download chưa
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/" + item.getUrlNotRegex() + ".pdf").getPath();
        if (new File(path).exists()) {
            enable = true;
            tvDownload.setText(R.string.downloaddone);
            tvDownload.setTextColor(getResources().getColor(R.color.colorPrimary));
            imDownload.setImageResource(R.drawable.ic_download_done);
            imDownload.setColorFilter(getResources().getColor(R.color.colorPrimary));
        }

        //Sự kiện buttonlike
        layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.user == null) {
                    Intent intent = new Intent(ReaderActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (like) {
                        like = !like;
                        myRef.child(MainActivity.user.username).child(item.getUrlNotRegex()).removeValue();
                        setLike(like);
                    } else {
                        like = !like;
                        myRef.child(MainActivity.user.username).child(item.getUrlNotRegex()).setValue(item);
                        setLike(like);
                    }
                }


            }
        });


        //Sự kiện buttonComment
        layoutComment.setOnClickListener(v -> {


            if (MainActivity.user == null) {
                //User == null => Login
                Intent intentLogin = new Intent(ReaderActivity.this, LoginActivity.class);
                startActivity(intentLogin);
            } else {
                //User != null mở giao diện comment
                Intent intentComment = new Intent(ReaderActivity.this, CommentActivity.class);
                intentComment.putExtra("BaiBao", item);
                startActivity(intentComment);
            }

        });

        layoutDownload.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (!enable) {

                    //Ghi file HTML vào Bộ nhớ Điện Thoại
//                        FileOutputStream out = openFileOutput(item.getUrlNotRegex() + ".html", MODE_PRIVATE);
//                        Document data = Jsoup.connect(item.getUrl()).get();
//                        out.write(data.html().getBytes());
//                        out.close();
//                        //Ghi Bai bao vào Database

//
//                        Toast.makeText(ReaderActivity.this, "Đã tải xuống", Toast.LENGTH_SHORT).show();
//                        ;
                    try {
                        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/");
                        final String fileName=item.getUrlNotRegex()+".pdf";

                        final ProgressDialog progressDialog=new ProgressDialog(ReaderActivity.this);
                        progressDialog.setMessage("Please wait");
                        progressDialog.show();
                        PdfView.createWebPrintJob(ReaderActivity.this, webView, directory, fileName, new PdfView.Callback() {

                            @Override
                            public void success(String path) {
                                progressDialog.dismiss();
                                db.baiBaoDao().insertBaiBao(new BaiBao(item.getTitle(), item.getImageUrl(), item.getDate(), path, item.getUrl()));
                                tvDownload.setText(R.string.downloaddone);
                                tvDownload.setTextColor(getResources().getColor(R.color.colorPrimary));
                                imDownload.setImageResource(R.drawable.ic_download_done);
                                imDownload.setColorFilter(getResources().getColor(R.color.colorPrimary));
                                Toast.makeText(ReaderActivity.this, "Tải thành công", Toast.LENGTH_LONG).show();
                                enable = !enable;
                            }

                            @Override
                            public void failure() {
                                progressDialog.dismiss();

                            }
                        });
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                } else {
                    tvDownload.setText(R.string.download);
                    tvDownload.setTextColor(getResources().getColor(R.color.defaultcolor));
                    imDownload.setImageResource(R.drawable.ic_download);
                    imDownload.setColorFilter(R.color.defaultcolor);

                    // Khi nhấn lại Download Lần nữa thì Xoá bản download đó đi
                    new File(path).delete();
                    db.baiBaoDao().deleteBaiBao(db.baiBaoDao().getBaiBaoByDownload(path).get(0));
                    Toast.makeText(ReaderActivity.this, "Xoá thành công", Toast.LENGTH_SHORT).show();
                    enable = !enable;
                }

            }
        });

        imNguon.setImageResource(item.getNguon());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });
        webView.loadUrl(item.getUrl());
        webView.getSettings().setJavaScriptEnabled(true);
    }

    private void setEvent() {
        webView = findViewById(R.id.webView);
        tvComment = findViewById(R.id.TVcomment);
        tvDownload = findViewById(R.id.TVdownload);
        tvLike = findViewById(R.id.TVLike);
        imComment = findViewById(R.id.comment);
        imDownload = findViewById(R.id.download);
        imLike = findViewById(R.id.like);
        layoutComment = findViewById(R.id.layout_comment);
        layoutDownload = findViewById(R.id.layout_download);
        layoutLike = findViewById(R.id.layout_like);
        imNguon = findViewById(R.id.logo_nguon);
        imBack = findViewById(R.id.back);
        imShare = findViewById(R.id.share);
    }

    private void setLike(Boolean a) {
        if (a) {
            tvLike.setTextColor(getResources().getColor(R.color.colorPrimary));
            imLike.setColorFilter(getResources().getColor(R.color.colorPrimary));
            Toast.makeText(getApplication(), "Đã yêu thích", Toast.LENGTH_LONG).show();
        } else {
            tvLike.setTextColor(getResources().getColor(R.color.defaultcolor));
            imLike.setColorFilter(getResources().getColor(R.color.defaultcolor));

        }
    }

    private void shareIt(Item item) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Nhóm 16");
        intent.putExtra(Intent.EXTRA_TEXT, item.getTitle() + "\nLink: " + item.getUrl());
        intent.setType("text/plain");
        startActivity(intent);
    }

}