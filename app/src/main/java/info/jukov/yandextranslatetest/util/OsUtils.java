package info.jukov.yandextranslatetest.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;
import info.jukov.yandextranslatetest.R;

/**
 * User: jukov
 * Date: 26.03.2017
 * Time: 10:29
 */

public final class OsUtils {

	public static void shortToast(@NonNull final Context context, @StringRes final int textRes) {
		Guard.checkNotNull(context, "null == context");

		Toast.makeText(context, textRes, Toast.LENGTH_SHORT).show();
	}

	public static void setTextToClipBoard(@NonNull final Context context, @NonNull final String text) {
		Guard.checkNotNull(context, "null == context");
		Guard.checkNotNull(text, "null == text");

		ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clipData = ClipData.newPlainText(context.getString(R.string.misc_clipBoard_label), text);

		clipboardManager.setPrimaryClip(clipData);
	}

	private OsUtils() {
	}
}
