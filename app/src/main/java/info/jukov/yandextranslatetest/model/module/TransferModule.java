package info.jukov.yandextranslatetest.model.module;

import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import info.jukov.yandextranslatetest.model.transfer.TransferManager;
import info.jukov.yandextranslatetest.util.Guard;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: jukov
 * Date: 02.04.2017
 * Time: 19:53
 */
@Module
public class TransferModule {

	private final TransferManager transferManager;

	@Inject
	public TransferModule(@NonNull final TransferManager transferManager) {
		Guard.checkNotNull(transferManager, "null == transferManager");

		this.transferManager = transferManager;
	}

	@Provides
	@Singleton
	public TransferManager getTransferManager() {
		return transferManager;
	}
}
