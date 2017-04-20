package info.jukov.yandextranslatetest.ui.screen.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.adapter.LanguageAdapter;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponse;
import info.jukov.yandextranslatetest.model.storage.Language;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.model.storage.preferences.LangPreferences;
import info.jukov.yandextranslatetest.presenter.TranslatePresenter;
import info.jukov.yandextranslatetest.presenter.TranslateView;
import info.jukov.yandextranslatetest.ui.DropdownView;
import info.jukov.yandextranslatetest.ui.base.Progressable;
import info.jukov.yandextranslatetest.ui.dialog.ErrorDialogBuilder;
import info.jukov.yandextranslatetest.ui.format.DictionaryConstructor;
import info.jukov.yandextranslatetest.util.KeyboardUtils;
import info.jukov.yandextranslatetest.util.Log;
import info.jukov.yandextranslatetest.util.OsUtils;
import info.jukov.yandextranslatetest.util.StringUtils;
import java.util.Set;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:31
 */

public final class TranslateFragment extends MvpAppCompatFragment implements TranslateView, Progressable {

	private static final Log LOG = new Log(TranslateFragment.class);

	private static final String LANG_DELIMITER = "-";

	private static final int HANDLER_MESSAGE_TRANSLATE = 1000;

	private static final int TRANSLATE_DELAY = 1000;

	@InjectPresenter TranslatePresenter presenter;

	@BindView(R.id.dropdownViewInputLang) DropdownView dropdownViewInputLang;
	@BindView(R.id.dropdownViewOutputLang) DropdownView dropdownViewOutputLang;
	@BindView(R.id.buttonSwapLang) ImageView buttonSwapLang;
	@BindView(R.id.editTextTranslatable) EditText editTextTranslatable;
	@BindView(R.id.textViewLettersCount) TextView textViewLettersCount;
	@BindView(R.id.buttonTranslate) Button buttonTranslate;
	@BindView(R.id.buttonFavorite) ImageButton buttonFavorite;
	@BindView(R.id.textViewTranslation) TextView textViewTranslated;
	@BindView(R.id.textViewTranscription) TextView textViewDict;
	@BindView(R.id.textViewCopyright) TextView textViewCopyright;
	@BindView(R.id.containerDictResult) LinearLayout containerDictResult;
	@BindView(R.id.containerTranslateResult) NestedScrollView containerTranslateResult;
	@BindView(R.id.progressBar) ProgressBar progressBar;

	private LanguageAdapter inputSpinnerAdapter;
	private LanguageAdapter outputSpinnerAdapter;

	private Language selectedInputLanguage;
	private Language selectedOutputLanguage;

	private int progressableTasksCount = 0;

	private Dialog currentDialog = null;

	private Handler translatableHandler;

	public static TranslateFragment newInstance() {

		Bundle args = new Bundle();

		TranslateFragment fragment = new TranslateFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
		@Nullable final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_translate, null);
		ButterKnife.bind(this, view);

		initUi();
		initAdapters();
		initSpinners();

