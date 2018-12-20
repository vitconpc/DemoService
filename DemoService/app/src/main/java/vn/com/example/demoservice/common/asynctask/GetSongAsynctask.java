package vn.com.example.demoservice.common.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import vn.com.example.demoservice.model.data.Song;

public class GetSongAsynctask extends AsyncTask<Void, Song, List<Song>> {

    private static final String NOT_ZERO = "!=0";
    private static final int POSITION_ONE = 1;
    private static final int POSITION_TWO = 2;
    private static final int POSITION_THREE = 3;
    private List<Song> songs;
    private GetSongCallback getSongCallback;
    private Context context;

    public GetSongAsynctask(GetSongCallback getSongCallback, Context context) {
        this.getSongCallback = getSongCallback;
        this.context = context;
        this.songs = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Song> doInBackground(Void... voids) {
        String selection = MediaStore.Audio.Media.IS_MUSIC + NOT_ZERO;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);
        while(cursor.moveToNext()) {
            Song song = new Song(cursor.getString(POSITION_TWO), cursor.getString(POSITION_ONE),cursor.getString(POSITION_THREE));
            publishProgress(song);
            songs.add(song);
        }
        return songs;
    }

    @Override
    protected void onProgressUpdate(Song... values) {
        getSongCallback.getItem(values[0]);
    }

    @Override
    protected void onPostExecute(List<Song> songs) {
        super.onPostExecute(songs);
        getSongCallback.getAllItem(songs);
    }
}
