package com.example.ahmed.topartist.model;

import java.io.Serializable;

/**
 * Created by ahmed on 9/10/2016.
 */
public class Artist  implements Serializable{
    private static final long id = 1L;
    private String artistName;
    private String playCount;
    private String listeners;
    private String artistIamge;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getPlayCount() {
        return playCount;
    }

    public void setPlayCount(String playCount) {
        this.playCount = playCount;
    }

    public String getListeners() {
        return listeners;
    }

    public void setListeners(String listeners) {
        this.listeners = listeners;
    }

    public String getArtistIamge() {
        return artistIamge;
    }

    public void setArtistIamge(String artistIamge) {
        this.artistIamge = artistIamge;
    }



}
