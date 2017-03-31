package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import info.jukov.yandextranslatetest.TranslateApp;
import info.jukov.yandextranslatetest.model.module.DatabaseModule;
import info.jukov.yandextranslatetest.model.storage.dao.DatabaseManager.OnTranslateProcessedListener;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import javax.inject.Inject;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:35
 */
@InjectViewState
public final class HistoryPresenter extends MvpPresenter<HistoryView> implements OnTranslateProcessedListener {

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
}
