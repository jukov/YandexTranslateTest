package info.jukov.yandextranslatetest.util;

import android.support.annotation.Nullable;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 22:25
 */

public final class StringUtils {

	public static boolean isNullOrEmpty(@Nullable final String target) {
		return target == null || target.isEmpty();
	}

	public static String formatInputLang(final String lang) {
		Guard.checkNotNull(lang, "null == lang");
		Guard.checkPreCondition(lang.length() == 5 || lang.contains("-"), "Lang must be correspond pattern 'xx-xx': " + lang);
		return lang.split("-", 2)[0];
	}

	public static String formatOutputLang(final String lang) {
		Guard.checkNotNull(lang, "null == lang");
		Guard.checkPreCondition(lang.length() == 5 || lang.contains("-"), "Lang must be correspond pattern 'xx-xx'");
		return lang.split("-", 2)[1];
	}

	private StringUtils() {
	}
}
