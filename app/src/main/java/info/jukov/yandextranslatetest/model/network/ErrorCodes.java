package info.jukov.yandextranslatetest.model.network;

/**
 * User: jukov
 * Date: 19.03.2017
 * Time: 14:33
 */

public interface ErrorCodes {

	int WRONG_API_KEY = 401;
	int BANNED_API_KEY = 402;
	int QUERY_DAILY_LIMIT_EXCEEDED = 403;
	int TEXT_DAILY_LIMIT_EXCEEDED = 404;
	int TEXT_TOO_LONG = 413;
	int LANG_NOT_SUPPORTED = 501;

	/**
	 * Кастомный код ошибки для случая, когда ключи не введены пользователем.
	 */
	int KEYS_NOT_SET_CUSTOM = 1000;

	/**
	 * Кастомный код ошибки для сетевых ошибок
	 */
	int NETWORK_ERROR_CUSTOM = 1001;
}
