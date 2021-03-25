package com.baichizhan.word;

import android.media.MediaPlayer;

import java.io.IOException;

public class Mp3 {
    private MediaPlayer mediaPlayer;

    public void openMp3(String url) {
        System.out.println(url);
//        if (url == null)
//            return;
        if (!url.contains("https"))
            url = url.replace("http", "https");

        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopMp3() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
