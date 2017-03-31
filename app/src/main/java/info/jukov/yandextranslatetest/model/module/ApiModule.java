package info.jukov.yandextranslatetest.model.module;

import dagger.Module;
import dagger.Provides;
import info.jukov.yandextranslatetest.model.network.dict.DictApi;
import info.jukov.yandextranslatetest.model.network.translate.TranslateApi;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 20:04
 */
@Module
public class ApiModule {

	private final DictApi dictApi;
	private final TranslateApi translateApi;

	@Inject
	public ApiModule(final DictApi dictApi,
					 final TranslateApi translateApi) {
		this.dictApi = dictApi;
		this.translateApi = translateApi;
	}

	@Provides @Singleton
	public DictApi getDictApi() {
		return dictApi;
	}

	@Provides @Singleton
	public TranslateApi getTranslateApi() {
		return translateApi;
	}
}
