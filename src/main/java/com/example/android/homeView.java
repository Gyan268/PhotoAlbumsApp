package com.example.android;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class homeView extends AppCompatActivity {

    private ListView listView;
    ArrayList<Album> albums = new ArrayList<Album>();
    public static final String ALBUM_NAME = "albumName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_view);
        Albums.readFromFile();
        albums = Albums.getAlbumList();
        listView = findViewById(R.id.album_listView);

        listView.setAdapter(new ArrayAdapter<Album>(this, R.layout.album, albums));
        listView.setOnItemClickListener(
                (p, v, pos, id) -> openAlbum(pos));
        registerActivities();

    }
    private void openAlbum(int pos) {
        Albums.readFromFile();
        Bundle bundle = new Bundle();
        bundle.putString("albumName", listView.getItemAtPosition(pos).toString());
        Intent intent = new Intent(this, openAlbumView.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            albums = Albums.readFromFile();
            listView.setAdapter(new ArrayAdapter<Album>(this, R.layout.album, albums));
        }
    }
    private ActivityResultLauncher<Intent> startForResultAdd;
    private ActivityResultLauncher<Intent> startForResultSearch;
    public void registerActivities() {
        startForResultAdd =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                addAlbum(result);
                            }
                        });
        startForResultSearch =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                            }
                        });
    }


    private void addAlbum(ActivityResult result) {
        Intent intent = result.getData();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }
        String name = bundle.getString(createAlbumView.ALBUM_NAME);
        Album album = Albums.createAlbum(name);
        if (album == null) {
            bundle.putString(AlertFragment.MESSAGE_KEY, "This album already exists. Nothing has been changed.");
            DialogFragment newFragment = new AlertFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }
        albums = Albums.readFromFile();
        listView.setAdapter(new ArrayAdapter<Album>(this, R.layout.album, albums));
    }

    private void createAlbum() {
        Intent intent = new Intent(this, createAlbumView.class);
        startForResultAdd.launch(intent);
    }
    private void searchPhotos() {
        Intent intent = new Intent(this, searchView.class);
        startForResultSearch.launch(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_album:
                createAlbum();
                return true;
            case R.id.action_search_photo:
                searchPhotos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}