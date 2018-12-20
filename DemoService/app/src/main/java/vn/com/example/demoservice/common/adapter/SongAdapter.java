package vn.com.example.demoservice.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.com.example.demoservice.R;
import vn.com.example.demoservice.model.data.Song;
import vn.com.example.demoservice.model.data.SongItemCallback;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<Song> songs;
    private Context context;
    private SongItemCallback itemCallback;

    public SongAdapter(List<Song> songs, Context context, SongItemCallback itemCallback) {
        this.songs = songs;
        this.context = context;
        this.itemCallback = itemCallback;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SongViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_item_song, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder songViewHolder, int i) {
        songViewHolder.bindData(songs.get(i));
    }

    @Override
    public int getItemCount() {
        return songs == null ? 0 : songs.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvAuthor;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            initView();
            setEvent(itemView);
        }

        private void setEvent(View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemCallback.SongItemClick(songs.get(getAdapterPosition()));
                }
            });
        }

        private void initView() {
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }

        public void bindData(Song song) {
            tvTitle.setText(song.getSongName());
            tvAuthor.setText(song.getAuthorName());
        }
    }
}
