package info.jukov.yandextranslatetest.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * User: jukov
 * Date: 26.03.2017
 * Time: 10:29
 */

public final class ToastUtils {

	public static void shortToast(@NonNull final Context context, @StringRes final int textRes) {
		Guard.checkNotNull(context, "null == context");

		Toast.makeText(context, textRes, Toast.LENGTH_SHORT).show();
	}

	private ToastUtils() {
	}
}
