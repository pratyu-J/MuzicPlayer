package com.example.muzicplayer;

import android.widget.ImageView;

public class SongClass {
    String songName;

    public SongClass() {
    }

    public SongClass(String songName) {
        this.songName = songName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

}
