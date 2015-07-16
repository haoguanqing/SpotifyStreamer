package com.example.guanqing.spotifystreamer.playTrack;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.example.guanqing.spotifystreamer.R;

import kaaes.spotify.webapi.android.models.Track;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlayTrackFragment extends android.support.v4.app.DialogFragment {
    final static String PARCEL_KEY = "TRACK_PARCEL_KEY";

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
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        return dialog;

        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

    public static PlayTrackFragment newInstance(Track track){
        PlayTrackFragment frag = new PlayTrackFragment();
        Bundle args = new Bundle();
        String trackId = track.id;
        args.putString(PARCEL_KEY, trackId);
        frag.setArguments(args);
        return frag;
    }


//    public interface NoticeDialogListener {
//        public void onPreviousButtonClick(android.support.v4.app.DialogFragment dialogFragment);
//        public void onPlayButtonClick(android.support.v4.app.DialogFragment dialogFragment);
//        public void onNextButtonClick(android.support.v4.app.DialogFragment dialogFragment);
//    }
}
