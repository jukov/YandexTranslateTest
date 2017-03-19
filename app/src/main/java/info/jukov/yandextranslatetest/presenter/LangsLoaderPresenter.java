package info.jukov.yandextranslatetest.presenter;

import android.support.annotation.NonNull;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import info.jukov.yandextranslatetest.TranslateApp;
import info.jukov.yandextranslatetest.model.CallbackWithLog;
import info.jukov.yandextranslatetest.model.module.ApiModule;
import info.jukov.yandextranslatetest.model.module.ContextModule;
import info.jukov.yandextranslatetest.model.network.ErrorCodes;
import info.jukov.yandextranslatetest.model.network.ErrorResponce;
import info.jukov.yandextranslatetest.model.network.translate.GetLangsResponce;
import info.jukov.yandextranslatetest.model.storage.preferences.LangPreferences;
import info.jukov.yandextranslatetest.util.Guard;
import info.jukov.yandextranslatetest.util.JsonUtils;
import info.jukov.yandextranslatetest.util.MultiSetBoolean;
import info.jukov.yandextranslatetest.util.MultiSetBoolean.OnValueTrueListener;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Response;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 11:47
 */
@InjectViewState
public final class LangsLoaderPresenter extends MvpPresenter<LangsLoaderView> {

	private enum Queries {
		TRANSLATE,
		DICT
	}

	private final MultiSetBoolean allQueriesLoaded =
		new MultiSetBoolean(Queries.values().length, new OnValueTrueListener() {
			@Override
			public void onTrue() {
				getViewState().onLoadFinished();
			}
		});

	private final Lock lock = new ReentrantLock();

	@Inject ApiModule apiModule;
	@Inject ContextModule contextModule;

	private boolean isLoadFailReported;

	/**
	 * Инициирует загрузку списка поддерживаемых языков для Api словаря и переводчика.
	 */
	public void getLangs(@NonNull final String uiCode) {
		Guard.checkNotNull(uiCode, "null == uiCode");

		isLoadFailReported = false;

		allQueriesLoaded.reset();
		apiModule.getDictApi().use(new DictLangsCallback(), null).getLangs();
		apiModule.getTranslateApi().use(new TranslateLangsCallback(), null).getLangs(uiCode);
	}

	@Override
	protected void onFirstViewAttach() {
		super.onFirstViewAttach();
		TranslateApp.getAppComponent().inject(this);
	}

	private void putDictToPreference(@NonNull final Set<String> langs) {
		LangPreferences.putDictLangs(contextModule.getContext(), langs);
		allQueriesLoaded.set(Queries.DICT);
	}

	private void putTranslateToPreference(@NonNull final Set<String> dirs,
										  @NonNull final Map<String, String> readableLangs) {
		LangPreferences.putTranslateLangs(contextModule.getContext(), dirs);
		LangPreferences.putReadebleLangWords(contextModule.getContext(), readableLangs);
		allQueriesLoaded.set(Queries.TRANSLATE);
	}

	private void reportLoadFailed(final int errorCode) {
		lock.lock();
		try {
			if (!isLoadFailReported) {
				isLoadFailReported = true;
				getViewState().onLoadFailed(errorCode);
			}
		} finally {
			lock.unlock();
		}
	}

	private final class DictLangsCallback extends CallbackWithLog<Set<String>> {

		@Override
		public void onResponse(final Call<Set<String>> call,
							   final Response<Set<String>> response) {
			super.onResponse(call, response);

			if (response.body() != null) {//Тело запроса получено, всё хорошо
				putDictToPreference(response.body());
				return;
			}

			if (getErrorBody() != null) {//Получено тело запроса с содержанием ошибки
				ErrorResponce errorResponce = JsonUtils
					.deserialize(ErrorResponce.class, getErrorBody());
				if (errorResponce != null) {
					reportLoadFailed(errorResponce.getCode());
					return;
				}
			}
			//Всё плохо, есть только код ошибки
			reportLoadFailed(response.code());
		}

		@Override
		public void onFailure(final Call<Set<String>> call, final Throwable t) {
			super.onFailure(call, t);
			//Сетевая ошибка
			reportLoadFailed(ErrorCodes.NETWORK_ERROR_CUSTOM);
		}
	}

	private final class TranslateLangsCallback extends CallbackWithLog<GetLangsResponce> {

		@Override
		public void onResponse(final Call<GetLangsResponce> call,
							   final Response<GetLangsResponce> response) {
			super.onResponse(call, response);

			if (response.body() != null) {//Тело запроса получено, всё хорошо
				putTranslateToPreference(response.body().getDirs(), response.body().getLangs());
				return;
			}

			if (getErrorBody() != null) {//Получено тело запроса с содержанием ошибки
				ErrorResponce errorResponce = JsonUtils
					.deserialize(ErrorResponce.class, getErrorBody());
				if (errorResponce != null) {
					reportLoadFailed(errorResponce.getCode());
					return;
				}
			}
			//Всё плохо, есть только код ошибки
			reportLoadFailed(response.code());
		}

		@Override
		public void onFailure(final Call<GetLangsResponce> call, final Throwable t) {
			super.onFailure(call, t);
			//Сетевая ошибка
			reportLoadFailed(ErrorCodes.NETWORK_ERROR_CUSTOM);
		}
	}

}
