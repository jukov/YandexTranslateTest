package info.jukov.yandextranslatetest.util;

import android.support.annotation.NonNull;
import java.io.Serializable;
import java.util.BitSet;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 6:15
 *
 * Класс для выполнения некоторых действий после соблюдения всех требуемых условий.
 *
 * В качестве аргумента обобщения принимает enum-класс,
 * элементы которого будут использоваться как флаги.
 */
public final class MultiSetBoolean<E extends Enum> implements Serializable {

	private final int capacity;
	private final BitSet value;

	private final transient OnValueTrueListener onValueTrueListener;

	public MultiSetBoolean(final int capacity, @NonNull final OnValueTrueListener onValueTrueListener) {
		Guard.checkNotNull(onValueTrueListener, "null == onValueTrueListener");
		Guard.checkPreCondition(capacity > 0, "capacity must be > 0");

		this.capacity = capacity;
		this.value = new BitSet(capacity);

		this.onValueTrueListener = onValueTrueListener;
	}

	/**
	 * Устанавливает флаг {@code E}.
	 */
	public void set(@NonNull final E flag) {
		Guard.checkNotNull(flag, "null == flag");

		value.set(flag.ordinal());
		onValueTrue();
	}

	/**
	 * Снимает флаг {@code E}.
	 */
	public void clear(final E flag) {
		Guard.checkNotNull(flag, "null == flag");

		value.clear(flag.ordinal());
	}

	/**
	 * Возвращает true, если все флаги были установлены.
	 * */
	public boolean isTrue() {

		for (int i = 0; i < capacity; i++) {
			if (value.get(i) == false) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Снимает все флаги.
	 */
	public void reset() {
		value.clear();
	}

	private void onValueTrue() {
		if (isTrue()) {
			onValueTrueListener.onTrue();
		}
	}

	public interface OnValueTrueListener {

		/**
		 * Метод, который будет вызван после установки всех флагов.
		 */
		void onTrue();
	}
}
