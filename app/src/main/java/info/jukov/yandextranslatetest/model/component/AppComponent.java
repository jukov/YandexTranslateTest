package info.jukov.yandextranslatetest.model.component;

import dagger.Component;
import info.jukov.yandextranslatetest.model.module.ApiModule;
import info.jukov.yandextranslatetest.model.module.ContextModule;
import info.jukov.yandextranslatetest.presenter.ApiKeysCheckPresenter;
import info.jukov.yandextranslatetest.presenter.LangsLoaderPresenter;
import info.jukov.yandextranslatetest.presenter.TranslatePresenter;
import javax.inject.Singleton;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 18:08
 */
@Component(modules = {ContextModule.class, ApiModule.class})
@Singleton
public interface AppComponent {

	void inject(LangsLoaderPresenter presenter);

	void inject(TranslatePresenter presenter);

	void inject(ApiKeysCheckPresenter presenter);
}
