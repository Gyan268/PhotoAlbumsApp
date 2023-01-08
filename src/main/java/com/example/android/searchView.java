package com.example.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class searchView extends AppCompatActivity {
    boolean isModePerson = true;
    TextView tagType;
    ArrayList<Album> albums = new ArrayList<>();
    ArrayList<String> uriList = new ArrayList<>();
    GridView gridView;
    Adapter adapter;
    EditText tagText;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);
        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        albums = Albums.getAlbumList();
        tagText = (EditText) findViewById(R.id.searchbar);
        tagType = (TextView)  findViewById(R.id.tagType);
        gridView = findViewById(R.id.gridviewsearch);
        adapter = new Adapter(uriList, this);
        gridView.setAdapter(adapter);
        tagText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println(charSequence);
//                ArrayList<Photo> album = new ArrayList<>();
//                ArrayList<String> tags = new ArrayList<>();
//                for (int j = 0; i < albums.size(); i++) {
//                    album = albums.get(i).getAlbum();
//                    for (int k = 0; k < album.size(); k++) {
//                        tags = album.get(i).getTags();
//                        if (tags.get(k).startsWith("Person=") && isModePerson) {
//
//                        } else if (tags.get(k).startsWith("Location=") && !isModePerson) {
//
//                        }
//                    }
//                }
                uriList.clear();
                if (charSequence.toString().isEmpty()) {
                    gridView.setAdapter(adapter);
                    return;
                }
                for (Album album: Albums.getAlbumList()) {
                    for (Photo photo: album.getAlbum()) {
                        for (String tag: photo.getTags()) {
                            if (isModePerson && tag.startsWith("Person=")) {
                                if (tag.split("Person=",2)[1].toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                                    uriList.add(photo.getURI().toString());
                                }
                            } else if (!isModePerson && tag.startsWith("Location=")) {
                                if (tag.split("Location=",2)[1].toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                                    uriList.add(photo.getURI().toString());
                                }
                            }
                        }
                    }
                }
                gridView.setAdapter(adapter);
//                for (String str: uriList) {
//                    System.out.println(str);
//                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        configureButtons();
    }
    public class Adapter extends BaseAdapter {
        private ArrayList<String> uriList;
        private Context context;
        private LayoutInflater layoutInflater;
        public Adapter(ArrayList<String> album, Context context) {
            this.uriList = album;
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return uriList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = layoutInflater.inflate(R.layout.row_items, viewGroup, false);
            }
            ImageView imagePhoto = (ImageView) view.findViewById(R.id.imageView);
            imagePhoto.setImageURI(Uri.parse(uriList.get(i)));
            return view;
        }

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
    private void configureButtons() {
        Switch switchtagsearch = (Switch) findViewById(R.id.switchtagsearch);
        switchtagsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uriList.clear();
                if (switchtagsearch.isChecked()) {
                    isModePerson = false;
                    tagType.setText("Location");
                    if (tagText.getText().toString().isEmpty()) {
                        gridView.setAdapter(adapter);
                        return;
                    }
                    for (Album album: Albums.getAlbumList()) {
                        for (Photo photo: album.getAlbum()) {
                            for (String tag: photo.getTags()) {
                                if (isModePerson && tag.startsWith("Person=")) {
                                    if (tag.split("Person=",2)[1].toLowerCase().startsWith(tagText.getText().toString().toLowerCase())) {
                                        uriList.add(photo.getURI().toString());
                                    }
                                } else if (!isModePerson && tag.startsWith("Location=")) {
                                    if (tag.split("Location=",2)[1].toLowerCase().startsWith(tagText.getText().toString().toLowerCase())) {
                                        uriList.add(photo.getURI().toString());
                                    }
                                }
                            }
                        }
                    }
//                    for (String str: uriList) {
//                        System.out.println(str);
//                    }
                    gridView.setAdapter(adapter);

                } else {

                    isModePerson = true;
                    tagType.setText("Person");
                    if (tagText.getText().toString().isEmpty()) {
                        gridView.setAdapter(adapter);
                        return;
                    }
                    for (Album album: Albums.getAlbumList()) {
                        for (Photo photo: album.getAlbum()) {
                            for (String tag: photo.getTags()) {
                                if (isModePerson && tag.startsWith("Person=")) {
                                    if (tag.split("Person=",2)[1].toLowerCase().startsWith(tagText.getText().toString().toLowerCase())) {
                                        uriList.add(photo.getURI().toString());
                                    }
                                } else if (!isModePerson && tag.startsWith("Location=")) {
                                    if (tag.split("Location=",2)[1].toLowerCase().startsWith(tagText.getText().toString().toLowerCase())) {
                                        uriList.add(photo.getURI().toString());
                                    }
                                }
                            }
                        }
                    }
//                    for (String str: uriList) {
//                        System.out.println(str);
//                    }
                    gridView.setAdapter(adapter);

                }
            }
        });
    }
}
