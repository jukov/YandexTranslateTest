package info.jukov.yandextranslatetest.model.module;

import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import info.jukov.yandextranslatetest.model.storage.dao.DatabaseManager;
import info.jukov.yandextranslatetest.util.Guard;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: jukov
 * Date: 30.03.2017
 * Time: 20:40
 */
@Module
public final class DatabaseModule {

	private final DatabaseManager databaseManager;

	@Inject
	public DatabaseModule(@NonNull final DatabaseManager databaseManager) {
		Guard.checkNotNull(databaseManager, "null == databaseManager");

		this.databaseManager = databaseManager;
	}

	@Provides @Singleton
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}
}
