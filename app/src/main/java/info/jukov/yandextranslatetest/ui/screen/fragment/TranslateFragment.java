package info.jukov.yandextranslatetest.ui.screen.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import info.jukov.yandextranslatetest.ui.base.Progressable;
import info.jukov.yandextranslatetest.ui.format.DictionaryConstructor;
import info.jukov.yandextranslatetest.util.KeyboardUtils;
import info.jukov.yandextranslatetest.util.Log;
import info.jukov.yandextranslatetest.util.StringUtils;
import info.jukov.yandextranslatetest.util.ToastUtils;
import java.util.HashSet;
import java.util.Set;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:31
 */

public final class TranslateFragment extends MvpAppCompatFragment implements TranslateView, Progressable {

	private static final Log LOG = new Log(TranslateFragment.class);

	private static final String LANG_DELIMITER = "-";

	@InjectPresenter TranslatePresenter presenter;

	@BindView(R.id.spinnerInputLanguage) Spinner spinnerInputLanguage;
	@BindView(R.id.spinnerOutputLanguage) Spinner spinnerOutputLanguage;
	@BindView(R.id.editTextTranslatable) EditText editTextTranslatable;
	@BindView(R.id.buttonTranslate) Button buttonTranslate;
	@BindView(R.id.buttonFavorite) ImageButton buttonFavorite;
	@BindView(R.id.textViewTranslated) TextView textViewTranslated;
	@BindView(R.id.textViewDict) TextView textViewDict;
	@BindView(R.id.containerDictResult) LinearLayout containerDictResult;
	@BindView(R.id.progressBar) ProgressBar progressBar;

	private LanguageAdapter inputSpinnerAdapter;
	private LanguageAdapter outputSpinnerAdapter;

	private String previousInputLangCode;
	private String previousOutputLangCode;

	private int progressableTasksCount = 0;

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

		initEditText();
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

	private void initEditText() {
		editTextTranslatable.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
				KeyboardUtils.hideSoftInput(getActivity());

				presenter.translate(getLangForServer(), getTranslatableText(), TranslateFragment.this);

				return true;
			}
		});
	}

	private void initAdapters() {
		final Set<Language> inputLangs = LangPreferences.getReadableWords(getContext());

		if (inputLangs == null) {
			setContentEnabled(false);
			return;
		}

		final Set<Language> outputLangs = new HashSet<>(inputLangs);

		final Set<Language> mostUsedInputLangs = LangPreferences.getMostUsedInputLangs(getContext());
		final Set<Language> mostUsedOutputLangs = LangPreferences.getMostUsedOutputLangs(getContext());

		if (mostUsedInputLangs != null && mostUsedInputLangs.size() > 0) {
			inputLangs.removeAll(mostUsedInputLangs);
			inputLangs.addAll(mostUsedInputLangs);
		}

		if (mostUsedOutputLangs != null && mostUsedOutputLangs.size() > 0) {
			outputLangs.removeAll(mostUsedOutputLangs);
			outputLangs.addAll(mostUsedOutputLangs);
		}

		inputSpinnerAdapter = new LanguageAdapter(getContext(), inputLangs);
		outputSpinnerAdapter = new LanguageAdapter(getContext(), outputLangs);
	}

	private void initSpinners() {
		spinnerInputLanguage.setAdapter(inputSpinnerAdapter);
		spinnerOutputLanguage.setAdapter(outputSpinnerAdapter);

		spinnerInputLanguage.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view,
				final int position, final long id) {
				if (position != 0) {
					inputSpinnerAdapter.pinItemToTop(position);
					spinnerInputLanguage.setSelection(0);
				}
			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {
			}
		});

		spinnerOutputLanguage.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view,
				final int position, final long id) {
				if (position != 0) {
					outputSpinnerAdapter.pinItemToTop(position);
					spinnerOutputLanguage.setSelection(0);
				}
			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {
			}
		});
	}

	@OnClick(R.id.buttonTranslate) void onTranslateClick() {
		KeyboardUtils.hideSoftInput(getActivity());
		presenter.translate(getLangForServer(), getTranslatableText(), this);
	}

	@OnClick(R.id.buttonFavorite) void onFavoriteClick() {
		presenter.addToFavorites();
	}

	@Override
	public void onTranslation(@NonNull final Translation translation) {

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
	}

	@Override
	public void onViewFullTranslation(final Translation translation) {
		onTranslation(translation);

		editTextTranslatable.setText(translation.getText());

		final int inputPosition = inputSpinnerAdapter.getPosition(StringUtils.formatInputLang(translation.getLang()));
		if (inputPosition != -1) {
			spinnerInputLanguage.setSelection(inputPosition);
		}

		final int outputPosition = outputSpinnerAdapter.getPosition(StringUtils.formatOutputLang(translation.getLang()));
		if (outputPosition != -1) {
			spinnerOutputLanguage.setSelection(outputPosition);
		}

		onFavoritesAction(translation.getIsFavorite());
	}

	@Override
	public void onEmptyInput() {
		ToastUtils.shortToast(getContext(), R.string.translateFragment_toast_errorNothingToTranslate);
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
		ToastUtils.shortToast(getContext(), R.string.translateFragment_toast_errorNothingToAddToFavorites);
	}

	@Override
	public void startProgress() {
		progressBar.setVisibility(View.VISIBLE);

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

	private void setContentEnabled(final boolean enabled) {
		spinnerInputLanguage.setEnabled(enabled);
		spinnerOutputLanguage.setEnabled(enabled);
		editTextTranslatable.setEnabled(enabled);
		buttonTranslate.setEnabled(enabled);
		buttonFavorite.setEnabled(enabled);

		if (enabled) {
			DrawableCompat.setTint(buttonFavorite.getDrawable(), ContextCompat.getColor(getContext(), R.color.colorAccent));
		} else {
			DrawableCompat.setTint(buttonFavorite.getDrawable(), ContextCompat.getColor(getContext(), R.color.favoriteDisabled));
		}
	}

	private String getLangForServer() {
		final String inputLang = getInputLang();
		final String outputLang = getOutputLang();
		return inputLang + LANG_DELIMITER + outputLang;
	}

	private String getInputLang() {
		return inputSpinnerAdapter.getItem(spinnerInputLanguage.getSelectedItemPosition());
	}

	private String getOutputLang() {
		return outputSpinnerAdapter.getItem(spinnerOutputLanguage.getSelectedItemPosition());
	}

	private String getTranslatableText() {
		return editTextTranslatable.getText().toString();
	}
}
