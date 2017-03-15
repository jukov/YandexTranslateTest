package info.jukov.yandextranslatetest;

import android.app.Application;
import android.content.Context;

/**
 * User: jukov
 * Date: 14.03.2017
 * Time: 23:06
 */

public class TranslateApp extends Application {

	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();

		context = getApplicationContext();
	}

	public static Context getContext() {
		return context;
	}

}
