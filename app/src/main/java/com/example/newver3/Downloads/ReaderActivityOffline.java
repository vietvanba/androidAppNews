package com.example.newver3.Downloads;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.newver3.Database.AppDatabase;
import com.example.newver3.Database.BaiBao;
import com.example.newver3.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class ReaderActivityOffline extends AppCompatActivity {
    PDFView pdfView;
    Button delete;
    AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_offline);
        db= AppDatabase.getInMemoryDatabase(this.getApplicationContext());
        pdfView=findViewById(R.id.pdfView);
        delete=findViewById(R.id.delete_page);
        Intent intent = getIntent();
        BaiBao baiBao= (BaiBao) intent.getSerializableExtra("BaiBao");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new File(baiBao.getDownLoadnguon()).delete();
                db.baiBaoDao().deleteBaiBao(baiBao);
                Toast.makeText(ReaderActivityOffline.this, "Xoá thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        File file = new File(baiBao.getDownLoadnguon());
        pdfView.fromFile(file).load();

    }
}