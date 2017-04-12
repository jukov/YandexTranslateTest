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
public final class HistoryPresenter extends MvpPresenter<HistoryView> implements DatabaseListener {

	@Inject DatabaseModule databaseModule;

	@Override
	protected void onFirstViewAttach() {
		TranslateApp.getAppComponent().inject(this);

		getViewState().onTranslationsFromDatabase(databaseModule.getDatabaseManager().getTranslationList());

		databaseModule.getDatabaseManager().addOnTranslateAddedListener(this);
	}

	@Override
	public void onTranslateProcessed(final Translation translation) {
		getViewState().onNewTranslation(translation);
	}

	@Override
	public void onTranslateDeleted(final Translation translation) {
		getViewState().onDeleteTranslation(translation);
	}

	@Override
	public void onHistoryDeleted() {
		getViewState().onHistoryDeleted();
	}

	@Override
	public void onFavoritesDeleted() {
		getViewState().onFavoritesDeleted();
	}
}
