package info.jukov.yandextranslatetest.ui.screen.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.adapter.TabAdapter;
import info.jukov.yandextranslatetest.model.network.ErrorCodes;
import info.jukov.yandextranslatetest.model.storage.dao.History;
import info.jukov.yandextranslatetest.ui.base.OnTextTranslatedListener;
import info.jukov.yandextranslatetest.ui.base.OnFavoriteStatusChangeListener;
import info.jukov.yandextranslatetest.ui.screen.fragment.FavoritesFragment;
import info.jukov.yandextranslatetest.ui.screen.fragment.HistoryFragment;
import info.jukov.yandextranslatetest.ui.screen.fragment.TranslateFragment;
import info.jukov.yandextranslatetest.util.ExtrasUtils;
import info.jukov.yandextranslatetest.util.Guard;
import info.jukov.yandextranslatetest.util.Log;

/**
 * User: jukov
 * Date: 14.03.2017
 * Time: 21:47
 */

public final class ScreenMainActivity extends AppCompatActivity implements
																OnTextTranslatedListener, OnFavoriteStatusChangeListener {

	public static final String ACTION_ERROR = ExtrasUtils
		.createExtraName("ACTION_ERROR", ScreenMainActivity.class);
	public static final String EXTRA_ERROR_CODE = ExtrasUtils
		.createExtraName("EXTRA_ERROR_CODE", ScreenMainActivity.class);

	private static final Log LOG = new Log(ScreenMainActivity.class);
	@BindView(R.id.toolbar) Toolbar toolbar;
	@BindView(R.id.tabLayout) TabLayout tabLayout;
	@BindView(R.id.viewPager) ViewPager viewPager;
	private TabAdapter tabAdapter;

	private final TranslateFragment translateFragment = TranslateFragment.newInstance();
	private final FavoritesFragment favoritesFragment = FavoritesFragment.newInstance();
	private final HistoryFragment historyFragment = HistoryFragment.newInstance();

	public static void start(@NonNull final Context context) {
		Guard.checkNotNull(context, "null == context");

		Intent starter = new Intent(context, ScreenMainActivity.class);
		context.startActivity(starter);
	}

	public static void startWithError(@NonNull final Context context, final int errorCode) {
		Guard.checkNotNull(context, "null == context");

		Intent starter = new Intent(context, ScreenMainActivity.class);
		starter.setAction(ACTION_ERROR);
		starter.putExtra(EXTRA_ERROR_CODE, errorCode);
		context.startActivity(starter);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);

		initTabLayout();

		if (getIntent() != null) {
			handleIntent(getIntent());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.toolbar_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.toolbarMenuSetttings:
				ScreenSettingsActivity.start(this);
				break;
			case R.id.toolbarMenuLicense:
				break;
			default:
				throw new IllegalStateException("Unexpected menu item");
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode,
		final Intent data) {

		if (requestCode == ScreenSettingsActivity.REQUEST_CODE_RESTART_AFTER_API_KEY_CHANGE &&
			resultCode == RESULT_OK) {
			ScreenSplashActivity.restartApp(this);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onTextTranslated(final History history) {
		historyFragment.addOrUpdateTranslation(history);
	}

	@Override
	public void onFavoriteStatusChange(final History history) {
		favoritesFragment.onFavoriteStatusChange(history);
		historyFragment.addOrUpdateTranslation(history);
	}

	private void handleIntent(@NonNull final Intent intent) {
		if (ACTION_ERROR.equals(intent.getAction())) {
			makeErrorDialog(intent.getIntExtra(EXTRA_ERROR_CODE, -1)).show();
		}
	}

	private void initTabLayout() {

		tabAdapter = new TabAdapter.Builder(this, getSupportFragmentManager())
			.addTab(translateFragment, R.string.fragmentTranslate_title)
			.addTab(favoritesFragment, R.string.fragmentFavorites_title)
			.addTab(historyFragment, R.string.fragmentHistory_title)
			.build();

		viewPager.setAdapter(tabAdapter);
		viewPager.setOffscreenPageLimit(3);

		tabLayout.setupWithViewPager(viewPager);
		tabAdapter.notifyDataSetChanged();
	}

	private Dialog makeErrorDialog(final int errorCode) {

		final AlertDialog.Builder builder = new Builder(this);

		final String title;
		final String text;
		final OnClickListener openSettingsListener = new OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				ScreenSettingsActivity.start(ScreenMainActivity.this);
			}
		};

		final OnClickListener exitListener = new OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				finish();
			}
		};

		final OnClickListener restartListener = new OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				ScreenSplashActivity.restartApp(ScreenMainActivity.this);
			}
		};

		switch (errorCode) {
			case ErrorCodes.WRONG_API_KEY:
				builder.setTitle(R.string.alertDialogInvalidKeys_wrongKeys_title)
					.setMessage(R.string.alertDialogInvalidKeys_wrongKeys_text)
					.setPositiveButton(R.string.alertDialogInvalidKeys_goToSettings_button,
						openSettingsListener);
				break;
			case ErrorCodes.BANNED_API_KEY:
				builder.setTitle(R.string.alertDialogInvalidKeys_bannedKeys_title)
					.setMessage(R.string.alertDialogInvalidKeys_bannedKeys_text)
					.setPositiveButton(R.string.alertDialogInvalidKeys_goToSettings_button,
						openSettingsListener);
				break;
			case ErrorCodes.DAILY_LIMIT_EXCEEDED:
				builder.setTitle(R.string.alertDialogInvalidKeys_exceededDailyLimit_title)
					.setMessage(R.string.alertDialogInvalidKeys_exceededDailyLimit_text)
					.setPositiveButton(R.string.alertDialogInvalidKeys_goToSettings_button,
						openSettingsListener);
				break;
			case ErrorCodes.KEYS_NOT_SET_CUSTOM:
				builder.setTitle(R.string.alertDialogInvalidKeys_keysNotSet_title)
					.setMessage(R.string.alertDialogInvalidKeys_keysNotSet_text)
					.setPositiveButton(R.string.alertDialogInvalidKeys_goToSettings_button,
						openSettingsListener);
				break;
			case ErrorCodes.NETWORK_ERROR_CUSTOM:
				builder.setTitle(R.string.alertDialogInvalidKeys_networkError_title)
					.setMessage(R.string.alertDialogInvalidKeys_networkError_text)
					.setPositiveButton(R.string.alertDialogInvalidKeys_tryAgain_button,
						restartListener);
				break;
			default:
				LOG.warning("Unexpected error code");
				builder.setTitle(R.string.alertDialogInvalidKeys_unexpectedError_title)
					.setMessage(R.string.alertDialogInvalidKeys_unexpectedError_text);
		}

		builder.setNegativeButton(R.string.alertDialogInvalidKeys_exit_button, exitListener);

		final Dialog dialog = builder.create();

		dialog.setCanceledOnTouchOutside(false);

		return dialog;
	}
}
