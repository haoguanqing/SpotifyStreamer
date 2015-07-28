package com.example.guanqing.spotifystreamer.playTrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.example.guanqing.spotifystreamer.R;
import com.example.guanqing.spotifystreamer.service.Utility;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;

public class PlayTrackActivity extends ActionBarActivity{
    //tag for debugging
    static final String LOG_TAG = PlayTrackFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_track);
        getSupportActionBar().hide();

        //show dialog as fragment for the device
        //TODO: modify the data passed by intent
        if (savedInstanceState==null){
            Intent intent = getIntent();
            ArrayList<String> jsonList = intent.getStringArrayListExtra(PlayTrackFragment.TRACK_LIST_KEY);
            ArrayList<Track> trackList  = Utility.getTrackListFromGson(jsonList);
            int position = intent.getIntExtra(PlayTrackFragment.TRACK_POSITION_KEY, 0);
            PlayTrackFragment fragment = PlayTrackFragment.newInstance(trackList, position);

            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.play_track_container, fragment)
                    .addToBackStack(null).commit();
        }

    }


}
