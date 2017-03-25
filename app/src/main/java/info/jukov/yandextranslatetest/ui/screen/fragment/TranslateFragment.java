package info.jukov.yandextranslatetest.ui.screen.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.adapter.LanguageAdapter;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponce;
import info.jukov.yandextranslatetest.model.storage.Language;
import info.jukov.yandextranslatetest.model.storage.Translation;
import info.jukov.yandextranslatetest.model.storage.preferences.LangPreferences;
import info.jukov.yandextranslatetest.presenter.TranslatePresenter;
import info.jukov.yandextranslatetest.presenter.TranslateView;
import info.jukov.yandextranslatetest.ui.format.DictionaryConstructor;
import info.jukov.yandextranslatetest.util.Log;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:31
 */

public final class TranslateFragment extends MvpAppCompatFragment implements TranslateView {

	private static final Log LOG = new Log(TranslateFragment.class);

	private static final String LANG_DELIMITER = "-";

	@InjectPresenter TranslatePresenter presenter;
	@BindView(R.id.spinnerInputLanguage) Spinner spinnerInputLanguage;
	@BindView(R.id.spinnerOutputLanguage) Spinner spinnerOutputLanguage;
	@BindView(R.id.editTextTranslatable) EditText editTextTranslatable;
	@BindView(R.id.buttonTranslate) Button buttonTranslate;
	@BindView(R.id.textViewTranslated) TextView textViewTranslated;
	@BindView(R.id.textViewDict) TextView textViewDict;
	@BindView(R.id.containerDictResult) LinearLayout containerDictResult;

	private LanguageAdapter inputSpinnerAdapter;
	private LanguageAdapter outputSpinnerAdapter;

	private OnTextTranslatedListener onTextTranslatedListener;

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

		initAdapters();
		initSpinners();

		return view;
	}

	@Override
	public void onPause() {
		super.onPause();

		LangPreferences
			.putMostUsedInputLangs(getContext(), inputSpinnerAdapter.getMostUsedLanguages());
		LangPreferences
			.putMostUsedOutputLangs(getContext(), outputSpinnerAdapter.getMostUsedLanguages());
	}

	@Override
	public void onAttach(final Context context) {
		super.onAttach(context);

		if (context instanceof OnTextTranslatedListener) {
			onTextTranslatedListener = (OnTextTranslatedListener) context;
		} else {
			LOG.error("Fragment attached to unexpected activity");
		}
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

	private void initAdapters() {
		final Set<Language> inputLangs = LangPreferences.getReadableWords(getContext());

		if (inputLangs == null) {
			setContentEnabled(false);
			return;
		}

		final Set<Language> outputLangs = new HashSet<>(inputLangs);

		final Set<Language> mostUsedInputLangs = LangPreferences
			.getMostUsedInputLangs(getContext());
		final Set<Language> mostUsedOutputLangs = LangPreferences
			.getMostUsedOutputLangs(getContext());

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

	@OnClick(R.id.buttonTranslate)
	void onTranslateClick() {
		presenter.translate(getLangForServer(), getTranslatableText());
	}

	@Override
	public void setTranslation(final List<String> translatedText) {

		final StringBuilder formattedText = new StringBuilder();

		for (final String text : translatedText) {
			formattedText.append(text).append("\n");
		}

		textViewDict.setText(formattedText);

		onTextTranslatedListener.onTextTranslated(new Translation(getTranslatableText(),
			getInputLang(), formattedText.toString(), getOutputLang()));
	}

	@Override
	public void setDictDefinition(final LookupResponce responce) {
		textViewTranslated
			.setText(responce.getDefinitions().get(0).getTranslations().get(0).getText());
		textViewDict
			.setText(DictionaryConstructor.formatDefinition(responce.getDefinitions().get(0)));
		DictionaryConstructor.visualiseLookupResponce(getContext(), containerDictResult, responce);
	}

	private void setContentEnabled(final boolean enabled) {
		spinnerInputLanguage.setEnabled(enabled);
		spinnerOutputLanguage.setEnabled(enabled);
		editTextTranslatable.setEnabled(enabled);
		buttonTranslate.setEnabled(enabled);
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

	public interface OnTextTranslatedListener {

		void onTextTranslated(Translation translation);
	}
}
