package info.jukov.yandextranslatetest.model.network;

/**
 * User: jukov
 * Date: 19.03.2017
 * Time: 14:59
 */

public class ErrorResponce {

	private int code;
	private String message;

	public int getCode() {
		return code;
	}

	public void setCode(final int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}
}
