package com.example.guanqing.spotifystreamer.playTrack;

import android.support.v4.app.FragmentActivity;

import com.example.guanqing.spotifystreamer.R;


public class PlayTrackActivity extends FragmentActivity implements PlayTrackFragment.NoticeDialogListener {

    public void showDialog(){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        PlayTrackFragment fragment = new PlayTrackFragment();
        boolean isTwoPane = getResources().getBoolean(R.bool.tablet_layout);
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
    }




    //implement the NoticeDialogListener interface
    @Override
    public void onPreviousButtonClick(android.support.v4.app.DialogFragment dialog){

    }
    @Override
    public void onPlayButtonClick(android.support.v4.app.DialogFragment dialog){

    }
    @Override
    public void onNextButtonClick(android.support.v4.app.DialogFragment dialog){

    }
}
