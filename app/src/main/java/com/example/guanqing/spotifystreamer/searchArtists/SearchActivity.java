package com.example.guanqing.spotifystreamer.searchArtists;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.guanqing.spotifystreamer.R;
import com.example.guanqing.spotifystreamer.playTrack.PlayTrackFragment;
import com.example.guanqing.spotifystreamer.service.PlayMediaService;
import com.example.guanqing.spotifystreamer.topTracks.TopTrackActivity;
import com.example.guanqing.spotifystreamer.topTracks.TopTrackFragment;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;


public class SearchActivity extends ActionBarActivity implements
        SearchFragment.Communicator,
        TopTrackFragment.Communicator{
    private static final String LOG_TAG = SearchActivity.class.getSimpleName();
    boolean mTwoPane;
    static final String ARTIST_INFO = "ARTIST_INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTwoPane = getResources().getBoolean(R.bool.tablet_layout);
        //if it is a tablet, use the fragment manager to conduct transaction
        if (mTwoPane){
            if (savedInstanceState==null) {
                //replace the fragment with the new one
                TopTrackFragment topTrackFragment = new TopTrackFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.top_track_container, topTrackFragment);
                transaction.commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //implement interfaces
    @Override
    public void onArtistSelected(Artist artist) {
        String imageUrl = "";
        if (artist.images.size() != 0) {
            imageUrl = artist.images.get(0).url;
        }
        String[] artistInfo = {artist.name, artist.id, imageUrl};
        if (mTwoPane){
            //two pane
            Bundle args = new Bundle();
            args.putStringArray(ARTIST_INFO, artistInfo);
            //set arguments for fragment
            TopTrackFragment topTrackFragment = new TopTrackFragment();
            topTrackFragment.setArguments(args);
            //fragment transaction
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.top_track_container, topTrackFragment);
            transaction.commit();
        }else{
            //one pane
            Intent intent = new Intent(this, TopTrackActivity.class)
                    .putExtra(Intent.EXTRA_TEXT, artistInfo);
            startActivity(intent);
        }
    }

    @Override
    public void onTrackSelected(ArrayList<Track> trackList, int position) {
        //show fragment as dialog on a tablet
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        PlayTrackFragment fragment = PlayTrackFragment.newInstance(trackList, position);
        fragment.show(fragmentManager, "dialog");

        Log.i(LOG_TAG, "HGQ: finish onTrackSelected with position = " + position);
        Log.i(LOG_TAG, "HGQ: track list as below:\n"+trackListString(trackList));

        //set tracklist for the service
        PlayMediaService.setTrackList(this, trackList);
    }

    public static String trackListString(ArrayList<Track> trackList) {
        String x = "";
        for (Track track: trackList){
            x += track.name + "\n";
        }
        return x;
    }
}
