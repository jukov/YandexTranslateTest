package info.jukov.yandextranslatetest.ui.screen.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.adapter.LanguageSpinnerAdapter;
import info.jukov.yandextranslatetest.model.storage.Language;
import info.jukov.yandextranslatetest.model.storage.preferences.LangPreferences;
import info.jukov.yandextranslatetest.presenter.TranslatePresenter;
import info.jukov.yandextranslatetest.presenter.TranslateView;
import java.util.List;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:31
 */

public final class TranslateFragment extends MvpAppCompatFragment implements TranslateView {

	private static final String LANG_SELIMITER = "-";

	public static TranslateFragment newInstance() {

		Bundle args = new Bundle();

		TranslateFragment fragment = new TranslateFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@InjectPresenter TranslatePresenter presenter;

	@BindView(R.id.spinnerInputLanguage) Spinner spinnerInputLanguage;
	@BindView(R.id.spinnerOutputLanguage) Spinner spinnerOutputLanguage;
	@BindView(R.id.editTextTranslatable) EditText editTextTranslatable;
	@BindView(R.id.buttonTranslate) Button buttonTranslate;
	@BindView(R.id.textViewTranslated) TextView textViewTranslated;
	@BindView(R.id.textViewDict) TextView textViewDict;

	private LanguageSpinnerAdapter inputSpinnerAdapter;
	private LanguageSpinnerAdapter outputSpinnerAdapter;

	private int lastInputLangPosition = 0;
	private int lastOutputLangPosition = 0;

	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
							 @Nullable final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_translate, null);
		ButterKnife.bind(this, view);

		initSpinners();

		return view;
	}

	private void initSpinners() {

		final List<Language> languages = LangPreferences.getReadableWords(getContext());

		if (languages == null) {
			setContentEnabled(false);
			return;
		}

		inputSpinnerAdapter = new LanguageSpinnerAdapter(getContext(), languages);
		outputSpinnerAdapter = new LanguageSpinnerAdapter(getContext(), languages);

		spinnerInputLanguage.setAdapter(inputSpinnerAdapter);
		spinnerOutputLanguage.setAdapter(outputSpinnerAdapter);

		spinnerInputLanguage.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view,
									   final int position, final long id) {
				if (0 != position) {
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
				if (lastOutputLangPosition != position) {
					outputSpinnerAdapter.pinItemToTop(position);
					lastOutputLangPosition = position;
					spinnerOutputLanguage.setSelection(0);
				}
			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {

			}
		});
	}

	@OnClick(R.id.buttonTranslate)
	void onTranslateClick() {
		presenter.translate(getLang(), editTextTranslatable.getText().toString());
	}

	@Override
	public void setTranslatedText(final String text) {
		textViewTranslated.setText(text);
	}

	@Override
	public void setDictText(final String text) {
		textViewDict.setText(text);
	}

	private void setContentEnabled(final boolean enabled) {
		spinnerInputLanguage.setEnabled(enabled);
		spinnerOutputLanguage.setEnabled(enabled);
		editTextTranslatable.setEnabled(enabled);
		buttonTranslate.setEnabled(enabled);
	}

	private String getLang() {
		final String inputLang = inputSpinnerAdapter.getItem(spinnerInputLanguage.getSelectedItemPosition());
		final String outputLang = outputSpinnerAdapter.getItem(spinnerOutputLanguage.getSelectedItemPosition());
		return inputLang + LANG_SELIMITER + outputLang;
	}
}
