package info.jukov.yandextranslatetest.model.network;

import java.util.List;

/**
 * User: jukov
 * Date: 14.03.2017
 * Time: 23:02
 */

public final class TranslateResponce {

	private int code;
	private String lang;
	private List<String> text;

	public TranslateResponce() {
	}

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

	public List<String> getText() {
		return text;
	}

	public void setText(final List<String> text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "TranslateResponce{" +
			"code=" + code +
			", lang='" + lang + '\'' +
			", text=" + text +
			'}';
	}
}
