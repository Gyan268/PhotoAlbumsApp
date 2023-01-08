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
public class Album implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Photo> album = new ArrayList<Photo>();
    private String name;
    public Album(String albumName) {
        System.out.println("in album constructor");
        this.name = albumName;
        Albums.writeToFile();
//        File folder = new File("/data/data/com.example.android/files/" + name + "/");
//        File[] photos = folder.listFiles();
//        if (photos == null) return;
//        for(int i = 0; i < photos.length; i++) {
//
//            Photo photo = Photo.read("/data/data/com.example.android/files/" + name + "/" + photos[i].getName());
//            System.out.println("/data/data/com.example.android/files/" + name + "/" + photos[i].getName());
//            album.add(photo);

    }
    public void addPhoto(Uri uri) {

        Photo photo = new Photo(uri);
        album.add(photo);
        Albums.writeToFile();

    }
    public Photo getPhotoByUri(String uri) {
        for (int i = 0; i < album.size(); i++) {
            if (album.get(i).getURI().toString().equals(uri)) {
                return album.get(i);
            }
        }
        return null;
    }
    public void deletePhoto(Uri uri) {
        for (int i = 0; i < album.size(); i++) {
            if (album.get(i).getURI().equals(uri)) {
                album.remove(i);
            }
        }
        Albums.writeToFile();
    }
    public void setName(String name) {
        this.name = name;
        Albums.writeToFile();
    }
    public void deleteAllPhotos() {
        album.clear();
    }
    public String getName() {
        return name;
    }
    public ArrayList<Photo> getAlbum() {
        return album;
    }
    public String toString() {   // used by ListView
        return name;
    }

    public boolean contains(Uri uri) {
        System.out.println(uri);
        for (Photo item: album) {
            System.out.println(item.getURI());
            if (item.getURI().equals(uri)) {
                return true;
            }
        }
        return false;
    }
}
