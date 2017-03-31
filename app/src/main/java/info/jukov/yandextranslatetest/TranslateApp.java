package info.jukov.yandextranslatetest;

import android.app.Application;
import info.jukov.yandextranslatetest.model.component.AppComponent;
import info.jukov.yandextranslatetest.model.component.DaggerAppComponent;
import info.jukov.yandextranslatetest.model.module.ApiModule;
import info.jukov.yandextranslatetest.model.module.ContextModule;
import info.jukov.yandextranslatetest.model.module.DatabaseModule;
import info.jukov.yandextranslatetest.model.network.dict.DictApi;
import info.jukov.yandextranslatetest.model.network.translate.TranslateApi;
import info.jukov.yandextranslatetest.model.storage.dao.DaoMaster;
import info.jukov.yandextranslatetest.model.storage.dao.DaoSession;
import info.jukov.yandextranslatetest.model.storage.dao.DatabaseManager;
import info.jukov.yandextranslatetest.ui.format.DictionaryConstructor;
import org.greenrobot.greendao.database.Database;

/**
 * User: jukov
 * Date: 14.03.2017
 * Time: 23:06
 */

public final class TranslateApp extends Application {

	private static final String DATABASE_NAME = "translations-database";

	private static AppComponent appComponent;

	public static AppComponent getAppComponent() {
		return appComponent;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		appComponent = buildComponent();

		DictionaryConstructor.init(this);
	}

	private AppComponent buildComponent() {
		return DaggerAppComponent.builder()
			.contextModule(new ContextModule(this))
			.apiModule(new ApiModule(new DictApi(this), new TranslateApi(this)))
			.databaseModule(new DatabaseModule(new DatabaseManager(openDatabase().getTranslationDao())))
			.build();
	}

	private DaoSession openDatabase() {
		DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, DATABASE_NAME);
		Database database = devOpenHelper.getWritableDb();
		return new DaoMaster(database).newSession();
	}

}
