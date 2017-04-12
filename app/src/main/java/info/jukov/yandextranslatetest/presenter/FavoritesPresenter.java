package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import info.jukov.yandextranslatetest.TranslateApp;
import info.jukov.yandextranslatetest.model.module.DatabaseModule;
import info.jukov.yandextranslatetest.model.storage.dao.DatabaseManager.DatabaseListener;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import javax.inject.Inject;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:35
 */
@InjectViewState
public final class FavoritesPresenter extends MvpPresenter<FavoritesView> implements DatabaseListener {

	@Inject DatabaseModule databaseModule;

	@Override
	protected void onFirstViewAttach() {
		TranslateApp.getAppComponent().inject(this);

		getViewState().onFavoritesFromDatabase(databaseModule.getDatabaseManager().getFavoritesList());

		databaseModule.getDatabaseManager().addOnTranslateAddedListener(this);
	}

	@Override
	public void onTranslateProcessed(final Translation translation) {
		getViewState().onNewFavorite(translation);
	}

	@Override
	public void onTranslateDeleted(final Translation translation) {
		getViewState().onDeleteFavorite(translation);
	}

	@Override
	public void onFavoritesDeleted() {
		getViewState().deleteFavorites();
	}

	@Override
	public void onHistoryDeleted() {
		//Unused
	}
}
