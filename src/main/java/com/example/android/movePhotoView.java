package com.example.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class movePhotoView extends AppCompatActivity {

    private ListView listView;
    ArrayList<Album> albums = new ArrayList<Album>();
    Album album;
    Photo photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Albums.readFromFile();
        albums = Albums.getAlbumList();
        listView = findViewById(R.id.album_listView);
        Intent intent = getIntent();
        album = Albums.getAlbum(intent.getStringExtra("albumName"));
        photo = album.getPhotoByUri(intent.getStringExtra("photoUri"));
        albums.remove(album);
        listView.setAdapter(new ArrayAdapter<Album>(this, R.layout.album, albums));
        listView.setOnItemClickListener(
                (p, v, pos, id) -> movePhoto(pos));

    }

    private void movePhoto(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to move the photo to this album?");
        //Adding positive button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (albums.get(pos).getPhotoByUri(getIntent().getStringExtra("photoUri")) == null) {
                    albums.get(pos).addPhoto(Uri.parse(getIntent().getStringExtra("photoUri")));
                    album.deletePhoto(Uri.parse(getIntent().getStringExtra("photoUri")));
                    Albums.replaceAlbum(getIntent().getStringExtra("albumName"), album);
                    albums.add(album);
                    Albums.writeToFile();
                    setResult(RESULT_OK, new Intent().putExtra("photoUri", getIntent().getStringExtra("photoUri")).putExtra("albumName",getIntent().getStringExtra("albumName")));
                    finish();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(AlertFragment.MESSAGE_KEY, "This album already contains this photo. Nothing has been changed.");
                DialogFragment newFragment = new AlertFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "badfields");
            }
        });
        //Adding negative button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        builder.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                albums.add(album);
                Albums.writeToFile();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}