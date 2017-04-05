package info.jukov.yandextranslatetest.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import info.jukov.yandextranslatetest.R;
import info.jukov.yandextranslatetest.model.network.ErrorCodes;
import info.jukov.yandextranslatetest.ui.screen.activity.SettingsActivity;
import info.jukov.yandextranslatetest.ui.screen.activity.SplashActivity;

/**
 * User: jukov
 * Date: 19.03.2017
 * Time: 14:33
 *
 * Класс с информацией о диалогах для того или иного события.
 */
public enum ErrorDialog {

	WRONG_API_KEY(
		ErrorCodes.WRONG_API_KEY,
		R.string.alertDialogInvalidKeys_wrongKeys_title,
		R.string.alertDialogInvalidKeys_wrongKeys_text,
		ListenerType.GO_TO_SETTINGS,
		ListenerType.EXIT,
		false
	),

	BANNED_API_KEY(
		ErrorCodes.BANNED_API_KEY,
		R.string.alertDialogInvalidKeys_bannedKeys_title,
		R.string.alertDialogInvalidKeys_bannedKeys_text,
		ListenerType.GO_TO_SETTINGS,
		ListenerType.EXIT,
		false
	),

	TEXT_TOO_LONG(
		ErrorCodes.TEXT_TOO_LONG,
		R.string.alertDialogInvalidKeys_textTooLong_title,
		R.string.alertDialogInvalidKeys_textTooLong_text,
		ListenerType.OK,
		ListenerType.NOTHING,
		true
	),

	TEXT_DAILY_LIMIT_EXCEEDED(
		ErrorCodes.TEXT_DAILY_LIMIT_EXCEEDED,
		R.string.alertDialogInvalidKeys_exceededDailyLimit_title,
		R.string.alertDialogInvalidKeys_exceededDailyLimit_text,
		ListenerType.GO_TO_SETTINGS,
		ListenerType.EXIT,
		true
	),

	QUERY_DAILY_LIMIT_EXCEEDED(
		ErrorCodes.QUERY_DAILY_LIMIT_EXCEEDED,
		R.string.alertDialogInvalidKeys_exceededDailyLimit_title,
		R.string.alertDialogInvalidKeys_exceededDailyLimit_text,
		ListenerType.GO_TO_SETTINGS,
		ListenerType.EXIT,
		true
	),

	LANG_NOT_SUPPORTED(
		ErrorCodes.LANG_NOT_SUPPORTED,
		R.string.alertDialogInvalidKeys_langNotSupported_title,
		R.string.alertDialogInvalidKeys_langNotSupported_text,
		ListenerType.OK,
		ListenerType.NOTHING,
		true
	),

	KEYS_NOT_SET_CUSTOM(
		ErrorCodes.KEYS_NOT_SET_CUSTOM,
		R.string.alertDialogInvalidKeys_keysNotSet_title,
		R.string.alertDialogInvalidKeys_keysNotSet_text,
		ListenerType.GO_TO_SETTINGS,
		ListenerType.EXIT,
		false
	),

	NETWORK_ERROR_CUSTOM(
		ErrorCodes.NETWORK_ERROR_CUSTOM,
		R.string.alertDialogInvalidKeys_networkError_title,
		R.string.alertDialogInvalidKeys_networkError_text,
		ListenerType.TRY_AGAIN,
		ListenerType.EXIT,
		false
	),

	UNEXPECTED_ERROR(
		-1,
		R.string.alertDialogInvalidKeys_unexpectedError_title,
		R.string.alertDialogInvalidKeys_unexpectedError_text,
		ListenerType.TRY_AGAIN,
		ListenerType.EXIT,
		false
	);

	public enum ListenerType {
		TRY_AGAIN(R.string.alertDialogInvalidKeys_tryAgain_button),
		GO_TO_SETTINGS(R.string.alertDialogInvalidKeys_goToSettings_button),
		EXIT(R.string.alertDialogInvalidKeys_exit_button),
		OK(android.R.string.ok),
		NOTHING(0);

		private final int buttonTextRes;

		ListenerType(final int buttonTextRes) {
			this.buttonTextRes = buttonTextRes;
		}

		public int getButtonTextRes() {
			return buttonTextRes;
		}
	}

	private final int errorCode;
	private final int titleRes;
	private final int messageRes;
	private final ListenerType positiveButton;
	private final ListenerType negativeButton;
	private final boolean isCancelable;

	public static ErrorDialog getByErrorCode(final int errorCode) {
		for (final ErrorDialog errorDialog : ErrorDialog.values()) {
			if (errorDialog.getErrorCode() == errorCode) {
				return errorDialog;
			}
		}

		return UNEXPECTED_ERROR;
	}

	public static Dialog BuildDialog(@NonNull final Activity activity, final int errorCode) {
		final ErrorDialog errorDialog = ErrorDialog.getByErrorCode(errorCode);

		final AlertDialog.Builder builder = new Builder(activity)
			.setTitle(errorDialog.getTitleRes())
			.setMessage(errorDialog.getMessageRes());

		final OnClickListener openSettingsListener = new OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				SettingsActivity.startForKeyChange(activity);
			}
		};

		final OnClickListener exitListener = new OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				activity.finish();
			}
		};

		final OnClickListener restartListener = new OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				SplashActivity.restartApp(activity);
			}
		};

		switch (errorDialog.getPositiveButton()) {
			case TRY_AGAIN:
				builder.setPositiveButton(errorDialog.getPositiveButton().getButtonTextRes(), restartListener);
				break;
			case GO_TO_SETTINGS:
				builder.setPositiveButton(errorDialog.getPositiveButton().getButtonTextRes(), openSettingsListener);
				break;
			case EXIT:
				builder.setPositiveButton(errorDialog.getPositiveButton().getButtonTextRes(), exitListener);
				break;
			case OK:
				builder.setPositiveButton(errorDialog.getPositiveButton().getButtonTextRes(), null);
				break;
			case NOTHING:
				//no listener
				break;
		}

		switch (errorDialog.getNegativeButton()) {
			case TRY_AGAIN:
				builder.setNegativeButton(errorDialog.getNegativeButton().getButtonTextRes(), restartListener);
				break;
			case GO_TO_SETTINGS:
				builder.setNegativeButton(errorDialog.getNegativeButton().getButtonTextRes(), openSettingsListener);
				break;
			case EXIT:
				builder.setNegativeButton(errorDialog.getNegativeButton().getButtonTextRes(), exitListener);
				break;
			case OK:
				builder.setNegativeButton(errorDialog.getNegativeButton().getButtonTextRes(), null);
				break;
			case NOTHING:
				//no listener
				break;
		}

		final Dialog dialog = builder.create();

		dialog.setCanceledOnTouchOutside(errorDialog.isCancelable());

		return dialog;
	}

	ErrorDialog(final int errorCode, final int titleRes, final int messageRes, final ListenerType positiveButton,
		final ListenerType negativeButton, final boolean isCancelable) {
		this.errorCode = errorCode;
		this.titleRes = titleRes;
		this.messageRes = messageRes;
		this.positiveButton = positiveButton;
		this.negativeButton = negativeButton;
		this.isCancelable = isCancelable;
	}

	@StringRes public int getErrorCode() {
		return errorCode;
	}

	@StringRes public int getTitleRes() {
		return titleRes;
	}

	@StringRes public int getMessageRes() {
		return messageRes;
	}

	public ListenerType getPositiveButton() {
		return positiveButton;
	}

	public ListenerType getNegativeButton() {
		return negativeButton;
	}

	public boolean isCancelable() {
		return isCancelable;
	}
}
