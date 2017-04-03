package info.jukov.yandextranslatetest.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.adapter.HistoryAdapter.HistoryViewHolder;
import info.jukov.yandextranslatetest.model.storage.dao.DatabaseManager;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.ui.base.TranslationListHolder;
import info.jukov.yandextranslatetest.util.Log;
import java.util.Iterator;

/**
 * User: jukov
 * Date: 28.03.2017
 * Time: 22:34
 *
 * Адаптер для отображения истории переводов
 */
public final class HistoryAdapter extends AbstractTranslateHistoryAdapter<HistoryViewHolder> {

	private static final Log LOG = new Log(HistoryAdapter.class);

	public HistoryAdapter(@NonNull final Context context, @NonNull final DatabaseManager databaseManager,
		@NonNull final TranslationListHolder translationListHolder, @NonNull final OnDataSetChangedListener dataSetChangedListener) {
		super(context, databaseManager, translationListHolder, dataSetChangedListener);
	}

	@Override
	public void processTranslation(@NonNull final Translation translation) {

		final int itemIndex = getTranslationList().indexOf(translation);

		if (itemIndex == -1) {
			getTranslationList().add(translation);

			notifyDataSetChanged();
			getDataSetChangedListener().onDataSetChange(getItemCount());

			LOG.verbose("Added; Size: " + getTranslationList().size() + "; Text: " + translation.getText());
		}
	}

	@Override
	public void deleteFavorites() {
		for (final Iterator<Translation> iterator = getTranslationList().iterator(); iterator.hasNext(); ) {
			if (iterator.next().getIsFavorite() == true) {
				iterator.remove();
			}
		}

		notifyDataSetChanged();
		getDataSetChangedListener().onDataSetChange(getItemCount());
	}

	@Override
	public void deleteHistory() {
		for (final Iterator<Translation> iterator = getTranslationList().iterator(); iterator.hasNext(); ) {
			if (iterator.next().getIsFavorite() == false) {
				iterator.remove();
			}
		}

		notifyDataSetChanged();
		getDataSetChangedListener().onDataSetChange(getItemCount());
	}

	@Override
	public HistoryViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

		final View view = getInflater().inflate(R.layout.recycler_history_item, parent, false);

		final HistoryViewHolder viewHolder = new HistoryViewHolder(view);

		return viewHolder;
	}

	public class HistoryViewHolder extends AbstractTranslateHistoryAdapter.ViewHolder {

		public HistoryViewHolder(final View itemView) {
			super(itemView);
		}

		@Override
		public void onFavoriteClick() {
			getTranslation().setIsFavorite(!getTranslation().getIsFavorite());

			setFavorite(getTranslation().getIsFavorite());

			super.onFavoriteClick();
		}
	}
}
