package info.jukov.yandextranslatetest.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import info.jukov.yandextranslatetest.TranslateApp;
import info.jukov.yandextranslatetest.model.network.CallbackWithProgress;
import info.jukov.yandextranslatetest.model.module.ApiModule;
import info.jukov.yandextranslatetest.model.module.DatabaseModule;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponse;
import info.jukov.yandextranslatetest.model.network.translate.TranslateResponse;
import info.jukov.yandextranslatetest.model.storage.dao.DatabaseManager.OnTranslateProcessedListener;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.ui.base.Progressable;
import info.jukov.yandextranslatetest.util.Guard;
import info.jukov.yandextranslatetest.util.JsonUtils;
import info.jukov.yandextranslatetest.util.MultiSetBoolean;
import info.jukov.yandextranslatetest.util.MultiSetBoolean.OnValueTrueListener;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Response;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:35
 */
@InjectViewState
public final class TranslatePresenter extends MvpPresenter<TranslateView> implements OnTranslateProcessedListener {

	private enum Queries {
		TRANSLATE,
		DICT
	}

	@Inject ApiModule apiModule;
	@Inject DatabaseModule databaseModule;

	private TranslateResponse translateResponse;
	private LookupResponse lookupResponse;

	private String lang;
	private String text;

	private Translation translation;

	private final MultiSetBoolean<Queries> allQueriesLoaded = new MultiSetBoolean<>(Queries.values().length, new OnValueTrueListener() {
		@Override
		public void onTrue() {

			translation = new Translation();

			if (lookupResponse != null && !lookupResponse.isEmpty()) {
				getViewState().onDictDefinition(lookupResponse);

				translation.setDictionatyResponse(JsonUtils.serialize(lookupResponse));
				translation.setText(text);
				translation.setLang(lang);

			} else if (translateResponse != null){
				getViewState().onTranslation(translateResponse.getText());

				translation.setTranslateResponse(translateResponse.getText());
				translation.setText(text);
				translation.setLang(lang);
			}

			lang = null;
			text = null;

			databaseModule.getDatabaseManager().processTranslate(translation);
		}
	});

	@Override
	protected void onFirstViewAttach() {
		TranslateApp.getAppComponent().inject(this);

		databaseModule.getDatabaseManager().addOnTranslateAddedListener(this);
	}

	@Override
	public void onTranslateProcessed(@NonNull final Translation translation) {
		Guard.checkNotNull(translation, "null == translation");

		if (translation.equals(this.translation)) {
			getViewState().onFavoritesAction(translation.getIsFavorite());
		}
	}

	public void translate(@NonNull final String lang, @NonNull final String text) {
		Guard.checkNotNull(lang, "null == lang");
		Guard.checkNotNull(text, "null == text");

		if (text.isEmpty()) {
			getViewState().onEmptyInput();
			return;
		}

		translation = databaseModule.getDatabaseManager().getTranslateFromDatabase(lang, text);

		if (translation != null) {
			if (translation.getTranslateResponse() != null) {
				getViewState().onTranslation(translation.getTranslateResponse());
			} else if (translation.getDictionatyResponse() != null) {
				getViewState().onDictDefinition(JsonUtils.deserialize(LookupResponse.class, translation.getDictionatyResponse()));
			}
			getViewState().onFavoritesAction(translation.getIsFavorite());
			return;
		}

		this.lang = lang;
		this.text = text;

		translateResponse = null;
		lookupResponse = null;

		allQueriesLoaded.reset();
		apiModule.getTranslateApi().use(new TranslateCallback(null), null).translate(lang, text);
		apiModule.getDictApi().use(new DictCallback(null), null).lookup(lang, text, null, null);
	}

	public void addToFavorites() {
		if (translation != null) {
			translation.setIsFavorite(!translation.getIsFavorite());

			databaseModule.getDatabaseManager().processTranslate(translation);

			getViewState().onFavoritesAction(translation.getIsFavorite());
		} else {
			getViewState().onNothingToAddToFavorite();
		}
	}

	private final class TranslateCallback extends CallbackWithProgress<TranslateResponse> {

		private TranslateCallback(@Nullable final Progressable progressable) {
			super(progressable);
		}

		@Override
		public void onResponse(final Call<TranslateResponse> call,
							   final Response<TranslateResponse> response) {
			super.onResponse(call, response);
			if (response.body() != null) {
				translateResponse = response.body();
			}
			allQueriesLoaded.set(Queries.TRANSLATE);
		}

		@Override
		public void onFailure(final Call<TranslateResponse> call, final Throwable t) {
			super.onFailure(call, t);
			allQueriesLoaded.set(Queries.TRANSLATE);
		}
	}

	private final class DictCallback extends CallbackWithProgress<LookupResponse> {

		private DictCallback(@Nullable final Progressable progressable) {
			super(progressable);
		}

		@Override
		public void onResponse(final Call<LookupResponse> call,
							   final Response<LookupResponse> response) {
			super.onResponse(call, response);
			if (response.body() != null) {
				lookupResponse = response.body();
			}
			allQueriesLoaded.set(Queries.DICT);
		}

		@Override
		public void onFailure(final Call<LookupResponse> call, final Throwable t) {
			super.onFailure(call, t);
			allQueriesLoaded.set(Queries.DICT);
		}
	}

}
