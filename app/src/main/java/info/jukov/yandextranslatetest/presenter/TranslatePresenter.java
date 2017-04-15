package info.jukov.yandextranslatetest.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import info.jukov.yandextranslatetest.TranslateApp;
import info.jukov.yandextranslatetest.model.module.ApiModule;
import info.jukov.yandextranslatetest.model.module.DatabaseModule;
import info.jukov.yandextranslatetest.model.module.TransferModule;
import info.jukov.yandextranslatetest.model.network.CallbackWithProgress;
import info.jukov.yandextranslatetest.model.network.ErrorCodes;
import info.jukov.yandextranslatetest.model.network.ErrorResponse;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponse;
import info.jukov.yandextranslatetest.model.network.translate.TranslateResponse;
import info.jukov.yandextranslatetest.model.storage.Language;
import info.jukov.yandextranslatetest.model.storage.dao.DatabaseManager.DatabaseListener;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.model.transfer.TransferManager.OnFullTranslateListener;
import info.jukov.yandextranslatetest.ui.base.Progressable;
import info.jukov.yandextranslatetest.ui.dialog.DialogCloser;
import info.jukov.yandextranslatetest.util.Guard;
import info.jukov.yandextranslatetest.util.JsonUtils;
import info.jukov.yandextranslatetest.util.MultiSetBoolean;
import info.jukov.yandextranslatetest.util.MultiSetBoolean.OnValueTrueListener;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Response;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:35
 */
