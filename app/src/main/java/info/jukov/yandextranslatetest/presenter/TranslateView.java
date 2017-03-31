package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponse;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 20:23
 */

public interface TranslateView extends MvpView {

	void onTranslation(String translatedText);

	void onDictDefinition(LookupResponse response);

	void onEmptyInput();

	void onNothingToAddToFavorite();

	void onFavoritesAction(final boolean added);
}
