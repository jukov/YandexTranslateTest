package info.jukov.yandextranslatetest.ui.screen.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceGroup;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.TranslateApp;
import info.jukov.yandextranslatetest.model.module.DatabaseModule;
import info.jukov.yandextranslatetest.util.ExtrasUtils;
import info.jukov.yandextranslatetest.util.StringUtils;
import javax.inject.Inject;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 8:01
 */

public final class SettingsFragment extends PreferenceFragmentCompat implements OnSharedPreferenceChangeListener {

	public static final String FRAGMENT_TAG = ExtrasUtils.createExtraName("FRAGMENT_TAG", SettingsFragment.class);

	private enum ConfirmablePreference {
		DELETE_HISTORY(
			R.string.alertDialogConfirmation_youSure_title,
			R.string.alertDialogConfirmation_deletingHistoryCannotBeUndone_title,
			R.string.alertDialogConfirmation_delete_button,
			R.string.alertDialogConfirmation_cancel_button),
		DELETE_FAVORITES(
			R.string.alertDialogConfirmation_youSure_title,
			R.string.alertDialogConfirmation_deletingFavoritesCannotBeUndone_title,
			R.string.alertDialogConfirmation_delete_button,
			R.string.alertDialogConfirmation_cancel_button);

		@StringRes final int titleRes;
		@StringRes final int messageRes;
		@StringRes final int positiveButtonTitleRes;
		@StringRes final int negativeButtonTitleRes;

		ConfirmablePreference(final int titleRes, final int messageRes, final int positiveButtonTitleRes,
			final int negativeButtonTitleRes) {
			this.titleRes = titleRes;
			this.messageRes = messageRes;
			this.positiveButtonTitleRes = positiveButtonTitleRes;
			this.negativeButtonTitleRes = negativeButtonTitleRes;
		}

		public int getTitleRes() {
			return titleRes;
		}

		public int getMessageRes() {
			return messageRes;
		}

		public int getPositiveButtonTitleRes() {
			return positiveButtonTitleRes;
		}

		public int getNegativeButtonTitleRes() {
			return negativeButtonTitleRes;
		}
	}

	@Inject DatabaseModule databaseModule;

	private boolean apiKeysChanged = false;

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

	public boolean isApiKeysChanged() {
		return apiKeysChanged;
	}

	@Override
	public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
		TranslateApp.getAppComponent().inject(this);

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
		if (getString(R.string.preferenceKey_translateApiKey).equals(key) || getString(R.string.preferenceKey_dictApiKey).equals(key)) {
			apiKeysChanged = true;
			updatePreferenceSummary(findPreference(key));
		}
	}

	@Override
	public boolean onPreferenceTreeClick(final Preference preference) {

		if (getString(R.string.preferenceKey_deleteHistory).equals(preference.getKey())) {
			makeConfirmDialog(ConfirmablePreference.DELETE_HISTORY).show();
		} else if (getString(R.string.preferenceKey_deleteFavorites).equals(preference.getKey())) {
			makeConfirmDialog(ConfirmablePreference.DELETE_FAVORITES).show();
		}

		return true;
	}

	private Dialog makeConfirmDialog(@NonNull final ConfirmablePreference confirmablePreference) {

		final AlertDialog.Builder builder = new Builder(getContext())
			.setTitle(confirmablePreference.getTitleRes())
			.setMessage(confirmablePreference.getMessageRes())
			.setPositiveButton(confirmablePreference.getPositiveButtonTitleRes(), new OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, final int which) {
					switch (confirmablePreference) {
						case DELETE_HISTORY:
							databaseModule.getDatabaseManager().deleteHistory();
							break;
						case DELETE_FAVORITES:
							databaseModule.getDatabaseManager().deleteFavorites();
							break;
					}
				}
			})
			.setNegativeButton(confirmablePreference.getNegativeButtonTitleRes(), new OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, final int which) {

				}
			});

		return builder.create();
	}
}
