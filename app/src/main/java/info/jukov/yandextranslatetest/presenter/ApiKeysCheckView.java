package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 21:03
 */

public interface ApiKeysCheckView extends MvpView {

	void onKeysEntered();

	void onKeysNotEntered();
}
