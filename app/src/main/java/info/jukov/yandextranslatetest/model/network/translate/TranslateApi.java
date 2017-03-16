package info.jukov.yandextranslatetest.model.network.translate;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.TranslateApp;
import info.jukov.yandextranslatetest.ui.base.Progressable;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * User: jukov
 * Date: 14.03.2017
 * Time: 23:08
 */

public final class TranslateApi {

	private static YandexTranslateApi api;

	private static String apiKey;

	static {
		api = new Retrofit.Builder()
			.baseUrl(TranslateApp.getContext().getString(R.string.translateUrl))
			.addConverterFactory(JacksonConverterFactory.create())
			.build()
			.create(YandexTranslateApi.class);

		apiKey = TranslateApp.getContext().getString(R.string.apiKeyTranslate);
	}

	private TranslateApi() {
	}

	public static <T> Sender use(@NonNull final Callback<T> callback,
								 @Nullable final Progressable progressable) {
		return new Sender(callback, progressable);
	}

	public static class Sender<T> {

		private Callback callback;

		@Nullable private Progressable progressable;

		private Sender(@NonNull final Callback<T> callback,
					   @Nullable final Progressable progressable) {
			this.callback = callback;
			this.progressable = progressable;
		}

		private void startProgress() {
			if (progressable != null) {
				progressable.startProgress();
			}
		}

		public void translate(@NonNull final String lang, @NonNull final String text) {
			api.translate(apiKey, lang, text).enqueue(callback);
			startProgress();
		}

	}
}
