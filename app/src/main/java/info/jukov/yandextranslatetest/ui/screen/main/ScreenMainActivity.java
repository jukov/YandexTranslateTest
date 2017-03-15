package info.jukov.yandextranslatetest.ui.screen.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import info.jukov.yandextranslatetest.R;

/**
 * User: jukov
 * Date: 14.03.2017
 * Time: 21:47
 */

public final class ScreenMainActivity extends MvpAppCompatActivity implements ScreenMainView {

	@InjectPresenter
	ScreenMainPresenter presenter;

	@BindView(R.id.editTextTranslatable)
	EditText editTextTranslatable;

	@BindView(R.id.buttonTranslate)
	Button buttonTranslate;

	@BindView(R.id.textViewTranslated)
	TextView textViewTranslated;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_main);

		ButterKnife.bind(this);
	}

	@OnClick(R.id.buttonTranslate)
	void onTranslateClick() {
		presenter.translate(null, editTextTranslatable.getText().toString());
	}

	@Override
	public void setTranslatedText(final String text) {
		textViewTranslated.setText(text);
	}
}
