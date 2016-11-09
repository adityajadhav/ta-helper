package com.cs442.team4.tahelper.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.cs442.team4.tahelper.R;

public class MyPreferenceFragment extends PreferenceFragment {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.userpreferences);
  }
}