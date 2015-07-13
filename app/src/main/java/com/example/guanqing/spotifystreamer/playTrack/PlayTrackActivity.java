package com.example.guanqing.spotifystreamer.playTrack;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;

import com.example.guanqing.spotifystreamer.R;

public class PlayTrackActivity extends FragmentActivity{
    private boolean isTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_play_track);
        isTwoPane = getResources().getBoolean(R.bool.tablet_layout);
    }

/*    public void showDialog(){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        PlayTrackFragment fragment = new PlayTrackFragment();
        if (isTwoPane){
            //show fragment as a dialog on tablets
            fragment.show(fragmentManager, "dialog");
        } else {
            //show the fragment fullscreen on a device
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(R.id.play_track_fragment, fragment)
                    .addToBackStack(null).commit();
        }
    }*/







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
