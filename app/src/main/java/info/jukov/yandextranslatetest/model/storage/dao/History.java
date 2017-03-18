package info.jukov.yandextranslatetest.model.storage.dao;

import android.support.annotation.NonNull;
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
	@NotNull private String lang;

	private String translatedText;
	private String serializetMeaning;

	@NonNull private boolean isFavorite;

	@Generated(hash = 1216172049)
	public History(Long _id, @NotNull String text, @NotNull String lang,
				   String translatedText, String serializetMeaning, boolean isFavorite) {
					this._id = _id;
					this.text = text;
					this.lang = lang;
		this.translatedText = translatedText;
		this.serializetMeaning = serializetMeaning;
		this.isFavorite = isFavorite;
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

	public String getSerializetMeaning() {
		return this.serializetMeaning;
	}

	public void setSerializetMeaning(String serializetMeaning) {
		this.serializetMeaning = serializetMeaning;
	}

	public boolean getIsFavorite() {
		return this.isFavorite;
	}

	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

}
