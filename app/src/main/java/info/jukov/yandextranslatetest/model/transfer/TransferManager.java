package info.jukov.yandextranslatetest.model.transfer;

import android.support.annotation.NonNull;
import info.jukov.yandextranslatetest.model.storage.dao.Translation;
import info.jukov.yandextranslatetest.util.Guard;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jukov
 * Date: 02.04.2017
 * Time: 19:56
 */

public class TransferManager {

	private final List<OnFullTranslateListener> listenerList;

	public TransferManager() {
		listenerList = new ArrayList<>();
	}

	public void addListener(@NonNull final OnFullTranslateListener listener) {
		Guard.checkNotNull(listener, "null == listener");

		listenerList.add(listener);
	}

	public void onFullTranslate(@NonNull final Translation translation) {
		Guard.checkNotNull(translation, "null == translation");

		for (final OnFullTranslateListener listener : listenerList) {
			listener.onFullTranslateListener(translation);
		}
	}

	public interface OnFullTranslateListener {
		void onFullTranslateListener(Translation translation);
	}

}
