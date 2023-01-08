package com.example.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class createAlbumView extends AppCompatActivity {

    EditText albumName;
//    Albums albums;
    public static final String ALBUM_NAME = "albumName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_album_view);
        configureButtons();

    }
    private void configureButtons() {
        Button createAlbum = (Button) findViewById(R.id.confirm_album_create);
        createAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumName  =  (EditText) findViewById(R.id.inputAlbumName);
                if (albumName.getText().toString().isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AlertFragment.MESSAGE_KEY, "Missing an album name");
                    DialogFragment newFragment = new AlertFragment();
                    newFragment.setArguments(bundle);
                    newFragment.show(getSupportFragmentManager(), "badfields");
                    return;
                } else {
                    save(view);
                }

            }
        });
        Button setViewHome = (Button) findViewById(R.id.cancel_album_create);
        setViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
    public void save(View view) {
        String name = albumName.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(ALBUM_NAME,name);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }
}
