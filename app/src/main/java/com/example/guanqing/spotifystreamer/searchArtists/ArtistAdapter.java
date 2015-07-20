package com.example.guanqing.spotifystreamer.searchArtists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guanqing.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by Guanqing on 2015/6/23.
 */
public class ArtistAdapter extends ArrayAdapter<Artist>{
    private final Context mContext;
    private ArrayList<Artist> artistList = new ArrayList<>();
    private int layoutResource;

    public ArtistAdapter(Context context, int res, ArrayList<Artist> artistList){
        super(context, res, artistList);
        this.mContext = context;
        this.layoutResource = res;
        this.artistList = artistList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View blockView;
        ViewHolder viewHolder = new ViewHolder();

        if (convertView!=null){
            viewHolder = (ViewHolder) convertView.getTag(R.id.artist_blockview_tag);
            blockView = convertView;
        }else{
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //inflate the view
            blockView = inflater.inflate(layoutResource, null);
            viewHolder.imageView = (ImageView) blockView.findViewById(R.id.artist_thumbnail);
            viewHolder.textView = (TextView) blockView.findViewById(R.id.artist_name);

            //set tag for the blockView
            blockView.setTag(R.id.artist_blockview_tag, viewHolder);
        }

        //get the artist for this block
        Artist currentArtist = artistList.get(position);

        //set images to ImageViews
        if (!currentArtist.images.isEmpty()){
            String imageUrl = currentArtist.images.get(1).url;
            Picasso.with(mContext).load(imageUrl).into(viewHolder.imageView);
        } else{
            Picasso.with(mContext).load(R.drawable.blank_cd).into(viewHolder.imageView);
        }

        //set text
        String artistName = currentArtist.name;
        viewHolder.textView.setText(artistName);

        //set different background colors for different blocks.
        // easier to extinguish
        if (position % 2 == 1){
            blockView.setBackgroundResource(R.color.customize_gray);
        }else {
            blockView.setBackgroundResource(R.color.primary_dark_material_dark);
        }

        return blockView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }


}
