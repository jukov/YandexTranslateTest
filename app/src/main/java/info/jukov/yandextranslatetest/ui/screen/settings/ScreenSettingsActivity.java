package info.jukov.yandextranslatetest.ui.screen.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import info.jukov.yandextranslatetest.R;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 7:17
 */

public class ScreenSettingsActivity extends AppCompatActivity {

	public static void start(final Context context) {
		Intent starter = new Intent(context, ScreenSettingsActivity.class);
		context.startActivity(starter);
	}

	@BindView(R.id.toolbar) Toolbar toolbar;

	@Override
	protected void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		SettingsFragment settingsFragment = SettingsFragment.newInstance();

		final FragmentTransaction fragmentTransaction = getSupportFragmentManager()
			.beginTransaction();
		fragmentTransaction
			.replace(R.id.fragmentContainer, settingsFragment, SettingsFragment.FRAGMENT_TAG);
		fragmentTransaction.commit();
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}
}
