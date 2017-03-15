package info.jukov.yandextranslatetest.ui.screen.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import info.jukov.yandextranslatetest.model.CallbackWithProgress;
import info.jukov.yandextranslatetest.model.network.Api;
import info.jukov.yandextranslatetest.model.network.TranslateResponce;
import info.jukov.yandextranslatetest.ui.base.Progressable;
import retrofit2.Call;
import retrofit2.Response;

/**
 * User: jukov
 * Date: 14.03.2017
 * Time: 21:48
 */
@InjectViewState
public class ScreenMainPresenter extends MvpPresenter<ScreenMainView> {

	void translate(@NonNull final String lang, @NonNull final String text) {
		Api.use(new TranslateCallback(null), null).translate("en-ru", text);
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

}
