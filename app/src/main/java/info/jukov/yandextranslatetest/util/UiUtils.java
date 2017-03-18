package info.jukov.yandextranslatetest.util;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 11:52
 */

public final class UiUtils {

	public static String getCurrentUiLanguage(@NonNull final Context context) {
		Guard.checkNotNull(context, "null == context");

		final String code = context.getResources().getConfiguration().locale.getCountry();
		return code.length() > 2 ? "en" : code.toLowerCase();

	}

	private UiUtils() {
	}
}
