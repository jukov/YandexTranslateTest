package info.jukov.yandextranslatetest.model.network.dict;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.ui.base.Progressable;
import info.jukov.yandextranslatetest.util.Guard;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * User: jukov
 * Date: 16.03.2017
 * Time: 22:33
 */

public final class DictApi {

	private final YandexDictionaryApi api;

	private final String apiKey;

	public DictApi(@NonNull final Context context) {
		Guard.checkNotNull(context, "null == context");

		api = new Retrofit.Builder()
			.baseUrl(context.getString(R.string.dictUrl))
			.addConverterFactory(JacksonConverterFactory.create())
			.build()
			.create(YandexDictionaryApi.class);

		final String preferenceKey = context.getString(R.string.preferenceKey_dictApiKey);
		apiKey = PreferenceManager.getDefaultSharedPreferences(context)
			.getString(preferenceKey, "");
	}

	public <T> DictApi.Sender use(@NonNull final Callback<T> callback,
								  @Nullable final Progressable progressable) {
		Guard.checkNotNull(callback, "null == callback");

		return new DictApi.Sender(callback, progressable);
	}

	public class Sender<T> {

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

		public void lookup(final String lang, final String text, @Nullable final String ui,
						   @Nullable final String flags) {
			Guard.checkNotNull(lang, "null == lang");
			Guard.checkNotNull(text, "null == text");

			api.lookup(apiKey, lang, text, ui, flags).enqueue(callback);
			startProgress();
		}

		public void getLangs() {
			api.getLangs(apiKey).enqueue(callback);
			startProgress();
		}
	}

}
