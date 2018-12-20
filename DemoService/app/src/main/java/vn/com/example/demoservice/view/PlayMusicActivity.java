package vn.com.example.demoservice.view;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import vn.com.example.demoservice.R;
import vn.com.example.demoservice.common.Constants;
import vn.com.example.demoservice.model.data.Song;
import vn.com.example.demoservice.service.MusicService;
import vn.com.example.demoservice.service.ServiceCallback;

public class PlayMusicActivity extends AppCompatActivity implements ServiceCallback {

    private boolean isBoundService;
    private ServiceConnection serviceConnection;
    private MusicService musicService;
    private Song song;
    private TextView tvname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        initView();
        initData();
    }

    private void initView() {
        tvname = findViewById(R.id.tv_name);
    }

    private void initData(){
        isBoundService = false;
        connectService();
    }

    private void connectService() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                musicService = ((MusicService.MyBinder) service).getMusicService();
                isBoundService = true;
                musicService.setCallback(PlayMusicActivity.this);
                getData();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                //DO NOTHING
            }
        };

        Intent intent = new Intent(PlayMusicActivity.this, MusicService.class);
//        intent.setAction(Constants.ACTION_BIND_SERVICE);
        startService(intent);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);

    }

    private void getData() {
        song = getIntent().getParcelableExtra(Constants.SONG);
        musicService.setSong(song);
        musicService.playSong();
    }

    @Override
    public void postTitle(String title) {
        tvname.setText(title);
    }

    @Override
    public void showToast(String data) {
        switch (data){
            case Constants.ACTION_PAUSE:{
                Toast.makeText(this, getString(R.string.music_is_stopped), Toast.LENGTH_SHORT).show();
            }
            break;
            case Constants.ACTION_PLAY:{
                Toast.makeText(this, getString(R.string.music_is_playing), Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    public void playMusic(View view) {
        if (!isBoundService){
            Toast.makeText(this, getString(R.string.not_connect_to_service), Toast.LENGTH_SHORT).show();
            return;
        }
        if (musicService.isPlay()){
            Toast.makeText(this, getString(R.string.music_is_playing), Toast.LENGTH_SHORT).show();
            return;
        }
        musicService.playSong();
    }

    public void pauseMusic(View view) {
        if (!isBoundService){
            Toast.makeText(this, getString(R.string.not_connect_to_service), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!musicService.isPlay()){
            Toast.makeText(this, getString(R.string.music_is_stopped), Toast.LENGTH_SHORT).show();
            return;
        }
        musicService.stopSong();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!musicService.isPlay()){
            Intent intent = new Intent(this,MusicService.class);
            stopService(intent);
        }
    }
}
