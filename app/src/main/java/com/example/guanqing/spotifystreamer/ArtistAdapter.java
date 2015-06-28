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

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by Guanqing on 2015/6/23.
 */
public class ArtistAdapter extends ArrayAdapter<Artist>{
    private final String LOG_TAG = ArtistAdapter.class.getSimpleName();
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
    public View getView(int position, View view, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View blockView = inflater.inflate(R.layout.fragment_main_block, null);
        ImageView imageView = (ImageView) blockView.findViewById(R.id.artist_thumbnail);
        TextView textView = (TextView) blockView.findViewById(R.id.artist_name);


        Artist currentArtist = getItem(position);

        //set images to ImageViews
        if (!currentArtist.images.isEmpty()){
            String imageUrl = currentArtist.images.get(0).url;
            Picasso.with(mContext).load(imageUrl).into(imageView);
        }

        //set text
        String artistName = currentArtist.name;
        textView.setText(artistName);


        //set different background colors for different blocks.
        // easier to extinguish
        if (position % 2==1){
            blockView.setBackgroundResource(R.color.background_material_dark);
        }

        return blockView;
    }


}
