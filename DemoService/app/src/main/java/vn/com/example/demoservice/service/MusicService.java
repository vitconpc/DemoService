package vn.com.example.demoservice.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.ArrayList;

import vn.com.example.demoservice.R;
import vn.com.example.demoservice.common.Constants;
import vn.com.example.demoservice.model.data.Song;
import vn.com.example.demoservice.view.PlayMusicActivity;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private MyBinder myBinder;
    private MediaPlayer mediaPlayer;
    private Song song;
    private ServiceCallback serviceCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        myBinder = new MyBinder();
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mediaPlayer.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null) {
            handlerIntent(intent);
        }
        return START_STICKY;
    }

    public void setCallback(ServiceCallback serviceCallback) {
        this.serviceCallback = serviceCallback;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void playSong() {
        String uri = song.getUri();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(uri);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            //DO NOTHING
        }
    }

    public boolean isPlay() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        serviceCallback.postTitle(song.getSongName() + Constants.CHAR_CONNECT + song.getAuthorName());
        mediaPlayer.start();
        postNotification();
    }

    private void handlerIntent(Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case Constants.ACTION_PLAY:
                if (isPlay()) {
                    serviceCallback.showToast(Constants.ACTION_PLAY);
                } else {
                    playSong();
                }
                break;
            case Constants.ACTION_PAUSE:
                if (isPlay()) {
                    stopSong();
                } else {
                    serviceCallback.showToast(Constants.ACTION_PAUSE);
                }
                break;
            default: {
                //DO NOTHING
            }
            break;

        }
    }

    private void postNotification() {
        Intent intent = new Intent(this, PlayMusicActivity.class);
        intent.putExtra(Constants.SONG,song);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent = new Intent(this, MusicService.class);
        playIntent.setAction(Constants.ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.getService(getApplicationContext(),
                0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent(this, MusicService.class);
        pauseIntent.setAction(Constants.ACTION_PAUSE);
        PendingIntent pausePendingIntent = PendingIntent.getService(getApplicationContext(),
                0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);

        remoteViews.setTextViewText(R.id.tv_song_name, song.getSongName() + Constants.CHAR_CONNECT + song.getAuthorName());

        remoteViews.setOnClickPendingIntent(R.id.image_pause, pausePendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.image_play, playPendingIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext()
                , Constants.CHANNEL_ID);

        builder.setContentIntent(pendingIntent)
                .setContent(remoteViews)
                .setSmallIcon(R.drawable.icon)
                .setPriority(1);

        Notification notification = builder.build();
        startForeground(Constants.ID_NOTIFICATION_SERVICE, notification);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playSong();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    public void stopSong() {
        mediaPlayer.pause();
    }

    public class MyBinder extends Binder {
        public MusicService getMusicService() {
            return MusicService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        stopSelf();
    }
}
