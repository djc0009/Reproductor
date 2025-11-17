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

        mediaPlayer = MediaPlayer.create(this, R.raw.cancion_caillou);

        Button btnPlay = findViewById(R.id.bPlay);
        Button btnPause = findViewById(R.id.bPause);
        seekBar = findViewById(R.id.sBar);
        tTiempo = findViewById(R.id.tTiempo);
        tDuracion = findViewById(R.id.tDuracion);

        // Botón Play
        btnPlay.setOnClickListener(view -> mediaPlayer.start());

        // Botón Pause
        btnPause.setOnClickListener(view -> mediaPlayer.pause());

        // Obtener duración cuando el MediaPlayer esté preparado
        mediaPlayer.setOnPreparedListener(mp -> {
            seekBar.setMax(mediaPlayer.getDuration());

            int durMin = mediaPlayer.getDuration() / 1000 / 60;
            int durSeg = mediaPlayer.getDuration() / 1000 % 60;
            tDuracion.setText(String.format("%02d:%02d", durMin, durSeg));

            tTiempo.setText("00:00");
        });

        // Iniciar actualización del tiempo
        handler.post(actualizar);
    }

    // Actualiza el progreso cada segundo
    private Runnable actualizar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                // Pongo el tiempo en el TextView
                int minutos = mediaPlayer.getCurrentPosition() / 1000 / 60;
                int segundos = mediaPlayer.getCurrentPosition() / 1000 % 60;
                String tiempo = String.format("%02d:%02d", minutos, segundos);
                tTiempo.setText(tiempo);

                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(actualizar);
    }
}
