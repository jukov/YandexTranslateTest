package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import info.jukov.yandextranslatetest.model.storage.Language;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.util.strategy.AddToStartSingleStrategy;
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
	@StateStrategyType(AddToEndSingleStrategy.class)
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
	@StateStrategyType(AddToEndSingleStrategy.class)
	void onFavoritesAction(boolean added);

	/**
	 * Отображает ошибку при загрузке.
	 *
	 * @param errorCode тип ошибки.
	 */
	@StateStrategyType(AddToEndSingleStrategy.class)
	void onLoadFailed(int errorCode);

	/**
	 * Меняет местами языки ввода и вывода.
	 */
	@StateStrategyType(AddToEndSingleStrategy.class)
	void swapLang();

	/**
	 * Выбирает язык ввода.
	 *
	 * @param position позиция языка в спиннере
	 */
	void selectInputLang(int position);

	/**
	 * Выбирает язык вывода.
	 *
	 * @param position позиция языка в спиннере
	 */
	void selectOutputLang(int position);

	/**
	 * Задает список языков ввода.
	 */
	@StateStrategyType(AddToStartSingleStrategy.class)
	void setInputLangs(Set<Language> inputLangs);

	/**
	 * Задает список языков вывода.
	 */
	@StateStrategyType(AddToStartSingleStrategy.class)
	void setOutputLangs(Set<Language> outputLangs);

	/**
	 * Закрывает диалог.
	 */
	@StateStrategyType(AddToEndSingleStrategy.class)
	void closeDialog();

	/**
	 * Сообщает о том, что переводимый текст слишком длинный.
	 */
	@StateStrategyType(OneExecutionStateStrategy.class)
	void onTextTooLong();
}
