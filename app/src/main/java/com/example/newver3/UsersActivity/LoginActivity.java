package com.example.newver3.UsersActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newver3.Adapter.Custom_Dialog;
import com.example.newver3.Firebase.Users;
import com.example.newver3.MainActivity;
import com.example.newver3.Others.DisplayAlert;
import com.example.newver3.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

//import com.example.newsver2.Database.AppDatabase;
//import com.example.news.database.User;

public class LoginActivity extends AppCompatActivity {
    Button btnSignUp,btnSignIn,btnForgotPassWord;
    TextInputEditText editUsername;
    EditText editPassword;
    Intent intent;



    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnSignIn = findViewById(R.id.button_signin);
        btnForgotPassWord = findViewById(R.id.button_forgot_password);
        btnSignUp = findViewById(R.id.button_signup);
        editUsername = findViewById(R.id.et_username);
        editPassword= findViewById(R.id.et_password);
        btnSignUp.setOnClickListener(setEvent);
        btnForgotPassWord.setOnClickListener(setEvent);
        btnSignIn.setOnClickListener(setEvent);



    }

    private View.OnClickListener setEvent = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_signin:
                {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
                    btnSignIn.startAnimation(animation);
                    if (!editPassword.getText().toString().equals("")||!editUsername.getText().toString().equals(""))
                    {
                        myRef.child(editUsername.getText().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                MainActivity.user=snapshot.getValue(Users.class);
                                if(MainActivity.user==null)
                                    Toast.makeText(LoginActivity.this, "Username không tồn tại", Toast.LENGTH_SHORT).show();
                                else {
                                    if(MainActivity.user.password.equals(editPassword.getText().toString()))
                                    {

                                        for (int i = 0; i <MainActivity.theloai.length ; i++) {
                                            MainActivity.theloai[i]=MainActivity.user.theloai.get(i);
                                        }
                                        editUsername.setText("");
                                        editPassword.setText("");
                                        MainActivity.textlogin.setText(MainActivity.user.name);

                                        Picasso.with(getApplicationContext()).load(Uri.parse(MainActivity.user.avatar)).into(MainActivity.avatarHeader);
                                        finish();
                                    }else
                                    {
                                       if(!editPassword.getText().toString().equals(""))
                                       {
                                           Toast.makeText(LoginActivity.this,"Sai mật khẩu",Toast.LENGTH_LONG).show();
                                           MainActivity.user=null;
                                       }
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("LOI",error.getMessage());
                            }
                        });
                    }else
                    {
                        Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case R.id.button_signup:{
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);

                    break;
                }

                case R.id.button_forgot_password:{

                    openDialog();

                    break;
                }
                default:
                    break;
            }
        }
    };
    public void openDialog()
    {
        Custom_Dialog custom_dialog= new Custom_Dialog();
        custom_dialog.show(getSupportFragmentManager(),"Forgot password");
    }
}