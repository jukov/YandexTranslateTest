package info.jukov.yandextranslatetest.util;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 6:16
 *
 * Утилитарный класс для проверки соблюдения различных условий.
 */
public final class Guard {

	private Guard() {
	}

	public static void checkPreCondition(final boolean condition, final String onFailMessage) {
		if (false == condition) {
			throw new IllegalStateException(onFailMessage);
		}
	}

	public static void checkNotNull(final Object target, final String onFailMessage) {
		if (null == target) {
			throw new IllegalStateException(onFailMessage);
		}
	}
}
