package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;
import java.util.List;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 20:23
 */

public interface TranslateView extends MvpView {

	void setTranslatedText(String text);

	void setTranslatedText(List<String> translatedText);

	void setDictText(String text);

}
