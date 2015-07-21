package com.example.guanqing.spotifystreamer.playTrack;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.guanqing.spotifystreamer.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlayTrackFragment extends android.support.v4.app.DialogFragment {
    public final static String PARCEL_KEY = "TRACK_PARCEL_KEY";
    public final static String TRACK_ID_KEY = "TRACK_ID_KEY";

    public PlayTrackFragment() {
    }

    public static PlayTrackFragment newInstance(String trackId){
        PlayTrackFragment frag = new PlayTrackFragment();
        Bundle args = new Bundle();
        args.putString(TRACK_ID_KEY, trackId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_play_track, container, false);
        Bundle args = getArguments();
        String trackId = "";
        if (args!=null){
            trackId = args.getString(TRACK_ID_KEY);
        }
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.artistName = (TextView) rootView.findViewById(R.id.artist_name);
        viewHolder.albumName = (TextView) rootView.findViewById(R.id.album_name);
        viewHolder.trackThumbnail = (ImageView) rootView.findViewById(R.id.track_thumbnail);
        viewHolder.trackName = (TextView) rootView.findViewById(R.id.track_name);
        viewHolder.seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        viewHolder.currentTime = (TextView) rootView.findViewById(R.id.current_time);
        viewHolder.totalTime = (TextView) rootView.findViewById(R.id.total_time);
        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() == null)
            return;
        if (getResources().getConfiguration().orientation==1){
            getDialog().getWindow().setLayout(386, 556);
        }else{
            getDialog().getWindow().setLayout(556, 394);
        }
    }

    //    public interface NoticeDialogListener {
//        public void onPreviousButtonClick(android.support.v4.app.DialogFragment dialogFragment);
//        public void onPlayButtonClick(android.support.v4.app.DialogFragment dialogFragment);
//        public void onNextButtonClick(android.support.v4.app.DialogFragment dialogFragment);
//    }

    private class ViewHolder {
        //hold a collection of views
        ImageView trackThumbnail;
        TextView artistName;
        TextView albumName;
        TextView trackName;
        SeekBar seekBar;
        TextView currentTime;
        TextView totalTime;
    }
}
