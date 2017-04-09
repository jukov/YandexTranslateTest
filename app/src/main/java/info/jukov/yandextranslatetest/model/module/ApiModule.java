package info.jukov.yandextranslatetest.model.module;

import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import info.jukov.yandextranslatetest.model.network.dict.DictApi;
import info.jukov.yandextranslatetest.model.network.translate.TranslateApi;
import info.jukov.yandextranslatetest.util.Guard;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 20:04
 */
@Module
public final class ApiModule {

	private final DictApi dictApi;
	private final TranslateApi translateApi;

	@Inject
	public ApiModule(@NonNull final DictApi dictApi, @NonNull final TranslateApi translateApi) {
		Guard.checkNotNull(dictApi, "null == dictApi");
		Guard.checkNotNull(translateApi, "null == translateApi");

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
