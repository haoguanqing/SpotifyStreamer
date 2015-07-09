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
        //view to be displayed in the result block
        View blockView;
        ViewHolder viewHolder = new ViewHolder();

        if (convertView!=null){
            viewHolder = (ViewHolder) convertView.getTag(R.id.top_track_blockbiew_tag);
            blockView = convertView;
        } else{
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //inflate the block
            blockView = inflater.inflate(res, null, false);
            //set the viewHolder
            viewHolder.imageView = (ImageView) blockView.findViewById(R.id.album_thumbnail);
            viewHolder.albumNameTextView = (TextView) blockView.findViewById(R.id.album_name);
            viewHolder.trackNameTextView = (TextView) blockView.findViewById(R.id.track_name);
            viewHolder.durationTextView = (TextView) blockView.findViewById(R.id.track_duration);

            blockView.setTag(R.id.top_track_blockbiew_tag, viewHolder);
        }


        Track currentTrack = trackList.get(position);

        //set images to ImageViews
        if (!currentTrack.album.images.isEmpty()){
            String imageUrl = currentTrack.album.images.get(0).url;
            Picasso.with(mContext).load(imageUrl).into( viewHolder.imageView);
        } else{
            Picasso.with(mContext).load(R.drawable.blank_cd).into(viewHolder.imageView);
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
        viewHolder.trackNameTextView.setText(trackName);

        String albumName = currentTrack.album.name;
        if (albumName.length()>27){
            albumName = albumName.split("\\(")[0];
            albumName = albumName.split(":")[0];
        }
        if (albumName.length()>27){
            albumName = albumName.substring(0,24)+"...";
        }
        viewHolder.albumNameTextView.setText(albumName);

        String duration = getFormattedDuration(currentTrack.duration_ms);
        viewHolder.durationTextView.setText(duration);


        //set different background colors for different blocks.
        // easier to extinguish
        if (position % 2==1){
            blockView.setBackgroundResource(R.color.customize_gray);
        }

        return blockView;
    }


    //parse long track_duration (192000) into formatted String (03:12)
    private String getFormattedDuration(long dur_ms){
        Long dur = dur_ms;
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

    private class ViewHolder {
        //hold a collection of views
        ImageView imageView;
        TextView trackNameTextView;
        TextView albumNameTextView;
        TextView durationTextView;
    }
}