package info.jukov.yandextranslatetest.ui.dialog;

/**
 * User: jukov
 * Date: 08.04.2017
 * Time: 12:06
 *
 * Супер-костыль для закрывания диалогов. Так как при восстановлении состояния Moxy
 * вызывает только те команды, которые были применены к View из Presenter,
 * команда на закрытие диалога тоже должна прийти из Presenter.
 *
 * Этот интерфейс должен реализовываться всеми Presenter,
 * которые показывают диалоги и передаваться в Listeners диалога.
 *
 */
public interface DialogCloser {

	/**
	 * Закрывает диалог.
	 * */
	void closeDialog();

}
