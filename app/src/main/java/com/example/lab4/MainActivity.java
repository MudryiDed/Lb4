package com.example.lab4;

import android.os.Bundle;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private ImageButton playButton, pauseButton, nextButton;
    private SeekBar seekBar;
    private Handler handler = new Handler();
    private TextView songTitle;

    private int[] songs = {R.raw.e_m, R.raw.mon};  // Добавь сюда второй трек
    private String[] songNames = {"Eminem - Mockingbird", "Eminem, ft.Rihana - Monster"};
    private int currentSongIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        nextButton = findViewById(R.id.nextButton);  // Кнопка "Next"
        seekBar = findViewById(R.id.seekBar);
        songTitle = findViewById(R.id.songTitle);

        // Загрузка первой песни
        loadSong();

        playButton.setOnClickListener(v -> {
            mediaPlayer.start();
            updateSeekBar();
        });

        pauseButton.setOnClickListener(v -> mediaPlayer.pause());

        nextButton.setOnClickListener(v -> {
            // Переключаем на следующий трек
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            currentSongIndex = (currentSongIndex + 1) % songs.length;
            loadSong();
            mediaPlayer.start();
            updateSeekBar();
        });

        mediaPlayer.setOnCompletionListener(mp -> mediaPlayer.seekTo(0));

        mediaPlayer.setOnSeekCompleteListener(mp -> mediaPlayer.seekTo(seekBar.getProgress()));
    }

    private void loadSong() {
        // Освобождаем текущий медиа-плеер, если он существует
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        // Загружаем новый трек
        mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex]);
        songTitle.setText(songNames[currentSongIndex]);
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        if (mediaPlayer.isPlaying()) {
            Runnable updater = this::updateSeekBar;
            handler.postDelayed(updater, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
