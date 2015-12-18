package com.example.ruandongchuan.doubanmovie.ui.fragment;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.example.ruandongchuan.doubanmovie.R;
import com.example.ruandongchuan.doubanmovie.constants.Setting;
import com.example.ruandongchuan.doubanmovie.util.ImageLoader.ImageLoader;
import com.example.ruandongchuan.doubanmovie.util.LogTool;
import com.example.ruandongchuan.doubanmovie.util.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private ListPreference mImageQuality;
    private ListPreference mLogoQuality;
    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);
        Preference clear = findPreference("clear");
        String cacheSize = Util.getRealSize(ImageLoader.getmInstance().getFileCacheSize());
        clear.setSummary(cacheSize);
        clear.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ImageLoader.getmInstance().clearFileCache();
                String cacheSize = Util.getRealSize(ImageLoader.getmInstance().getFileCacheSize());
                preference.setSummary(cacheSize);
                return false;
            }
        });
        mImageQuality = (ListPreference) findPreference("imageQuality");
        mLogoQuality = (ListPreference) findPreference("logoQuality");
        mImageQuality.setSummary(mImageQuality.getEntry());
        mLogoQuality.setSummary(mLogoQuality.getEntry());
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().
                registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("swipe_load_image")){
            ImageLoader.getmInstance().setIsSwipeLoadImage(sharedPreferences.getBoolean("swipe_load_image", false));
            LogTool.i("swipe_load_image", sharedPreferences.getBoolean("swipe_load_image", false) + "");
        }
        if (key.equals("use_mobile")){
            boolean bl = sharedPreferences.getBoolean("use_mobile",false);
            Setting.setIsAllowMobile(bl);
        }
        if (key.equals("mImageQuality")){
            mImageQuality.setSummary(mImageQuality.getEntry());
            Setting.setImageQuality(Integer.parseInt(mImageQuality.getValue()));
        }
        if (key.equals("mLogoQuality")){
            mLogoQuality.setSummary(mLogoQuality.getEntry());
            Setting.setLogoQuality(Integer.parseInt(mLogoQuality.getValue()));
        }
    }
}
