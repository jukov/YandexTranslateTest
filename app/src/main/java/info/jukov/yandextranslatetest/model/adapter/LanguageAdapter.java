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
import info.jukov.yandextranslatetest.util.Guard;
import info.jukov.yandextranslatetest.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: jukov
 * Date: 19.03.2017
 * Time: 16:33
 *
 * Адаптер для вывода списка языков в спиннер.
 */

public final class LanguageAdapter extends BaseAdapter {

	private static final Log LOG = new Log(LanguageAdapter.class);

	private static final int MOST_USED_ITEM_COUNT = 3;

	/**
	 * Сортирует объекты типа {@link Language}.
	 * В начало списка попадают объекты с {@link Language#getMostUsedPriority()} больше нуля,
	 * затем все остальные объекты по {@link Language#getReadableLangWord()} в алфивитном порядке.
	 */
	private static final Comparator<Language> MOST_USED_COMPARATOR = new Comparator<Language>() {
		//В данной ситуации переполнение int невозможно на практике
		@SuppressWarnings("SubtractionInCompareTo")
		@Override
		public int compare(final Language o1, final Language o2) {
			//Если у одного из элементов указана позиция в топе, то сравниваем по позиции
			if (o1.getMostUsedPriority() != 0 || o2.getMostUsedPriority() != 0) {
				return o2.getMostUsedPriority() - o1.getMostUsedPriority();

				//Если позиция не указана, то сортируем по алфавиту
			} else {
				return String.CASE_INSENSITIVE_ORDER
					.compare(o1.getReadableLangWord(), o2.getReadableLangWord());
			}
		}
	};

	private final List<Language> languages;

	private final LayoutInflater inflater;

	public LanguageAdapter(@NonNull final Context context, @NonNull final Collection<Language> languages) {
		Guard.checkNotNull(context, "null == context");
		Guard.checkNotNull(languages, "null == languages");

		this.languages = new ArrayList<>(languages);

		Collections.sort(this.languages, MOST_USED_COMPARATOR);

		inflater = LayoutInflater.from(context);
	}

	/**
	 * Закрепляет элемент с кодом языка {@code code} в верхнюю часть списка.
	 *
	 * @param code код языка закрепляемого элемента.
	 * @return true - если элемент был найден и прикреплен. false - если элемент найден не был.
	 */
	public boolean pinItemToTop(@NonNull final String code) {
		Guard.checkNotNull(code, "null == code");
		Guard.checkPreCondition(code.length() >= 2 && code.length() <= 3, "Code must contain only two or three letters");

		for (int i = 0; i < languages.size(); i++) {
			if (languages.get(i).getCode().equals(code)) {
				pinItemToTop(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * Закрепляет элемент с индексом {@code position} в верхнюю часть списка.
	 * Если элементов больше, чем {@link LanguageAdapter#MOST_USED_ITEM_COUNT}, то
	 * самый старый закрепленный элемент открепляется.
	 *
	 * @param position индекс закрепляемого элемента.
	 */
	public void pinItemToTop(final int position) {
		Guard.checkPreCondition(position < languages.size(), "Position too big");
		Guard.checkPreCondition(position >= 0, "Position too small");

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

	/**
	 * @return список самых используемых языков.
	 */
	public List<Language> getMostUsedLanguages() {

		final List<Language> mostUsedLanguages = new ArrayList<>(MOST_USED_ITEM_COUNT);

		for (int i = 0; i < (languages.size() > MOST_USED_ITEM_COUNT ? MOST_USED_ITEM_COUNT : languages.size()); i++) {

			final Language language = languages.get(i);

			if (language.getMostUsedPriority() == 0) {
				break;
			}

			mostUsedLanguages.add(language);
		}

		return mostUsedLanguages;
	}

	/**
	 * @return индекс языка по его коду. Если язык не найден, возвращает -1.
	 */
	public int getPosition(@NonNull final String langCode) {
		Guard.checkNotNull(langCode, "null == langCode");

		for (int i = 0; i < languages.size(); i++) {
			final Language language = languages.get(i);
			if (langCode.equals(language.getCode())) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public int getCount() {
		return languages.size();
	}

	@Override
	public Language getItem(final int position) {
		return languages.get(position);
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {

		final ViewHolder viewHolder;

		View view = convertView;
		if (view != null) {
			viewHolder = (ViewHolder) view.getTag();
		} else {
			view = inflater.inflate(R.layout.component_lang_item_text, parent, false);
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
