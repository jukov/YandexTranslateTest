package info.jukov.yandextranslatetest.util;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 8:16
 */

public final class ExtrasUtils {

	private ExtrasUtils() {
	}

	public static String createExtraName(final String extraName, final Class clazz) {
		return clazz.getCanonicalName() + "." + extraName;
	}
}
