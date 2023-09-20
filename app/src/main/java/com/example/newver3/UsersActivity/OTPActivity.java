package com.example.newver3.UsersActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.newver3.Firebase.Users;
import com.example.newver3.JavaMailAPI.JavaMailAPI;
import com.example.newver3.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class OTPActivity extends AppCompatActivity {
    String OTP;
    TextView textmail;
    Button btn_resent,btn_verify;
    PinView pinView;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        Intent intent = getIntent();
        Users newUser= (Users) intent.getSerializableExtra("newsUser");

        OTP=getRandomNumberString();

        textmail=findViewById(R.id.text_mail);
        btn_resent=findViewById(R.id.btn_resentOTP);
        btn_verify=findViewById(R.id.btn_verify);
        pinView=findViewById(R.id.pin_view);
        textmail.setText("Mã OTP của bạn đã được gửi tới mail: \n"+newUser.mail);
        btn_resent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail(OTPActivity.this,newUser.mail,"Mã OTP của bạn","Mã OTP của bạn là: "+OTP);
            }
        });
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pinView.getText().toString().equals(OTP))
                {
                    myRef.child(newUser.username).setValue(newUser);
                    Toast.makeText(OTPActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }else
                {
                    Toast.makeText(OTPActivity.this, "Vui lòng nhập lại OTP", Toast.LENGTH_SHORT).show();
                }


            }
        });

        sendMail(this,newUser.mail,"Mã OTP của bạn","Mã OTP của bạn là: "+OTP);



    }
    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
    public static void sendMail(Context mContext, String mEmail, String mSubject, String mMessage)
    {
        JavaMailAPI javaMailAPI = new JavaMailAPI(mContext,mEmail,mSubject,mMessage);
        javaMailAPI.execute();
    }
}