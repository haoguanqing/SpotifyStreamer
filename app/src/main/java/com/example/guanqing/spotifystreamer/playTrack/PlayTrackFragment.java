package com.example.guanqing.spotifystreamer.playTrack;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.guanqing.spotifystreamer.R;
import com.example.guanqing.spotifystreamer.service.PlayMediaService;
import com.example.guanqing.spotifystreamer.service.TrackProgressEvent;
import com.example.guanqing.spotifystreamer.service.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.models.Track;


/**
 * A dialog fragment containing a simple view.
 */
public class PlayTrackFragment extends android.support.v4.app.DialogFragment {
    //tag for debugging
    static final String LOG_TAG = PlayTrackFragment.class.getSimpleName();
    //key strings for bundles and intents
    public final static String TRACK_JSON_STRING_KEY = "TRACK_JSON_STRING_KEY";
    public final static String TRACK_LIST_KEY = "TRACK_LIST_KEY";
    public final static String TRACK_POSITION_KEY = "TRACK_POSITION_KEY";
    //show the playing status
    private boolean mIsPlaying;
    //use view holder to set view components
    private ViewHolder viewHolder;
    //define context and data storage variables
    private Context mContext;
    private ArrayList<Track> mTrackList;
    private int mPosition;

    public PlayTrackFragment() {}

    //create a new instance of the fragment
    public static PlayTrackFragment newInstance(ArrayList<Track> trackList, int position){
        PlayTrackFragment frag = new PlayTrackFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(TRACK_JSON_STRING_KEY, Utility.getJsonStringListFromTracks(trackList));
        args.putInt(TRACK_POSITION_KEY, position);
        frag.setArguments(args);

        Log.i(LOG_TAG, "HGQ: PlayTrackFragment_newInstance created");
        return frag;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current state
        ArrayList<String> jsonList = Utility.getJsonStringListFromTracks(mTrackList);
        savedInstanceState.putStringArrayList(TRACK_JSON_STRING_KEY, jsonList);
        savedInstanceState.putInt(TRACK_POSITION_KEY, mPosition);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize variables
        mIsPlaying = false;
        viewHolder = new ViewHolder();
        mContext = getActivity();

        if (savedInstanceState==null){
            Bundle args = getArguments();
            ArrayList<String> stringList = args.getStringArrayList(TRACK_JSON_STRING_KEY);
            mTrackList = Utility.getTrackListFromGson(stringList);
            mPosition = args.getInt(TRACK_POSITION_KEY);
            Log.i(LOG_TAG, "HGQ: PlayTrackFragment_onCreate get track position = " + mPosition);
        }else{
            //retrieve data from saved instance
            ArrayList<String> jsonList = savedInstanceState.getStringArrayList(TRACK_JSON_STRING_KEY);
            mTrackList = Utility.getTrackListFromGson(jsonList);
            mPosition = savedInstanceState.getInt(TRACK_POSITION_KEY);
            savedInstanceState.clear();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_play_track, container, false);

        //initialize viewHolder
        viewHolder.artistName = (TextView) rootView.findViewById(R.id.artist_name);
        viewHolder.albumName = (TextView) rootView.findViewById(R.id.album_name);
        viewHolder.trackThumbnail = (ImageView) rootView.findViewById(R.id.track_thumbnail);
        viewHolder.trackName = (TextView) rootView.findViewById(R.id.track_name);
        viewHolder.seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        viewHolder.currentTime = (TextView) rootView.findViewById(R.id.current_time);
        viewHolder.totalTime = (TextView) rootView.findViewById(R.id.total_time);
        viewHolder.playButton = (ImageButton) rootView.findViewById(R.id.playButton);
        viewHolder.nextButton = (ImageButton) rootView.findViewById(R.id.nextButton);
        viewHolder.prevButton = (ImageButton) rootView.findViewById(R.id.previousButton);

        //play track on create view
        if (mIsPlaying==false){
            PlayMediaService.playTrack(mContext, mPosition);
            mIsPlaying = true;
            updateTrackInfo();
            updatePlayButton();
        }

        //set onClickListeners for the buttons
        viewHolder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsPlaying) {
                    PlayMediaService.pauseTrack(mContext);
                } else {
                    PlayMediaService.resumeTrack(mContext);
                }
                mIsPlaying = !mIsPlaying;
                updatePlayButton();
            }
        });

        viewHolder.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update position
                if (mPosition==mTrackList.size()-1){
                    mPosition = 0;
                }else{
                    mPosition++;
                }
                //play the next track
                PlayMediaService.nextTrack(mContext);
                //update view
                mIsPlaying = true;
                updatePlayButton();
                updateTrackInfo();
            }
        });

        viewHolder.prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update position
                if (mPosition == 0) {
                    mPosition = mTrackList.size() - 1;
                } else {
                    mPosition--;
                }
                //play the previous track
                PlayMediaService.previousTrack(mContext);

                //update view
                mIsPlaying = true;
                updateTrackInfo();
                updatePlayButton();
            }
        });

        viewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    PlayMediaService.setTrackProgress(mContext, progress);
                    viewHolder.seekBar.setProgress(progress);
                    viewHolder.currentTime.setText(Utility.getFormattedDuration(progress*1000));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        EventBus.getDefault().register(this);
        return rootView;
    }

    private void updatePlayButton(){
        if (mIsPlaying){
            viewHolder.playButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
        }else{
            viewHolder.playButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
        }
    }

    private void updateTrackInfo(){
        //select track
        Track currentTrack = mTrackList.get(mPosition);
        //set track info
        viewHolder.albumName.setText(currentTrack.album.name);
        viewHolder.artistName.setText(currentTrack.artists.get(0).name);
        viewHolder.trackName.setText(currentTrack.name);
        viewHolder.seekBar.setProgress(0);
        viewHolder.seekBar.setMax(30);
        viewHolder.totalTime.setText(Utility.getFormattedDuration(30000));
        //set track thumbnail
        if (!currentTrack.album.images.isEmpty()){
            String imageUrl;
            //use small image if possible
            int len = currentTrack.album.images.size()-1;
            if(len>=2){
                imageUrl = currentTrack.album.images.get(1).url;
            }else{
                imageUrl = currentTrack.album.images.get(len).url;
            }
            Picasso.with(mContext).load(imageUrl).into(viewHolder.trackThumbnail);
        } else{
            Picasso.with(mContext).load(R.drawable.blank_cd).into(viewHolder.trackThumbnail);
        }
    }

    //handle event posted in the service
    public void onEventMainThread(TrackProgressEvent event){
        int progress = event.getCurrentProgress();
        viewHolder.currentTime.setText(Utility.getFormattedDuration(progress));
        viewHolder.seekBar.setProgress(progress/1000);
        Log.i(LOG_TAG, "HGQ: on event main thread " + Utility.getFormattedDuration(progress));
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
            getDialog().getWindow().setLayout(386, 560);
        }else{
            getDialog().getWindow().setLayout(556, 394);
        }
    }

    private class ViewHolder {
        //hold a collection of views
        ImageView trackThumbnail;
        TextView artistName;
        TextView albumName;
        TextView trackName;
        SeekBar seekBar;
        TextView currentTime;
        TextView totalTime;
        ImageButton prevButton;
        ImageButton playButton;
        ImageButton nextButton;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
