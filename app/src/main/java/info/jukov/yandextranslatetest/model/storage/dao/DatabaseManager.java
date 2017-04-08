package info.jukov.yandextranslatetest.model.storage.dao;

import android.support.annotation.NonNull;
import info.jukov.yandextranslatetest.model.storage.dao.TranslationDao.Properties;
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

	private static final Log LOG = new Log(DatabaseManager.class);

	private final TranslationDao translationDao;

	private final List<Translation> translationList;

	private final List<DatabaseListener> listenerList;

	public DatabaseManager(final TranslationDao translationDao) {
		this.translationDao = translationDao;

		translationList = new ArrayList<>(translationDao.loadAll());

		//Сортировка по возрастанию id (больше id - младше перевод)
		Collections.sort(translationList, new Comparator<Translation>() {
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
		});

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

		final int itemIndex = translationList.indexOf(translation);

		if (itemIndex != -1) {
			translationList.remove(translation);
			translationList.add(itemIndex, translation);

			translationDao.update(translation);
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
		translationDao.queryBuilder()
			.where(TranslationDao.Properties.IsFavorite.eq(Boolean.FALSE))
			.buildDelete().executeDeleteWithoutDetachingEntities();

		notifyHistoryDeleted();
	}

	public void deleteFavorites() {
		translationDao.queryBuilder()
			.where(TranslationDao.Properties.IsFavorite.eq(Boolean.TRUE))
			.buildDelete().executeDeleteWithoutDetachingEntities();

		notifyFavoritesDeleted();
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

		void onHistoryDeleted();

		void onFavoritesDeleted();
	}
}
