package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class setTagView extends AppCompatActivity {
    Album album;
    Photo photo;
//    Switch tagSwitch;
    EditText tagText;

    Boolean isModePerson = true;
    TextView tagTypeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settag_view);
        Intent intent = getIntent();
        Switch tagSwitch = (Switch) findViewById(R.id.setTagSwitch);
        tagTypeText = findViewById(R.id.tagTypeText);
        tagText  =  (EditText) findViewById(R.id.tagValueText);
        album = Albums.getAlbum(intent.getStringExtra("albumName"));
        photo = album.getPhotoByUri(intent.getStringExtra("photoUri"));
        configureButtons();
    }

    private void configureButtons() {
        Button confirmButton = (Button) findViewById(R.id.confirmSetTag);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tagText.getText().toString().isEmpty()){
                    Bundle bundle = new Bundle();
                    bundle.putString(AlertFragment.MESSAGE_KEY, "Missing an tag value");
                    DialogFragment newFragment = new AlertFragment();
                    newFragment.setArguments(bundle);
                    newFragment.show(getSupportFragmentManager(), "badfields");
                    return;
                }
                if (!Albums.getAlbum(getIntent().getStringExtra("albumName")).getPhotoByUri(
                        getIntent().getStringExtra("photoUri")).setTags(
                        isModePerson, tagText.getText().toString())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AlertFragment.MESSAGE_KEY, "This tag is already on this photo");
                    DialogFragment newFragment = new AlertFragment();
                    newFragment.setArguments(bundle);
                    newFragment.show(getSupportFragmentManager(), "badfields");
                    return;
                }
                Albums.writeToFile();
//                if (isModePerson) {
//                    System.out.println("person" + "=" + tagText.getText().toString());
//                } else {
//                    System.out.println("location" + "=" + tagText.getText().toString());
//                }
                setResult(RESULT_OK);
                finish();
            }
        });
        Button cancelButton = (Button) findViewById(R.id.cancelSetTag);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Switch tagSwitch = (Switch) findViewById(R.id.setTagSwitch);
        tagSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tagSwitch.isChecked()) {
                    isModePerson = false;
                    tagTypeText.setText("Location");
                } else {
                    isModePerson = true;
                    tagTypeText.setText("Person");
                }
            }
        });
    }
}
