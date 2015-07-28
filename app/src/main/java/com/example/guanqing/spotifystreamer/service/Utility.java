package com.example.guanqing.spotifystreamer.service;

import com.example.guanqing.spotifystreamer.searchArtists.ArtistParcel;
import com.google.gson.Gson;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Guanqing on 2015/7/21.
 */
public class Utility {
    //do the conversion between ArrayList of artists and ArrayList of strings using Gson
    public static ArrayList<Artist> getArtistListFromGson(ArrayList<String> strList){
        ArrayList<Artist> artistList = new ArrayList<>();
        for (String s: strList){
            Artist artist = new Gson().fromJson(s, Artist.class);
            artistList.add(artist);
        }
        return artistList;
    }
    public static ArrayList<String> getJsonStringListFromArtists(ArrayList<Artist> artistList){
        ArrayList<String> strList = new ArrayList<>();
        for (Artist artist: artistList){
            String s = new Gson().toJson(artist);
            strList.add(s);
        }
        return strList;
    }


    //do the conversion between ArrayList of tracks and ArrayList of strings using Gson
    public static ArrayList<Track> getTrackListFromGson(ArrayList<String> strList){
        ArrayList<Track> trackList = new ArrayList<>();
        for (String s: strList){
            Track track = new Gson().fromJson(s, Track.class);
            trackList.add(track);
        }
        return trackList;
    }
    public static ArrayList<String> getJsonStringListFromTracks(ArrayList<Track> trackList){
        ArrayList<String> strList = new ArrayList<>();
        for (Track track: trackList){
            String s = new Gson().toJson(track);
            strList.add(s);
        }
        return strList;
    }

    //parse long track_duration (192000) into formatted String (03:12)
    public static String getFormattedDuration(long dur_ms){
        Long dur = dur_ms;
        int totalSec = dur.intValue()/1000;
        int min = totalSec/60;
        int sec = totalSec - min * 60;
        String minute = min+"";
        String second = sec+"";

        if (min==0){
            minute = "00";
        } else if (min<10){
            minute = "0"+min;
        }

        if(sec==0) {
            second = "00";
        }else if (sec<10){
            second = "0"+sec;
        }
        return minute+":"+second;
    }

    //converts an arraylist of artists into parcelables and vise versa
    public static ArrayList<Artist> getArtistsList(ArrayList<ArtistParcel> parcelList){
        ArrayList<Artist> artistsList = new ArrayList<>();
        for (ArtistParcel parcel : parcelList){
            artistsList.add(parcel.getArtist());
        }
        return  artistsList;
    }
    public static ArrayList<ArtistParcel> getArtistsParcelList(ArrayList<Artist> artistsList){
        ArrayList<ArtistParcel> parcelList = new ArrayList<>();
        for (Artist artist : artistsList){
            parcelList.add(new ArtistParcel(artist));
        }
        return parcelList;
    }

}
