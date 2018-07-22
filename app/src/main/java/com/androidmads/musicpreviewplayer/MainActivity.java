package com.androidmads.musicpreviewplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    TextView tvSongTitle, tvSongArtist;
    ImageView playPause, imgSongArt;
    MediaPlayer mp = new MediaPlayer();

    MediaMetadataRetriever metaRetriever;
    byte[] art;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSongTitle = findViewById(R.id.song_title);
        tvSongArtist = findViewById(R.id.song_artist);
        playPause = findViewById(R.id.play_pause);
        imgSongArt = findViewById(R.id.song_art);

        if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            try {
                assignSongDetails(getIntent().getData().getPath());
                mp.setDataSource(getIntent().getData().getPath());
                mp.prepare();
                mp.start();
                playPause.setImageResource(R.drawable.ic_pause);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mp.isPlaying()) {
                    mp.pause();
                    playPause.setImageResource(R.drawable.ic_play);
                } else {
                    mp.start();
                    playPause.setImageResource(R.drawable.ic_pause);
                }
            }
        });

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                playPause.setImageResource(R.drawable.ic_play);
            }
        });

    }

    void assignSongDetails(String filePath) {
        metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(filePath);
        try {
            tvSongTitle.setText(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            tvSongArtist.setText(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            try {
                art = metaRetriever.getEmbeddedPicture();
                Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
                imgSongArt.setImageBitmap(songImage);
            } catch (Exception ex) {
                imgSongArt.setImageResource(R.mipmap.ic_launcher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        assert mp != null;
        if (mp.isPlaying()) {
            mp.stop();
        }
    }
}
