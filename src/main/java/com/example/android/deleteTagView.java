package com.example.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class deleteTagView extends AppCompatActivity {

    private ListView listView;
    ArrayList<String> tagList = new ArrayList<>();
    Album album;
    Photo photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Albums.readFromFile();

        listView = findViewById(R.id.album_listView);
        Intent intent = getIntent();
        album = Albums.getAlbum(intent.getStringExtra("albumName"));
        photo = album.getPhotoByUri(intent.getStringExtra("photoUri"));
        tagList = photo.getTags();
        if (tagList == null) {
            finish();
            return;
        }

        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.album, tagList));
        listView.setOnItemClickListener(
                (p, v, pos, id) -> deleteTag(pos));

    }
    private void deleteTag(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to remove this tag?");
        //Adding positive button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                photo.deleteTag(listView.getItemAtPosition(pos).toString());
                Albums.writeToFile();
                finish();
                startActivity(getIntent());
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
                save();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save() {
        setResult(RESULT_OK);
        finish();
    }
}
