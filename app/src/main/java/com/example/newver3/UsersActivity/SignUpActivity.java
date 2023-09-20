package com.example.newver3.UsersActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

//import com.example.newver3.database.AppDatabase;

public class SignUpActivity extends AppCompatActivity {
    Intent intent;
    Button btnSignUp;
    TextInputEditText editUsername,editName,editEmail;
    EditText editPassword;
    String regex="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");

    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;
    private TextView txtNgaySinh;
    private ImageButton btnNgaySinh;
    final Calendar c = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnNgaySinh = findViewById(R.id.add_btnNgaySinh);

        btnSignUp = findViewById(R.id.button_signup);
        editUsername = findViewById(R.id.et_username);
        editPassword= findViewById(R.id.et_password);
        editName= findViewById(R.id.et_name);
        editEmail=findViewById(R.id.et_email);
        txtNgaySinh = findViewById(R.id.add_ngaySinh);
        btnSignUp.setOnClickListener(setEvent);



        this.btnNgaySinh.setOnClickListener(v -> buttonSelectDate());

        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH);
        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        txtNgaySinh.setText(lastSelectedDayOfMonth + "/" + (lastSelectedMonth + 1) + "/" + lastSelectedYear);

    }
    private void buttonSelectDate() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                txtNgaySinh.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
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
    private View.OnClickListener setEvent = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId() /*to get clicked view id**/) {

                case R.id.button_signup:
                        if (editUsername.getText().toString().equals("")||editPassword.getText().toString().equals("")||editName.getText().toString().equals("")||txtNgaySinh.getText().toString().equals("")||editEmail.getText().toString().equals("")) {
                            Toast.makeText(SignUpActivity.this, "Không được để trống các trường dữ liệu", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    boolean check = true;

                                    for (DataSnapshot item: snapshot.getChildren()) {
                                        if(item.getValue(Users.class).username.equals(editUsername.getText().toString()))
                                        {
                                            Toast.makeText(SignUpActivity.this, "Username đã tồn tại\n Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                                            check=false;
                                            break;
                                        }
                                        else
                                        if(item.getValue(Users.class).mail.equals(editEmail.getText().toString().toLowerCase()))
                                        {
                                            Toast.makeText(SignUpActivity.this, "Mail đã tồn tại\n Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                                            check=false;
                                            break;
                                        }else
                                        {
                                            if(!editEmail.getText().toString().matches(regex))
                                            {
                                                Toast.makeText(SignUpActivity.this, "Sai định dạng Mail\n Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                                                check=false;
                                                break;
                                            }
                                        }
                                    }
                                    if(check==true)
                                    {
                                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",java.util.Locale.ENGLISH);
                                        formatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                                        List<Boolean> tam =new ArrayList<>();
                                        for(boolean theloai: MainActivity.theloai)
                                        {
                                            tam.add(true);
                                        }
                                        Users newUser = new Users(editUsername.getText().toString(),editPassword.getText().toString(),editName.getText().toString(),txtNgaySinh.getText().toString(),formatter.format(new Date().getTime()),editEmail.getText().toString(),false,tam,"");

                                        Intent intentOTP = new Intent(SignUpActivity.this, OTPActivity.class);
                                        intentOTP.putExtra("newsUser", (Serializable) newUser);
                                        startActivity(intentOTP);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        break;
                default:
                    break;
            }
        }
    };
}