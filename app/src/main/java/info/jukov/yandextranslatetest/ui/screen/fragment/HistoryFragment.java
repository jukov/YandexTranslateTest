package info.jukov.yandextranslatetest.ui.screen.fragment;

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
import info.jukov.yandextranslatetest.model.adapter.HistoryAdapter;
import info.jukov.yandextranslatetest.model.storage.Translation;
import info.jukov.yandextranslatetest.util.Guard;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:31
 */

public final class HistoryFragment extends MvpAppCompatFragment {

	public static HistoryFragment newInstance() {

		Bundle args = new Bundle();

		HistoryFragment fragment = new HistoryFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@BindView(R.id.recyclerViewHistory) RecyclerView recyclerViewHistory;

	private HistoryAdapter historyAdapter;

	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
							 @Nullable final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_history, null);
		ButterKnife.bind(this, view);

		initRecycler();

		return view;
	}

	private void initRecycler() {

		historyAdapter = new HistoryAdapter(getContext());

		recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerViewHistory.setAdapter(historyAdapter);
	}

	public void addTranslation(@NonNull final Translation translation) {
		Guard.checkNotNull(translation, "null == translation");

		historyAdapter.addTranslation(translation);
	}
}
