package info.jukov.yandextranslatetest.ui.screen.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.arellomobile.mvp.presenter.InjectPresenter;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.adapter.HistoryAdapter;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.presenter.HistoryPresenter;
import info.jukov.yandextranslatetest.presenter.HistoryView;
import java.util.List;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:31
 */

public final class HistoryFragment extends BaseTranslationsListFragment implements HistoryView {

	@InjectPresenter HistoryPresenter historyPresenter;

	public static HistoryFragment newInstance() {
		HistoryFragment fragment = new HistoryFragment();
		return fragment;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
		@Nullable final Bundle savedInstanceState) {
		final View view = super.onCreateView(inflater, container, savedInstanceState);

		initUi();
		initRecycler();

		return view;
	}

	@Override
	public void onHistoryFromDatabase(final List<Translation> translationList) {
		adapter.setTranslations(translationList);
	}

	@Override
	public void onNewTranslation(final Translation translation) {
		adapter.processTranslation(translation);
	}

	@Override
	public void onDeleteTranslation(final Translation translation) {
		adapter.deleteTranslation(translation);
	}

	@Override
	public void onHistoryDeleted() {
		adapter.deleteHistory();
	}

	@Override
	public void onFavoritesDeleted() {
		adapter.deleteFavorites();
	}

	private void initUi() {
		textViewEmptyList.setText(R.string.historyFragment_textViewEmptyList);
	}

	private void initRecycler() {

		adapter = new HistoryAdapter(getContext(), databaseModule.getDatabaseManager(), this, this);

		recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerViewHistory.setAdapter(adapter);
	}
}
