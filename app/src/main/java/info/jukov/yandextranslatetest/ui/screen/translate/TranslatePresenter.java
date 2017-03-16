package info.jukov.yandextranslatetest.ui.screen.translate;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import info.jukov.yandextranslatetest.model.CallbackWithProgress;
import info.jukov.yandextranslatetest.model.network.dict.DictApi;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponce;
import info.jukov.yandextranslatetest.model.network.translate.TranslateApi;
import info.jukov.yandextranslatetest.model.network.translate.TranslateResponce;
import info.jukov.yandextranslatetest.ui.base.Progressable;
import retrofit2.Call;
import retrofit2.Response;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:35
 */
@InjectViewState
public class TranslatePresenter extends MvpPresenter<TranslateView> {

	void translate(@NonNull final String lang, @NonNull final String text) {
		TranslateApi.use(new TranslateCallback(null), null).translate("en-ru", text);
		DictApi.use(new DictCallback(null), null).lookup("en-ru", text, null, null);
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
				getViewState().setTranslatedText(response.body().getText().toString());
			}
		}

		@Override
		public void onFailure(final Call<TranslateResponce> call, final Throwable t) {
			super.onFailure(call, t);
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
				getViewState().setTranslatedText(response.body().toString());
			}
		}

		@Override
		public void onFailure(final Call<LookupResponce> call, final Throwable t) {
			super.onFailure(call, t);
		}
	}

}
