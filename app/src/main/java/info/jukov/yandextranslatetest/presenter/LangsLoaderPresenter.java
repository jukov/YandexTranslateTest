package info.jukov.yandextranslatetest.presenter;

import android.support.annotation.NonNull;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import info.jukov.yandextranslatetest.model.CallbackWithLog;
import info.jukov.yandextranslatetest.model.module.ApiModule;
import info.jukov.yandextranslatetest.model.network.translate.GetLangsResponce;
import info.jukov.yandextranslatetest.util.Guard;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Response;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 11:47
 */
@InjectViewState
public final class LangsLoaderPresenter extends MvpPresenter<LangsLoaderView> {

	@Inject ApiModule apiModule;

	public void getLangs(@NonNull final String uiCode) {
		Guard.checkNotNull(uiCode, "null == uiCode");

		apiModule.getDictApi().use(new DictLangsCallback(), null).getLangs();
		apiModule.getTranslateApi().use(new TranslateLangsCallback(), null).getLangs(uiCode);
	}

	private void putDictToPreference(final List<String> langs) {
	}

	private final class DictLangsCallback extends CallbackWithLog<List<String>> {

		private DictLangsCallback() {
		}

		@Override
		public void onResponse(final Call<List<String>> call,
							   final Response<List<String>> response) {
			super.onResponse(call, response);
		}

		@Override
		public void onFailure(final Call<List<String>> call, final Throwable t) {
			super.onFailure(call, t);
		}
	}

	private final class TranslateLangsCallback extends CallbackWithLog<GetLangsResponce> {

		private TranslateLangsCallback() {
		}

		@Override
		public void onResponse(final Call<GetLangsResponce> call,
							   final Response<GetLangsResponce> response) {
			super.onResponse(call, response);
		}

		@Override
		public void onFailure(final Call<GetLangsResponce> call, final Throwable t) {
			super.onFailure(call, t);
		}
	}

}
