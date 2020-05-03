package com.example.muzicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQ_PERMISSION = 100;
    private static final int PERMISSION_COUNT = 1;
    ListView musicList;
    String[] items;

    private boolean isPlayerInit;
   // MusicListAdapter adapter = new MusicListAdapter();
    public List<File> musicFiles= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicList = findViewById(R.id.list);

    }

    public void fillMusicList(){
        musicFiles = findSongs(Environment.getExternalStorageDirectory());
        items = new String[musicFiles.size()];

        for(int i=0; i<musicFiles.size(); i++){

            items[i] = musicFiles.get(i).getName().toString().replace(".mp3", "");

        }

            MusicListAdapter musicListAdapter = new MusicListAdapter(this, items);
            musicList.setAdapter(musicListAdapter);

        /*ArrayAdapter<String> songAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
        musicList.setAdapter(songAdapter);*/
    }

    public ArrayList<File> findSongs(File file){

        ArrayList<File> songlist = new ArrayList<>();
        File[] files = file.listFiles();

        for(File singleFile: files){

            if(singleFile.isDirectory() && !singleFile.isHidden()){
                songlist.addAll(findSongs(singleFile));
            }
            else {
                if(singleFile.getName().endsWith(".mp3")){
                    songlist.add(singleFile);
                }
            }

        }
        return songlist;
    }


    @SuppressLint("NewApi")
    public boolean isPermissionDenied(){
        for( int i= 0; i< PERMISSION_COUNT; i++ ){
            if(checkSelfPermission(PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED){
                return true;
            }

        }
        return false;
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(isPermissionDenied()){
            ((ActivityManager) (this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
            recreate();
        }
        else {
            onResume();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isPermissionDenied()){
            requestPermissions(PERMISSIONS, REQ_PERMISSION);
            return;
        }

        if(!isPlayerInit){
            fillMusicList();

           /* adapter.setData(musicFiles);
            musicList.setAdapter(adapter);*/
            isPlayerInit = true;
        }
    }
}
