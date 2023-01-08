package com.example.android;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AlertFragment extends DialogFragment {
    public static final String MESSAGE_KEY = "message_key";

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        Bundle bundle = getArguments();
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        builder.setMessage(bundle.getString(MESSAGE_KEY))
                .setPositiveButton("OK", (dialog,id) -> {});

        // Create the AlertDialog object and return it
        return builder.create();
    }
}