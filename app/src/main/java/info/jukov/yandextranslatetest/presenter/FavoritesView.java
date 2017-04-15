package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import java.util.List;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 20:23
 */

public interface FavoritesView extends MvpView {

	/**
	 * Отображает избраенные переводы из базы данных.
	 */
	void onFavoritesFromDatabase(List<Translation> translationList);

	/**
	 * Отображает новый избранный перевод.
	 */
	void onNewFavorite(Translation translation);

	/**
	 * Удаляет избранный перевод.
	 */
	void onDeleteFavorite(Translation translation);

	/**
	 * Удаляет все избранные переводы.
	 */
	void deleteFavorites();
}
