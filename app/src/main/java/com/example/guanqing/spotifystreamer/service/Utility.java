package com.example.guanqing.spotifystreamer.service;

import com.example.guanqing.spotifystreamer.searchArtists.ArtistParcel;
import com.example.guanqing.spotifystreamer.topTracks.TrackParcel;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Guanqing on 2015/7/21.
 */
public class Utility {
    //public static ArrayList<Track> savedTrackList;

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

    //converts an arraylist of tracks into parcelables and vise versa
    public static ArrayList<Track> getTrackList(ArrayList<TrackParcel> parcelList){
        ArrayList<Track> trackList = new ArrayList<>();
        for (TrackParcel parcel : parcelList){
            trackList.add(parcel.getTrack());
        }
        return  trackList;
    }
    public static ArrayList<TrackParcel> getTrackParcelList(ArrayList<Track> trackList){
        ArrayList<TrackParcel> parcelList = new ArrayList<>();
        for (Track track : trackList){
            parcelList.add(new TrackParcel(track));
        }
        return parcelList;
    }
}
