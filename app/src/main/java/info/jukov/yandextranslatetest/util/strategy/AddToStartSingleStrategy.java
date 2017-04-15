package info.jukov.yandextranslatetest.util.strategy;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;
import java.util.Iterator;
import java.util.List;

/**
 * User: jukov
 * Date: 15.04.2017
 * Time: 12:39
 */

public final class AddToStartSingleStrategy implements StateStrategy {
	@Override
	public <View extends MvpView> void beforeApply(final List<ViewCommand<View>> currentState, final ViewCommand<View> incomingCommand) {
		Iterator<ViewCommand<View>> iterator = currentState.iterator();

		while (iterator.hasNext()) {
			ViewCommand<View> entry = iterator.next();

			if (entry.getClass() == incomingCommand.getClass()) {
				iterator.remove();
				break;
			}
		}

		currentState.add(0, incomingCommand);
	}

	@Override
	public <View extends MvpView> void afterApply(final List<ViewCommand<View>> currentState, final ViewCommand<View> incomingCommand) {
		// pass
	}
}
