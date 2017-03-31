package info.jukov.yandextranslatetest.model.storage.dao;

import android.support.annotation.NonNull;
import info.jukov.yandextranslatetest.model.storage.dao.TranslationDao.Properties;
import info.jukov.yandextranslatetest.util.Log;
import java.util.ArrayList;
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

	private final List<OnTranslateProcessedListener> listenerList;

	public DatabaseManager(final TranslationDao translationDao) {
		this.translationDao = translationDao;

		translationList = new ArrayList<>(translationDao.loadAll());

		listenerList = new ArrayList<>();
	}


	/**
	 * Возвращает все имеющиеся переводы.
	 * */
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
	 * О добавлении поступлении нового объекта оповещаются все {@link OnTranslateProcessedListener}.
	 */
	public void processTranslate(@NonNull final Translation translation) {

		final int itemIndex = translationList.indexOf(translation);

		if (itemIndex != -1) {
			translationList.remove(translation);
			translationList.add(itemIndex, translation);

			translationDao.update(translation);
		} else {
			translationList.add(translation);

			translationDao.insert(translation);
		}

		notifyListeners(translation);
	}

	public void addOnTranslateAddedListener(@NonNull final OnTranslateProcessedListener listener) {
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

	private void notifyListeners(@NonNull final Translation translation) {
		for (final OnTranslateProcessedListener listener : listenerList) {
			if (listener != null) {
				listener.onTranslateProcessed(translation);
			}
		}
	}

	public interface OnTranslateProcessedListener {

		void onTranslateProcessed(final Translation translation);
	}
}
