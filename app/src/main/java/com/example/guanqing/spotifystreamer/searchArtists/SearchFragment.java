package com.example.guanqing.spotifystreamer.searchArtists;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.guanqing.spotifystreamer.R;

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
public class SearchFragment extends Fragment {
    private final String LOG_TAG = SearchFragment.class.getSimpleName();
    private final String ARTIST_PARCEL_KEY = "ARTIST_PARCEL_KEY";
    private SpotifyService mSpotifyService = null;
    Communicator communicator;
    final ArrayList<Artist> artistsList = new ArrayList<>();

    public SearchFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save the user's current state
        ArrayList<ArtistParcel> list = new ArrayList<>();
        for (Artist artist : artistsList){
            list.add(new ArtistParcel(artist));
        }
        outState.putParcelableArrayList(ARTIST_PARCEL_KEY, list);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get the saved state so as to avoid redo the keyword searching on create view
        if (savedInstanceState!=null){
            artistsList.clear();
            ArrayList<ArtistParcel> list = savedInstanceState.getParcelableArrayList(ARTIST_PARCEL_KEY);
            for (ArtistParcel parcel: list){
                artistsList.add(parcel.getArtist());
            }
            savedInstanceState.clear();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            communicator = (Communicator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Communicator");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get spotify service
        SpotifyApi api = new SpotifyApi();
        mSpotifyService = api.getService();
        //get views
        final View rootView = inflater.inflate(R.layout.fragment_main_search, container, false);
        final SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);
        final ListView listView = (ListView) rootView.findViewById(R.id.search_results_listView);

        //create adapter and set to listview
        final ArtistAdapter adapter = new ArtistAdapter(
                getActivity(),R.layout.fragment_main_block, artistsList);
        listView.setAdapter(adapter);

        //dynamically define searchview
        searchView.setQueryHint(getString(R.string.search_artist_hint));
        if (savedInstanceState==null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(final String newText) {
                    mSpotifyService.searchArtists(newText, new Callback<ArtistsPager>() {
                        @Override
                        public void success(ArtistsPager artistsPager, Response response) {
                            artistsList.clear();
                            if (artistsPager.artists.total != 0) {
                                artistsList.addAll(new ArrayList<>(artistsPager.artists.items));
                            } else {
                                noResultToast();
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void failure(RetrofitError error) {}

                        public void noResultToast() {
                            String text = getString(R.string.no_result_found) + newText;
                            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return true;
                }
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artistsList.get(position);
                //use Communicator instead of directly building an intent
                communicator.onArtistSelected(artist);
            }
        });

        return rootView;
    }


    public interface Communicator {
        void onArtistSelected(Artist artist);
    }

}
