package info.jukov.yandextranslatetest.ui.screen.fragment;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceGroup;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.util.ExtrasUtils;
import info.jukov.yandextranslatetest.util.StringUtils;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 8:01
 */

public final class SettingsFragment extends PreferenceFragmentCompat
	implements OnSharedPreferenceChangeListener {

	public static final String FRAGMENT_TAG = ExtrasUtils.createExtraName("FRAGMENT_TAG", SettingsFragment.class);

	public static SettingsFragment newInstance() {
		SettingsFragment fragment = new SettingsFragment();
		return fragment;
	}

	private static void initSummary(final Preference preference) {
		if (preference instanceof PreferenceGroup) {
			PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
			for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
				initSummary(preferenceGroup.getPreference(i));
			}
		} else {
			updatePreferenceSummary(preference);
		}
	}

	private static void updatePreferenceSummary(final Preference preference) {
		if (preference instanceof EditTextPreference) {
			EditTextPreference editTextPref = (EditTextPreference) preference;
			if (!StringUtils.isNullOrEmpty(editTextPref.getText())) {
				preference.setSummary(editTextPref.getText());
			} else {
				preference.setSummary(R.string.activityPreference_notSet);
			}
		}
	}

	private boolean apiKeysChanged = false;

	public boolean isApiKeysChanged() {
		return apiKeysChanged;
	}

	@Override
	public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
		addPreferencesFromResource(R.xml.preferences);
		initSummary(getPreferenceScreen());
	}

	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
			.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
			.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences,
										  final String key) {
		if (getString(R.string.preferenceKey_translateApiKey).equals(key) ||
			getString(R.string.preferenceKey_dictApiKey).equals(key)) {
			apiKeysChanged = true;
		}
		updatePreferenceSummary(findPreference(key));
	}
}
