package info.jukov.yandextranslatetest.ui.screen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.presenter.ScreenMainPresenter;
import info.jukov.yandextranslatetest.presenter.ScreenMainView;
import info.jukov.yandextranslatetest.ui.TabAdapter;
import info.jukov.yandextranslatetest.ui.screen.fragment.FavoritesFragment;
import info.jukov.yandextranslatetest.ui.screen.fragment.HistoryFragment;
import info.jukov.yandextranslatetest.ui.screen.fragment.TranslateFragment;

/**
 * User: jukov
 * Date: 14.03.2017
 * Time: 21:47
 */

public final class ScreenMainActivity extends MvpAppCompatActivity implements ScreenMainView {

	public static void start(final Context context) {
		Intent starter = new Intent(context, ScreenMainActivity.class);
		context.startActivity(starter);
	}

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

	private void initTabLayout() {

		final TabAdapter tabAdapter = new TabAdapter.Builder(this, getSupportFragmentManager())
			.addTab(TranslateFragment.newInstance(), R.string.fragmentTranslate_title)
			.addTab(FavoritesFragment.newInstance(), R.string.fragmentFavorites_title)
			.addTab(HistoryFragment.newInstance(), R.string.fragmentHistory_title)
			.build();

		viewPager.setAdapter(tabAdapter);
		tabLayout.setupWithViewPager(viewPager);
		tabAdapter.notifyDataSetChanged();
	}
}
