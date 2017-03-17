package info.jukov.yandextranslatetest.util;

import android.support.annotation.NonNull;
import java.io.Serializable;
import java.util.BitSet;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 6:15
 */
public final class MultiSetBoolean<E extends Enum> implements Serializable {

	private final int capacity;
	private final BitSet value;

	private transient OnValueTrueListener onValueTrueListener;

	public MultiSetBoolean(final int capacity, @NonNull final OnValueTrueListener onValueTrueListener) {
		Guard.checkNotNull(onValueTrueListener, "null == onValueTrueListener");
		Guard.checkInvariant(capacity > 0, "capacity must be > 0");

		this.capacity = capacity;
		this.value = new BitSet(capacity);

		this.onValueTrueListener = onValueTrueListener;
	}

	public void set(@NonNull final E flag) {
		Guard.checkNotNull(flag, "null == flag");

		value.set(flag.ordinal());
		onValueTrue();
	}

	public void clear(final E flag) {
		Guard.checkNotNull(flag, "null == flag");

		value.clear(flag.ordinal());
	}

	public boolean isTrue() {

		for (int i = 0; i < capacity; i++) {
			if (value.get(i) == false) {
				return false;
			}
		}

		return true;
	}

	public void reset() {
		value.clear();
	}

	private void onValueTrue() {
		if (isTrue() && onValueTrueListener != null) {
			onValueTrueListener.onTrue();
		}
	}

	public interface OnValueTrueListener {

		void onTrue();
	}
}
