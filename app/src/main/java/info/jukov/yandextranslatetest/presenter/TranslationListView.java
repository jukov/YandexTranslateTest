package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;

/**
 * User: jukov
 * Date: 12.04.2017
 * Time: 20:05
 */

public interface TranslationListView extends MvpView {

	/**
	 * Показывает диалог с действиями над переводом.
	 * */
	void showActionDialog(Translation translation);

	/**
	 * Закрывает диалог.
	 */
	void closeDialog();

}
