package info.jukov.yandextranslatetest.ui.screen.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.MvpAppCompatFragment;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.TranslateApp;
import info.jukov.yandextranslatetest.model.adapter.AbstractTranslateHistoryAdapter;
import info.jukov.yandextranslatetest.model.adapter.AbstractTranslateHistoryAdapter.OnDataSetChangedListener;
import info.jukov.yandextranslatetest.model.module.DatabaseModule;
import info.jukov.yandextranslatetest.model.module.TransferModule;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.ui.base.TranslationListHolder;
import info.jukov.yandextranslatetest.util.Log;
import javax.inject.Inject;

/**
 * User: jukov
 * Date: 03.04.2017
 * Time: 23:23
 */

public class BaseTranslationsListFragment extends MvpAppCompatFragment implements OnDataSetChangedListener, TranslationListHolder {

	private static final Log LOG = new Log(BaseTranslationsListFragment.class);

	@BindView(R.id.recyclerViewHistory) RecyclerView recyclerViewHistory;
	@BindView(R.id.textViewEmptyList) TextView textViewEmptyList;

	@Inject DatabaseModule databaseModule;
	@Inject TransferModule transferModule;

	protected AbstractTranslateHistoryAdapter adapter;

	@Nullable @Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
		@Nullable final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_history, null);
		ButterKnife.bind(this, view);
		TranslateApp.getAppComponent().inject(this);

		return view;
	}

	@Override
	public void onDataSetChange(final int currentSize) {
		if (currentSize == 0) {
			switchUiToEmptySplash();
		} else {
			switchUiToList();
		}
	}

	@Override
	public void viewFullTranslation(@NonNull final Translation translation) {
		transferModule.getTransferManager().onFullTranslate(translation);
	}

	private void switchUiToEmptySplash() {
		textViewEmptyList.setVisibility(View.VISIBLE);
		recyclerViewHistory.setVisibility(View.GONE);
	}

	private void switchUiToList() {
		textViewEmptyList.setVisibility(View.GONE);
		recyclerViewHistory.setVisibility(View.VISIBLE);
	}

}
