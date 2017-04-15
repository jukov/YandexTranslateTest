package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import java.util.List;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 20:23
 */

public interface HistoryView extends MvpView {

	/**
	 * Отображает историю переводов из базы данных.
	 */
	void onHistoryFromDatabase(List<Translation> translationList);

	/**
	 * Отображает новый перевод.
	 */
	void onNewTranslation(Translation translation);

	/**
	 * Удаляет историю.
	 */
	void onHistoryDeleted();

	/**
	 * Снимает отметки с избранных переводов.
	 */
	void onFavoritesDeleted();

	/**
	 * Удаляет перевод из списка.
	 */
	void onDeleteTranslation(Translation translation);
}
