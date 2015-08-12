package com.example.guanqing.spotifystreamer.searchArtists;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.guanqing.spotifystreamer.R;
import com.example.guanqing.spotifystreamer.playTrack.PlayTrackFragment;
import com.example.guanqing.spotifystreamer.service.PlayMediaService;
import com.example.guanqing.spotifystreamer.service.TrackProgressEvent;
import com.example.guanqing.spotifystreamer.topTracks.TopTrackActivity;
import com.example.guanqing.spotifystreamer.topTracks.TopTrackFragment;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;


public class SearchActivity extends ActionBarActivity implements
        SearchFragment.Communicator,
        TopTrackFragment.Communicator{
    private static final String LOG_TAG = SearchActivity.class.getSimpleName();
    private boolean mTwoPane;
    private Track mCurrentTrack;
    MenuItem shareIcon;
    public String[] artistInfo;
    static final String ARTIST_INFO = "ARTIST_INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        artistInfo = null;
        mCurrentTrack = null;
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
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        shareIcon = menu.findItem(R.id.menu_item_share);
        shareIcon.setVisible(false);
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

    private Intent createShareIntent(){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String shareContent = "Hey! I am listening to the top 10 tracks of " + artistInfo[0] + " using SpotifyStreamer!";
        if (mCurrentTrack!= null){
            String url = mCurrentTrack.album.images.get(1).url;
            shareContent = "Hey! I am listening to " + mCurrentTrack.name
                    + " - " + mCurrentTrack.artists.get(0).name
                    + " using SpotifyStreamer! (" + url + ")";
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
        return shareIntent;
    }

    //implement interfaces
    @Override
    public void onArtistSelected(Artist artist) {
        shareIcon.setVisible(true);
        String imageUrl = "";
        if (artist.images.size() != 0) {
            imageUrl = artist.images.get(0).url;
        }
        String[] strArray = {artist.name, artist.id, imageUrl};
        artistInfo = strArray;
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

    //handle event posted in the service
    public void onEventMainThread(TrackProgressEvent event){
        mCurrentTrack = event.getTrack();
    }

    @Override
    public void onTrackSelected(ArrayList<Track> trackList, int position) {
        //show fragment as dialog on a tablet
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        PlayTrackFragment fragment = PlayTrackFragment.newInstance(trackList, position);
        fragment.show(fragmentManager, "dialog");

        //set tracklist for the service
        PlayMediaService.setTrackList(this, trackList);
    }
}
