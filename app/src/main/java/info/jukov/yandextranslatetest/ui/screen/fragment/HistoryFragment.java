package info.jukov.yandextranslatetest.ui.screen.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.MvpAppCompatFragment;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.TranslateApp;
import info.jukov.yandextranslatetest.model.adapter.HistoryAdapter;
import info.jukov.yandextranslatetest.model.storage.dao.History;
import info.jukov.yandextranslatetest.ui.base.OnFavoriteStatusChangeListener;
import info.jukov.yandextranslatetest.ui.screen.activity.ScreenMainActivity;
import info.jukov.yandextranslatetest.util.Guard;
import info.jukov.yandextranslatetest.util.Log;
import java.util.List;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:31
 */

public final class HistoryFragment extends MvpAppCompatFragment {

	private static final Log LOG = new Log(HistoryFragment.class);

	@BindView(R.id.recyclerViewHistory) RecyclerView recyclerViewHistory;
	private HistoryAdapter historyAdapter;

	private OnFavoriteStatusChangeListener onFavoriteStatusChangeListener;

	public static HistoryFragment newInstance() {

		Bundle args = new Bundle();

		HistoryFragment fragment = new HistoryFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
		@Nullable final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_history, null);
		ButterKnife.bind(this, view);

		initRecycler();

		return view;
	}

	@Override
	public void onAttach(final Context context) {
		super.onAttach(context);

		if (context instanceof ScreenMainActivity) {
			onFavoriteStatusChangeListener = (ScreenMainActivity) context;
		} else {
			LOG.error("Fragment attached to unexpected activity");
		}
	}

	private void initRecycler() {

		final List<History> historyList = TranslateApp.getDaoSession().getHistoryDao().loadAll();

		historyAdapter = new HistoryAdapter(getContext(), TranslateApp.getDaoSession().getHistoryDao(), historyList,
			onFavoriteStatusChangeListener);

		recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerViewHistory.setAdapter(historyAdapter);
	}

	public void addOrUpdateTranslation(@NonNull final History history) {
		Guard.checkNotNull(history, "null == history");

		historyAdapter.addOrUpdateHistoryItem(history);
	}
}
