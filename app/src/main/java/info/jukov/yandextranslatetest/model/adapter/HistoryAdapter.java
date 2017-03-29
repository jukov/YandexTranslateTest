package info.jukov.yandextranslatetest.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.adapter.HistoryAdapter.HistoryViewHolder;
import info.jukov.yandextranslatetest.model.storage.dao.History;
import info.jukov.yandextranslatetest.model.storage.dao.HistoryDao;
import info.jukov.yandextranslatetest.ui.base.OnFavoriteStatusChangeListener;
import info.jukov.yandextranslatetest.util.Guard;
import java.util.List;

/**
 * User: jukov
 * Date: 28.03.2017
 * Time: 22:34
 */

public final class HistoryAdapter extends AbstractHistoryAdapter<HistoryViewHolder> {

	public HistoryAdapter(@NonNull final Context context, @NonNull final HistoryDao historyDao,
		@Nullable final List list, @NonNull final OnFavoriteStatusChangeListener listener) {
		super(context, historyDao, list, listener);

	}

	public void addOrUpdateHistoryItem(@NonNull final History history) {

		final int itemIndex = historyList.indexOf(history);

		if (itemIndex != -1) {
			historyList.remove(history);
			historyList.add(itemIndex, history);

			notifyItemChanged(itemIndex);

		} else {
			historyList.add(history);

			historyDao.insert(history);

			notifyDataSetChanged();
		}
	}

	@Override
	public HistoryViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

		final View view = inflater.inflate(R.layout.recycler_history_item, parent, false);

		final HistoryViewHolder viewHolder = new HistoryViewHolder(view);

		return viewHolder;
	}

	public class HistoryViewHolder extends AbstractHistoryAdapter.ViewHolder {

		public HistoryViewHolder(final View itemView) {
			super(itemView);
		}

		@Override
		public void onFavoriteClick() {
			final History history = historyList.get(getHolderPosition());
			history.setIsFavorite(!history.getIsFavorite());

			setFavorite(history.getIsFavorite());

			listener.onFavoriteStatusChange(history);

			historyDao.update(history);
		}
	}
}
