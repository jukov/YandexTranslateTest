package info.jukov.yandextranslatetest.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.adapter.FavoritesAdapter.FavoritesViewHolder;
import info.jukov.yandextranslatetest.model.storage.dao.DatabaseManager;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.ui.base.TranslationListHolder;
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

	public FavoritesAdapter(@NonNull final Context context, @NonNull final DatabaseManager databaseManager,
		@NonNull final TranslationListHolder translationListHolder, @NonNull final OnDataSetChangedListener dataSetChangedListener) {
		super(context, databaseManager, translationListHolder, dataSetChangedListener);
	}

	@Override
	public void processTranslation(@NonNull final Translation translation) {
		Guard.checkNotNull(translation, "null == translation");

		final int itemIndex = getTranslationList().indexOf(translation);

		if (translation.getIsFavorite() == true) {
			if (itemIndex != -1) {
				getTranslationList().get(itemIndex).setIsFavorite(true);

				LOG.verbose("Updated; Size: " + getTranslationList().size() + "; Text: " + translation.getText());

				getDataSetChangedListener().onDataSetChange(getItemCount());

				LOG.verbose("Updated; Size: " + getTranslationList().size() + "; Text: " + translation.getText());

			} else {
				getTranslationList().add(translation);

				getDataSetChangedListener().onDataSetChange(getItemCount());

				LOG.verbose("Added; Size: " + getTranslationList().size() + "; Text: " + translation.getText());
			}
		} else {
			if (itemIndex != -1) {
				getTranslationList().remove(translation);

				getDataSetChangedListener().onDataSetChange(getItemCount());

				LOG.verbose("Removed; Size: " + getTranslationList().size() + "; Text: " + translation.getText());
			}
		}
	}

	@Override
	public void deleteFavorites() {
		getTranslationList().clear();

		getDataSetChangedListener().onDataSetChange(getItemCount());
	}

	@Override
	public void deleteHistory() {
		getTranslationList().clear();
	}

	@Override
	public FavoritesViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
		final View view = getInflater().inflate(R.layout.recycler_history_item, parent, false);

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
