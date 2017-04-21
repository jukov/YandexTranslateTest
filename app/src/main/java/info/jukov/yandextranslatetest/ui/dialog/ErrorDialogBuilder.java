package info.jukov.yandextranslatetest.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import info.jukov.yandextranslatetest.ui.dialog.ErrorDialog.ButtonType;
import info.jukov.yandextranslatetest.ui.screen.activity.SettingsActivity;
import info.jukov.yandextranslatetest.ui.screen.activity.SplashActivity;
import info.jukov.yandextranslatetest.util.Guard;

/**
 * User: jukov
 * Date: 08.04.2017
 * Time: 12:44
 *
 * Сборщик диалогов ошибок.
 */
public final class ErrorDialogBuilder {

	public static Dialog BuildDialog(@NonNull final Activity activity, final int errorCode, @NonNull final DialogCloser dialogCloser) {
		Guard.checkNotNull(activity, "null == activity");
		Guard.checkNotNull(dialogCloser, "null == dialogCloser");

		final ErrorDialog errorDialog = ErrorDialog.getByErrorCode(errorCode);

		final AlertDialog.Builder builder = new Builder(activity)
			.setTitle(errorDialog.getTitleRes())
			.setMessage(errorDialog.getMessageRes());

		switch (errorDialog.getPositiveButton()) {
			case TRY_AGAIN:
			case GO_TO_SETTINGS:
			case EXIT:
			case OK:
				builder.setPositiveButton(errorDialog.getPositiveButton().getButtonTextRes(), null);
				break;
			case NOTHING:
				//no listener
				break;
		}

		switch (errorDialog.getNegativeButton()) {
			case TRY_AGAIN:
			case GO_TO_SETTINGS:
			case EXIT:
			case OK:
				builder.setNegativeButton(errorDialog.getNegativeButton().getButtonTextRes(), null);
				break;
			case NOTHING:
				//no listener
				break;
		}

		final Dialog dialog = builder.create();

		dialog.setCanceledOnTouchOutside(errorDialog.isCancelable());
		dialog.setCancelable(errorDialog.isCancelable());

		dialog.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(final DialogInterface dialog) {
				final Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
				if (positiveButton != null) {
					setListener(activity, dialogCloser, positiveButton, errorDialog.getPositiveButton());
				}

				final Button negativeButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
				if (negativeButton != null) {
					setListener(activity, dialogCloser, negativeButton, errorDialog.getNegativeButton());
				}
			}
		});

		return dialog;
	}

	private static void setListener(final Activity activity, final DialogCloser dialogCloser,
		final Button button, final ButtonType buttonType) {

		switch (buttonType) {
			case TRY_AGAIN:
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View v) {
						SplashActivity.restartApp(activity);
						dialogCloser.closeDialog();
					}
				});
				break;
			case GO_TO_SETTINGS:
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View v) {
						SettingsActivity.startForKeyChange(activity);
						dialogCloser.closeDialog();
					}
				});
				break;
			case EXIT:
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View v) {
						activity.finish();
						dialogCloser.closeDialog();
					}
				});
				break;
			case OK:
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View v) {
						dialogCloser.closeDialog();
					}
				});
				break;
			case NOTHING:
		}
	}

	private ErrorDialogBuilder() {
	}
}
