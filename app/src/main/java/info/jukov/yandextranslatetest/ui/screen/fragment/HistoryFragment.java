package info.jukov.yandextranslatetest.ui.screen.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.TranslateApp;
import info.jukov.yandextranslatetest.model.adapter.HistoryAdapter;
import info.jukov.yandextranslatetest.model.module.DatabaseModule;
import info.jukov.yandextranslatetest.model.storage.dao.DatabaseManager;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.presenter.HistoryPresenter;
import info.jukov.yandextranslatetest.presenter.HistoryView;
import info.jukov.yandextranslatetest.util.Log;
import java.util.List;
import javax.inject.Inject;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:31
 */

public final class HistoryFragment extends MvpAppCompatFragment implements HistoryView {

	private static final Log LOG = new Log(HistoryFragment.class);

	@BindView(R.id.recyclerViewHistory) RecyclerView recyclerViewHistory;

	private HistoryAdapter historyAdapter;

	@Inject DatabaseModule databaseModule;

	@InjectPresenter HistoryPresenter presenter;

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
		TranslateApp.getAppComponent().inject(this);

		initRecycler();

		return view;
	}

	@Override
	public void onTranslationsFromDatabase(final List<Translation> translationList) {
		historyAdapter.setTranslations(translationList);
	}

	@Override
	public void onNewTranslation(final Translation translation) {
		historyAdapter.processTranslation(translation);
	}

	@Override
	public void onHistoryDeleted() {
		historyAdapter.deleteHistory();
	}

	@Override
	public void onFavoritesDeleted() {
		historyAdapter.deleteFavorites();
	}

	private void initRecycler() {

		historyAdapter = new HistoryAdapter(getContext(), databaseModule.getDatabaseManager());

		recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerViewHistory.setAdapter(historyAdapter);
	}
}
