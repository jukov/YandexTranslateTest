package info.jukov.yandextranslatetest.model.component;

import dagger.Component;
import info.jukov.yandextranslatetest.model.module.ApiModule;
import info.jukov.yandextranslatetest.model.module.ContextModule;
import info.jukov.yandextranslatetest.model.module.DatabaseModule;
import info.jukov.yandextranslatetest.model.module.TransferModule;
import info.jukov.yandextranslatetest.presenter.ApiKeysCheckPresenter;
import info.jukov.yandextranslatetest.presenter.FavoritesPresenter;
import info.jukov.yandextranslatetest.presenter.HistoryPresenter;
import info.jukov.yandextranslatetest.presenter.LangsLoaderPresenter;
import info.jukov.yandextranslatetest.presenter.MainPresenter;
import info.jukov.yandextranslatetest.presenter.TranslatePresenter;
import info.jukov.yandextranslatetest.ui.screen.fragment.BaseTranslationListFragment;
import info.jukov.yandextranslatetest.ui.screen.fragment.SettingsFragment;
import javax.inject.Singleton;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 18:08
 */
@Component(modules = {ContextModule.class, ApiModule.class, DatabaseModule.class, TransferModule.class})
@Singleton
public interface AppComponent {

	void inject(LangsLoaderPresenter target);

	void inject(TranslatePresenter target);

	void inject(FavoritesPresenter target);

	void inject(HistoryPresenter target);

	void inject(ApiKeysCheckPresenter target);

	void inject(BaseTranslationListFragment target);

	void inject(SettingsFragment target);

	void inject(MainPresenter target);
}
