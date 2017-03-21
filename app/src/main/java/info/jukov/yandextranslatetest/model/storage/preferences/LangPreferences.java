package info.jukov.yandextranslatetest.model.storage.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import info.jukov.yandextranslatetest.model.storage.Language;
import info.jukov.yandextranslatetest.util.ExtrasUtils;
import info.jukov.yandextranslatetest.util.Guard;
import info.jukov.yandextranslatetest.util.Log;
import java.util.ArrayList;
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

	private static final String FILE = ExtrasUtils.createExtraName("FILE", LangPreferences.class);
	private static final String READEBLE_LANG_WORDS = ExtrasUtils.createExtraName("LANG_", LangPreferences.class);
	private static final String DICT_LANGS = ExtrasUtils
		.createExtraName("FILE", LangPreferences.class);
	private static final String TRANSLATE_LANGS = ExtrasUtils
		.createExtraName("FILE", LangPreferences.class);

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
	 * в виде строки "xx-NNNN-P", где xx - двухбуквенный код языка;
	 * NNNN - читаемое название языка; p - позиция языка в списке частоиспользуемых.
	 * */
	public static void putReadebleLangWords(@NonNull final Context context,
											@NonNull final Map<String, String> words) {
		Guard.checkNotNull(words, "null == words");

		final Set<String> packedLangs = new HashSet<>();

		for (final Map.Entry<String, String> word : words.entrySet()) {
			packedLangs.add(word.getKey() + LANG_DELIMITER + word.getValue() + LANG_DELIMITER + "0");
		}

		final SharedPreferences.Editor editor = getPreferences(context).edit()
			.putStringSet(READEBLE_LANG_WORDS, packedLangs);

		editor.commit();
	}

	/**
	 * Возвращает поддерживаемые языки в виде листа объектов {@link Language}
	 * */
	@Nullable
	public static List<Language> getReadableWords(@NonNull final Context context) {

		Set<String> packedLangs = getPreferences(context).getStringSet(READEBLE_LANG_WORDS, null);

		if (packedLangs == null) {
			return null;
		}

		final List<Language> languages = new ArrayList<>();

		for (final String packedLang : packedLangs) {
			final int firstDelimiterIndex = packedLang.indexOf(LANG_DELIMITER);
			Guard.checkPreCondition(firstDelimiterIndex >= 0, "First delimiter not found");

			final int secondDelimiterIndex = packedLang.indexOf(LANG_DELIMITER, firstDelimiterIndex + 1);
			Guard.checkPreCondition(secondDelimiterIndex >= 0, "Second delimiter not found");

			final String code = packedLang.substring(0, firstDelimiterIndex);
			final String readableLangWord = packedLang.substring(firstDelimiterIndex + 1, secondDelimiterIndex);
			final int positionInMostIsed;

			try {
				positionInMostIsed = Integer.valueOf(packedLang.substring(secondDelimiterIndex + 1));
			} catch (final NumberFormatException e) {
				LOG.error(e.toString());
				return null;
			}

			languages.add(new Language(code, readableLangWord, positionInMostIsed));
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

	private LangPreferences() {
	}
}
