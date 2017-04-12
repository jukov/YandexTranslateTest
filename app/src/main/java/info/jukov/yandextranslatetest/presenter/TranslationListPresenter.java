package info.jukov.yandextranslatetest.presenter;

import android.support.annotation.NonNull;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.ui.dialog.DialogCloser;

/**
 * User: jukov
 * Date: 12.04.2017
 * Time: 20:05
 */
@InjectViewState
public class TranslationListPresenter extends MvpPresenter<TranslationListView> implements DialogCloser {

	public void showActionDialog(@NonNull final Translation translation) {
		getViewState().showActionDialog(translation);
	}

	@Override
	public void closeDialog() {
		getViewState().closeDialog();
	}
}
