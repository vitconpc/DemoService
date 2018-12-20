package vn.com.example.demoservice.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.com.example.demoservice.R;
import vn.com.example.demoservice.common.Constants;
import vn.com.example.demoservice.common.adapter.SongAdapter;
import vn.com.example.demoservice.common.asynctask.GetSongAsynctask;
import vn.com.example.demoservice.common.asynctask.GetSongCallback;
import vn.com.example.demoservice.model.data.Song;
import vn.com.example.demoservice.model.data.SongItemCallback;

public class ListMusicActivity extends AppCompatActivity implements SongItemCallback, GetSongCallback {

    private static final int REQUESTCODE = 100;
    private List<Song> songs;
    private SongAdapter songAdapter;
    private RecyclerView rvSongs;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_music);
        initview();
        initData();
        checkPermission();
        getData();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUESTCODE);
        }else{
            getData();
        }
    }


    private void getData() {
        new GetSongAsynctask(this, this).execute();
    }

    private void initview() {
        rvSongs = findViewById(R.id.rv_musics);
    }

    private void initData() {
        songs = new ArrayList<>();
        songAdapter = new SongAdapter(songs, this, this);
        rvSongs.setHasFixedSize(true);
        rvSongs.setLayoutManager(new LinearLayoutManager(this));
        rvSongs.setAdapter(songAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUESTCODE && grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUESTCODE);
        }else{
            getData();
        }
    }

    @Override
    public void SongItemClick(Song song) {
        Intent intent = new Intent(this,PlayMusicActivity.class);
        intent.putExtra(Constants.SONG,song);
        startActivity(intent);
    }

    @Override
    public void getItem(Song song) {
        songs.add(song);
        songAdapter.notifyDataSetChanged();
    }

    @Override
    public void getAllItem(List<Song> songs) {
        this.songs.clear();
        this.songs.addAll(songs);
        songAdapter.notifyDataSetChanged();
    }
}
