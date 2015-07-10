package com.example.guanqing.spotifystreamer;

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
    private SpotifyService mSpotifyService = null;
    ArtistSelectListener mListener;

    public SearchFragment() {
        final SpotifyApi api = new SpotifyApi();
        mSpotifyService = api.getService();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mListener = (ArtistSelectListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ArtistSelectListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get views
        final View rootView = inflater.inflate(R.layout.fragment_main_search, container, false);
        final SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);
        final ListView listView = (ListView) rootView.findViewById(R.id.search_results_listView);
        final ArrayList<Artist> artistsList = new ArrayList<>();
        //create adapter and set to listview
        final ArtistAdapter adapter = new ArtistAdapter(
                getActivity(),R.layout.fragment_main_block, artistsList);
        listView.setAdapter(adapter);

        //dynamically define searchview
        searchView.setQueryHint(getString(R.string.search_artist_hint));
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artistsList.get(position);
                String imageUrl = "";
                if (artist.images.size() != 0) {
                    imageUrl = artist.images.get(0).url;
                }
                String[] artistInfo = {artist.name, artist.id, imageUrl};
                //use ArtistSelectListener instead of building intent directly
                mListener.onArtistSelected(artistInfo);

            }

        });

        return rootView;
    }

    public interface ArtistSelectListener {
        public void onArtistSelected(String[] artistInfo);
    }

}
