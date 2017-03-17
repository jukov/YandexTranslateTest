package info.jukov.yandextranslatetest.util;

import android.support.annotation.NonNull;

/**
 * User: jukov
 * Date: 04.03.2017
 * Time: 20:43
 */

public final class Log {

	private final String tag;

	public Log(@NonNull final Class clazz) {
		Guard.checkNotNull(clazz, "null == class");
		tag = clazz.getName();
	}

	public void info(final String msg) {
		android.util.Log.d(tag, msg);
	}

	public void error(final String msg) {
		android.util.Log.e(tag, msg);
	}

}
