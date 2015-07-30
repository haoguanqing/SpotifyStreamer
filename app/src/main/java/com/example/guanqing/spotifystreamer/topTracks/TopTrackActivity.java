package com.example.guanqing.spotifystreamer.topTracks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.guanqing.spotifystreamer.R;
import com.example.guanqing.spotifystreamer.playTrack.PlayTrackActivity;
import com.example.guanqing.spotifystreamer.playTrack.PlayTrackFragment;
import com.example.guanqing.spotifystreamer.searchArtists.SearchFragment;
import com.example.guanqing.spotifystreamer.service.PlayMediaService;
import com.example.guanqing.spotifystreamer.service.Utility;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;


public class TopTrackActivity extends ActionBarActivity implements TopTrackFragment.Communicator{
    private final String LOG_TAG = SearchFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setTitle(getString(R.string.title_activity_detail));
        String[] artistInfo = null;
        if (getIntent()!=null){
            artistInfo = getIntent().getStringArrayExtra(Intent.EXTRA_TEXT);
            getSupportActionBar().setSubtitle(artistInfo[0]);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState==null){
            Bundle args = new Bundle();
            args.putStringArray(TopTrackFragment.ARTIST_INFO, artistInfo);
            TopTrackFragment topTrackFragment = new TopTrackFragment();
            topTrackFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.top_track_container, topTrackFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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

    @Override
    public void onTrackSelected(ArrayList<Track> trackList, int position) {
        //show the fragment fullscreen on a phone
        Intent intent = new Intent(this, PlayTrackActivity.class);
        ArrayList<String> jsonList = Utility.getJsonStringListFromTracks(trackList);
        intent.putStringArrayListExtra(PlayTrackFragment.TRACK_LIST_KEY, jsonList);
        intent.putExtra(PlayTrackFragment.TRACK_POSITION_KEY, position);
        startActivity(intent);
        Log.i(LOG_TAG, "HGQ: TopTrackActivity finish onTrackSelected and send intent");

        //set tracklist for the service
        PlayMediaService.setTrackList(this, trackList);
    }
}

