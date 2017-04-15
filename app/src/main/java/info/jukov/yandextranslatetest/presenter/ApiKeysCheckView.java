package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 21:03
 */
public interface ApiKeysCheckView extends MvpView {

	/**
	 * Оповещает view-слой о том, что api ключи введены пользователем.
	 * */
	@StateStrategyType(OneExecutionStateStrategy.class)
	void onKeysEntered();

	/**
	 * Оповещает view-слой о том, что api не были введены пользователем.
	 * */
	@StateStrategyType(OneExecutionStateStrategy.class)
	void onKeysNotEntered();
}
