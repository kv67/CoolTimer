package ru.kve.cooltimer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private CountDownTimer timer;
  private SeekBar seekBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    seekBar = findViewById(R.id.seekBar);
    seekBar.setProgress(0);
    seekBar.setMax(3600000);
    seekBar.setProgress(60);

//    seekBar.post(new Runnable() {
//      @Override
//      public void run() {
//        seekBar.setProgress(60);
//      }
//    });

    timer = new CountDownTimer(3600000, 1000) {
      @Override
      public void onTick(long millisUntilFinished) {
        if (seekBar.getProgress() > 0) {
          seekBar.setProgress(seekBar.getProgress() - 1);
        } else {
          timer.cancel();
        }
      }

      @Override
      public void onFinish() {

      }
    };

    // timer.start();
  }
}
