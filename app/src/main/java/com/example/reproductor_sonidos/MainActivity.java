package com.example.reproductor_sonidos;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private MediaPlayer mp1, mp2, mp3, mp4; // ← 4 sonidos
    private SeekBar seekBar;
    private TextView tTiempo, tDuracion;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mediaPlayer = MediaPlayer.create(this, R.raw.caillou);
        
        Button btnPlay = findViewById(R.id.bPlay);
        Button btnPause = findViewById(R.id.bPause);
        Button sonido1 = findViewById(R.id.bSonido1);
        Button sonido2 = findViewById(R.id.bSonido2);
        Button sonido3 = findViewById(R.id.bSonido3);
        Button sonido4 = findViewById(R.id.bSonido4);

        seekBar = findViewById(R.id.sBar);
        tTiempo = findViewById(R.id.tTiempo);
        tDuracion = findViewById(R.id.tDuracion);

        mp1 = MediaPlayer.create(this, R.raw.sonido1);
        mp2 = MediaPlayer.create(this, R.raw.sonido2);
        mp3 = MediaPlayer.create(this, R.raw.sonido3);
        mp4 = MediaPlayer.create(this, R.raw.sonido4);

        btnPlay.setOnClickListener(view -> mediaPlayer.start());
        btnPause.setOnClickListener(view -> mediaPlayer.pause());

        mediaPlayer.setOnPreparedListener(mp -> {
            seekBar.setMax(mediaPlayer.getDuration());
            int durMin = mediaPlayer.getDuration() / 1000 / 60;
            int durSeg = mediaPlayer.getDuration() / 1000 % 60;
            tDuracion.setText(String.format("%02d:%02d", durMin, durSeg));
            tTiempo.setText("00:00");
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) mediaPlayer.seekTo(progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        sonido1.setOnClickListener(v -> mp1.start());
        sonido2.setOnClickListener(v -> mp2.start());
        sonido3.setOnClickListener(v -> mp3.start());
        sonido4.setOnClickListener(v -> mp4.start());

        handler.post(actualizar);
    }

    private Runnable actualizar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                int minutos = mediaPlayer.getCurrentPosition() / 1000 / 60;
                int segundos = mediaPlayer.getCurrentPosition() / 1000 % 60;
                tTiempo.setText(String.format("%02d:%02d", minutos, segundos));
                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(actualizar);

        if (mediaPlayer != null) mediaPlayer.release();
        if (mp1 != null) mp1.release();
        if (mp2 != null) mp2.release();
        if (mp3 != null) mp3.release();
        if (mp4 != null) mp4.release();
    }
}
