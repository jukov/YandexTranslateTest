package info.jukov.yandextranslatetest.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import info.jukov.yandextranslatetest.TranslateApp;
import info.jukov.yandextranslatetest.model.module.TransferModule;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.model.transfer.TransferManager.OnFullTranslateListener;
import info.jukov.yandextranslatetest.ui.dialog.DialogCloser;
import javax.inject.Inject;

/**
 * User: jukov
 * Date: 02.04.2017
 * Time: 20:42
 */
@InjectViewState
public final class MainPresenter extends MvpPresenter<MainView> implements OnFullTranslateListener, DialogCloser {

	@Inject TransferModule transferModule;

	@Override
	protected void onFirstViewAttach() {
		super.onFirstViewAttach();
		TranslateApp.getAppComponent().inject(this);

		transferModule.getTransferManager().addListener(this);
	}

	@Override
	public void onFullTranslateListener(final Translation translation) {
		getViewState().moveToTranslateTab();
	}

	@Override
	public void closeDialog() {
		getViewState().closeDialog();
	}
}
