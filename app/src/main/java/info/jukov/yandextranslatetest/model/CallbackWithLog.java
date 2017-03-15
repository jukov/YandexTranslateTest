package info.jukov.yandextranslatetest.model;

import info.jukov.yandextranslatetest.util.Log;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * User: jukov
 * Date: 05.03.2017
 * Time: 15:41
 */

public class CallbackWithLog<T> implements Callback<T> {

	private Log log;

	private String errorBody;

	protected CallbackWithLog() {
		log = new Log(Callback.class);
	}

	@Override
	public void onResponse(final Call<T> call, final Response<T> response) {
		log.info(response.toString());
		log.info(response.headers().toString());
		if (response.body() != null) {
			log.info(response.body().toString());
		} else {
			try {
				errorBody = response.errorBody().string();
				log.info(errorBody);
			} catch (final IOException e) {
				log.error(e.toString());
			}
		}
	}

	@Override
	public void onFailure(final Call<T> call, final Throwable t) {
		log.info(t.toString());
	}

	protected String getErrorBody() {
		return errorBody;
	}
}
