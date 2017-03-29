package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponce;
import info.jukov.yandextranslatetest.model.storage.dao.History;
import java.util.List;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 20:23
 */

public interface TranslateView extends MvpView {

	void onTranslation(List<String> translatedText);

	void onDictDefinition(LookupResponce responce);

	void onEmptyInput();

	@StateStrategyType(OneExecutionStateStrategy.class)
	void onTextTranslated(History history);

	void onTranslateAddedToFavorites(History history);

	void onNothingToAddToFavorite();
}
