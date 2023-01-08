package com.example.android;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class selectImageView extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    Album album;
    Photo photo;
    int index;
    ArrayList<Photo> photoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_photo_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView = findViewById(R.id.showImage);
        textView = findViewById(R.id.tags);
        Intent intent = getIntent();
        album = Albums.getAlbum(intent.getStringExtra("albumName"));
        photoList = album.getAlbum();
        index = intent.getIntExtra("index", 0);
        photo = photoList.get(index);
        imageView.setImageURI(photo.getURI());
        String tags = "";
        for (String tag: photo.getTags()) {
            tags += tag + " ";
        }
        textView.setText(tags);
        configureButtons();
        registerActivities();
    }

    private void configureButtons() {
        Button deleteTag = (Button) findViewById(R.id.delete_tag);
        deleteTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(selectImageView.this, deleteTagView.class);
                intent.putExtra("albumName", album.getName()).putExtra("photoUri", photo.getURI().toString());
                startForResultDeleteTag.launch(intent);
            }
        });
        Button setTag = (Button) findViewById(R.id.setTag);
        setTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(selectImageView.this, setTagView.class);
                intent.putExtra("albumName", album.getName()).putExtra("photoUri", photo.getURI().toString());
                startForResultTag.launch(intent);
            }
        });
        ImageButton forwardButton = (ImageButton) findViewById(R.id.forwardButton);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index < photoList.size()-1) {
                    index++;
                } else {
                    index = 0;
                }
                photo = photoList.get(index);
                imageView.setImageURI(photo.getURI());
                String tags = "";
                for (String tag: photo.getTags()) {
                    tags += tag + " ";
                }
                textView.setText(tags);
            }
        });
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index > 0) {
                    index--;
                } else {
                    index = photoList.size()-1;
                }
                photo = photoList.get(index);
                imageView.setImageURI(photo.getURI());
                String tags = "";
                for (String tag: photo.getTags()) {
                    tags += tag + " ";
                }
                textView.setText(tags);
            }
        });
        Button removeButton = (Button) findViewById(R.id.remove_photo);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                album.deletePhoto(photo.getURI());
                Albums.replaceAlbum(album.getName(), album);
                photoList = album.getAlbum();
                if (index > 0) {
                    index--;
                } else {
                    index = photoList.size()-1;
                }
                if (photoList.isEmpty()) {
                    Albums.writeToFile();
                    setResult(RESULT_OK, new Intent());
                    finish();
                    return;
                }
                photo = photoList.get(index);
                imageView.setImageURI(photo.getURI());
                String tags = "";
                for (String tag: photo.getTags()) {
                    tags += tag + " ";
                }
                textView.setText(tags);
            }
        });
        Button moveButton = (Button) findViewById(R.id.movephoto);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(selectImageView.this, movePhotoView.class);
                intent.putExtra("albumName", album.getName()).putExtra("photoUri", photo.getURI().toString());
                startForResultMove.launch(intent);
            }
        });
    }
    private ActivityResultLauncher<Intent> startForResultMove;
    private ActivityResultLauncher<Intent> startForResultTag;
    private ActivityResultLauncher<Intent> startForResultDeleteTag;
    public void registerActivities() {
        startForResultMove =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                movePhoto(result);
                            }
                        });
        startForResultTag =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                tagPhoto(result);
                            }
                        });
        startForResultDeleteTag =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {

//                            if (result.getResultCode() == Activity.RESULT_OK) {
                                deleteTagPhoto(result);
//                            }
                        });
    }

    private void deleteTagPhoto(ActivityResult result) {
        String tags = "";
        for (String tag: Albums.getAlbum(album.getName()).getAlbum().get(index).getTags()) {
            tags += tag + " ";
        }
        System.out.println(tags);
        textView.setText(tags);
    }

    private void tagPhoto(ActivityResult result) {
        String tags = "";
        for (String tag: photo.getTags()) {
            tags += tag + " ";
        }
        System.out.println("tags: " + tags);
        textView.setText(tags);
    }

    private void movePhoto(ActivityResult result) {
        Albums.readFromFile();
//        album = Albums.getAlbum(result.getData().getStringExtra("albumName"));
        System.out.println("albumName: " + album);
        System.out.println("photoUri: " + result.getData().getStringExtra("photoUri"));
//        album.deletePhoto(Uri.parse(result.getData().getStringExtra("photoUri")));
        album.deletePhoto(photo.getURI());
        Albums.replaceAlbum(album.getName(), album);
        photoList = album.getAlbum();
        if (index < photoList.size()-1) {
            index++;
        } else {
            index = 0;
        }
        System.out.println("is photo list empty?: " + photoList.isEmpty());
        if (photoList.isEmpty()) {
            setResult(RESULT_OK, new Intent());
            finish();
            return;
        }
        photo = photoList.get(index);
        imageView.setImageURI(photo.getURI());
        String tags = "";
        for (String tag: photo.getTags()) {
            tags += tag + " ";
        }
        textView.setText(tags);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
