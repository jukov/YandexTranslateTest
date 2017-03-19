package info.jukov.yandextranslatetest.ui.screen.activity;

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

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 7:17
 */

public final class ScreenSettingsActivity extends AppCompatActivity {

	public static final int REQUEST_CODE_RESTART_AFTER_API_KEY_CHANGE = 655;

	public static void start(final AppCompatActivity activity) {
		Intent starter = new Intent(activity, ScreenSettingsActivity.class);
		activity.startActivityForResult(starter, REQUEST_CODE_RESTART_AFTER_API_KEY_CHANGE);
	}

	@BindView(R.id.toolbar) Toolbar toolbar;

	private SettingsFragment settingsFragment;

	@Override
	protected void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		settingsFragment = SettingsFragment.newInstance();

		final FragmentTransaction fragmentTransaction = getSupportFragmentManager()
			.beginTransaction();
		fragmentTransaction
			.replace(R.id.fragmentContainer, settingsFragment, SettingsFragment.FRAGMENT_TAG);
		fragmentTransaction.commit();
	}

	@Override
	public boolean onSupportNavigateUp() {
		if (settingsFragment.isApiKeysChanged()) {
			setResult(RESULT_OK);
		} else {
			setResult(RESULT_CANCELED);
		}
		finish();
		return true;
	}
}