package com.example.guanqing.spotifystreamer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Guanqing on 2015/6/23.
 */
public class ArtistAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] imageUrl;
    private final String[] artistName;
    public ArtistAdapter(Activity context, String[] imageUrl, String[] artistName){
        super(context, R.layout.fragment_main_block, artistName);
        this.context = context;
        this.imageUrl = imageUrl;
        this.artistName = artistName;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View blockView = inflater.inflate(R.layout.fragment_main_block, null, true);
        ImageView imageView = (ImageView) blockView.findViewById(R.id.artist_thumbnail);
        TextView textView = (TextView) blockView.findViewById(R.id.artist_name);

        //three ways to set images to ImageViews
        Picasso.with(context).load(imageUrl[position]).into(imageView);
        //Picasso.with(context).load(R.drawable.cover).into(imageView);
        //imageView.setImageResource(R.drawable.cover);

        //set text
        textView.setText(artistName[position]);

        //set background colors with different shades of gray for different blocks.
        // easier to extinguish
        if (position % 4==0){
            blockView.setBackgroundResource(R.color.gray);
        }else if(position%4==2){
            blockView.setBackgroundResource(R.color.gray2);
        }

        return blockView;
    }
}
