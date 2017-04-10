package info.jukov.yandextranslatetest.ui.screen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.ui.screen.fragment.SettingsFragment;
import info.jukov.yandextranslatetest.util.ExtrasUtils;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 7:17
 */

public final class SettingsActivity extends AppCompatActivity {

	public static final int REQUEST_CODE_RESTART_AFTER_API_KEY_CHANGE = 655;

	private static final String EXTRA_FOR_KEY_CHANGE = ExtrasUtils.createExtraName("EXTRA_FOR_KEY_CHANGE", SettingsActivity.class);

	@BindView(R.id.toolbar) Toolbar toolbar;

	private SettingsFragment settingsFragment;

	private boolean isForKeyChange = false;

	public static void start(final Activity activity) {
		Intent starter = new Intent(activity, SettingsActivity.class);
		activity.startActivityForResult(starter, REQUEST_CODE_RESTART_AFTER_API_KEY_CHANGE);
	}

	public static void startForKeyChange(final Activity activity) {
		Intent starter = new Intent(activity, SettingsActivity.class);
		starter.putExtra(EXTRA_FOR_KEY_CHANGE, true);
		activity.startActivityForResult(starter, REQUEST_CODE_RESTART_AFTER_API_KEY_CHANGE);
	}

	@Override
	protected void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		ButterKnife.bind(this);

		if (getIntent() != null) {
			isForKeyChange = getIntent().getBooleanExtra(EXTRA_FOR_KEY_CHANGE, false);
		}

		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		settingsFragment = SettingsFragment.newInstance();

		final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.fragmentContainer, settingsFragment, SettingsFragment.FRAGMENT_TAG);
		fragmentTransaction.commit();
	}

	@Override
	public boolean onSupportNavigateUp() {
		if (settingsFragment.isNeedToRestart() || isForKeyChange) {
			setResult(RESULT_OK);
		} else {
			setResult(RESULT_CANCELED);
		}
		finish();
		return true;
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		outState.putBoolean(EXTRA_FOR_KEY_CHANGE, isForKeyChange);
	}

	@Override
	protected void onRestoreInstanceState(final Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		isForKeyChange = savedInstanceState.getBoolean(EXTRA_FOR_KEY_CHANGE, false);
	}
}
