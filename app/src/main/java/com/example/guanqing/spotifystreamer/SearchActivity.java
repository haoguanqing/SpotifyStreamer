package com.example.guanqing.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class SearchActivity extends ActionBarActivity implements SearchFragment.ArtistSelectListener {
    boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.top_track_container)!=null){
            mTwoPane = true;
            if (savedInstanceState==null) {
                //replace the fragment with the new one
                TopTrackFragment topTrackFragment = new TopTrackFragment();
                topTrackFragment.setArguments(new Bundle());
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.top_track_container, topTrackFragment);
                transaction.commit();
            }
        }else{
            mTwoPane = false;
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onArtistSelected(String[] artistInfo) {
        if (mTwoPane){
            //two pane
            Bundle args = new Bundle();
            args.putStringArray(TopTrackFragment.ARTIST_INFO, artistInfo);
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
}
