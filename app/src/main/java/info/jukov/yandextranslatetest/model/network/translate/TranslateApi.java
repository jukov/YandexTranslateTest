package info.jukov.yandextranslatetest.model.network.translate;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.TranslateApp;
import info.jukov.yandextranslatetest.ui.base.Progressable;
import info.jukov.yandextranslatetest.util.Guard;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * User: jukov
 * Date: 14.03.2017
 * Time: 23:08
 */

public final class TranslateApi {

	private static final YandexTranslateApi api;

	private static final String apiKey;

	public static <T> Sender use(@NonNull final Callback<T> callback,
								 @Nullable final Progressable progressable) {
		Guard.checkNotNull(callback, "null == callback");

		return new Sender(callback, progressable);
	}

	static {
		api = new Retrofit.Builder()
			.baseUrl(TranslateApp.getContext().getString(R.string.translateUrl))
			.addConverterFactory(JacksonConverterFactory.create())
			.build()
			.create(YandexTranslateApi.class);

		final String preferenceKey = TranslateApp.getContext()
			.getString(R.string.preferenceKey_translateApiKey);
		apiKey = PreferenceManager.getDefaultSharedPreferences(TranslateApp.getContext())
			.getString(preferenceKey, "");
	}

	private TranslateApi() {
	}

	public static class Sender<T> {

		private final Callback callback;

		@Nullable private final Progressable progressable;

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
			Guard.checkNotNull(lang, "null == lang");
			Guard.checkNotNull(text, "null == text");

			api.translate(apiKey, lang, text).enqueue(callback);
			startProgress();
		}

	}
}
