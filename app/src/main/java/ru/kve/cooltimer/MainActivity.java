package ru.kve.cooltimer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private CountDownTimer timer;
  private SeekBar seekBar;
  private TextView textView;
  private Button buttonStart;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    buttonStart = findViewById(R.id.buttonStart);
    textView = findViewById(R.id.textView);
    seekBar = findViewById(R.id.seekBar);
    seekBar.setMax(600);
    seekBar.setProgress(60);

    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int min = progress / 60;
        int sec = progress % 60;
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

    timer = new CountDownTimer(600000, 1000) {
      @Override
      public void onTick(long millisUntilFinished) {
        if (seekBar.getProgress() > 0) {
          seekBar.setProgress(seekBar.getProgress() - 1);
        } else {
          buttonStart.setText("START");
          seekBar.setEnabled(true);
          timer.cancel();
        }
      }

      @Override
      public void onFinish() {
        // not used
      }
    };
  }

  public void onClickStart(View view) {
    if (((TextView) view).getText().equals("START")) {
      ((TextView) view).setText("STOP");
      seekBar.setEnabled(false);
      timer.start();
    } else {
      ((TextView) view).setText("START");
      seekBar.setEnabled(true);
      timer.cancel();
    }
  }
}
