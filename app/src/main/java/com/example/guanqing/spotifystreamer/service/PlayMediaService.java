package com.example.guanqing.spotifystreamer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.guanqing.spotifystreamer.R;
import com.example.guanqing.spotifystreamer.searchArtists.SearchActivity;
import com.example.guanqing.spotifystreamer.topTracks.TopTrackActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
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
    public static final String ACTION_SET_TRACK_PROGRESS = "com.example.guanqing.spotifystreamer.action.SET_TRACK_PROGRESS";
    public static final String ACTION_PREV = "com.example.guanqing.spotifystreamer.action.PREV";
    public static final String ACTION_PLAY = "com.example.guanqing.spotifystreamer.action.PLAY";
    public static final String ACTION_PAUSE = "com.example.guanqing.spotifystreamer.action.PAUSE";
    public static final String ACTION_RESUME = "com.example.guanqing.spotifystreamer.action.RESUME";
    public static final String ACTION_NEXT = "com.example.guanqing.spotifystreamer.action.NEXT";
    //intent keys & constants
    public static final String TRACK_POSITION_KEY = "TRACK_POSITION_KEY";
    public static final String TRACK_PROGRESS_KEY = "TRACK_PROGRESS_KEY";
    public static final String PREF_SHOW_CONTROLS_IN_LOCKSCREEN = "pref_show_controls_in_lockscreen";
    public static final int NOTIFICATION_ID = 98789;

    //media player
    private MediaPlayer mPlayer;
    private BroadcastTrackProgressTask mBroadcastTask;
    private static ArrayList<Track> mTrackList;
    private int mCurrentPosition;

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
        }else if (action.equals(ACTION_SET_TRACK_PROGRESS)){
            setTrackProgress(intent.getIntExtra(TRACK_PROGRESS_KEY, 0));
        }

        return START_NOT_STICKY;
    }

    //------set tracks list------
    public static void setTrackList(Context context, ArrayList<Track> lst){
        mTrackList = lst;
    }

    //------play track------
    public static void playTrack(Context context, int position){
        Intent serviceIntent = new Intent(context, PlayMediaService.class);
        serviceIntent.setAction(ACTION_PLAY);
        serviceIntent.putExtra(TRACK_POSITION_KEY, position);
        context.startService(serviceIntent);
    }

    private void playTrack(Intent intent){
        stopPlayTrack();

        mCurrentPosition = intent.getIntExtra(TRACK_POSITION_KEY, 0);
        String url = mTrackList.get(mCurrentPosition).preview_url;
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
        setNotification();
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
        setNotification();
    }

    public static Intent getPauseIntent(Context context){
        Intent intent = new Intent(context, PlayMediaService.class);
        intent.setAction(ACTION_PAUSE);
        return intent;
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

            mBroadcastTask = new BroadcastTrackProgressTask();
            mBroadcastTask.execute();
        }
        setNotification();
    }

    public static Intent getResumeIntent(Context context){
        Intent intent = new Intent(context, PlayMediaService.class);
        intent.setAction(ACTION_RESUME);
        return intent;
    }

    //------play next track------
    public static void nextTrack(Context context){
        Intent serviceIntent = new Intent(context, PlayMediaService.class);
        serviceIntent.setAction(ACTION_NEXT);
        context.startService(serviceIntent);
    }

    private void nextTrack(){
        if (mCurrentPosition == mTrackList.size()-1){
            mCurrentPosition = 0;
        }else{
            mCurrentPosition = mCurrentPosition +1;
        }
        playTrack(this, mCurrentPosition);
        setNotification();
    }

    public static Intent getPlayNextIntent(Context context){
        Intent intent = new Intent(context, PlayMediaService.class);
        intent.setAction(ACTION_NEXT);
        return intent;
    }

    //------play previous track------
    public static void previousTrack(Context context){
        Intent serviceIntent = new Intent(context, PlayMediaService.class);
        serviceIntent.setAction(ACTION_PREV);
        context.startService(serviceIntent);
    }

    private void previousTrack(){
        if (mCurrentPosition ==0){
            mCurrentPosition = mTrackList.size()-1;
        }else{
            mCurrentPosition = mCurrentPosition - 1;
        }
        playTrack(this, mCurrentPosition);
        setNotification();
    }

    public static Intent getPlayPrevIntent(Context context){
        Intent intent = new Intent(context, PlayMediaService.class);
        intent.setAction(ACTION_PREV);
        return intent;
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

    //------SeekBar settings------
    public static void setTrackProgress(Context context, int progress){
        Intent intent = new Intent(context, PlayMediaService.class);
        intent.setAction(ACTION_SET_TRACK_PROGRESS);
        intent.putExtra(TRACK_PROGRESS_KEY, progress);
        context.startService(intent);
    }

    private void setTrackProgress(int progress){
        mPlayer.seekTo(progress * 1000);
    }

    //------broadcast functions------
    private void broadcastTrackProgress(){
        TrackProgressEvent event = TrackProgressEvent.newInstance(
                mTrackList.get(mCurrentPosition),
                mPlayer.getCurrentPosition(),
                mPlayer.getDuration());
        EventBus.getDefault().post(event);
    }

    class BroadcastTrackProgressTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            while(!isCancelled()){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                if(mPlayer.isPlaying()) {
                    broadcastTrackProgress();
                }
            }
            return null;
        }
    }

    //----set notification when tracks are start to play----
    private void setNotification(){
        Track track = mTrackList.get(mCurrentPosition);
        //new remote view
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.notification);
        view.setTextViewText(R.id.track_name, track.name);
        view.setTextViewText(R.id.artist_name, track.artists.get(0).name);

        //playback controls
        view.setOnClickPendingIntent(R.id.previousButton,
                PendingIntent.getService(this, 0, getPlayPrevIntent(this), 0));
        view.setOnClickPendingIntent(R.id.nextButton,
                PendingIntent.getService(this, 0, getPlayNextIntent(this), 0));
        if(mPlayer!=null && mPlayer.isPlaying()){
            view.setImageViewResource(R.id.playButton, android.R.drawable.ic_media_pause);
            view.setOnClickPendingIntent(R.id.playButton,
                    PendingIntent.getService(this, 0, getPauseIntent(this), 0));
        }else{
            view.setImageViewResource(R.id.playButton, android.R.drawable.ic_media_play);
            view.setOnClickPendingIntent(R.id.playButton,
                    PendingIntent.getService(this, 0, getResumeIntent(this), 0));
        }

        //content action
        Intent showAppIntent;
        if(getResources().getBoolean(R.bool.tablet_layout)){
            showAppIntent = new Intent(this, SearchActivity.class);
        }else{
            showAppIntent = new Intent(this, TopTrackActivity.class);
        }
        showAppIntent.setAction(Intent.ACTION_MAIN);
        showAppIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent showAppPendingIntent = PendingIntent.getActivity(this, 0, showAppIntent,0);

        //prepare notification
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContent(view)
                .setContentIntent(showAppPendingIntent)
                .setOngoing(mPlayer != null && mPlayer.isPlaying());

        //show playback controls in lockscreen
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean showControlInLockscreen = sharedPreferences.getBoolean(PREF_SHOW_CONTROLS_IN_LOCKSCREEN, true);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH && showControlInLockscreen) {
            notificationBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        //display notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = notificationBuilder.build();
        notificationManager.notify(NOTIFICATION_ID, notification);

        //load thumbnail
        String imageUrl = track.album.images.get(1).url;
        Picasso.with(this).load(imageUrl).into(view, R.id.track_thumbnail, NOTIFICATION_ID, notification);

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
