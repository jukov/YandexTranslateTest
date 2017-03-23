package info.jukov.yandextranslatetest.model.storage.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import info.jukov.yandextranslatetest.model.storage.Language;
import info.jukov.yandextranslatetest.util.ExtrasUtils;
import info.jukov.yandextranslatetest.util.Guard;
import info.jukov.yandextranslatetest.util.JsonUtils;
import info.jukov.yandextranslatetest.util.Log;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 12:13
 */

public final class LangPreferences {

	private static final Log LOG = new Log(LangPreferences.class);

	//@formatter:off
	private static final String FILE = ExtrasUtils.createExtraName("FILE", LangPreferences.class);
	private static final String READEBLE_LANG_WORDS = ExtrasUtils.createExtraName("READEBLE_LANG_WORDS", LangPreferences.class);
	private static final String DICT_LANGS = ExtrasUtils.createExtraName("DICT_LANGS", LangPreferences.class);
	private static final String TRANSLATE_LANGS = ExtrasUtils.createExtraName("TRANSLATE_LANGS", LangPreferences.class);
	private static final String MOST_USED_INPUT_LANGS = ExtrasUtils.createExtraName("MOST_USED_INPUT_LANGS", LangPreferences.class);
	private static final String MOST_USED_OUTPUT_LANGS = ExtrasUtils.createExtraName("MOST_USED_OUTPUT_LANGS", LangPreferences.class);
	//@formatter:on

	private static final String LANG_DELIMITER = "-";

	private static SharedPreferences getPreferences(final Context context) {
		Guard.checkNotNull(context, "null == context");

		return context.getApplicationContext().getSharedPreferences(FILE, Context.MODE_PRIVATE);
	}

	/**
	 * Стирает все данные о поддерживаемых языках.
	 */
	public static void clear(final Context context) {
		getPreferences(context).edit().clear().commit();
	}

	/**
	 * Записывает в {@link SharedPreferences} названия поддерживаемых языков
	 * в виде строки "xx-NNNN", где xx - двухбуквенный код языка, NNNN - читаемое название языка.
	 * */
	public static void putReadebleLangWords(@NonNull final Context context,
											@NonNull final Map<String, String> words) {
		Guard.checkNotNull(words, "null == words");

		final Set<String> packedLangs = new HashSet<>();

		for (final Map.Entry<String, String> word : words.entrySet()) {
			packedLangs.add(word.getKey() + LANG_DELIMITER + word.getValue());
		}

		final SharedPreferences.Editor editor = getPreferences(context).edit()
			.putStringSet(READEBLE_LANG_WORDS, packedLangs);

		editor.commit();
	}

	/**
	 * Возвращает поддерживаемые языки в виде листа объектов {@link Language}
	 * */
	@Nullable
	public static Set<Language> getReadableWords(@NonNull final Context context) {

		Set<String> packedLangs = getPreferences(context).getStringSet(READEBLE_LANG_WORDS, null);

		if (packedLangs == null) {
			return null;
		}

		final Set<Language> languages = new HashSet<>();

		for (final String packedLang : packedLangs) {
			final int delimiterIndex = packedLang.indexOf(LANG_DELIMITER);
			Guard.checkPreCondition(delimiterIndex >= 0, "First delimiter not found");

			final String code = packedLang.substring(0, delimiterIndex);
			final String readableLangWord = packedLang.substring(delimiterIndex + 1);

			languages.add(new Language(code, readableLangWord));
		}

		return languages;
	}

	/**
	 * Записывает в {@link SharedPreferences} пары языков, которые поддерживаются Api словаря.
	 * */
	public static void putDictLangs(@NonNull final Context context,
									@NonNull final Set<String> langs) {
		Guard.checkNotNull(langs, "null == langs");

		getPreferences(context).edit().putStringSet(DICT_LANGS, langs).commit();
	}

	/**
	 * Возвращает пары языков, которые поддерживаются Api словаря.
	 * */
	@Nullable
	public static Set<String> getDictLangs(@NonNull final Context context) {
		return getPreferences(context).getStringSet(DICT_LANGS, null);
	}

	/**
	 * Записывает в {@link SharedPreferences} пары языков, которые поддерживаются Api переводчика.
	 * */
	public static void putTranslateLangs(@NonNull final Context context,
										 @NonNull final Set<String> langs) {
		Guard.checkNotNull(langs, "null == langs");

		getPreferences(context).edit().putStringSet(TRANSLATE_LANGS, langs).commit();
	}

	/**
	 * Возвращает пары языков, которые поддерживаются Api переводчика.
	 * */
	@Nullable
	public static Set<String> getTranslateLangs(@NonNull final Context context) {
		return getPreferences(context).getStringSet(TRANSLATE_LANGS, null);
	}

	/**
	 * Записывает в {@link SharedPreferences} список самых используемых исходных языков
	 */
	public static void putMostUsedInputLangs(@NonNull final Context context,
											 final List<Language> mostUsedlanguages) {
		putMostUsedLangs(context, new HashSet<Language>(mostUsedlanguages), MOST_USED_INPUT_LANGS);
	}

	/**
	 * Записывает в {@link SharedPreferences} список самых используемых переводящих языков
	 */
	public static void putMostUsedOutputLangs(@NonNull final Context context,
											  final List<Language> mostUsedlanguages) {
		putMostUsedLangs(context, new HashSet<Language>(mostUsedlanguages), MOST_USED_OUTPUT_LANGS);
	}

	/**
	 * Возвращает список самых используемых исходных языков
	 */
	@Nullable
	public static Set<Language> getMostUsedInputLangs(@NonNull final Context context) {
		return getMostUsedLangs(context, MOST_USED_INPUT_LANGS);
	}

	/**
	 * Возвращает список самых используемых переводящих языков
	 */
	@Nullable
	public static Set<Language> getMostUsedOutputLangs(@NonNull final Context context) {
		return getMostUsedLangs(context, MOST_USED_OUTPUT_LANGS);
	}

	private static void putMostUsedLangs(final Context context,
										 final Set<Language> mostUsedlanguages, final String key) {

		final Set<String> serializedLangs = new HashSet<>();

		for (final Language language : mostUsedlanguages) {
			serializedLangs.add(JsonUtils.serialize(language));
		}

		getPreferences(context).edit().putStringSet(key, serializedLangs)
			.commit();
	}

	private static Set<Language> getMostUsedLangs(final Context context, final String key) {

		Set<String> serializedLangs = getPreferences(context)
			.getStringSet(key, null);

		if (serializedLangs == null) {
			return null;
		}

		final Set<Language> languages = new HashSet<>();

		for (final String serializedLang : serializedLangs) {
			final Language language = JsonUtils.deserialize(Language.class, serializedLang);

			if (language != null) {
				languages.add(language);
			}
		}

		return languages;
	}

	private LangPreferences() {
	}
}
