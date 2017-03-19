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

	private StringUtils() {
	}
}
