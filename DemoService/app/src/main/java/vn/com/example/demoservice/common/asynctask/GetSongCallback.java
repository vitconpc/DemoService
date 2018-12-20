package vn.com.example.demoservice.common.asynctask;

import java.util.List;

import vn.com.example.demoservice.model.data.Song;

public interface GetSongCallback {
    void getItem(Song song);
    void getAllItem(List<Song> songs);
}
