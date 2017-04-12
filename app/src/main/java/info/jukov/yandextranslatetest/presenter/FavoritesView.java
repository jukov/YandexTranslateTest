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

	void onFavoritesFromDatabase(List<Translation> translationList);

	void onNewFavorite(Translation translation);

	void onDeleteFavorite(Translation translation);

	void deleteFavorites();
}
