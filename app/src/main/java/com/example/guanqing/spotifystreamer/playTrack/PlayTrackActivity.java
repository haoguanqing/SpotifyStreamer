package com.example.guanqing.spotifystreamer.playTrack;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.example.guanqing.spotifystreamer.R;

public class PlayTrackActivity extends ActionBarActivity{

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_play_track);

        //show dialog as fragment for the device
        if (savedInstanceState==null){
            String trackId = getIntent().getStringExtra(PlayTrackFragment.TRACK_ID_KEY);

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            PlayTrackFragment fragment = PlayTrackFragment.newInstance(trackId);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.play_track_container, fragment)
                    .addToBackStack(null).commit();
        }

        Log.i("PLAY TRACK", "PlayTrackActivity onCreate");
    }









/*    //implement the NoticeDialogListener interface
    @Override
    public void onPreviousButtonClick(android.support.v4.app.DialogFragment dialog){
    }
    @Override
    public void onPlayButtonClick(android.support.v4.app.DialogFragment dialog){
    }
    @Override
    public void onNextButtonClick(android.support.v4.app.DialogFragment dialog){
    }*/
}
