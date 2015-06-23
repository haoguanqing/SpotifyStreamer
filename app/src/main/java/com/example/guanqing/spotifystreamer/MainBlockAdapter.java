package com.example.guanqing.spotifystreamer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Guanqing on 2015/6/23.
 */
public class MainBlockAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] imageUrl;
    private final String[] artistName;
    public MainBlockAdapter(Activity context, String[] imageUrl, String[] artistName){
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

        //Picasso.with(context).load(imageUrl[position]).into(imageView);
        imageView.setImageResource(R.drawable.cover);
        textView.setText(artistName[position]);

        if (position%4==0){
            blockView.setBackgroundResource(R.color.gray);
        }else if(position%4==2){
            blockView.setBackgroundResource(R.color.gray2);
        }

        return blockView;
    }
}
