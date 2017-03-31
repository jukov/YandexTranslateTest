package info.jukov.yandextranslatetest.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.adapter.FavoritesAdapter.FavoritesViewHolder;
import info.jukov.yandextranslatetest.model.storage.dao.DatabaseManager;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.util.Guard;
import info.jukov.yandextranslatetest.util.Log;

/**
 * User: jukov
 * Date: 28.03.2017
 * Time: 22:32
 *
 * Адаптер для отображения избранных переводов.
 */
public final class FavoritesAdapter extends AbstractTranslateHistoryAdapter<FavoritesViewHolder> {

	private static final Log LOG = new Log(FavoritesAdapter.class);

	public FavoritesAdapter(@NonNull final Context context, @NonNull final DatabaseManager databaseManager) {
		super(context, databaseManager);
	}

	public void processFavorite(@NonNull final Translation translation) {
		Guard.checkNotNull(translation, "null == translation");

		final int itemIndex = translationList.indexOf(translation);

		if (translation.getIsFavorite() == true) {
			if (itemIndex != -1) {
				translationList.get(itemIndex).setIsFavorite(true);

				LOG.verbose("Updated; Size: " + translationList.size() + "; Text: " + translation.getText());

				notifyItemChanged(itemIndex);

				LOG.verbose("Updated; Size: " + translationList.size() + "; Text: " + translation.getText());

			} else {
				translationList.add(translation);

				notifyDataSetChanged();

				LOG.verbose("Added; Size: " + translationList.size() + "; Text: " + translation.getText());
			}
		} else {
			if (itemIndex != -1) {
				translationList.remove(itemIndex);
				notifyItemRemoved(itemIndex);

				LOG.verbose("Removed; Size: " + translationList.size() + "; Text: " + translation.getText());
			}
		}
	}

	@Override
	public FavoritesViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

		final View view = inflater.inflate(R.layout.recycler_history_item, parent, false);

		final FavoritesViewHolder viewHolder = new FavoritesViewHolder(view);

		return viewHolder;
	}

	public class FavoritesViewHolder extends AbstractTranslateHistoryAdapter.ViewHolder {

		public FavoritesViewHolder(final View itemView) {
			super(itemView);
		}

		@Override
		public void onFavoriteClick() {

			getTranslation().setIsFavorite(!getTranslation().getIsFavorite());
			super.onFavoriteClick();
		}
	}
}
