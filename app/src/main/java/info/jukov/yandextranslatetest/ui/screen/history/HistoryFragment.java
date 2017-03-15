package info.jukov.yandextranslatetest.ui.screen.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import com.arellomobile.mvp.MvpAppCompatFragment;
import info.jukov.yandextranslatetest.R;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 19:31
 */

public final class HistoryFragment extends MvpAppCompatFragment {

	public static HistoryFragment newInstance() {

		Bundle args = new Bundle();

		HistoryFragment fragment = new HistoryFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@BindView(R.id.editTextTranslatable)
	EditText editTextTranslatable;

	@BindView(R.id.buttonTranslate)
	Button buttonTranslate;

	@BindView(R.id.textViewTranslated)
	TextView textViewTranslated;

	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
							 @Nullable final Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_translate, null);

		return view;
	}
}
