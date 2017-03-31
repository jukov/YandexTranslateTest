package info.jukov.yandextranslatetest.util;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * User: jukov
 * Date: 31.03.2017
 * Time: 22:40
 */

public final class KeyboardUtils {

	public static void hideSoftInput(final Activity activity) {
		View view = activity.getCurrentFocus();

		if (view == null) {
			view = new View(activity);
		}

		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	private KeyboardUtils() {
	}
}
