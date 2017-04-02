package info.jukov.yandextranslatetest.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
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
import info.jukov.yandextranslatetest.model.adapter.AbstractTranslateHistoryAdapter.ViewHolder;
import info.jukov.yandextranslatetest.model.storage.dao.DatabaseManager;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.ui.base.TranslateListHolder;
import info.jukov.yandextranslatetest.util.Guard;
import info.jukov.yandextranslatetest.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jukov
 * Date: 21.03.2017
 * Time: 22:36
 *
 * Базовый адаптер для отображения переводов.
 */
public abstract class AbstractTranslateHistoryAdapter<VH extends ViewHolder> extends RecyclerView.Adapter<VH> {

	protected final List<Translation> translationList;

	protected final LayoutInflater inflater;

	private final DatabaseManager databaseManager;

	private final TranslateListHolder translateListHolder;

	protected  AbstractTranslateHistoryAdapter(@NonNull final Context context, @NonNull final DatabaseManager databaseManager,
		@NonNull final TranslateListHolder translateListHolder) {
		Guard.checkNotNull(context, "null == context");
		Guard.checkNotNull(databaseManager, "null == databaseManager");
		Guard.checkNotNull(translateListHolder, "null == translateListHolder");

		inflater = LayoutInflater.from(context);

		this.translateListHolder = translateListHolder;
		this.databaseManager = databaseManager;

		translationList = new ArrayList<>();
	}

	@Override
	public void onBindViewHolder(final VH holder, final int position) {
		holder.bind(translationList.get(position));
	}

	@Override
	public int getItemCount() {
		return translationList.size();
	}

	public void setTranslations(final List<Translation> translationList) {
		this.translationList.addAll(translationList);
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.textViewInput) TextView textViewInput;
		@BindView(R.id.textViewOutput) TextView textViewOutput;
		@BindView(R.id.textViewInputLang) TextView textViewInputLang;
		@BindView(R.id.textViewOutputLang) TextView textViewOutputLang;
		@BindView(R.id.containerText) LinearLayout conteinerText;
		@BindView(R.id.imageViewFavorite) ImageView imageViewFavorite;

		private Translation translation;

		protected ViewHolder(final View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);

			conteinerText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					translateListHolder.viewFullTranslation(translation);
				}
			});

			imageViewFavorite.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					onFavoriteClick();
				}
			});
		}

		public void bind(@NonNull final Translation translation) {
			this.translation = translation;

			textViewInput.setText(translation.getText());
			textViewInputLang.setText(StringUtils.formatInputLang(translation.getLang()).toUpperCase());
			textViewOutputLang.setText(StringUtils.formatOutputLang(translation.getLang()).toUpperCase());

			setFavorite(translation.getIsFavorite());

			if (translation.getTranslateResponse() != null) {
				textViewOutput.setText(translation.getTranslateResponse());
			} else if (translation.getDictionatyResponse() != null) {
				textViewOutput.setText(translation.getDictionatyResponse());
			}
		}

		public Translation getTranslation() {
			return translation;
		}

		protected void onFavoriteClick() {
			databaseManager.processTranslate(translation);
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