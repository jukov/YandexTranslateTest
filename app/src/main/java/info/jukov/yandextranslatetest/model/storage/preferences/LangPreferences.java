package info.jukov.yandextranslatetest.model.storage.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import info.jukov.yandextranslatetest.util.ExtrasUtils;
import info.jukov.yandextranslatetest.util.Guard;
import java.util.Map;
import java.util.Set;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 12:13
 */

public final class LangPreferences {

	private static final String FILE = ExtrasUtils.createExtraName("FILE", LangPreferences.class);
	private static final String READEBLE_LANG_WORD_PREFIX = ExtrasUtils
		.createExtraName("LANG_", LangPreferences.class);
	private static final String DICT_LANGS = ExtrasUtils
		.createExtraName("FILE", LangPreferences.class);
	private static final String TRANSLATE_LANGS = ExtrasUtils
		.createExtraName("FILE", LangPreferences.class);

	private static SharedPreferences getPreferences(final Context context) {
		Guard.checkNotNull(context, "null == context");

		return context.getApplicationContext().getSharedPreferences(FILE, Context.MODE_PRIVATE);
	}

	public static void clear(final Context context) {
		getPreferences(context).edit().clear().commit();
	}

	public static void putReadebleLangWords(@NonNull final Context context,
											@NonNull final Map<String, String> words) {
		Guard.checkNotNull(words, "null == words");

		final SharedPreferences.Editor editor = getPreferences(context).edit();

		for (final Map.Entry<String, String> word : words.entrySet()) {
			editor.putString(word.getKey(), word.getValue());
		}

		editor.commit();
	}

	public static String getReadableWord(@NonNull final Context context,
										 @NonNull final String code) {
		Guard.checkNotNull(code, "null == code");

		return getPreferences(context).getString(code, null);
	}

	public static void putDictLangs(@NonNull final Context context,
									@NonNull final Set<String> langs) {
		Guard.checkNotNull(langs, "null == langs");

		getPreferences(context).edit().putStringSet(DICT_LANGS, langs).commit();
	}

	public static Set<String> getDictLangs(@NonNull final Context context) {
		return getPreferences(context).getStringSet(DICT_LANGS, null);
	}

	public static void putTranslateLangs(@NonNull final Context context,
										 @NonNull final Set<String> langs) {
		Guard.checkNotNull(langs, "null == langs");

		getPreferences(context).edit().putStringSet(TRANSLATE_LANGS, langs).commit();
	}

	public static Set<String> getTranslateLangs(@NonNull final Context context) {
		return getPreferences(context).getStringSet(TRANSLATE_LANGS, null);
	}

	private LangPreferences() {
	}
}
