package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponce;
import java.util.List;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 20:23
 */

public interface TranslateView extends MvpView {

	void setTranslation(List<String> translatedText);

	void setDictDefinition(LookupResponce responce);

}
