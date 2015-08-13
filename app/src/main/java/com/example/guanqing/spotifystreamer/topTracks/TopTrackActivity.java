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
import com.example.guanqing.spotifystreamer.service.TrackProgressEvent;
import com.example.guanqing.spotifystreamer.service.Utility;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.models.Track;


public class TopTrackActivity extends ActionBarActivity implements TopTrackFragment.Communicator{
    private final String LOG_TAG = SearchFragment.class.getSimpleName();
    private android.support.v7.widget.ShareActionProvider mShareActionProvider;
    private String[] artistInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setTitle(getString(R.string.title_activity_detail));
        artistInfo = null;
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
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }if(id == R.id.menu_item_share){
            if (artistInfo!= null) {
                startActivity(Intent.createChooser(createShareIntent(), "Share"));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    Track mCurrentTrack;
    //handle event posted in the service
    public void onEventMainThread(TrackProgressEvent event){
        mCurrentTrack = event.getTrack();
    }

    private Intent createShareIntent(){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String shareContent = "Hey! I am listening to the top 10 tracks of " + artistInfo[0] + " using SpotifyStreamer!";
        if (mCurrentTrack!= null){
            String url = mCurrentTrack.preview_url;
            shareContent = "Hey! I am listening to " + mCurrentTrack.name
                    + " - " + mCurrentTrack.artists.get(0).name
                    + " using SpotifyStreamer! (" + url + ")";
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
        return shareIntent;
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

