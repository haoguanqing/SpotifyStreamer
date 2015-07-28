package com.example.guanqing.spotifystreamer.playTrack;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.example.guanqing.spotifystreamer.R;
import com.example.guanqing.spotifystreamer.service.Utility;
import com.example.guanqing.spotifystreamer.topTracks.TrackParcel;

import java.util.ArrayList;

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
            Bundle bundle = getIntent().getBundleExtra(PlayTrackFragment.TRACK_BUNDLE_KEY);
            ArrayList<TrackParcel> parcelList = bundle.getParcelableArrayList(PlayTrackFragment.TRACK_PARCEL_KEY);
            int position = bundle.getInt(PlayTrackFragment.TRACK_POSITION_KEY);
            PlayTrackFragment fragment = PlayTrackFragment.newInstance(Utility.getTrackList(parcelList), position);

            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.play_track_container, fragment)
                    .addToBackStack(null).commit();
        }

        Log.i(LOG_TAG, "HGQ: PlayTrackActivity_onCreate");
    }


}
