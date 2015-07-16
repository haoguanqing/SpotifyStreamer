package com.example.guanqing.spotifystreamer.searchArtists;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.guanqing.spotifystreamer.R;
import com.example.guanqing.spotifystreamer.playTrack.PlayTrackFragment;
import com.example.guanqing.spotifystreamer.topTracks.TopTrackActivity;
import com.example.guanqing.spotifystreamer.topTracks.TopTrackFragment;

import kaaes.spotify.webapi.android.models.Artist;


public class SearchActivity extends ActionBarActivity implements
        SearchFragment.Communicator,
        TopTrackFragment.Communicator{
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*    public boolean isTwoPane(){
        return mTwoPane;
    }*/




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
    public void onTrackSelected(String trackId) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        PlayTrackFragment fragment = PlayTrackFragment.newInstance(trackId);

            //show fragment as dialog on a tablet
            fragment.show(fragmentManager, "dialog");

    }
}