@InjectViewState
public final class TranslatePresenter extends MvpPresenter<TranslateView> implements DatabaseListener, OnFullTranslateListener,
																					 DialogCloser {

	public static final int MAX_LENGTH_OF_TRANSLATABLE_TEXT = 10000;

	private enum Queries {
		TRANSLATE,
		DICT
	}

	@Inject ApiModule apiModule;

	@Inject DatabaseModule databaseModule;
	@Inject TransferModule transferModule;

	private TranslateResponse translateResponse;
	private LookupResponse lookupResponse;

	private String lang;
	private String text;

	private Translation currentTranslation;

	private final MultiSetBoolean<Queries> allQueriesLoaded = new MultiSetBoolean<>(Queries.values().length, new OnValueTrueListener() {
		@Override
		public void onTrue() {

			currentTranslation = new Translation();

			if (translateResponse != null) {
				currentTranslation.setTranslateResponse(translateResponse.getText());
			}

			if (lookupResponse != null && !lookupResponse.isEmpty()) {
				currentTranslation.setDictionaryResponse(JsonUtils.serialize(lookupResponse));
			}

			currentTranslation.setText(text);
			currentTranslation.setLang(lang);

			getViewState().onTranslation(currentTranslation);

			lang = null;
			text = null;

			databaseModule.getDatabaseManager().processTranslate(currentTranslation);
		}
	});

	@Override
	protected void onFirstViewAttach() {
		TranslateApp.getAppComponent().inject(this);

		databaseModule.getDatabaseManager().addOnTranslateAddedListener(this);
		transferModule.getTransferManager().addListener(this);
	}

	public void initLangs(@Nullable final Set<Language> readableWords, @Nullable final Set<Language> mostUsedInputLangs,
		@Nullable final Set<Language> mostUsedOutputLangs) {

		if (readableWords == null) {
			return;
		}

		final Set<Language> inputLangs = new HashSet<>(readableWords);
		final Set<Language> outputLangs = new HashSet<>();

		//Клонируем языки, чтобы избежать коллизий из-за общего
		// параметра mostUsedPriority в Language
		for (final Language language : readableWords) {
			outputLangs.add(language.clone());
		}

		if (mostUsedInputLangs != null && mostUsedInputLangs.size() > 0) {
			//Вставляем языки с расставленным приоритетом
			inputLangs.removeAll(mostUsedInputLangs);
			inputLangs.addAll(mostUsedInputLangs);
		}

		if (mostUsedOutputLangs != null && mostUsedOutputLangs.size() > 0) {
			//Вставляем языки с расставленным приоритетом
			outputLangs.removeAll(mostUsedOutputLangs);
			outputLangs.addAll(mostUsedOutputLangs);
		}

		getViewState().setInputLangs(inputLangs);
		getViewState().setOutputLangs(outputLangs);
	}

	public void onTranslate(@NonNull final String lang, @NonNull final String text, @NonNull final Progressable progressable) {
		Guard.checkNotNull(lang, "null == lang");
		Guard.checkNotNull(text, "null == text");
		Guard.checkNotNull(progressable, "null == progressable");

		translate(lang, text, progressable);
	}

	public void onFavoriteClick() {
		addToFavorites();
	}

	public void onSwapLangClick() {
		getViewState().swapLang();
	}

	public void onInputLangSelected(final int position) {
		if (position != 0) {
			getViewState().selectInputLang(position);
		}
	}

	public void onOutputLangSelected(final int position) {
		if (position != 0) {
			getViewState().selectOutputLang(position);
		}
	}

	@Override
	public void onTranslateProcessed(@NonNull final Translation translation) {
		Guard.checkNotNull(translation, "null == currentTranslation");

		if (translation.equals(this.currentTranslation)) {
			getViewState().onFavoritesAction(translation.getIsFavorite());
		}
	}

	@Override
	public void onTranslateDeleted(@NonNull final Translation translation) {
		Guard.checkNotNull(translation, "null == translation");

		if (translation.equals(currentTranslation)) {
			getViewState().onFavoritesAction(false);
		}
	}

	@Override
	public void onHistoryDeleted() {
		//unused
	}

	@Override
	public void onFavoritesDeleted() {
		currentTranslation = null;

		getViewState().onFavoritesAction(false);
	}

	@Override
	public void onFullTranslateListener(@NonNull final Translation translation) {
		Guard.checkNotNull(translation, "null == translation");

		currentTranslation = translation;

		getViewState().onTranslation(translation);
	}

	@Override
	public void closeDialog() {
		getViewState().closeDialog();
	}

	private void translate(final String lang, final String text, final Progressable progressable) {

		if (text.isEmpty()) {
			getViewState().onEmptyInput();
			return;
		}

		if (text.length() > MAX_LENGTH_OF_TRANSLATABLE_TEXT) {
			getViewState().onTextTooLong();
			return;
		}

		currentTranslation = databaseModule.getDatabaseManager().getTranslateFromDatabase(lang, text);

		if (currentTranslation != null) {
			getViewState().onTranslation(currentTranslation);
			getViewState().onFavoritesAction(currentTranslation.getIsFavorite());
			return;
		}

		this.lang = lang;
		this.text = text;

		translateResponse = null;
		lookupResponse = null;

		allQueriesLoaded.reset();
		apiModule.getTranslateApi().use(new TranslateCallback(progressable), progressable).translate(lang, text);
		apiModule.getDictApi().use(new DictCallback(progressable), progressable).lookup(lang, text, null, null);
	}

	private void addToFavorites() {
		if (currentTranslation != null) {
			currentTranslation.setIsFavorite(!currentTranslation.getIsFavorite());

			databaseModule.getDatabaseManager().processTranslate(currentTranslation);

			getViewState().onFavoritesAction(currentTranslation.getIsFavorite());
		} else {
			getViewState().onNothingToAddToFavorite();
		}
	}

	private void reportLoadFailed(final int errorCode) {
		getViewState().onLoadFailed(errorCode);
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
				allQueriesLoaded.set(Queries.TRANSLATE);
				return;
			}

			if (getErrorBody() != null) {
				ErrorResponse errorResponse = JsonUtils.deserialize(ErrorResponse.class, getErrorBody());
				if (errorResponse != null) {
					reportLoadFailed(errorResponse.getCode());
				}
			}

			reportLoadFailed(response.code());
		}

		@Override
		public void onFailure(final Call<TranslateResponse> call, final Throwable t) {
			super.onFailure(call, t);
			reportLoadFailed(ErrorCodes.NETWORK_ERROR_CUSTOM);
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

			//Ошибки от Словаря игнорируются, в случае если запрос от словаря
			// не прошел просто показываем результат Переводчика
			allQueriesLoaded.set(Queries.DICT);
		}

		@Override
		public void onFailure(final Call<LookupResponse> call, final Throwable t) {
			super.onFailure(call, t);
			allQueriesLoaded.set(Queries.DICT);
		}
	}
}
