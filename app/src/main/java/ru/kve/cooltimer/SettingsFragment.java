package ru.kve.cooltimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    addPreferencesFromResource(R.xml.timer_preferences);
    SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
    int count = getPreferenceScreen().getPreferenceCount();
    for (int i = 0; i < count; i++) {
      onSharedPreferenceChanged(sharedPreferences, getPreferenceScreen().getPreference(i).getKey());
    }

    Preference preference = findPreference("default_interval");
    preference.setOnPreferenceChangeListener(this);
  }

  @Override
  public boolean onPreferenceChange(Preference preference, Object newValue) {
    if (preference.getKey().equals("default_interval")) {
      try {
        int interval = Integer.parseInt((String) newValue);
        if (interval < 0 || interval > 600) {
          Toast.makeText(getContext(), R.string.interval_between_msg, Toast.LENGTH_LONG).show();
        } else {
          return true;
        }
      } catch (NumberFormatException err) {
        Toast.makeText(getContext(), R.string.interval_must_be_integer, Toast.LENGTH_LONG).show();
      }
    }
    return false;
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Preference pref = findPreference(key);
    if (pref instanceof EditTextPreference) {
      EditTextPreference editTextPref = (EditTextPreference) pref;
      pref.setSummary(editTextPref.getText());
    } else
      if (pref instanceof ListPreference) {
        ListPreference listPref = (ListPreference) pref;
        String value = sharedPreferences.getString(key, "");
        int index = listPref.findIndexOfValue(value);
        if (index >= 0) {
          pref.setSummary(listPref.getEntries()[index]);
        }
      }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
  }

}
