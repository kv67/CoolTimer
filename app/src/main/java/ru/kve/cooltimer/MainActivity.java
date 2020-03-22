package ru.kve.cooltimer;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

  private static final String DEFAULT_INTERVAL = "default_interval";

  private CountDownTimer timer;
  private SeekBar seekBar;
  private TextView textView;
  private Button buttonStart;
  private ImageView imageView;
  private SharedPreferences sharedPreferences;

  @SuppressLint("SourceLockedOrientationActivity")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    imageView = findViewById(R.id.imageView);
    buttonStart = findViewById(R.id.buttonStart);
    textView = findViewById(R.id.textView);
    seekBar = findViewById(R.id.seekBar);

    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int min = progress / 60;
        int sec = progress % 60;
        String time = (min < 10 ? "0" : "") + min + ":" + (sec < 10 ? "0" : "") + sec;
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

    int interval = Integer.parseInt(sharedPreferences.getString(DEFAULT_INTERVAL, "30"));
    seekBar.setMax(600);
    seekBar.setProgress(interval);


    timer = new CountDownTimer(600000, 1000) {
      @Override
      public void onTick(long millisUntilFinished) {
        seekBar.setProgress(seekBar.getProgress() - 1);
        if (seekBar.getProgress() <= 0) {
          buttonStart.setText(getResources().getString(R.string.start_label));
          seekBar.setEnabled(true);
          timer.cancel();

          if (sharedPreferences.getBoolean("enable_sound", true)) {
            String melody = sharedPreferences.getString("timer_melody", "ring");
            int id = R.raw.ring;
            if (melody.equals("ring")) {
              id = R.raw.ring;
            } else
              if (melody.equals("ring_waw")) {
                id = R.raw.ring_w;
              } else
                if (melody.equals("bell")) {
                  id = R.raw.bell;
                }

            MediaPlayer mp = MediaPlayer.create(MainActivity.this, id);
            mp.start();
          }

          if (sharedPreferences.getBoolean("enable_animation", true)) {
            imageView.animate().setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
              @Override
              public void onAnimationUpdate(ValueAnimator animation) {

                if (imageView.getRotation() == 0 && animation.getCurrentPlayTime() > 100) {
                  imageView.animate().setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                      if (animation.getRepeatCount() == 0) {
                        animation.setRepeatCount(1);
                        animation.setRepeatMode(ValueAnimator.REVERSE);
                      }
                    }
                  }).rotation(-40).setDuration(100).start();

                }

                if (animation.getRepeatCount() == 0) {
                  animation.setRepeatCount(1);
                  animation.setRepeatMode(ValueAnimator.REVERSE);
                }
              }
            }).rotation(40).setDuration(100).start();
          }
        }
      }

      @Override
      public void onFinish() {
        // not used
      }
    };

    sharedPreferences.registerOnSharedPreferenceChangeListener(this);
  }


  public void onClickStart(View view) {
    if (((TextView) view).getText().toString().equals(getResources().getString(R.string.start_label))) {
      ((TextView) view).setText(getResources().getString(R.string.stop_label));
      seekBar.setEnabled(false);
      timer.start();
    } else {
      ((TextView) view).setText(getResources().getString(R.string.start_label));
      seekBar.setEnabled(true);
      timer.cancel();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.timer_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_settings) {
      Intent settingsIntent = new Intent(this, SettingsActivity.class);
      startActivity(settingsIntent);
      return true;
    } else
      if (item.getItemId() == R.id.action_about) {
        Intent aboutIntent = new Intent(this, AboutActivity.class);
        startActivity(aboutIntent);
        return true;
      }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.equals(DEFAULT_INTERVAL)) {
      int interval = Integer.parseInt(sharedPreferences.getString(DEFAULT_INTERVAL, "30"));
      seekBar.setProgress(interval);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
  }
}
