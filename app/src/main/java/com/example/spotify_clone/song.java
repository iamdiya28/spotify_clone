package com.example.spotify_clone;

import android.net.Uri;

public class song {
    String title;
    Uri uri;
    Uri artwork;
    int size;
    int duration;

    public String getTitle() {
        return title;
    }

    public Uri getUri() {
        return uri;
    }

    public Uri getArtwork() {
        return artwork;
    }

    public int getSize() {
        return size;
    }

    public int getDuration() {
        return duration;
    }


    public song(String title, Uri uri, Uri artwork, int size, int duration) {
        this.title = title;
        this.uri = uri;
        this.artwork = artwork;
        this.size = size;
        this.duration = duration;
    }


}
