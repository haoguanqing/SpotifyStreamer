package com.example.guanqing.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Guanqing on 2015/6/28.
 */
public class TrackAdapter extends ArrayAdapter{
    final Context mContext;
    final int res;
    final ArrayList<Track> trackList;
    public TrackAdapter(Context context, int res, ArrayList<Track> trackList){
        super(context, res, trackList);
        this.mContext = context;
        this.res = res;
        this.trackList = trackList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View blockView = inflater.inflate(res, null);
        ImageView imageView = (ImageView) blockView.findViewById(R.id.album_thumbnail);
        TextView albumNameTextView = (TextView) blockView.findViewById(R.id.album_name);
        TextView trackNameTextView = (TextView) blockView.findViewById(R.id.track_name);
        TextView durationTextView = (TextView) blockView.findViewById(R.id.track_duration);

        Track currentTrack = trackList.get(position);

        //set images to ImageViews
        if (!currentTrack.album.images.isEmpty()){
            String imageUrl = currentTrack.album.images.get(0).url;
            Picasso.with(mContext).load(imageUrl).into(imageView);
        } else{
            Picasso.with(mContext).load(R.drawable.blank_cd).into(imageView);
        }

        //set texts
        String trackName = currentTrack.name;
        trackNameTextView.setText(trackName);

        String albumName = currentTrack.album.name;
        albumNameTextView.setText(albumName);

        String duration = Long.toString(currentTrack.duration_ms);
        durationTextView.setText(duration);


        //set different background colors for different blocks.
        // easier to extinguish
        if (position % 2==1){
            blockView.setBackgroundResource(R.color.background_material_dark);
        }

        return blockView;
    }
}