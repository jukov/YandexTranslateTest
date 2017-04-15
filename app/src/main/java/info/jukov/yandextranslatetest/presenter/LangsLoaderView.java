package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 11:47
 */

public interface LangsLoaderView extends MvpView {

	/**
	 * Сообщает view слою о успещной загрузке данных (на данный момент - о загрузке переводов).
	 */
	@StateStrategyType(OneExecutionStateStrategy.class)
	void onLoadFinished();

	/**
	 * Сообщает view слою о неудачной загрузке данных.
	 */
	@StateStrategyType(OneExecutionStateStrategy.class)
	void onLoadFailed(int errorCode);
}
