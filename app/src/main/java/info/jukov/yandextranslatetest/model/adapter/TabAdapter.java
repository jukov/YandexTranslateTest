package info.jukov.yandextranslatetest.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public final class TabAdapter extends FragmentStatePagerAdapter {

	private static final int EXPECTED_TAB_COUNT = 3;

	private final ArrayList<TabWrapper> tabs;
	private final Context context;

	private TabAdapter(final Context context, final FragmentManager fragmentManager) {
		super(fragmentManager);
		Guard.checkNotNull(fragmentManager, "null == fragmentManager");
		Guard.checkNotNull(context, "null == context");

		this.context = context;

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

	@Nullable
	public MvpAppCompatFragment getItemByTitle(@StringRes final int titleRes) {

		for (final TabWrapper tabWrapper : tabs) {
			if (tabWrapper.getTitle().equals(context.getString(titleRes))) {
				return tabWrapper.getFragment();
			}
		}

		return null;
	}

	@Override
	public int getCount() {
		return tabs.size();
	}

	public static class Builder {

		private final TabAdapter tabAdapter;

		private final Context context;

		public Builder(@NonNull final Context context, @NonNull final FragmentManager fragmentManager) {
			Guard.checkNotNull(context, "null == context");
			Guard.checkNotNull(fragmentManager, "null == fragmentManager");

			this.context = context;
			tabAdapter = new TabAdapter(context, fragmentManager);
		}

		public Builder addTab(@NonNull final MvpAppCompatFragment fragment, @StringRes final int titleRes) {
			Guard.checkNotNull(fragment, "null == fragment");

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

		private TabWrapper(@NonNull final MvpAppCompatFragment fragment, @StringRes final String title) {
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
