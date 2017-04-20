package info.jukov.yandextranslatetest.ui.screen.fragment;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.TranslateApp;
import info.jukov.yandextranslatetest.model.adapter.BaseTranslateHistoryAdapter;
import info.jukov.yandextranslatetest.model.adapter.BaseTranslateHistoryAdapter.OnDataSetChangedListener;
import info.jukov.yandextranslatetest.model.module.DatabaseModule;
import info.jukov.yandextranslatetest.model.module.TransferModule;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.presenter.TranslationListPresenter;
import info.jukov.yandextranslatetest.presenter.TranslationListView;
import info.jukov.yandextranslatetest.ui.base.TranslationListHolder;
import info.jukov.yandextranslatetest.util.Guard;
import info.jukov.yandextranslatetest.util.Log;
import info.jukov.yandextranslatetest.util.OsUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * User: jukov
 * Date: 03.04.2017
 * Time: 23:23
 */

public abstract class BaseTranslationListFragment extends MvpAppCompatFragment implements TranslationListView, OnQueryTextListener,
																						  OnDataSetChangedListener, TranslationListHolder {

	private static final Log LOG = new Log(BaseTranslationListFragment.class);

	@InjectPresenter TranslationListPresenter translationListPresenter;

	@BindView(R.id.recyclerViewHistory) RecyclerView recyclerViewHistory;
	@BindView(R.id.textViewEmptyList) TextView textViewEmptyList;
	@BindView(R.id.searchView) SearchView searchView;
	@BindView(R.id.containerTranslations) LinearLayout containerTranslations;

	@Inject DatabaseModule databaseModule;
	@Inject TransferModule transferModule;

	protected BaseTranslateHistoryAdapter adapter;

	private Dialog currentDialog = null;

	private int animationDuration;

	@Nullable @Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
		@Nullable final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_history, null);
		ButterKnife.bind(this, view);
		TranslateApp.getAppComponent().inject(this);

		searchView.setOnQueryTextListener(this);

		animationDuration = getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);

		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (currentDialog != null) {
			currentDialog.dismiss();
		}
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

	@Override
	public void onLongItemClick(@NonNull final Translation translation) {
		translationListPresenter.showActionDialog(translation);
	}

	@Override
	public void showActionDialog(@NonNull final Translation translation) {
		Guard.checkNotNull(translation, "null == translation");

		currentDialog = new AlertDialog.Builder(getActivity())
			.setTitle(R.string.translationListFragment_ActionsDialog_title)
			.setItems(R.array.translationListFragment_ActionsDialog_actions, null)
			.create();

		currentDialog.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(final DialogInterface dialog) {
				((AlertDialog) dialog).getListView().setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
						switch (position) {
							case 0:
								OsUtils.setTextToClipBoard(getContext(), translation.getTranslateResponse());
								break;
							case 1:
								databaseModule.getDatabaseManager().deleteTranslation(translation);
								break;
							default:
								LOG.warning("Unexpected item position");
						}
						translationListPresenter.closeDialog();
					}
				});
			}
		});

		currentDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(final DialogInterface dialog) {
				translationListPresenter.closeDialog();
				LOG.info("dismiss");
			}
		});

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
	public boolean onQueryTextChange(final String newText) {
		final List<Translation> filteredTranslationList = filter(getTranslationList(), newText);
		adapter.replaceAll(filteredTranslationList);
		recyclerViewHistory.scrollToPosition(0);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(final String query) {
		return false;
	}

	private static List<Translation> filter(final List<Translation> translationList, final String rawQuery) {
		final String query = rawQuery.toLowerCase();

		final List<Translation> filteredTranslationList = new ArrayList<>();
		for (final Translation translation : translationList) {
			if (translation.getText().toLowerCase().contains(query) || translation.getTranslateResponse().toLowerCase().contains(query)) {
				filteredTranslationList.add(translation);
			}
		}

		return filteredTranslationList;
	}

	protected abstract List<Translation> getTranslationList();

	private void switchUiToEmptySplash() {
		textViewEmptyList.setAlpha(0.0f);
		textViewEmptyList.setVisibility(View.VISIBLE);

		textViewEmptyList.animate()
			.alpha(1.0f)
			.setDuration(animationDuration)
			.setListener(null);

		containerTranslations.animate()
			.alpha(0.0f)
			.setDuration(animationDuration)
			.setListener(new AnimatorListener() {
				@Override
				public void onAnimationEnd(final Animator animation) {
					containerTranslations.setVisibility(View.GONE);
				}

				@Override public void onAnimationStart(final Animator animation) { }
				@Override public void onAnimationCancel(final Animator animation) { }
				@Override public void onAnimationRepeat(final Animator animation) { }
			});
	}

	private void switchUiToList() {
		if (containerTranslations.getVisibility() != View.VISIBLE) {
			containerTranslations.setAlpha(0.0f);
			containerTranslations.setVisibility(View.VISIBLE);

			containerTranslations.animate()
				.alpha(1.0f)
				.setDuration(animationDuration)
				.setListener(null);

			textViewEmptyList.animate()
				.alpha(0.0f)
				.setDuration(animationDuration)
				.setListener(new AnimatorListener() {
					@Override
					public void onAnimationEnd(final Animator animation) {
						textViewEmptyList.setVisibility(View.GONE);
					}

					@Override public void onAnimationStart(final Animator animation) { }
					@Override public void onAnimationCancel(final Animator animation) { }
					@Override public void onAnimationRepeat(final Animator animation) { }
				});
		}
	}

}
