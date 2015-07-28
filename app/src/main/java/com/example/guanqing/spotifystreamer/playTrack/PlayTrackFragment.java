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
import com.example.guanqing.spotifystreamer.searchArtists.SearchActivity;
import com.example.guanqing.spotifystreamer.service.PlayMediaService;
import com.example.guanqing.spotifystreamer.service.Utility;
import com.example.guanqing.spotifystreamer.topTracks.TrackParcel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;


/**
 * A dialog fragment containing a simple view.
 */
public class PlayTrackFragment extends android.support.v4.app.DialogFragment {
    //tag for debugging
    static final String LOG_TAG = PlayTrackFragment.class.getSimpleName();
    //key strings for bundles and intents
    public final static String TRACK_PARCEL_KEY = "TRACK_PARCEL_KEY";
    public final static String TRACK_BUNDLE_KEY = "TRACK_BUNDLE_KEY";
    public final static String TRACK_LIST_KEY = "TRACK_LIST_KEY";
    public final static String TRACK_POSITION_KEY = "TRACK_POSITION_KEY";

    //show the playing status
    private boolean mIsPlaying = false;
    private boolean mIsStopped = true;
    //use view holder to set view components
    private ViewHolder viewHolder = new ViewHolder();

    private Context mContext;
    private ArrayList<Track> mTrackList = new ArrayList<>();
    private int mPosition = -1;

    public PlayTrackFragment() {}

    //create a new instance of the fragment
    public static PlayTrackFragment newInstance(ArrayList<Track> trackList, int position){
        PlayTrackFragment frag = new PlayTrackFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(TRACK_PARCEL_KEY, Utility.getTrackParcelList(trackList));
        args.putInt(TRACK_POSITION_KEY, position);
        frag.setArguments(args);

        Log.i(LOG_TAG, "HGQ: PlayTrackFragment_newInstance created");
        return frag;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState==null){
            Bundle args = getArguments();
            if (args!=null){
                mTrackList.clear();
                ArrayList<TrackParcel> parcelList = args.getParcelableArrayList(TRACK_PARCEL_KEY);
                mTrackList = Utility.getTrackList(parcelList);
                mPosition = args.getInt(TRACK_POSITION_KEY);
            }
            mContext = getActivity();
            Log.i(LOG_TAG, "HGQ: PlayTrackFragment_onCreate get track position = " + mPosition);
            Log.i(LOG_TAG, "HGQ: PlayTrackFragment_onCreate get tracklist as follow:\n" + SearchActivity.trackListString(mTrackList));
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

        updateTrackInfo();
        updatePlayButton();

        //
        viewHolder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIsStopped){
                    PlayMediaService.playTrack(mContext, mPosition);
                    mIsPlaying = true;
                    mIsStopped = false;
                    updatePlayButton();
                } else {
                    if (mIsPlaying) {
                        PlayMediaService.pauseTrack(mContext);
                        mIsPlaying = !mIsPlaying;
                    } else {
                        PlayMediaService.resumeTrack(mContext);
                        mIsPlaying = !mIsPlaying;
                    }
                    updatePlayButton();
                }
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
                if(mIsStopped){
                    PlayMediaService.playTrack(mContext, mPosition);

                    mIsStopped = false;
                    updatePlayButton();
                }else{
                    PlayMediaService.nextTrack(mContext);
                }
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
                if (mPosition==mTrackList.size()-1){
                    mPosition = 0;
                }else{
                    mPosition--;
                }
                //play the previous track
                if(mIsStopped){
                    PlayMediaService.playTrack(mContext, mPosition);
                    mIsStopped = false;
                    updatePlayButton();
                }else{
                    PlayMediaService.previousTrack(mContext);
                }
                //update view
                mIsPlaying = true;
                updateTrackInfo();
                updatePlayButton();
            }
        });

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
        viewHolder.totalTime.setText(Utility.getFormattedDuration(currentTrack.duration_ms));
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

}
