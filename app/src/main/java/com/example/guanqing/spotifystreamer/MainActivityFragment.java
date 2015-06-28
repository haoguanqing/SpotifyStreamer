package com.example.guanqing.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private SpotifyService mSpotifyService = null;

    public MainActivityFragment() {
        final SpotifyApi api = new SpotifyApi();
        mSpotifyService = api.getService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_search, container, false);
        SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);
        ListView listView = (ListView) rootView.findViewById(R.id.search_results_list);
        final ArrayList<Artist> artistsList = new ArrayList<>();
        //ArtistAdapter adapter = new ArtistAdapter(getActivity(), artistsList);

        searchView.setQueryHint(getString(R.string.search_artist_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSpotifyService.searchArtists(newText, new Callback<ArtistsPager>() {


                    @Override
                    public void success(ArtistsPager artistsPager, Response response) {
                        artistsList.clear();
                        artistsList.addAll(new ArrayList<>(artistsPager.artists.items));
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
                return true;
            }
        });


        final ArtistAdapter adapter = new ArtistAdapter(
                getActivity(),R.layout.fragment_main_block, artistsList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artistsList.get(position);
                buildTrackIntent(artist);
            }

            public void buildTrackIntent(Artist artist) {
                //explicit intent
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                String[] nameAndId = {artist.name, artist.id};
                intent.putExtra(Intent.EXTRA_TEXT, nameAndId);
                startActivity(intent);
            }
        });

        return rootView;
    }

}
