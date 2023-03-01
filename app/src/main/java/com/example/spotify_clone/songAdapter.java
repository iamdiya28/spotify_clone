package com.example.spotify_clone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class songAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<song> songs;
    public songAdapter(Context context, List<song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.song_row_item,parent,false);

        return new songviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        song song= songs.get(position);
        songviewholder songviewholder= (songviewholder) holder;
        songviewholder.titleholder.setText(song.getTitle());
        songviewholder.durationholder.setText(String.valueOf(song.getDuration()));
        songviewholder.sizeholder.setText(String.valueOf(song.getSize()));
        Uri artworkuri=song.getArtwork();
        if(artworkuri!= null){
            songviewholder.artviewholder.setImageURI(artworkuri);
            if(songviewholder.artviewholder.getDrawable()==null){
                songviewholder.artviewholder.setImageResource(R.drawable.img);
            }
        }
        songviewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,song.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    public static class songviewholder extends RecyclerView.ViewHolder {

        ImageView artviewholder;
        TextView titleholder,durationholder,sizeholder;

        public songviewholder(@NonNull View itemView) {

            super(itemView);
            artviewholder=itemView.findViewById(R.id.artwork);
            titleholder=itemView.findViewById(R.id.titleview);
            durationholder=itemView.findViewById(R.id.durationview);
            sizeholder=itemView.findViewById(R.id.sizeview);

        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void filtersongs(List<song> filteredlist){
        songs=filteredlist;
        notifyDataSetChanged();
    }
}
