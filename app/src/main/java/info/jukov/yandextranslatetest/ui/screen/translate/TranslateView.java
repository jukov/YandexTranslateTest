package info.jukov.yandextranslatetest.ui.screen.translate;

import com.arellomobile.mvp.MvpView;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 20:23
 */

public interface TranslateView extends MvpView {

	void setTranslatedText(String text);

	void setDictText(String text);

}
