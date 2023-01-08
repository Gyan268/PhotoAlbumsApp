package com.example.android;

import android.net.Uri;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Photo implements Serializable {


    private static final long serialVersionUID = 1L;
    private String URI;//location of photo
    private ArrayList<String> tags = new ArrayList<>();
    private String caption;

    public Photo(Uri URI) {
        this.URI = URI.toString();
    }
    public void setURI(Uri URI) {
        this.URI = URI.toString();
    }


    public boolean setTags(boolean isPerson, String value) {
        String str;

        if (isPerson) {
            str = "Person" + "=" + value;
        } else {
            str = "Location" + "=" + value;
        }
        if (tags == null) {
            tags = new ArrayList<>();
        }
        if (tags.contains(str)) {
            return false;
        } else {
            tags.add(str);
            System.out.println(str);
            return true;
        }

    }

    public void deleteTag(String tagPair) {
        tags.remove(tagPair);
    }
    public ArrayList<String> getTags() {
        return tags;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }
    public Uri getURI() {
        return Uri.parse(URI);
    }

    public String getCaption() {
        return caption;
    }
    public static void write(Photo photo, String REF) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(REF));
            oos.writeObject(photo);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Photo read(String REF) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(REF));
            Photo photo = (Photo)ois.readObject();
            ois.close();
            return photo;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }


}
