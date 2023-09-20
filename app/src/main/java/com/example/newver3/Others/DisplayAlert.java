package com.example.newver3.Others;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class DisplayAlert {
    private AlertDialog.Builder builder;
    private Activity activity;

    public void displayAlert(boolean booleanFinish) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(booleanFinish) activity.finish();
            }
        });
        //Display the alert dialog.
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public DisplayAlert(AlertDialog.Builder builder, Activity activity) {
        this.builder = builder;
        this.activity = activity;
    }
}
