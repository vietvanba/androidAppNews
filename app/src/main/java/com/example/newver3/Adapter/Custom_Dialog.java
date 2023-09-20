package com.example.newver3.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.newver3.Firebase.Users;
import com.example.newver3.JavaMailAPI.JavaMailAPI;
import com.example.newver3.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Custom_Dialog extends AppCompatDialogFragment {
    TextInputEditText fg_username;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_forgot_pass, null);

        fg_username = view.findViewById(R.id.fg_username);

        builder.setView(view).setTitle("Nhập thông tin :").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Random random = new Random();

                try {
                    Query alluser = myRef;
                    alluser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Users user = null;
                            for (DataSnapshot listuser : snapshot.getChildren()) {
                                if (listuser.getKey().equals(fg_username.getText().toString())) {
                                    user = listuser.getValue(Users.class);
                                    break;
                                }

                            }
                            if (user != null) {
                                int pass = Math.abs(random.nextInt());
                                user.password = String.valueOf(pass);
                                myRef.child(user.username).setValue(user);
                                String content = "Mật khẩu của bạn là : " + String.valueOf(pass);
                                JavaMailAPI javaMailAPI = new JavaMailAPI(view.getContext(), user.mail, "Mật khẩu mới của bạn là", content);
                                javaMailAPI.execute();
                            } else {
                                Toast.makeText(view.getContext(), "User không tồn tại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } catch (Exception ex) {

                }


            }
        });


        return builder.create();
    }
}
