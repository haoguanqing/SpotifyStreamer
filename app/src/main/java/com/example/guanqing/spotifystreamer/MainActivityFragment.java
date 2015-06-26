package com.example.guanqing.spotifystreamer;

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
        final ArrayList<Artist> artistArrayList = new ArrayList<>();
        //ArtistAdapter adapter = new ArtistAdapter(getActivity(), artistArrayList);

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
                        artistArrayList.clear();
                        artistArrayList.addAll(new ArrayList<>(artistsPager.artists.items));
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
                return true;
            }
        });


        String[] urls = {"https://i.scdn.co/image/2bc67201ad4ed2d810185697d97da86aa7128e18",
                "https://i.scdn.co/image/0b3c04473aa6a2db8235e5092ec3413f35752b8d",
                "https://i.scdn.co/image/626be859c3f3e64df9c4032a2f3b4bfacd9875d2",
                "https://i.scdn.co/image/5a115ce35ef55f4ba6aa81c3675b5ec3eeb3f90f",
                "https://i.scdn.co/image/77970fa30303f6ddcd5b3b3520bf1c6296521ba9",
                "http://i.imgur.com/DvpvklR.png",
                "https://i.scdn.co/image/626be859c3f3e64df9c4032a2f3b4bfacd9875d2"};

        String[] names = {"Gaga", "Muse", "The Black Parade", "Linkin Park", "10 Years", "Green Day", "Olivia Ong"};

        final ArtistAdapter adapter = new ArtistAdapter(
                getActivity(),
                urls,
                names
        );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //explicit intent
                //String artistName = adapter.getItem(position).name;
                //Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, artistName);
            }
        });

        return rootView;
    }


    //========================================================================================
    //connect to internet

}
