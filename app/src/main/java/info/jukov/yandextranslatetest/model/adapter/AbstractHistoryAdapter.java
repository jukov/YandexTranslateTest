package info.jukov.yandextranslatetest.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.adapter.AbstractHistoryAdapter.ViewHolder;
import info.jukov.yandextranslatetest.model.storage.dao.History;
import info.jukov.yandextranslatetest.model.storage.dao.HistoryDao;
import info.jukov.yandextranslatetest.ui.base.OnFavoriteStatusChangeListener;
import info.jukov.yandextranslatetest.util.Guard;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jukov
 * Date: 21.03.2017
 * Time: 22:36
 */

public abstract class AbstractHistoryAdapter<VH extends ViewHolder> extends RecyclerView.Adapter<VH> {

	protected final List<History> historyList;

	protected final LayoutInflater inflater;

	protected final HistoryDao historyDao;

	protected final OnFavoriteStatusChangeListener listener;

	private static String formatInputLang(final String lang) {
		Guard.checkPreCondition(lang.length() == 5 || lang.contains("-"),
			"Lang must be correspond pattern 'xx-xx': " + lang);
		return lang.split("-", 2)[0].toUpperCase();
	}

	private static String formatOutputLang(final String lang) {
		Guard.checkPreCondition(lang.length() == 5 || lang.contains("-"),
			"Lang must be correspond pattern 'xx-xx'");
		return lang.split("-", 2)[1].toUpperCase();
	}

	protected AbstractHistoryAdapter(@NonNull final Context context, @NonNull final HistoryDao historyDao,
		@Nullable final List<History> historyList, @NonNull final OnFavoriteStatusChangeListener listener) {
		Guard.checkNotNull(context, "null == context");
		Guard.checkNotNull(historyDao, "null == historyDao");
		Guard.checkNotNull(listener, "null == listener");

		inflater = LayoutInflater.from(context);

		this.historyDao = historyDao;
		this.listener = listener;

		if (historyList != null) {
			this.historyList = new ArrayList<>(historyList);
		} else {
			this.historyList = new ArrayList<>();
		}
	}

	@Override
	public void onBindViewHolder(final VH holder, final int position) {

		final History history = historyList.get(position);

		holder.textViewInput.setText(history.getText());
		holder.textViewInputLang.setText(formatInputLang(history.getLang()));
		holder.textViewOutputLang.setText(formatOutputLang(history.getLang()));

		holder.setFavorite(history.getIsFavorite());

		if (history.getTranslateResponse() != null) {
			holder.textViewOutput.setText(history.getTranslateResponse());
		} else if (history.getDictionatyResponse() != null) {
			holder.textViewOutput.setText(history.getDictionatyResponse());
		}

		holder.setHolderPosition(position);
	}

	@Override
	public int getItemCount() {
		return historyList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.textViewInput) TextView textViewInput;
		@BindView(R.id.textViewOutput) TextView textViewOutput;
		@BindView(R.id.textViewInputLang) TextView textViewInputLang;
		@BindView(R.id.textViewOutputLang) TextView textViewOutputLang;
		@BindView(R.id.containerText) LinearLayout conteinerText;
		@BindView(R.id.imageViewFavorite) ImageView imageViewFavorite;
		private int holderPosition;

		protected ViewHolder(final View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);

			conteinerText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {

				}
			});

			imageViewFavorite.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					onFavoriteClick();
				}
			});
		}

		public int getHolderPosition() {
			return holderPosition;
		}

		public void setHolderPosition(final int holderPosition) {
			this.holderPosition = holderPosition;
		}

		protected void onFavoriteClick() {
			listener.onFavoriteStatusChange(historyList.get(holderPosition));
		}

		protected void setFavorite(final boolean isFavorite) {
			if (isFavorite) {
				imageViewFavorite.setImageResource(R.drawable.ic_heart);
			} else {
				imageViewFavorite.setImageResource(R.drawable.ic_heart_outline);
			}
		}
	}
}