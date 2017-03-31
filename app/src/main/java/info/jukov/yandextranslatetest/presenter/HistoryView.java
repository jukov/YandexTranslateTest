package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import java.util.List;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 20:23
 */

public interface HistoryView extends MvpView {

	void onTranslationsFromDatabase(List<Translation> translationList);

	void onNewTranslation(Translation translation);

}
