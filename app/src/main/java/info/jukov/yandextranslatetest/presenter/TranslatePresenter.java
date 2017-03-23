package info.jukov.yandextranslatetest.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import info.jukov.yandextranslatetest.TranslateApp;
import info.jukov.yandextranslatetest.model.CallbackWithProgress;
import info.jukov.yandextranslatetest.model.module.ApiModule;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponce;
import info.jukov.yandextranslatetest.model.network.translate.TranslateResponce;
import info.jukov.yandextranslatetest.ui.base.Progressable;
import info.jukov.yandextranslatetest.util.Guard;
import info.jukov.yandextranslatetest.util.MultiSetBoolean;
import info.jukov.yandextranslatetest.util.MultiSetBoolean.OnValueTrueListener;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Response;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:35
 */
@InjectViewState
public final class TranslatePresenter extends MvpPresenter<TranslateView> {

	private enum Queries {
		TRANSLATE,
		DICT
	}

	@Inject ApiModule apiModule;

	private TranslateResponce translateResponce;
	private LookupResponce lookupResponce;

	private final MultiSetBoolean<Queries> allQueriesLoaded = new MultiSetBoolean<>(
		Queries.values().length, new OnValueTrueListener() {
		@Override
		public void onTrue() {
			if (!lookupResponce.isEmpty()) {
				getViewState().setTranslatedText(lookupResponce.toString());
			} else {
				getViewState().setTranslatedText(translateResponce.getText());
			}
		}
	});

	@Override
	protected void onFirstViewAttach() {
		TranslateApp.getAppComponent().inject(this);
	}

	public void translate(@NonNull final String lang, @NonNull final String text) {
		Guard.checkNotNull(lang, "null == lang");
		Guard.checkNotNull(text, "null == text");

		allQueriesLoaded.reset();
		apiModule.getTranslateApi().use(new TranslateCallback(null), null).translate(lang, text);
		apiModule.getDictApi().use(new DictCallback(null), null).lookup(lang, text, null, null);
	}

	private final class TranslateCallback extends CallbackWithProgress<TranslateResponce> {

		private TranslateCallback(@Nullable final Progressable progressable) {
			super(progressable);
		}

		@Override
		public void onResponse(final Call<TranslateResponce> call,
							   final Response<TranslateResponce> response) {
			super.onResponse(call, response);
			if (response.body() != null) {
				translateResponce = response.body();
				allQueriesLoaded.set(Queries.TRANSLATE);
			}
		}

		@Override
		public void onFailure(final Call<TranslateResponce> call, final Throwable t) {
			super.onFailure(call, t);
			allQueriesLoaded.set(Queries.DICT);

		}
	}

	private final class DictCallback extends CallbackWithProgress<LookupResponce> {

		private DictCallback(@Nullable final Progressable progressable) {
			super(progressable);
		}

		@Override
		public void onResponse(final Call<LookupResponce> call,
							   final Response<LookupResponce> response) {
			super.onResponse(call, response);
			if (response.body() != null) {
				lookupResponce = response.body();
				allQueriesLoaded.set(Queries.DICT);
			}
		}

		@Override
		public void onFailure(final Call<LookupResponce> call, final Throwable t) {
			super.onFailure(call, t);
			allQueriesLoaded.set(Queries.DICT);
		}
	}

}
