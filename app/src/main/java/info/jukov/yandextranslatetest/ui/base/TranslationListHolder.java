package info.jukov.yandextranslatetest.ui.base;

import info.jukov.yandextranslatetest.model.storage.dao.Translation;

/**
 * User: jukov
 * Date: 01.04.2017
 * Time: 22:12
 *
 * Интерфейс для индикации объектов, которые отображают список переводов.
 */
public interface TranslationListHolder {

	/**
	 * Инициирует отображение выбранного пользователем перевода целиком.
	 * */
	void viewFullTranslation(Translation translation);

}
