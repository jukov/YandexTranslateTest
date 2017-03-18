package info.jukov.yandextranslatetest.model;

import android.support.annotation.Nullable;
import info.jukov.yandextranslatetest.ui.base.Progressable;
import retrofit2.Call;
import retrofit2.Response;

/**
 * User: jukov
 * Date: 05.03.2017
 * Time: 14:29
 */

public class CallbackWithProgress<T> extends CallbackWithLog<T> {

	private final Progressable progressable;

	public CallbackWithProgress(@Nullable final Progressable progressable) {
		this.progressable = progressable;
	}

	@Override
	public void onResponse(final Call<T> call, final Response<T> response) {
		super.onResponse(call, response);
		stopProgress();
	}

	@Override
	public void onFailure(final Call<T> call, final Throwable t) {
		super.onFailure(call, t);
		stopProgress();
	}

	private void stopProgress() {
		if (progressable != null) {
			progressable.stopProgress();
		}
	}
}
