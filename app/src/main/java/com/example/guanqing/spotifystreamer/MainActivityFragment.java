package com.example.guanqing.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_search, container, false);

        String[] urls = {"http://i.imgur.com/DvpvklR.png",
                "https://i.scdn.co/image/a370c003642050eeaec0bc604409aa585ca92297",
                "https://i.scdn.co/image/61de0bd715d34d394b95f5191ad2a4aed0059132"};
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
}
