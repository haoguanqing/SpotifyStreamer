package com.example.guanqing.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private String keyword;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_search, container, false);

        EditText editText = (EditText) rootView.findViewById(R.id.search_bar);
        keyword = editText.getText().toString();

/*
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    //Your code goes here
                    SpotifyApi api = new SpotifyApi();
                    SpotifyService spotify = api.getService();
                    ArtistsPager results = spotify.searchArtists(keyword);
                    Log.i("results", "=========================================================================================================");
                    Log.i("results", results.toString());
                    Toast toast = Toast.makeText(getActivity(), results.toString(), Toast.LENGTH_LONG);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
*/



        String[] urls = {"https://i.scdn.co/image/2bc67201ad4ed2d810185697d97da86aa7128e18",
                "https://i.scdn.co/image/0b3c04473aa6a2db8235e5092ec3413f35752b8d",
                "https://i.scdn.co/image/626be859c3f3e64df9c4032a2f3b4bfacd9875d2",
                "https://i.scdn.co/image/5a115ce35ef55f4ba6aa81c3675b5ec3eeb3f90f",
                "https://i.scdn.co/image/77970fa30303f6ddcd5b3b3520bf1c6296521ba9",
                "http://i.imgur.com/DvpvklR.png",
                "https://i.scdn.co/image/626be859c3f3e64df9c4032a2f3b4bfacd9875d2"};

        String[] names = {"Gaga", "Muse", "The Black Parade", "Linkin Park", "10 Years", "Green Day", "Olivia Ong"};

        MainBlockAdapter adapter = new MainBlockAdapter(
                getActivity(),
                urls,
                names
        );
        ListView listView = (ListView) rootView.findViewById(R.id.search_results_list);
        listView.setAdapter(adapter);

        return rootView;
    }


    //========================================================================================
    //connect to internet

}
