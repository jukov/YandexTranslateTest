package info.jukov.yandextranslatetest.ui.screen.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.ui.TabAdapter;
import info.jukov.yandextranslatetest.ui.screen.favorites.FavoritesFragment;
import info.jukov.yandextranslatetest.ui.screen.history.HistoryFragment;
import info.jukov.yandextranslatetest.ui.screen.translate.TranslateFragment;

/**
 * User: jukov
 * Date: 14.03.2017
 * Time: 21:47
 */

public final class ScreenMainActivity extends MvpAppCompatActivity implements ScreenMainView {

	@InjectPresenter
	ScreenMainPresenter presenter;

	@BindView(R.id.toolbar) Toolbar toolbar;
	@BindView(R.id.tabLayout) TabLayout tabLayout;
	@BindView(R.id.viewPager) ViewPager viewPager;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);

		initTabLayout();
	}

	private void initTabLayout() {

		TabAdapter tabAdapter = new TabAdapter.Builder(this, getSupportFragmentManager())
			.addTab(TranslateFragment.newInstance(), R.string.fragmentTranslate_title)
			.addTab(FavoritesFragment.newInstance(), R.string.fragmentFavorites_title)
			.addTab(HistoryFragment.newInstance(), R.string.fragmentHistory_title)
			.build();

		viewPager.setAdapter(tabAdapter);
		tabLayout.setupWithViewPager(viewPager);
		tabAdapter.notifyDataSetChanged();
	}
}
