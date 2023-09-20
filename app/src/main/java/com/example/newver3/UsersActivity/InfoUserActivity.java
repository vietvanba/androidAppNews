package com.example.newver3.UsersActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newver3.Firebase.Comment;
import com.example.newver3.Firebase.Users;
import com.example.newver3.Database.Item;
import com.example.newver3.MainActivity;
import com.example.newver3.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InfoUserActivity extends AppCompatActivity {
    TextInputEditText username,password,newpassword,mail,dateofbirth,name;
    private Uri image = null;

    final Calendar c = Calendar.getInstance();
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;

    RelativeLayout imgUser,dsLike,dsComment;
    TextView tv_name,dateCreateAccount;
    Button update,logout;
    ImageButton btn_date;
    ImageView imageView;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    DatabaseReference myRefNews = database.getReference("news");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef =storage.getReferenceFromUrl("gs://myproject-d348d.appspot.com/avatars");

    private static int REQUEST_CODE = 1999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);
        username=findViewById(R.id.text_username);
        password=findViewById(R.id.text_password);
        newpassword=findViewById(R.id.text_new_pass);
        mail=findViewById(R.id.textMail);
        imgUser=findViewById(R.id.imgUser);
        tv_name=findViewById(R.id.tv_name);
        dateCreateAccount=findViewById(R.id.tv_account);
        btn_date=findViewById(R.id.btn_date);
        dateofbirth=findViewById(R.id.tv_birthday);
        update=findViewById(R.id.btn_update);
        logout=findViewById(R.id.btn_logout);
        name=findViewById(R.id.textName);
        imageView=findViewById(R.id.imgAccUser);
        dsLike=findViewById(R.id.DS_like);
        dsComment=findViewById(R.id.DS_Comment);


        dsLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Item> listLiked= new ArrayList<>();
                Query allLike = myRefNews.child("like").child(MainActivity.user.username);
                allLike.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item:snapshot.getChildren()
                             ) {
                            listLiked.add(item.getValue(Item.class));

                        }
                        Intent intent = new Intent(InfoUserActivity.this, LikedAndCommentedActivity.class);
                        intent.putExtra("linkBaiBao", (Serializable) listLiked);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        dsComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Item> listComment= new ArrayList<>();
                Query allComment = myRefNews.child("comment");
                allComment.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot news:snapshot.getChildren()) {
                            Boolean check=false;
                            for(DataSnapshot listComment:news.child("listComment").getChildren())
                            {
                                Comment comment= listComment.getValue(Comment.class);
                                if(comment.username.equals(MainActivity.user.username))
                                {
                                    check=true;
                                    break;
                                }
                            }
                            if(check)
                            {
                                listComment.add(news.child("item").getValue(Item.class));
                            }

                        }
                        Intent intent = new Intent(InfoUserActivity.this,LikedAndCommentedActivity.class);
                        intent.putExtra("linkBaiBao", (Serializable) listComment);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        if(MainActivity.user.avatar.equals(""))
        {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar));
        }else
        {
            Picasso.with(this).load(Uri.parse(MainActivity.user.avatar)).into(imageView);
        }

        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH);
        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        btn_date.setOnClickListener(v -> buttonSelectDate());
        
        dateofbirth.setText(MainActivity.user.dateOfBirth);
        username.setText(MainActivity.user.username);
        mail.setText(MainActivity.user.mail);
        tv_name.setText(MainActivity.user.name);
        name.setText(MainActivity.user.name);

        dateCreateAccount.setText("Ngày lập tài khoản : "+MainActivity.user.dateCreateAccount);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.user=null;
                MainActivity.theloai= new boolean[]{true, true, true, true, true, true};
                MainActivity.textlogin.setText("Đăng nhập");
                MainActivity.avatarHeader.setImageDrawable(getResources().getDrawable(R.drawable.avatar));
                finish();
            }
        });

        imgUser.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, REQUEST_CODE);

        });
        update.setOnClickListener(v -> {
            Users updateUser;

            updateUser=MainActivity.user;
            updateUser.mail=mail.getText().toString();
            updateUser.name=name.getText().toString();
            updateUser.dateOfBirth=dateofbirth.getText().toString();


            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = ((RoundedDrawable) imageView.getDrawable()).getSourceBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = storageRef.child(MainActivity.user.username).putBytes(data);


            uploadTask.addOnSuccessListener((OnSuccessListener<? super UploadTask.TaskSnapshot>) (taskSnapshot)->{
               StorageMetadata storageMetadata = taskSnapshot.getMetadata();
               Task<Uri> downloadUrl=storageRef.child(MainActivity.user.username).getDownloadUrl();
               downloadUrl.addOnSuccessListener((OnSuccessListener) (uri)->{
                   String imageReference=uri.toString();
                   MainActivity.user.avatar=imageReference;
                   myRef.child(MainActivity.user.username).setValue(MainActivity.user);
                });
            });

            if(password.getText().toString().equals(MainActivity.user.password))
            {
                if(newpassword.getText().toString().equals(""))
                {
                    updateUser(updateUser);
                }else
                {
                    updateUser.password=newpassword.getText().toString();
                    updateUser(updateUser);
                }


            }else
            {
                Toast.makeText(this, "Sai mật khẩu hoặc bị trống trường dữ liệu", Toast.LENGTH_SHORT).show();
            }


        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                image = imageUri;
                imageView.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Có gì đó không đúng. Vui lòng kiểm tra lại!!", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "Bạn chưa chọn ảnh", Toast.LENGTH_LONG).show();
        }
    }
    private void buttonSelectDate() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                dateofbirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                lastSelectedYear = year;
                lastSelectedMonth = monthOfYear+1;
                lastSelectedDayOfMonth = dayOfMonth;
            }
        };

        DatePickerDialog datePickerDialog ;

        datePickerDialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
        datePickerDialog.show();
    }

    private void updateUser(Users updateUser)
    {
        try {

            myRef.child(updateUser.username).setValue(updateUser);
            MainActivity.user=updateUser;
            username.setText(MainActivity.user.username);
            mail.setText(MainActivity.user.mail);
            tv_name.setText(MainActivity.user.name);
            name.setText(MainActivity.user.name);
            password.setText("");
            newpassword.setText("");
            dateofbirth.setText(MainActivity.user.dateOfBirth);
            Toast.makeText(InfoUserActivity.this, "Update thành công", Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {

            e.printStackTrace();
        }
    }

}
