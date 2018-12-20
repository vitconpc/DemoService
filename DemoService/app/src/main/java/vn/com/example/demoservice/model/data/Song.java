package vn.com.example.demoservice.model.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    private String songName;
    private String authorName;
    private String uri;

    public Song(String songName, String authorName, String uri) {
        this.songName = songName;
        this.authorName = authorName;
        this.uri = uri;
    }

    protected Song(Parcel in) {
        songName = in.readString();
        authorName = in.readString();
        uri = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(songName);
        dest.writeString(authorName);
        dest.writeString(uri);
    }
}
