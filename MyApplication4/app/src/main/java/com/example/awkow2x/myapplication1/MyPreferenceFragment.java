package com.example.awkow2x.myapplication1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

public class MyPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private String SWITCH1 ;
    private String SWITCH2 ;

    private SwitchPreference isSwitch1Preferences;
    private SwitchPreference isSwitch2Preferences;

    private AlarmHelper alarmHelper;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        alarmHelper = new AlarmHelper();
        init();
        setSummaries();
    }

    private void init() {
        SWITCH1 = getResources().getString(R.string.key_switch1);
        SWITCH2 = getResources().getString(R.string.key_switch2);

        isSwitch1Preferences = (SwitchPreference) findPreference(SWITCH1);
        isSwitch2Preferences = (SwitchPreference) findPreference(SWITCH2);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(SWITCH1)) {
            isSwitch1Preferences.setChecked(sharedPreferences.getBoolean(SWITCH1, false));
            if (sharedPreferences.getBoolean(SWITCH1, false) == true) {
                alarmHelper.setRepeatingAlarm(getContext(), AlarmHelper.TYPE_NOTIF_1, AlarmHelper.ID_NOTIF_1,
                        "09:47", getResources().getString(R.string.msg));
            }
            else if (sharedPreferences.getBoolean(SWITCH1, false) == false) {
                Intent intent = new Intent(getContext(), AlarmHelper.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alarmHelper.ID_NOTIF_1, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            }
        }
        if (key.equals(SWITCH2)) {
            isSwitch2Preferences.setChecked(sharedPreferences.getBoolean(SWITCH2, false));
        }
    }

    private void setSummaries() {
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();
        isSwitch1Preferences.setChecked(sh.getBoolean(SWITCH1, false));
        isSwitch2Preferences.setChecked(sh.getBoolean(SWITCH2, false));
    }
}
