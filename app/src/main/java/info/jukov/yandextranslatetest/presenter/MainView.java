package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import info.jukov.yandextranslatetest.util.strategy.AddToStartSingleStrategy;

/**
 * User: jukov
 * Date: 02.04.2017
 * Time: 20:41
 */

public interface MainView extends MvpView {

	/**
	 * Перемещает пользователя на таб с переводами.
	 */
	@StateStrategyType(AddToStartSingleStrategy.class)
	void moveToTranslateTab();

	/**
	 * Закрывает диалог.
	 */
	@StateStrategyType(AddToEndSingleStrategy.class)
	void closeDialog();
}
