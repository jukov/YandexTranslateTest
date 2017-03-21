package info.jukov.yandextranslatetest.model.storage;

/**
 * User: jukov
 * Date: 19.03.2017
 * Time: 19:12
 */

public class Language {

	private final String code;
	private final String readableLangWord;
	private int mostUsedPriority;

	public Language(final String code, final String readableLangWord,
					final int mostUsedPriority) {
		this.code = code;
		this.readableLangWord = readableLangWord;
		this.mostUsedPriority = mostUsedPriority;
	}

	public String getCode() {
		return code;
	}

	public String getReadableLangWord() {
		return readableLangWord;
	}

	public int getMostUsedPriority() {
		return mostUsedPriority;
	}

	public void setMostUsedPriority(final int mostUsedPriority) {
		this.mostUsedPriority = mostUsedPriority;
	}

	@Override
	public String toString() {
		return "Language{" +
			"code='" + code + '\'' +
			", readableLangWord='" + readableLangWord + '\'' +
			", mostUsedPriority=" + mostUsedPriority +
			'}';
	}
}