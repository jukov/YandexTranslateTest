package info.jukov.yandextranslatetest.ui.screen.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.util.ExtrasUtils;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 8:01
 */

public final class SettingsFragment extends PreferenceFragmentCompat {

	public static final String FRAGMENT_TAG = ExtrasUtils.createExtraName("FRAGMENT_TAG", SettingsFragment.class);

	public static SettingsFragment newInstance() {
		SettingsFragment fragment = new SettingsFragment();
		return fragment;
	}

	@Override
	public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
		addPreferencesFromResource(R.xml.preferences);
	}
}
