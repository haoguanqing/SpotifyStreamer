package com.example.guanqing.spotifystreamer.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.example.guanqing.spotifystreamer.searchArtists.SearchActivity;

import java.io.IOException;
import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Guanqing on 2015/7/21.
 */
public class PlayMediaService extends Service
        implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener{
    //tag for debugging
    private static final String LOG_TAG = PlayMediaService.class.getSimpleName();

    //intent actions to handle
    public static final String ACTION_SET_TRACKLIST = "com.example.guanqing.spotifystreamer.action.SET_TRACKLIST";
    public static final String ACTION_PREV = "com.example.guanqing.spotifystreamer.action.PREV";
    public static final String ACTION_PLAY = "com.example.guanqing.spotifystreamer.action.PLAY";
    public static final String ACTION_PAUSE = "com.example.guanqing.spotifystreamer.action.PAUSE";
    public static final String ACTION_RESUME = "com.example.guanqing.spotifystreamer.action.RESUME";
    public static final String ACTION_NEXT = "com.example.guanqing.spotifystreamer.action.NEXT";
    //intent keys
    public static final String TRACKLIST_KEY = "TRACKLIST_KEY";
    public static final String TRACK_POSITION_KEY = "TRACK_POSITION_KEY";

    //The volume we reduce to when we lose audio focus
    public static final float DUCK_VOLUME = 0.1f;

    //media player
    MediaPlayer mPlayer;

    private static ArrayList<Track> trackList = new ArrayList<>();
    private static ArrayList<String> trackUrlList = new ArrayList<>();
    private int currentPosition = -99;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action.equals(ACTION_PREV)){
            previousTrack();
        } else if (action.equals(ACTION_PLAY)){
            playTrack(intent);
        }else if (action.equals(ACTION_PAUSE)){
            pauseTrack();
        }else if (action.equals(ACTION_RESUME)){
            resumeTrack();
        }else if (action.equals(ACTION_NEXT)){
            nextTrack();
        }

        return START_NOT_STICKY;
    }

    //------set tracks list------
    public static void setTrackList(Context context, ArrayList<Track> lst){
        trackList = lst;
        Log.i(LOG_TAG, "HGQ: Service set tracklist as follow:\n" + SearchActivity.trackListString(trackList));
    }

    //------play track------
    public static void playTrack(Context context, int position){
        Intent serviceIntent = new Intent(context, PlayMediaService.class);
        serviceIntent.setAction(ACTION_PLAY);
        serviceIntent.putExtra(TRACK_POSITION_KEY, position);
        context.startService(serviceIntent);
        Log.i(LOG_TAG, "HGQ: Service pass playTrack intent with position = "+ position);
    }

    private void playTrack(Intent intent){
        stopPlayTrack();

        currentPosition = intent.getIntExtra(TRACK_POSITION_KEY, 0);
        String url = trackList.get(currentPosition).preview_url;
        Log.i(LOG_TAG, "HGQ: start playTrack service with url = " + url);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnPreparedListener(this);
        try {
            mPlayer.setDataSource(url);
            mPlayer.prepareAsync();
        }catch (IOException e){
            Log.e(LOG_TAG, "HGQ: Service play track IOException");
        }
    }

    //------pause track------
    public static void pauseTrack(Context context){
        Intent serviceIntent = new Intent(context, PlayMediaService.class);
        serviceIntent.setAction(ACTION_PAUSE);
        context.startService(serviceIntent);
    }

    private void pauseTrack(){
        if(mPlayer!=null){
            mPlayer.pause();
        }
    }

    //------resume track------
    public static void resumeTrack(Context context){
        Intent serviceIntent = new Intent(context, PlayMediaService.class);
        serviceIntent.setAction(ACTION_RESUME);
        context.startService(serviceIntent);
    }

    private void resumeTrack(){
        if(mPlayer!=null){
            mPlayer.start();
        }
    }

    //------play next track------
    public static void nextTrack(Context context){
        Intent serviceIntent = new Intent(context, PlayMediaService.class);
        serviceIntent.setAction(ACTION_NEXT);
        context.startService(serviceIntent);
    }

    private void nextTrack(){
        if (currentPosition==trackList.size()-1){
            currentPosition = 0;
        }else{
            currentPosition = currentPosition+1;
        }

        playTrack(this, currentPosition);
    }

    //------play previous track------
    public static void previousTrack(Context context){
        Intent serviceIntent = new Intent(context, PlayMediaService.class);
        serviceIntent.setAction(ACTION_PREV);
        context.startService(serviceIntent);
    }

    private void previousTrack(){
        if (currentPosition==0){
            currentPosition = trackList.size()-1;
        }else{
            currentPosition = currentPosition - 1;
        }
        playTrack(this, currentPosition);
    }

    //------stop play track------
    private void stopPlayTrack(){
        if (mPlayer==null) {return;}

        if(mPlayer.isPlaying()){
            mPlayer.stop();
        }
        mPlayer.setOnPreparedListener(null);
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
    }


    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public void onPrepared(MediaPlayer mp) {
        resumeTrack();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextTrack(this);
    }

    @Override
    public void onDestroy() {
        stopPlayTrack();
    }
}
