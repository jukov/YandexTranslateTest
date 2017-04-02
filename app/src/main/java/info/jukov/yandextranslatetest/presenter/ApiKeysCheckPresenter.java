package info.jukov.yandextranslatetest.presenter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.TranslateApp;
import info.jukov.yandextranslatetest.model.module.ContextModule;
import javax.inject.Inject;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 21:02
 */
@InjectViewState
public final class ApiKeysCheckPresenter extends MvpPresenter<ApiKeysCheckView> {

	@Inject ContextModule contextModule;

	@Override
	protected void onFirstViewAttach() {
		TranslateApp.getAppComponent().inject(this);
	}

	/**
	 * Проверяет, введены ли Api-ключи.
	 * Если ключи введены, вызывает {@link ApiKeysCheckView#onKeysEntered()},
	 * в противном случае - {@link ApiKeysCheckView#onKeysNotEntered()}
	 */
	public void check() {
		final SharedPreferences preferences = PreferenceManager
			.getDefaultSharedPreferences(contextModule.getContext());

		final String dictPreferenceKey = contextModule.getContext()
			.getString(R.string.preferenceKey_dictApiKey);
		final String translatePreferenceKey = contextModule.getContext()
			.getString(R.string.preferenceKey_translateApiKey);

		final String dictApiKey = preferences.getString(dictPreferenceKey, "");
		final String translateApiKey = preferences.getString(translatePreferenceKey, "");

		if (!dictApiKey.isEmpty() && !translateApiKey.isEmpty()) {
			getViewState().onKeysEntered();
		} else {
			getViewState().onKeysNotEntered();
		}
	}

}
