package info.jukov.yandextranslatetest.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

	public void info(@Nullable final String msg) {
		android.util.Log.d(tag, msg);
	}

	public void warning(@Nullable final String msg) {
		android.util.Log.w(tag, msg);
	}

	public void error(@Nullable final String msg) {
		android.util.Log.e(tag, msg);
	}

}
