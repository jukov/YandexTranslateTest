package info.jukov.yandextranslatetest.model.storage;

/**
 * User: jukov
 * Date: 19.03.2017
 * Time: 19:12
 */

public final class Language implements Cloneable {

	private String code;
	private String readableLangWord;
	private int mostUsedPriority;

	public Language() {
	}

	public Language(final String code, final String readableLangWord) {
		this.code = code;
		this.readableLangWord = readableLangWord;
	}

	public Language(final String code, final String readableLangWord, final int mostUsedPriority) {
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

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final Language language = (Language) o;

		if (!getCode().equals(language.getCode())) {
			return false;
		}
		if (!getReadableLangWord().equals(language.getReadableLangWord())) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = getCode().hashCode();
		result = 31 * result + getReadableLangWord().hashCode();
		return result;
	}

	@Override
	public Language clone() {
		try {
			return ((Language) super.clone());
		} catch (final CloneNotSupportedException e) {
			throw new IllegalStateException("Clone not supported?");
		}
	}
}