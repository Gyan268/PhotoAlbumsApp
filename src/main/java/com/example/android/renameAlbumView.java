package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class renameAlbumView extends AppCompatActivity {
    Album album;
    EditText albumName;
    //    Albums albums;
    public static final String ALBUM_NAME = "albumName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rename_album_view);
        Intent intent = getIntent();
        album = Albums.getAlbum(intent.getStringExtra("albumName"));
        configureButtons();

    }
    private void configureButtons() {
        Button renameAlbum = (Button) findViewById(R.id.confirm_album_rename);
        renameAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumName  =  (EditText) findViewById(R.id.inputNewAlbumName);
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
        Button setViewHome = (Button) findViewById(R.id.cancel_album_rename);
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
        bundle.putString("albumName",name);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }
}
