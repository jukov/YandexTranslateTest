package info.jukov.yandextranslatetest.model.network.translate;

import java.util.List;

/**
 * User: jukov
 * Date: 14.03.2017
 * Time: 23:02
 */

public final class TranslateResponse {

	private int code;
	private String lang;
	private List<String> text;

	public int getCode() {
		return code;
	}

	public void setCode(final int code) {
		this.code = code;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(final String lang) {
		this.lang = lang;
	}

	public String getText() {
		if (text.size() > 0) {
			return text.get(0);
		}

		return null;
	}

	public void setText(final List<String> text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "TranslateResponse{" +
			"code=" + code +
			", lang='" + lang + '\'' +
			", text=" + text +
			'}';
	}
}
