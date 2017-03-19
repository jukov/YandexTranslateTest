package info.jukov.yandextranslatetest.ui.screen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.network.ErrorCodes;
import info.jukov.yandextranslatetest.presenter.ApiKeysCheckPresenter;
import info.jukov.yandextranslatetest.presenter.ApiKeysCheckView;
import info.jukov.yandextranslatetest.presenter.LangsLoaderPresenter;
import info.jukov.yandextranslatetest.presenter.LangsLoaderView;
import info.jukov.yandextranslatetest.util.ExtrasUtils;
import info.jukov.yandextranslatetest.util.Log;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 11:43
 */

public final class ScreenSplashActivity extends MvpAppCompatActivity
	implements LangsLoaderView, ApiKeysCheckView {

	public static final String ACTION_RESTART = ExtrasUtils
		.createExtraName("ACTION_RESTART", ScreenSplashActivity.class);

	private static final Log LOG = new Log(ScreenSplashActivity.class);

	public static void restartApp(@NonNull final AppCompatActivity activity) {
		final Intent intent = activity.getBaseContext().getPackageManager()
			.getLaunchIntentForPackage(activity.getPackageName())
			.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		activity.startActivity(intent);
		activity.finish();

		Runtime.getRuntime().exit(0);
	}

	@InjectPresenter LangsLoaderPresenter langsLoaderPresenter;
	@InjectPresenter ApiKeysCheckPresenter apiKeysCheckPresenter;

	@Override
	protected void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		LOG.info(Boolean.toString(null == savedInstanceState));
	}

	@Override
	protected void onStart() {
		super.onStart();
		apiKeysCheckPresenter.check();
	}

	@Override
	public void onLoadFinished() {
		ScreenMainActivity.start(this);
		finish();
	}

	@Override
	public void onLoadFailed(final int errorCode) {
		ScreenMainActivity.startWithError(this, errorCode);
		finish();
	}

	@Override
	public void onKeysEntered() {
		langsLoaderPresenter.getLangs(getString(R.string.lang_code));
	}

	@Override
	public void onKeysNotEntered() {
		ScreenMainActivity.startWithError(this, ErrorCodes.KEYS_NOT_SET_CUSTOM);
		finish();
	}
}
