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
import com.arellomobile.mvp.presenter.InjectPresenter;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.TranslateApp;
import info.jukov.yandextranslatetest.model.adapter.FavoritesAdapter;
import info.jukov.yandextranslatetest.model.module.DatabaseModule;
import info.jukov.yandextranslatetest.model.module.TransferModule;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.presenter.FavoritesPresenter;
import info.jukov.yandextranslatetest.presenter.FavoritesView;
import info.jukov.yandextranslatetest.ui.base.TranslateListHolder;
import info.jukov.yandextranslatetest.ui.screen.activity.ScreenMainActivity;
import info.jukov.yandextranslatetest.util.Guard;
import info.jukov.yandextranslatetest.util.Log;
import java.util.List;
import javax.inject.Inject;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:31
 */

public final class FavoritesFragment extends MvpAppCompatFragment implements FavoritesView, TranslateListHolder {

	private static final Log LOG = new Log(HistoryFragment.class);

	@BindView(R.id.recyclerViewHistory) RecyclerView recyclerViewHistory;

	private FavoritesAdapter favoritesAdapter;

	@Inject DatabaseModule databaseModule;
	@Inject TransferModule transferModule;

	@InjectPresenter FavoritesPresenter presenter;

	public static FavoritesFragment newInstance() {
		FavoritesFragment fragment = new FavoritesFragment();
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
	public void onFavoritesFromDatabase(final List<Translation> translationList) {
		favoritesAdapter.setTranslations(translationList);
	}

	@Override
	public void onNewFavorite(final Translation translation) {
		favoritesAdapter.processFavorite(translation);
	}

	@Override
	public void deleteFavorites() {
		favoritesAdapter.deleteFavorites();
	}

	@Override
	public void viewFullTranslation(@NonNull final Translation translation) {
		transferModule.getTransferManager().onFullTranslate(translation);
	}

	public void onFavoriteStatusChange(@NonNull final Translation translation) {
		Guard.checkNotNull(translation, "null == translation");

		favoritesAdapter.processFavorite(translation);
	}

	private void initRecycler() {

		favoritesAdapter = new FavoritesAdapter(getContext(), databaseModule.getDatabaseManager(), this);

		recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerViewHistory.setAdapter(favoritesAdapter);
	}
}
