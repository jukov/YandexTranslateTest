package info.jukov.yandextranslatetest.model.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * User: jukov
 * Date: 16.03.2017
 * Time: 21:30
 */
@SuppressWarnings("LocalCanBeFinal")
@Entity(nameInDb = "history")
public final class History {

	@Id private Long _id;
	@NotNull private String text;
	@NotNull private String translatedText;
	@NotNull private String lang;

	@Generated(hash = 217715439)
	public History(Long _id, @NotNull String text, @NotNull String translatedText,
									@NotNull String lang) {
					this._id = _id;
					this.text = text;
					this.translatedText = translatedText;
					this.lang = lang;
	}

	@Generated(hash = 869423138)
	public History() {
	}

	public Long get_id() {
		return this._id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTranslatedText() {
		return this.translatedText;
	}

	public void setTranslatedText(String translatedText) {
		this.translatedText = translatedText;
	}

	public String getLang() {
		return this.lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}
