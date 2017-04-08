package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import info.jukov.yandextranslatetest.model.storage.Language;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import java.util.Set;

/**
 * User: jukov
 * Date: 15.03.2017
 * Time: 20:23
 */

public interface TranslateView extends MvpView {

	/**
	 * Отображает перевод.
	 */
	void onTranslation(Translation translation);

	/**
	 * Сообщает о том, что не введены входные данные.
	 */
	@StateStrategyType(OneExecutionStateStrategy.class)
	void onEmptyInput();

	/**
	 * Сообщает о том, что нет перевода для добавления в избранное.
	 */
	@StateStrategyType(OneExecutionStateStrategy.class)
	void onNothingToAddToFavorite();

	/**
	 * Отображает статус добавления текущего перевода в избранное
	 *
	 * @param added содержит информацию о статусе. Если true, перевод добавлен в избранное.
	 */
	void onFavoritesAction(boolean added);

	/**
	 * Отображает ошибку при загрузке.
	 *
	 * @param errorCode тип ошибки.
	 */
	void onLoadFailed(int errorCode);

	/**
	 * Скрывает клавиатуру.
	 */
	void hideKeyboard();

	/**
	 * Меняет местами языки ввода и вывода.
	 */
	void swapLang();

	/**
	 * Выбирввет язык ввода.
	 *
	 * @param position позиция языка
	 */
	void selectInputLang(int position);

	/**
	 * Выбирввет язык вывода.
	 *
	 * @param position позиция языка
	 */
	void selectOutputLang(int position);

	/**
	 * Задает список языков ввода.
	 */
	void setInputLangs(Set<Language> inputLangs);

	/**
	 * Задает список языков вывода.
	 */
	void setOutputLangs(Set<Language> outputLangs);

	/**
	 * Закрывает диалог.
	 */
	void closeDialog();

	/**
	 * Сообщает о том, что переводимый текст слишком длинный.
	 */
	@StateStrategyType(OneExecutionStateStrategy.class)
	void onTextTooLong();
}
