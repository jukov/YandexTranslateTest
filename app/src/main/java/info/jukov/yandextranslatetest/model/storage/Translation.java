package info.jukov.yandextranslatetest.model.storage;

/**
 * User: jukov
 * Date: 21.03.2017
 * Time: 22:46
 */

public class Translation {

	private final String inputText;
	private final String inputLang;

	private final String outputText;
	private final String outputLang;

	public Translation(final String inputText, final String inputLang, final String outputText,
					   final String outputLang) {
		this.inputText = inputText;
		this.inputLang = inputLang.toUpperCase();
		this.outputText = outputText;
		this.outputLang = outputLang.toUpperCase();
	}

	public String getInputText() {
		return inputText;
	}

	public String getInputLang() {
		return inputLang;
	}

	public String getOutputText() {
		return outputText;
	}

	public String getOutputLang() {
		return outputLang;
	}
}
