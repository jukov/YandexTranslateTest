package info.jukov.yandextranslatetest.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.adapter.HistoryAdapter.HistoryViewHolder;
import info.jukov.yandextranslatetest.model.storage.dao.DatabaseManager;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.util.Log;

/**
 * User: jukov
 * Date: 28.03.2017
 * Time: 22:34
 *
 * Адаптер для отображения истории переводов
 */
public final class HistoryAdapter extends AbstractTranslateHistoryAdapter<HistoryViewHolder> {

	private static final Log LOG = new Log(HistoryAdapter.class);

	public HistoryAdapter(@NonNull final Context context, @NonNull final DatabaseManager databaseManager) {
		super(context, databaseManager);
	}

	public void processTranslation(@NonNull final Translation translation) {

		final int itemIndex = translationList.indexOf(translation);

		if (itemIndex != -1) {
			translationList.remove(translation);
			translationList.add(itemIndex, translation);

			notifyItemChanged(itemIndex);

			LOG.verbose("Updated; Size: " + translationList.size() + "; Text: " + translation.getText());

		} else {
			translationList.add(translation);

			notifyDataSetChanged();

			LOG.verbose("Added; Size: " + translationList.size() + "; Text: " + translation.getText());
		}
	}

	@Override
	public HistoryViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

		final View view = inflater.inflate(R.layout.recycler_history_item, parent, false);

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