		return view;
	}

	@Override
	public void onPause() {
		super.onPause();

		if (inputSpinnerAdapter != null && outputSpinnerAdapter != null) {
			LangPreferences.putMostUsedInputLangs(getContext(), inputSpinnerAdapter.getMostUsedLanguages());
			LangPreferences.putMostUsedOutputLangs(getContext(), outputSpinnerAdapter.getMostUsedLanguages());
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		updateLetterCounter(editTextTranslatable.length());
	}

	private void initUi() {

		editTextTranslatable.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
				presenter.onTranslate(getLangCodeForServer(), getTranslatableText(), TranslateFragment.this);

				return true;
			}
		});

		translatableHandler = new Handler() {
			@Override
			public void handleMessage(final Message msg) {
				if (msg.what == HANDLER_MESSAGE_TRANSLATE) {
					presenter.onTranslate(getLangCodeForServer(), getTranslatableText(), TranslateFragment.this);
				}
			}
		};

		editTextTranslatable.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
				updateLetterCounter(s.length());
			}

			@Override
			public void afterTextChanged(final Editable s) {
				translatableHandler.removeMessages(HANDLER_MESSAGE_TRANSLATE);
				if (s.length() >= 3) {
					translatableHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_TRANSLATE, TRANSLATE_DELAY);
				}
			}

			@Override
			public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
			}
		});

		textViewCopyright.setText(Html.fromHtml(getString(R.string.translateFragment_copyright)));
		textViewCopyright.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private void initAdapters() {
		presenter.initLangs(LangPreferences.getReadableWords(getContext()), LangPreferences.getMostUsedInputLangs(getContext()),
			LangPreferences.getMostUsedOutputLangs(getContext()));
	}

	private void initSpinners() {

		dropdownViewInputLang.addOnHeaderClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				KeyboardUtils.hideSoftInput(getActivity());
			}
		});

		dropdownViewOutputLang.addOnHeaderClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				KeyboardUtils.hideSoftInput(getActivity());
			}
		});

		dropdownViewInputLang.addOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				presenter.onInputLangSelected(position);
			}
		});

		dropdownViewOutputLang.addOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				presenter.onOutputLangSelected(position);
			}
		});
	}

	@OnClick(R.id.buttonTranslate)
	void onTranslateClick() {
		KeyboardUtils.hideSoftInput(getActivity());
		presenter.onTranslate(getLangCodeForServer(), getTranslatableText(), this);
	}

	@OnClick(R.id.buttonFavorite)
	void onFavoriteClick() {
		presenter.onFavoriteClick();
	}

	@OnClick(R.id.buttonSwapLang)
	void onSwapLangClick() {
		presenter.onSwapLangClick();
	}

	@Override
	public void onTranslation(@NonNull final Translation translation) {

		containerTranslateResult.scrollTo(0, 0);

		textViewTranslated.setText(translation.getTranslateResponse());

		final LookupResponse lookupResponse = translation.getLookupResponse();

		if (lookupResponse != null) {
			textViewDict.setText(DictionaryConstructor.formatDefinition(lookupResponse));

			containerDictResult.removeAllViews();

			textViewDict.setVisibility(View.VISIBLE);
			containerDictResult.setVisibility(View.VISIBLE);

			DictionaryConstructor.makeLookupResponse(getContext(), containerDictResult, lookupResponse);

		} else {
			textViewDict.setVisibility(View.GONE);
			containerDictResult.setVisibility(View.GONE);
		}

		translatableHandler.removeMessages(HANDLER_MESSAGE_TRANSLATE);

		if (!editTextTranslatable.getText().toString().equals(translation.getText())) {
			editTextTranslatable.setText(translation.getText());
		}

		final int inputPosition = inputSpinnerAdapter.getPosition(StringUtils.formatInputLang(translation.getLang()));
		if (inputPosition != -1) {
			inputSpinnerAdapter.pinItemToTop(inputPosition);
			dropdownViewInputLang.setText(inputSpinnerAdapter.getFirstItem().getReadableLangWord());
			selectedInputLanguage = inputSpinnerAdapter.getFirstItem();
		}

		final int outputPosition = outputSpinnerAdapter.getPosition(StringUtils.formatOutputLang(translation.getLang()));
		if (outputPosition != -1) {
			outputSpinnerAdapter.pinItemToTop(outputPosition);
			dropdownViewOutputLang.setText(outputSpinnerAdapter.getFirstItem().getReadableLangWord());
			selectedOutputLanguage = outputSpinnerAdapter.getFirstItem();
		}

		setButtonSwapLangEnabled(true);

		onFavoritesAction(translation.getIsFavorite());
	}

	@Override
	public void onEmptyInput() {
		OsUtils.shortToast(getContext(), R.string.translateFragment_toast_errorNothingToTranslate);
	}

	@Override
	public void onFavoritesAction(final boolean added) {
		if (added) {
			buttonFavorite.setImageResource(R.drawable.ic_heart);
		} else {
			buttonFavorite.setImageResource(R.drawable.ic_heart_outline);
		}
	}

	@Override
	public void onNothingToAddToFavorite() {
		OsUtils.shortToast(getContext(), R.string.translateFragment_toast_errorNothingToAddToFavorites);
	}

	@Override
	public void onLoadFailed(final int errorCode) {
		if (currentDialog != null) {
			currentDialog.dismiss();
		}
		currentDialog = ErrorDialogBuilder.BuildDialog(getActivity(), errorCode, presenter);
		currentDialog.show();
	}

	@Override
	public void closeDialog() {
		if (currentDialog != null) {
			currentDialog.dismiss();
			currentDialog = null;
		} else {
			LOG.warning("Attempt to close nonexistent dialog");
		}
	}

	@Override
	public void swapLang() {
		inputSpinnerAdapter.pinItemToTop(selectedOutputLanguage.getCode());
		dropdownViewInputLang.setText(selectedOutputLanguage.getReadableLangWord());

		outputSpinnerAdapter.pinItemToTop(selectedInputLanguage.getCode());
		dropdownViewOutputLang.setText(selectedInputLanguage.getReadableLangWord());

		final Language temp = selectedInputLanguage;
		selectedInputLanguage = selectedOutputLanguage;
		selectedOutputLanguage = temp;
	}

	@Override
	public void selectInputLang(final int position) {
		if (inputSpinnerAdapter.isWithDetectLanguage() && position == 0) {
			selectedInputLanguage = inputSpinnerAdapter.getItem(position);
			setButtonSwapLangEnabled(false);
		} else {
			inputSpinnerAdapter.pinItemToTop(position);
			selectedInputLanguage = inputSpinnerAdapter.getFirstItem();
			setButtonSwapLangEnabled(true);
		}
		dropdownViewInputLang.setText(selectedInputLanguage.getReadableLangWord());
	}

	@Override
	public void selectOutputLang(final int position) {
		if (position != 0) {
			outputSpinnerAdapter.pinItemToTop(position);
			selectedOutputLanguage = outputSpinnerAdapter.getFirstItem();
			dropdownViewOutputLang.setText(selectedOutputLanguage.getReadableLangWord());
		}
	}

	@Override
	public void setInputLangs(final Set<Language> inputLangs) {
		inputSpinnerAdapter = new LanguageAdapter(getContext(), inputLangs, true);
		dropdownViewInputLang.setAdapter(inputSpinnerAdapter);
		dropdownViewInputLang.setText(inputSpinnerAdapter.getFirstItem().getReadableLangWord());
		selectedInputLanguage = inputSpinnerAdapter.getFirstItem();
	}

	@Override
	public void setOutputLangs(final Set<Language> outputLangs) {
		outputSpinnerAdapter = new LanguageAdapter(getContext(), outputLangs, false);
		dropdownViewOutputLang.setAdapter(outputSpinnerAdapter);
		dropdownViewOutputLang.setText(outputSpinnerAdapter.getFirstItem().getReadableLangWord());
		selectedOutputLanguage = outputSpinnerAdapter.getFirstItem();
	}

	@Override
	public void onTextTooLong() {
		OsUtils.shortToast(getContext(), R.string.translateFragment_toast_errorTextTooLong);
	}

	@Override
	public void startProgress() {
		progressBar.setVisibility(View.VISIBLE);

		textViewTranslated.setText("");
		textViewDict.setText("");
		containerDictResult.removeAllViews();

		setContentEnabled(false);

		progressableTasksCount++;
	}

	@Override
	public void stopProgress() {
		progressableTasksCount--;
		if (progressableTasksCount <= 0) {

			progressBar.setVisibility(View.INVISIBLE);

			setContentEnabled(true);
		}
	}

	private void updateLetterCounter(final int lettersCount) {
		if (lettersCount > 100) {
			textViewLettersCount.setText(getString(R.string.translateFragment_textViewLettersCount, Integer.toString(lettersCount)));
			textViewLettersCount.setVisibility(View.VISIBLE);
		} else {
			textViewLettersCount.setVisibility(View.INVISIBLE);
		}
	}

	private void setContentEnabled(final boolean enabled) {
		dropdownViewInputLang.setEnabled(enabled);
		dropdownViewOutputLang.setEnabled(enabled);
		editTextTranslatable.setEnabled(enabled);
		buttonTranslate.setEnabled(enabled);
		buttonFavorite.setEnabled(enabled);

		if (!selectedInputLanguage.getCode().equals(LanguageAdapter.DETECT_LANGS_CODE)) {
			setButtonSwapLangEnabled(enabled);
		}

		if (getContext() != null) {
			if (enabled) {
				DrawableCompat.setTint(buttonFavorite.getDrawable(), ContextCompat.getColor(getContext(), R.color.colorAccent));
			} else {
				DrawableCompat.setTint(buttonFavorite.getDrawable(), ContextCompat.getColor(getContext(), R.color.colorDisabled));
			}
		}
	}

	private void setButtonSwapLangEnabled(final boolean enabled) {
		buttonSwapLang.setEnabled(enabled);
		if (getContext() != null) {
			if (enabled) {
				DrawableCompat.setTint(buttonSwapLang.getDrawable(), ContextCompat.getColor(getContext(), R.color.colorAccent));
			} else {
				DrawableCompat.setTint(buttonSwapLang.getDrawable(), ContextCompat.getColor(getContext(), R.color.colorDisabled));
			}
		}
	}

	private String getLangCodeForServer() {
		final String inputLang = getInputLangCode();
		final String outputLang = getOutputLangCode();
		if (LanguageAdapter.DETECT_LANGS_CODE.equals(inputLang)) {
			return outputLang;
		}
		return inputLang + LANG_DELIMITER + outputLang;
	}

	private String getInputLangCode() {
		return selectedInputLanguage.getCode();
	}

	private String getOutputLangCode() {
		return selectedOutputLanguage.getCode();
	}

	private String getTranslatableText() {
		return editTextTranslatable.getText().toString();
	}
}
