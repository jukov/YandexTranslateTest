package info.jukov.yandextranslatetest.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.storage.Translation;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jukov
 * Date: 21.03.2017
 * Time: 22:36
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

	private final List<Translation> translations;

	private final LayoutInflater inflater;

	public HistoryAdapter(@NonNull final Context content) {
		translations = new ArrayList<>();

		inflater = LayoutInflater.from(content);
	}

	@Override
	public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

		final View view = inflater.inflate(R.layout.recycler_history_item, parent, false);

		final ViewHolder viewHolder = new ViewHolder(view);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		holder.textViewInput.setText(translations.get(position).getInputText());
		holder.textViewOutput.setText(translations.get(position).getOutputText());
		holder.textViewInputLang.setText(translations.get(position).getInputLang());
		holder.textViewOutputLang.setText(translations.get(position).getOutputLang());
	}

	@Override
	public int getItemCount() {
		return translations.size();
	}

	public void addTranslation(@NonNull final Translation translation) {
		translations.add(translation);

		notifyDataSetChanged();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.textViewInput) TextView textViewInput;
		@BindView(R.id.textViewOutput) TextView textViewOutput;
		@BindView(R.id.textViewInputLang) TextView textViewInputLang;
		@BindView(R.id.textViewOutputLang) TextView textViewOutputLang;

		public ViewHolder(final View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
