package com.example.guanqing.spotifystreamer.service;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Guanqing on 2015/7/29.
 */
public class TrackProgressEvent {
    private Track mTrack;
    private int mCurrentProgress;
    private int mMaxProgress;

    public TrackProgressEvent(Track track, int currentProgress, int maxProgress) {
        this.mTrack = track;
        this.mCurrentProgress = currentProgress;
        this.mMaxProgress = maxProgress;
    }

    public Track getTrack() {
        return mTrack;
    }
    public int getCurrentProgress() {
        return mCurrentProgress;
    }
    public int getMaxProgress() {
        return mMaxProgress;
    }

    public static TrackProgressEvent newInstance(Track track, int currentProgress, int maxProgress){
        return  new TrackProgressEvent(track, currentProgress, maxProgress);
    }

}
