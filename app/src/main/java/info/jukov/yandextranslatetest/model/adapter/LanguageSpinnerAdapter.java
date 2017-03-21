package info.jukov.yandextranslatetest.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.storage.Language;
import info.jukov.yandextranslatetest.util.Log;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * User: jukov
 * Date: 19.03.2017
 * Time: 16:33
 */

public final class LanguageSpinnerAdapter extends BaseAdapter {

	private static final Log LOG = new Log(LanguageSpinnerAdapter.class);

	private static final int MOST_USED_ITEM_COUNT = 3;

	/**
	 * Сортирует объекты типа {@link Language}.
	 * В начало списка попадают объекты с {@link Language#getMostUsedPriority()} больше нуля,
	 * затем все остальные объекты по {@link Language#getReadableLangWord()} в алфивитном порядке.
	 */
	private static final Comparator<Language> mostUsedcomparator = new Comparator<Language>() {
		//В данной ситуации переполнение int невозможно на практике
		@SuppressWarnings("SubtractionInCompareTo")
		@Override
		public int compare(final Language o1, final Language o2) {
			//Если у одного из элементов указана позиция в топе, то сравниваем по позиции
			if (o1.getMostUsedPriority() != 0 || o2.getMostUsedPriority() != 0) {
				return o1.getMostUsedPriority() - o2.getMostUsedPriority();

				//Если позиция не указана, то сортируем по алфавиту
			} else {
				return String.CASE_INSENSITIVE_ORDER
					.compare(o1.getReadableLangWord(), o2.getReadableLangWord());
			}
		}
	};

	private final List<Language> languages;

	private final LayoutInflater inflater;

	public LanguageSpinnerAdapter(@NonNull final Context context,
								  @NonNull final List<Language> languages) {

		this.languages = new ArrayList<>(languages);

		this.languages.sort(mostUsedcomparator);

		inflater = LayoutInflater.from(context);
	}

	/**
	 * Закрепляет элемент с индексом {@code position} в верхнюю часть списка.
	 * Если элементов больше, чем {@link LanguageSpinnerAdapter#MOST_USED_ITEM_COUNT}, то
	 * самый старый закрепленный элемент открепляется.
	 *
	 * @param position индекс закрепляемого элемента
	 */
	public void pinItemToTop(final int position) {

		//Лист отсортирован по приоритету - проверяя первое значение
		//узнаем количество прикрепленных объектов
		final int currentPinnedItemsCount = languages.get(0).getMostUsedPriority();

		//Перемещаем выбранный элемент в топ
		final Language tempLanguage = languages.remove(position);
		languages.add(0, tempLanguage);

		//Определяем текущий максимальный приоритет
		int priority = currentPinnedItemsCount < MOST_USED_ITEM_COUNT
			? currentPinnedItemsCount + 1 : MOST_USED_ITEM_COUNT;

		//Проставляем приоритет всем языкам
		for (int i = 0; i < languages.size(); i++) {
			final Language language = languages.get(i);
			language.setMostUsedPriority(priority);
			priority = priority > 0 ? priority - 1 : 0;
		}

		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return languages.size();
	}

	@Override
	public String getItem(final int position) {
		return languages.get(position).getCode();
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {

		ViewHolder viewHolder;

		View view = convertView;
		if (view != null) {
			viewHolder = (ViewHolder) view.getTag();
		} else {
			view = inflater.inflate(R.layout.spinner_lang_item, parent, false);
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		}

		final Language language = languages.get(position);

		viewHolder.setText(language.getReadableLangWord());
		viewHolder.setDividerVisibility(language.getMostUsedPriority() == 1);

		return view;
	}

	static class ViewHolder {

		@BindView(R.id.textView) TextView language;
		@BindView(R.id.divider) View divider;

		private ViewHolder(final View view) {
			ButterKnife.bind(this, view);
		}

		private void setText(final String text) {
			language.setText(text);
		}

		private void setDividerVisibility(final boolean visibility) {
			if (visibility) {
				divider.setVisibility(View.VISIBLE);
			} else {
				divider.setVisibility(View.GONE);
			}
		}
	}
}