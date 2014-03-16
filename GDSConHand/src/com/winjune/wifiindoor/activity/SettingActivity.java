package com.winjune.wifiindoor.activity;

import com.winjune.wifiindoor.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;




public class SettingActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 所的的值将会自动保存到SharePreferences
		addPreferencesFromResource(R.xml.setting);
	}
}


