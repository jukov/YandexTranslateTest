package info.jukov.yandextranslatetest.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.adapter.FavoritesAdapter.FavoritesViewHolder;
import info.jukov.yandextranslatetest.model.storage.dao.History;
import info.jukov.yandextranslatetest.model.storage.dao.HistoryDao;
import info.jukov.yandextranslatetest.ui.base.OnFavoriteStatusChangeListener;
import info.jukov.yandextranslatetest.util.Guard;
import info.jukov.yandextranslatetest.util.Log;
import java.util.List;

/**
 * User: jukov
 * Date: 28.03.2017
 * Time: 22:32
 */

public final class FavoritesAdapter extends AbstractHistoryAdapter<FavoritesViewHolder> {

	private static final Log LOG = new Log(AbstractHistoryAdapter.class);

	public FavoritesAdapter(@NonNull final Context context, @NonNull final HistoryDao historyDao,
		@Nullable final List<History> historyList, @NonNull final OnFavoriteStatusChangeListener listener) {
		super(context, historyDao, historyList, listener);
	}

	public void addOrRemoveFavorite(@NonNull final History history) {
		Guard.checkNotNull(history, "null == history");

		final int itemIndex = historyList.indexOf(history);

		if (history.getIsFavorite() == true) {
			if (itemIndex != -1) {
				historyList.get(itemIndex).setIsFavorite(true);
				notifyItemChanged(itemIndex);

			} else {
				historyList.add(history);

				historyDao.update(history);

				notifyDataSetChanged();
			}
		} else {
			if (itemIndex != -1) {
				historyList.remove(itemIndex);
				notifyItemRemoved(itemIndex);

				LOG.warning("Non favorite item in favorite list");
			}
		}
	}

	@Override
	public FavoritesViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

		final View view = inflater.inflate(R.layout.recycler_history_item, parent, false);

		final FavoritesViewHolder viewHolder = new FavoritesViewHolder(view);

		return viewHolder;
	}

	public class FavoritesViewHolder extends AbstractHistoryAdapter.ViewHolder {

		public FavoritesViewHolder(final View itemView) {
			super(itemView);
		}

		@Override
		public void onFavoriteClick() {
			final History history = historyList.get(getHolderPosition());

			if (history.getIsFavorite() == true) {
				listener.onFavoriteStatusChange(historyList.get(getHolderPosition()));
				historyList.remove(getHolderPosition());
				notifyItemRemoved(getHolderPosition());
			} else {
				LOG.warning("Non favorite item in favorite list");
				setFavorite(history.getIsFavorite());
			}

			history.setIsFavorite(!history.getIsFavorite());
			historyDao.update(history);
		}
	}
}
