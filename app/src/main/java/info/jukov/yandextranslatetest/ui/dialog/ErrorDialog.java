package info.jukov.yandextranslatetest.ui.dialog;

import android.support.annotation.StringRes;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.network.ErrorCodes;

/**
 * User: jukov
 * Date: 19.03.2017
 * Time: 14:33
 *
 * Объекты с информацией для построения диалога на ту или иную ошибку.
 */
public enum ErrorDialog {

	WRONG_API_KEY(
		ErrorCodes.WRONG_API_KEY,
		R.string.alertDialogInvalidKeys_wrongKeys_title,
		R.string.alertDialogInvalidKeys_wrongKeys_text,
		ButtonType.GO_TO_SETTINGS,
		ButtonType.EXIT,
		false
	),

	BANNED_API_KEY(
		ErrorCodes.BANNED_API_KEY,
		R.string.alertDialogInvalidKeys_bannedKeys_title,
		R.string.alertDialogInvalidKeys_bannedKeys_text,
		ButtonType.GO_TO_SETTINGS,
		ButtonType.EXIT,
		false
	),

	TEXT_TOO_LONG(
		ErrorCodes.TEXT_TOO_LONG,
		R.string.alertDialogInvalidKeys_textTooLong_title,
		R.string.alertDialogInvalidKeys_textTooLong_text,
		ButtonType.OK,
		ButtonType.NOTHING,
		true
	),

	TEXT_DAILY_LIMIT_EXCEEDED(
		ErrorCodes.TEXT_DAILY_LIMIT_EXCEEDED,
		R.string.alertDialogInvalidKeys_exceededDailyLimit_title,
		R.string.alertDialogInvalidKeys_exceededDailyLimit_text,
		ButtonType.GO_TO_SETTINGS,
		ButtonType.EXIT,
		true
	),

	QUERY_DAILY_LIMIT_EXCEEDED(
		ErrorCodes.QUERY_DAILY_LIMIT_EXCEEDED,
		R.string.alertDialogInvalidKeys_exceededDailyLimit_title,
		R.string.alertDialogInvalidKeys_exceededDailyLimit_text,
		ButtonType.GO_TO_SETTINGS,
		ButtonType.EXIT,
		true
	),

	LANG_NOT_SUPPORTED(
		ErrorCodes.LANG_NOT_SUPPORTED,
		R.string.alertDialogInvalidKeys_langNotSupported_title,
		R.string.alertDialogInvalidKeys_langNotSupported_text,
		ButtonType.OK,
		ButtonType.NOTHING,
		true
	),

	KEYS_NOT_SET_CUSTOM(
		ErrorCodes.KEYS_NOT_SET_CUSTOM,
		R.string.alertDialogInvalidKeys_keysNotSet_title,
		R.string.alertDialogInvalidKeys_keysNotSet_text,
		ButtonType.GO_TO_SETTINGS,
		ButtonType.EXIT,
		false
	),

	NETWORK_ERROR_CUSTOM(
		ErrorCodes.NETWORK_ERROR_CUSTOM,
		R.string.alertDialogInvalidKeys_networkError_title,
		R.string.alertDialogInvalidKeys_networkError_text,
		ButtonType.TRY_AGAIN,
		ButtonType.EXIT,
		false
	),

	UNEXPECTED_ERROR(
		-1,
		R.string.alertDialogInvalidKeys_unexpectedError_title,
		R.string.alertDialogInvalidKeys_unexpectedError_text,
		ButtonType.TRY_AGAIN,
		ButtonType.EXIT,
		false
	);

	/**
	 * Объекты с информацией о типе действия при нажатии на кнопку на диалоге.
	 * */
	public enum ButtonType {
		TRY_AGAIN(R.string.alertDialogInvalidKeys_tryAgain_button),
		GO_TO_SETTINGS(R.string.alertDialogInvalidKeys_goToSettings_button),
		EXIT(R.string.alertDialogInvalidKeys_exit_button),
		OK(android.R.string.ok),
		NOTHING(0);

		private final int buttonTextRes;

		ButtonType(final int buttonTextRes) {
			this.buttonTextRes = buttonTextRes;
		}

		public int getButtonTextRes() {
			return buttonTextRes;
		}
	}

	private final int errorCode;
	private final int titleRes;
	private final int messageRes;
	private final ButtonType positiveButton;
	private final ButtonType negativeButton;
	private final boolean isCancelable;

	public static ErrorDialog getByErrorCode(final int errorCode) {
		for (final ErrorDialog errorDialog : ErrorDialog.values()) {
			if (errorDialog.getErrorCode() == errorCode) {
				return errorDialog;
			}
		}

		return UNEXPECTED_ERROR;
	}

	ErrorDialog(final int errorCode, final int titleRes, final int messageRes, final ButtonType positiveButton,
		final ButtonType negativeButton, final boolean isCancelable) {
		this.errorCode = errorCode;
		this.titleRes = titleRes;
		this.messageRes = messageRes;
		this.positiveButton = positiveButton;
		this.negativeButton = negativeButton;
		this.isCancelable = isCancelable;
	}

	@StringRes
	public int getErrorCode() {
		return errorCode;
	}

	@StringRes
	public int getTitleRes() {
		return titleRes;
	}

	@StringRes
	public int getMessageRes() {
		return messageRes;
	}

	public ButtonType getPositiveButton() {
		return positiveButton;
	}

	public ButtonType getNegativeButton() {
		return negativeButton;
	}

	public boolean isCancelable() {
		return isCancelable;
	}
}
