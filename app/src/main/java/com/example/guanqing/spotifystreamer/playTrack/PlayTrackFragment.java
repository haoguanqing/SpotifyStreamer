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

    public PlayTrackFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_track, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public interface NoticeDialogListener {
        public void onPreviousButtonClick(android.support.v4.app.DialogFragment dialogFragment);
        public void onPlayButtonClick(android.support.v4.app.DialogFragment dialogFragment);
        public void onNextButtonClick(android.support.v4.app.DialogFragment dialogFragment);
    }
}
