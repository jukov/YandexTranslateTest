package info.jukov.yandextranslatetest.ui.screen.translate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import info.jukov.yandextranslatetest.R;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:31
 */

public final class TranslateFragment extends MvpAppCompatFragment implements TranslateView {

	public static TranslateFragment newInstance() {

		Bundle args = new Bundle();

		TranslateFragment fragment = new TranslateFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@InjectPresenter TranslatePresenter presenter;

	@BindView(R.id.editTextTranslatable) EditText editTextTranslatable;
	@BindView(R.id.buttonTranslate) Button buttonTranslate;
	@BindView(R.id.textViewTranslated) TextView textViewTranslated;
	@BindView(R.id.textViewDict) TextView textViewDict;

	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
							 @Nullable final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_translate, null);
		ButterKnife.bind(this, view);

		return view;
	}

	@OnClick(R.id.buttonTranslate)
	void onTranslateClick() {
		presenter.translate(null, editTextTranslatable.getText().toString());
	}

	@Override
	public void setTranslatedText(final String text) {
		textViewTranslated.setText(text);
	}

	@Override
	public void setDictText(final String text) {
		textViewDict.setText(text);
	}
}
