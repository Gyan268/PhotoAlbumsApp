package com.example.android;

import com.example.android.Album;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class Albums {
    private static ArrayList<Album> albums = new ArrayList<Album>();
    public static void writeToFile() {
        try {
//            File data = new File("/data/data/com.example.android/albums.dat");
//            data.createNewFile();
            FileOutputStream fos = new FileOutputStream("/data/data/com.example.android/albums.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            System.out.println("Writing to albums.dat");
//            for (Album album: albums) {
//                System.out.println(album.getName());
//            }
            oos.writeObject(albums);
            oos.close();
            fos.close();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }
    public static ArrayList<Album> readFromFile() {
        try {
//            File data = new File("/data/data/com.example.android/albums.dat");
//            data.createNewFile();
            FileInputStream fis = new FileInputStream("/data/data/com.example.android/albums.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            albums = (ArrayList<Album>) ois.readObject();
            System.out.println("Reading from albums.dat");
//            for (Album album: albums) {
//                System.out.println(album.getName());
//            }
            ois.close();
            fis.close();
        } catch (IOException|ClassNotFoundException e) {
//            e.printStackTrace();
        }
        return albums;
    }

    public static Album getAlbum(String name) {

        for (Album item: albums) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }
    public static Album createAlbum(String name) {
        System.out.println("creating album " + name);
        if (getAlbum(name) != null) return null;
        Album album = new Album(name);
        albums.add(album);
        writeToFile();
        System.out.println("created album " + name);
        return album;
    }
    public static boolean renameAlbum(String name, Album album) {
        System.out.println("renaming album " + album.getName() + " to " + name);
        if (getAlbum(name) != null) return false;
        albums.get(albums.indexOf(album)).setName(name);
        writeToFile();
        System.out.println("renamed album " + album.getName() + " to " + name);
        return true;
    }
    public static void deleteAlbum(String name) {
        System.out.println("deleting album " + name);
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getName().equals(name)) {
                albums.get(i).deleteAllPhotos();
                albums.remove(albums.get(i));
            }
        }
        writeToFile();
        System.out.println("deleted album " + name);
    }
    public static void replaceAlbum(String name, Album album) {
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getName().equals(album.getName())) {
                albums.set(i, album);
            }
        }
    }
    public static ArrayList<Album> getAlbumList() {
        return albums;
    }
}
