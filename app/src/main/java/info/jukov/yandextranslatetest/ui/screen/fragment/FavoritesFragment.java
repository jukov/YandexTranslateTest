package info.jukov.yandextranslatetest.ui.screen.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.arellomobile.mvp.presenter.InjectPresenter;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.adapter.FavoritesAdapter;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.presenter.FavoritesPresenter;
import info.jukov.yandextranslatetest.presenter.FavoritesView;
import info.jukov.yandextranslatetest.util.Guard;
import java.util.List;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:31
 */

public final class FavoritesFragment extends BaseTranslationsListFragment implements FavoritesView {

	@InjectPresenter FavoritesPresenter favoritesPresenter;

	public static FavoritesFragment newInstance() {
		FavoritesFragment fragment = new FavoritesFragment();
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
	public void onFavoritesFromDatabase(final List<Translation> translationList) {
		adapter.setTranslations(translationList);
	}

	@Override
	public void onNewFavorite(final Translation translation) {
		adapter.processTranslation(translation);
	}

	@Override
	public void onDeleteFavorite(final Translation translation) {
		adapter.deleteTranslation(translation);
	}

	@Override
	public void deleteFavorites() {
		adapter.deleteFavorites();
	}

	@Override
	public void viewFullTranslation(@NonNull final Translation translation) {
		transferModule.getTransferManager().onFullTranslate(translation);
	}

	@Override
	public void onDataSetChange(final int currentSize) {
		if (currentSize == 0) {
			switchUiToEmptySplash();
		} else {
			switchUiToList();
		}
	}

	private void switchUiToEmptySplash() {
		textViewEmptyList.setVisibility(View.VISIBLE);
		recyclerViewHistory.setVisibility(View.GONE);
	}

	private void switchUiToList() {
		textViewEmptyList.setVisibility(View.GONE);
		recyclerViewHistory.setVisibility(View.VISIBLE);
	}

	public void onFavoriteStatusChange(@NonNull final Translation translation) {
		Guard.checkNotNull(translation, "null == translation");

		adapter.processTranslation(translation);
	}

	private void initUi() {
		textViewEmptyList.setText(R.string.favoritesFragment_textViewEmptyList);
	}

	private void initRecycler() {

		adapter = new FavoritesAdapter(getContext(), databaseModule.getDatabaseManager(), this, this);

		recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerViewHistory.setAdapter(adapter);
	}
}
