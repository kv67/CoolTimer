package ru.kve.cooltimer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private CountDownTimer timer;
  private SeekBar seekBar;
  private TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    textView = findViewById(R.id.textView);
    seekBar = findViewById(R.id.seekBar);
    seekBar.setMax(600000);
    seekBar.setProgress(60000);

    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int min = progress / 1000 / 60;
        int sec = progress / 1000 % 60;
        String time = (min < 10 ? "0" : "") + String.valueOf(min) + ":" +
            (sec < 10 ? "0" : "") + String.valueOf(sec);
        textView.setText(time);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        // not used
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        // not used
      }
    });

//    seekBar.post(new Runnable() {
//      @Override
//      public void run() {
//        seekBar.setProgress(60);
//      }
//    });

    timer = new CountDownTimer(600000, 1000) {
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

  public void onClickStart(View view) {
    if (((TextView) view).getText().equals("START")) {
      ((TextView) view).setText("STOP");
      timer.start();
    } else {
      ((TextView) view).setText("START");
      timer.cancel();
    }
  }
}
