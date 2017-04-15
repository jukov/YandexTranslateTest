package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;

/**
 * User: jukov
 * Date: 12.04.2017
 * Time: 20:05
 */

public interface TranslationListView extends MvpView {

	/**
	 * Показывает диалог с действиями над переводом.
	 * */
	@StateStrategyType(AddToEndSingleStrategy.class)
	void showActionDialog(Translation translation);

	/**
	 * Закрывает диалог.
	 */
	@StateStrategyType(AddToEndSingleStrategy.class)
	void closeDialog();

}
