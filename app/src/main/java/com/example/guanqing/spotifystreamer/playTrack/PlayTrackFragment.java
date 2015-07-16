package com.example.guanqing.spotifystreamer.playTrack;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.guanqing.spotifystreamer.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlayTrackFragment extends android.support.v4.app.DialogFragment {
    public final static String PARCEL_KEY = "TRACK_PARCEL_KEY";
    public final static String TRACK_ID_KEY = "TRACK_ID_KEY";

    public PlayTrackFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_play_track, container, false);
        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }

    public static PlayTrackFragment newInstance(String trackId){
        PlayTrackFragment frag = new PlayTrackFragment();
        Bundle args = new Bundle();
        args.putString(TRACK_ID_KEY, trackId);
        frag.setArguments(args);
        return frag;
    }


//    public interface NoticeDialogListener {
//        public void onPreviousButtonClick(android.support.v4.app.DialogFragment dialogFragment);
//        public void onPlayButtonClick(android.support.v4.app.DialogFragment dialogFragment);
//        public void onNextButtonClick(android.support.v4.app.DialogFragment dialogFragment);
//    }
}
