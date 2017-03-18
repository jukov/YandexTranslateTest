package info.jukov.yandextranslatetest.ui;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.arellomobile.mvp.MvpAppCompatFragment;
import info.jukov.yandextranslatetest.util.Guard;
import java.util.ArrayList;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 20:27
 */

public class TabAdapter extends FragmentStatePagerAdapter {

	private static final int EXPECTED_TAB_COUNT = 3;

	private final ArrayList<TabWrapper> tabs;

	private TabAdapter(final FragmentManager fragmentManager) {
		super(fragmentManager);
		Guard.checkNotNull(fragmentManager, "null == fragmentManager");

		tabs = new ArrayList<>(EXPECTED_TAB_COUNT);
	}

	@Override
	public CharSequence getPageTitle(final int position) {
		return tabs.get(position).getTitle();
	}

	@Override
	public Fragment getItem(final int position) {
		return tabs.get(position).getFragment();
	}

	@Override
	public int getCount() {
		return tabs.size();
	}

	public static class Builder {

		private final TabAdapter tabAdapter;

		private final Context context;

		public Builder(final Context context, final FragmentManager fm) {
			this.context = context;
			tabAdapter = new TabAdapter(fm);
		}

		public Builder addTab(final MvpAppCompatFragment fragment, @StringRes final int titleRes) {
			tabAdapter.tabs.add(new TabWrapper(fragment, context.getString(titleRes)));
			return this;
		}

		public TabAdapter build() {
			return tabAdapter;
		}

	}

	private static class TabWrapper {

		@StringRes private final String title;
		private final MvpAppCompatFragment fragment;

		private TabWrapper(final MvpAppCompatFragment fragment, @StringRes final String title) {
			this.fragment = fragment;
			this.title = title;
		}

		public String getTitle() {
			return title;
		}

		public MvpAppCompatFragment getFragment() {
			return fragment;
		}
	}
}
