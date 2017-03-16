package info.jukov.yandextranslatetest;

import android.app.Application;
import android.content.Context;
import info.jukov.yandextranslatetest.model.dao.DaoMaster;
import info.jukov.yandextranslatetest.model.dao.DaoSession;
import org.greenrobot.greendao.database.Database;

/**
 * User: jukov
 * Date: 14.03.2017
 * Time: 23:06
 */

public class TranslateApp extends Application {

	private static final String DATABASE_NAME = "words.db";

	private static Context context;

	private static DaoSession daoSession;

	public static Context getContext() {
		return context;
	}

	public static DaoSession getDaoSession() {
		return daoSession;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		context = getApplicationContext();

		DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, DATABASE_NAME);
		Database database = devOpenHelper.getReadableDb();
		daoSession = new DaoMaster(database).newSession();
	}

}
