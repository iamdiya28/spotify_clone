package com.example.spotify_clone;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    com.example.spotify_clone.songAdapter songAdapter;
    List<song> allSongs=new ArrayList<>();
    ActivityResultLauncher<String>StoragePermissionLauncher;
    final String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.app_name));

        recyclerView=findViewById(R.id.recyclerview);
        StoragePermissionLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(),granted->{

            if(granted){
                fetchSongs();
            }
            else{
                userResponse();
            }
        });
        StoragePermissionLauncher.launch(permission);

    }

    private void userResponse() {
        if(ContextCompat.checkSelfPermission(this,permission)== PackageManager.PERMISSION_GRANTED){
            fetchSongs();
        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(shouldShowRequestPermissionRationale(permission)){
                new AlertDialog.Builder(this).setTitle("Requesting Permsiion")
                        .setMessage("Allow us to fetch songs on your device")
                        .setPositiveButton("allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StoragePermissionLauncher.launch(permission);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).show();
            }
        }
        else{
            Toast.makeText(this, "You cancelled", Toast.LENGTH_SHORT).show();
        }

    }

    private void fetchSongs(){
        List<song> songs= new ArrayList<>();
        Uri mediaStoreUri;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            mediaStoreUri= MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        }
        else{
            mediaStoreUri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection= new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM_ID,
        };

        String sortOrder= MediaStore.Audio.Media.DATE_ADDED + "DESC";

        try(Cursor cursor= getContentResolver().query(mediaStoreUri,projection,null, null, sortOrder)){
            int idColumn= cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int nameColumn= cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int durationColumn= cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int sizeColumn= cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            int albumIdColumn= cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);

            while(cursor.moveToNext()){
                long id= cursor.getLong(idColumn);
                String name= cursor.getString(nameColumn);
                int duration= cursor.getInt(durationColumn);
                int size= cursor.getInt(sizeColumn);
                long albumId= cursor.getLong(albumIdColumn);

                Uri uri= ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                Uri albumArtworkUri= ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),albumId);

                name= name.substring(0, name.lastIndexOf("."));

                song song= new song(name, uri, albumArtworkUri, size, duration);

                songs.add(song);
            }

            showSongs(songs);
        }

    }

    private void showSongs(List<song> songs) {
        if(songs.size()==0){
            Toast.makeText(this, "No songs", Toast.LENGTH_SHORT).show();
            return;
        }

        allSongs.clear();
        allSongs.addAll(songs);

        String title= getResources().getString(R.string.app_name) + "-" + songs.size();
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        song song;
    }
}