package com.example.android;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class openAlbumView extends AppCompatActivity {
    public static final String ALBUM_NAME = "albumName";
    private static final int REQUEST_IMAGE_GET = 1;
//    Album album;
    ArrayList<Photo> photoList;
    GridView gridView;
    String albumName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_album_view);
        Bundle bundle = getIntent().getExtras();
        albumName = bundle.getString("albumName");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Albums.readFromFile();

        System.out.println("bundle.getString(albumName): "+bundle.getString("albumName"));
//        album = Albums.getAlbum(bundle.getString("albumName"));
        System.out.println("printing album names");
        for (int i = 0; i < Albums.getAlbumList().size(); i++) {
            System.out.println(Albums.getAlbumList().get(i).getName());
        }
        getSupportActionBar().setTitle(bundle.getString("albumName"));
        gridView = findViewById(R.id.gridview);
        Adapter adapter = new Adapter(Albums.getAlbum(albumName).getAlbum(), this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(openAlbumView.this, selectImageView.class);
                intent.putExtra("index", i).putExtra("albumName", Albums.getAlbum(albumName).getName());
                startForResultViewImage.launch(intent);
            }
        });
        registerActivities();

    }
    public class Adapter extends BaseAdapter {
        private ArrayList<Photo> album;
        private Context context;
        private LayoutInflater layoutInflater;
        public Adapter(ArrayList<Photo> album, Context context) {
            this.album = album;
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return album.size();
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
            System.out.println("here in getview for openalbum");
            if (view == null) {
                view = layoutInflater.inflate(R.layout.row_items, viewGroup, false);
            }
            ImageView imagePhoto = (ImageView) view.findViewById(R.id.imageView);
            imagePhoto.setImageURI(album.get(i).getURI());

            return view;
        }

    }
    private ActivityResultLauncher<Intent> startForResultEditName;
    private ActivityResultLauncher<Intent> startForResultViewImage;
    public void registerActivities() {
        startForResultEditName =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                renameAlbum(result);
                            }
                        });
        startForResultViewImage =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                viewImage(result);
                            }
                        });
    }

    private void viewImage(ActivityResult result) {
        System.out.println("viewing image");
        Albums.readFromFile();
//        album = Albums.getAlbum(Albums.getAlbum(albumName).getName());
        finish();
        startActivity(getIntent());
    }

    private void renameAlbum(ActivityResult result) {

        Intent intent = result.getData();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }
        String name = bundle.getString(createAlbumView.ALBUM_NAME);

        if (!Albums.renameAlbum(bundle.getString("albumName"), Albums.getAlbum(albumName))) {
            bundle.putString(AlertFragment.MESSAGE_KEY, "This album already exists. Nothing has been changed.");
            DialogFragment newFragment = new AlertFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return; // does not quit activity, just returns from method
        }
        albumName = bundle.getString("albumName");
        getSupportActionBar().setTitle(albumName);
        Albums.readFromFile();
        Intent intent1 = new Intent();
        intent1.putExtra("update", "1");
        setResult(RESULT_OK, intent1);
//        finish();
//        startActivity(getIntent());
        Adapter adapter = new Adapter(Albums.getAlbum(albumName).getAlbum(), this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(openAlbumView.this, selectImageView.class);
                intent.putExtra("index", i).putExtra("albumName", Albums.getAlbum(albumName).getName());
                startForResultViewImage.launch(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.open_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_photo:
                addPhotoButton();
                return true;
            case R.id.action_delete_album:
                deleteAlbumButton();
                return true;
            case R.id.action_rename_album:
                renameAlbumButton();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void renameAlbumButton() {
        System.out.println("renaming album");
        Intent intent = new Intent(openAlbumView.this, renameAlbumView.class);
        intent.putExtra("albumName", Albums.getAlbum(albumName).getName());
        startForResultEditName.launch(intent);
    }

    private void addPhotoButton() {
        System.out.println("adding photo");
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Adapter adapter = new Adapter(Albums.getAlbum(albumName).getAlbum(), this);
        gridView.setAdapter(adapter);

        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri PATH = data.getData();
            getContentResolver().takePersistableUriPermission(PATH,Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (Albums.getAlbum(albumName).getAlbum().isEmpty()) {


                Albums.getAlbum(albumName).addPhoto(PATH);

                Albums.writeToFile();
//                Adapter adapter = new Adapter(album.getAlbum(), this);
                gridView.setAdapter(adapter);
                return;
            }


            if (Albums.getAlbum(albumName).contains(PATH)) {
                Bundle bundle = new Bundle();
                bundle.putString(AlertFragment.MESSAGE_KEY, "This photo has already been added. Nothing has been changed.");
                DialogFragment newFragment = new AlertFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "badfields");
                return;
            }



            Albums.getAlbum(albumName).addPhoto(PATH);

            Albums.writeToFile();
//            Adapter adapter = new Adapter(album.getAlbum(), this);
            gridView.setAdapter(adapter);
        }
    }
    private void deleteAlbumButton() {

        System.out.println("deleting album");
        if (Albums.getAlbum(Albums.getAlbum(albumName).getName()) != null) {
            System.out.println("in deleteAlbumButton: " + Albums.getAlbum(albumName).getName());
            Albums.deleteAlbum(Albums.getAlbum(albumName).getName());
            Albums.readFromFile();
            Intent intent = new Intent();
            intent.putExtra("update", "1");
            setResult(RESULT_OK, intent);
            finish();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(AlertFragment.MESSAGE_KEY, "This album does not exist. Nothing has been changed.");
        DialogFragment newFragment = new AlertFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "badfields");
    }
}