package info.jukov.yandextranslatetest.model.module;

import android.content.Context;
import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import info.jukov.yandextranslatetest.util.Guard;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 18:03
 */
@Module
public class ContextModule {

	private final Context context;

	@Inject
	public ContextModule(@NonNull final Context context) {
		Guard.checkNotNull(context, "null == context");

		this.context = context;
	}

	@Provides
	@Singleton
	public Context getContext() {
		return context;
	}
}
