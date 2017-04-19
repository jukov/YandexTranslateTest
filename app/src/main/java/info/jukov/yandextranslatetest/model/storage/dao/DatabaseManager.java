package info.jukov.yandextranslatetest.model.storage.dao;

import android.support.annotation.NonNull;
import info.jukov.yandextranslatetest.model.storage.dao.TranslationDao.Properties;
import info.jukov.yandextranslatetest.util.Guard;
import info.jukov.yandextranslatetest.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * User: jukov
 * Date: 30.03.2017
 * Time: 20:31
 *
 * Класс для работы с данными о переводах.
 * Складывает новые переводы в базу данных, оповещает подписанные объекты о прибытии переводов.
 * Предоставляет ранее сохраненные переводы.
 */
public class DatabaseManager {

	public static final Comparator<Translation> TRANSLATION_COMPARATOR = new Comparator<Translation>() {
		@Override
		public int compare(final Translation o1, final Translation o2) {
			if (o1.get_id() == null) {
				return 1;
			}

			if (o2.get_id() == null) {
				return -1;
			}

			if (o1.get_id() > o2.get_id()) {
				return -1;
			} else if (o1.get_id() < o2.get_id()) {
				return 1;
			} else {
				return 0;
			}
		}
	};

	private static final Log LOG = new Log(DatabaseManager.class);

	private final TranslationDao translationDao;

	private final List<Translation> translationList;

	private final List<DatabaseListener> listenerList;

	public DatabaseManager(@NonNull final TranslationDao translationDao) {
		Guard.checkNotNull(translationDao, "null == translationDao");

		this.translationDao = translationDao;

		translationList = new ArrayList<>(translationDao.loadAll());

		//Сортировка по возрастанию id (больше id - младше перевод)
		Collections.sort(translationList, TRANSLATION_COMPARATOR);

		listenerList = new ArrayList<>();
	}


	/**
	 * Возвращает все имеющиеся переводы.
	 */
	public List<Translation> getTranslationList() {
		return translationList;
	}

	/**
	 * Возвращает переводы, помеченные как избранные.
	 */
	public List<Translation> getFavoritesList() {
		final List<Translation> favoriteTranslationList = new ArrayList<>(translationList);

		for (final Iterator<Translation> iterator = favoriteTranslationList.iterator(); iterator.hasNext(); ) {
			if (iterator.next().getIsFavorite() == false) {
				iterator.remove();
			}
		}

		return favoriteTranslationList;
	}

	/**
	 * Обрабатывает поступивший объект {@link Translation}.
	 *
	 * В общем случае, просто записывает его в список и вставляет в базу данных.
	 *
	 * Если объект уже был в списке, подразумевается что изменился его {@link Translation#getIsFavorite()}.
	 * В этом случае объект перезаписывается в список и обновляется в базе данных.
	 *
	 * О добавлении поступлении нового объекта оповещаются все {@link DatabaseListener}.
	 */
	public void processTranslate(@NonNull final Translation translation) {
		Guard.checkNotNull(translation, "null == translation");

		final int itemIndex = translationList.indexOf(translation);

		if (itemIndex != -1) {
			translationList.remove(translation);
			translationList.add(itemIndex, translation);

//			Нельзя сделать просто update(), так как из-за определения языка может прийти перевод, который уже есть в базе, но не имеет id.
//			Поэтому, сначала проверяем наличие перевода.
			final Translation oldTranslation = getTranslateFromDatabase(translation.getLang(), translation.getText());

			if (oldTranslation != null) {
				oldTranslation.setIsFavorite(translation.getIsFavorite());
				translationDao.update(oldTranslation);
			}
		} else {
			translationList.add(0, translation);

			translationDao.insert(translation);
		}

		notifyTranslateProcessed(translation);
	}

	public void addOnTranslateAddedListener(@NonNull final DatabaseListener listener) {
		listenerList.add(listener);
	}

	public Translation getTranslateFromDatabase(@NonNull final String lang, @NonNull final String text) {
		Guard.checkNotNull(lang, "null == lang");
		Guard.checkNotNull(text, "null == text");

		final List<Translation> translationList = translationDao.queryBuilder()
			.where(Properties.Lang.eq(lang), Properties.Text.eq(text))
			.build()
			.list();

		if (translationList.size() == 1) {
			return translationList.get(0);
		}

		if (translationList.size() > 1) {
			LOG.warning("Unexpected count of translations");
			return translationList.get(0);
		}

		return null;
	}

	public void deleteHistory() {
		translationDao.deleteAll();

		translationList.clear();

		notifyHistoryDeleted();
	}

	public void deleteFavorites() {

		for (final Translation translation : translationList) {
			translation.setIsFavorite(false);
		}

		notifyFavoritesDeleted();
	}

	public void deleteTranslation(@NonNull final Translation translation) {
		Guard.checkNotNull(translation, "null == translation");
		translationDao.delete(translation);

		translationList.remove(translation);

		notifyTranslateDeleted(translation);
	}

	private void notifyTranslateDeleted(@NonNull final Translation translation) {
		for (final DatabaseListener listener : listenerList) {
			if (listener != null) {
				listener.onTranslateDeleted(translation);
			}
		}
	}

	private void notifyTranslateProcessed(@NonNull final Translation translation) {
		for (final DatabaseListener listener : listenerList) {
			if (listener != null) {
				listener.onTranslateProcessed(translation);
			}
		}
	}

	private void notifyHistoryDeleted() {
		for (final DatabaseListener listener : listenerList) {
			if (listener != null) {
				listener.onHistoryDeleted();
			}
		}
	}

	private void notifyFavoritesDeleted() {
		for (final DatabaseListener listener : listenerList) {
			if (listener != null) {
				listener.onFavoritesDeleted();
			}
		}
	}

	public interface DatabaseListener {

		void onTranslateProcessed(final Translation translation);

		void onTranslateDeleted(final Translation translation);

		void onHistoryDeleted();

		void onFavoritesDeleted();
	}
}
