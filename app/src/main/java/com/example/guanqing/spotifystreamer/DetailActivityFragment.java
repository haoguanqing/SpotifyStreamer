package com.example.guanqing.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private SpotifyService mSpotifyService = null;
    private String[] artistInfo;
    private ArrayList<Track> trackList = new ArrayList<>();

    public DetailActivityFragment() {
        final SpotifyApi api = new SpotifyApi();
        mSpotifyService = api.getService();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current state
        ArrayList<TrackParcel> list = new ArrayList<>();
        for (Track track : trackList){
            list.add(new TrackParcel(track));
        }
        savedInstanceState.putParcelableArrayList("key", list);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artistInfo = getActivity().getIntent().getStringArrayExtra(Intent.EXTRA_TEXT);

        //get the precious state from savedInstanceState
        if (savedInstanceState!=null){
            ArrayList<TrackParcel> list = savedInstanceState.getParcelableArrayList("key");
            trackList.clear();
            for (TrackParcel track : list){
                trackList.add(track.getTrack());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.artist_thumbnail);
        final ListView trackListView = (ListView) rootView.findViewById(R.id.top_track_listView);
        final TrackAdapter adapter = new TrackAdapter(
                getActivity(),R.layout.fragment_detail_block, trackList);
        //set the adapter to trackListView
        trackListView.setAdapter(adapter);

        final String artistName = artistInfo[0];
        final String artistId = artistInfo[1];
        final String url = artistInfo[2];
        final Map<String, Object> countryParameter = new HashMap<>();
        countryParameter.put("country", "us");

        //set the thumbnail for the artist
        if (!url.isEmpty()){
            Picasso.with(getActivity()).load(url).into(imageView);
        }
        if (savedInstanceState==null) {
            mSpotifyService.getArtistTopTrack(artistId, countryParameter, new Callback<Tracks>() {
                @Override
                public void success(Tracks tracks, Response response) {

                    trackList.clear();
                    trackList.addAll(new ArrayList<>(tracks.tracks));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }


                @Override
                public void failure(RetrofitError error) {
                }
            });
        }

        trackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: will complete in stage 2
                Toast.makeText(getActivity(), "To be done in stage 2", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

}

