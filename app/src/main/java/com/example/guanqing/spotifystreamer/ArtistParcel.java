package com.example.guanqing.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Guanqing on 2015/6/30.
 */
public class ArtistParcel implements Parcelable{
    private Artist artist;

    public ArtistParcel(Artist artist){
        this.artist = artist;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.readParcelable(Track.class.getClassLoader());
    }

    public static final Creator<ArtistParcel> CREATOR
            = new Creator<ArtistParcel>() {
        public ArtistParcel createFromParcel(Parcel in) {
            return new ArtistParcel(in);
        }

        public ArtistParcel[] newArray(int size) {
            return new ArtistParcel[size];
        }
    };

    private ArtistParcel(Parcel in) {
        artist = in.readParcelable(Artist.class.getClassLoader());
    }

    public Artist getArtist(){
        return artist;
    }
}

