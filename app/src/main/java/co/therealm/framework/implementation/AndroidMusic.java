package co.therealm.framework.implementation;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;

import co.therealm.framework.Music;


/**
 * Created by GeoffF on 3/03/2015.
 */
public class AndroidMusic implements Music, OnCompletionListener, OnSeekCompleteListener, OnPreparedListener, OnVideoSizeChangedListener {
    MediaPlayer mediaPlayer;
    boolean isPrepared = false;

    public AndroidMusic(AssetFileDescriptor assetDescriptor) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
                    assetDescriptor.getStartOffset(),
                    assetDescriptor.getLength());
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);

        } catch (Exception e) {
            throw new RuntimeException("Couldn't load music");
        }
    }

    @Override
    public void dispose() {

        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }

    @Override
    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public boolean isStopped() {
        return !isPrepared;
    }

    @Override
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }


    @Override
    public void play() {
        if (mediaPlayer.isPlaying()) {
            return;
        }

        try {
            synchronized (this) {
                if (!isPrepared) {
                    mediaPlayer.prepare();
                }
                mediaPlayer.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setLooping(boolean isLooping) {
        mediaPlayer.setLooping(isLooping);
    }

    @Override
    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    @Override
    public void stop() {
        if (mediaPlayer.isPlaying() == true){
            mediaPlayer.stop();

            synchronized (this) {
                isPrepared = false;
            }
        }
    }


    @Override
    public void onCompletion(MediaPlayer player) {
        synchronized (this) {
            isPrepared = false;
        }
    }

    @Override
    public void seekBegin() {
        mediaPlayer.seekTo(0);
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        synchronized (this) {
            isPrepared = true;
        }
    }


    @Override
    public void onSeekComplete(MediaPlayer player) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer player, int width, int height) {
        // TODO Auto-generated method stub
    }

}
