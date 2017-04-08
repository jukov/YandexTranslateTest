package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;

/**
 * User: jukov
 * Date: 02.04.2017
 * Time: 20:41
 */

public interface MainView extends MvpView {

	void moveToTranslateTab();

	void closeDialog();
}
