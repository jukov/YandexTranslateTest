package info.jukov.yandextranslatetest.ui.screen.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.presenter.LangsLoaderPresenter;
import info.jukov.yandextranslatetest.presenter.LangsLoaderView;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 11:43
 */

public final class ScreenSplashActivity extends MvpAppCompatActivity implements LangsLoaderView {

	@InjectPresenter LangsLoaderPresenter presenter;

	@Override
	protected void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		ScreenMainActivity.start(this);
		finish();
	}


}
