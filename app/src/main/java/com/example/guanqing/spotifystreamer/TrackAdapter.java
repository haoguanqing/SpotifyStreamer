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
        //make sure the names are not too long
        String trackName = currentTrack.name;
        if (trackName.length()>37){
            trackName = trackName.split("\\(")[0];
            trackName = trackName.split("-")[0];
        }
        if (trackName.length()>34){
            trackName = trackName.substring(0,34)+"...";
        }
        trackNameTextView.setText(trackName);

        String albumName = currentTrack.album.name;
        if (albumName.length()>27){
            albumName = albumName.split("\\(")[0];
            albumName = albumName.split(":")[0];
        }
        if (albumName.length()>27){
            albumName = albumName.substring(0,24)+"...";
        }
        albumNameTextView.setText(albumName);

        String duration = getFormattedDuration(currentTrack.duration_ms);
        durationTextView.setText(duration);


        //set different background colors for different blocks.
        // easier to extinguish
        if (position % 2==1){
            blockView.setBackgroundResource(R.color.customize_gray);
        }

        return blockView;
    }

    public String getFormattedDuration(long dur_ms){
        //parse long track_duration into formatted String
        Long dur = new Long(dur_ms);
        int totalSec = dur.intValue()/1000;
        int min = totalSec/60;
        int sec = totalSec - min * 60;
        String minute = min+"";
        String second = sec+"";

        if (min==0){
            minute = "00";
        } else if (min<10){
            minute = "0"+min;
        }

        if(sec==0) {
            second = "00";
        }else if (sec<10){
                second = "0"+sec;
        }
        return minute+":"+second;
    }
}