package com.onadasoft.weatherdaily;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class NotConnectedDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //if (getActivity() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
            builder.setTitle("No Internet");
            builder.setMessage("Please connect to your internet");

            builder.setPositiveButton("Wifi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
                    dialog.cancel();
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });
            builder.setNegativeButton("Mobile Data", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
//                    intent.setComponent(new ComponentName("com.android.settings","com.android.phone.NetworkSetting"));
//
                    startActivityForResult(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS), 0);
                    dialog.cancel();
                    // getActivity().finish();
                }
            });

            return builder.create();


    }
}
