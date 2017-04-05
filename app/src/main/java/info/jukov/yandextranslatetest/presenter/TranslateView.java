package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 20:23
 */

public interface TranslateView extends MvpView {

	void onTranslation(Translation translation);

	void onViewFullTranslation(Translation translation);

	void onEmptyInput();

	void onNothingToAddToFavorite();

	void onFavoritesAction(boolean added);

	void onLoadFailed(int errorCode);
}
