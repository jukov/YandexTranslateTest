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

	private String translateResponse;
	private String dictionatyResponse;

	@NonNull private boolean isFavorite;

	@Generated(hash = 1265441618)
	public History(Long _id, @NotNull String text, @NotNull String lang,
									String translateResponse, String dictionatyResponse,
									boolean isFavorite) {
					this._id = _id;
					this.text = text;
					this.lang = lang;
					this.translateResponse = translateResponse;
					this.dictionatyResponse = dictionatyResponse;
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

	public String getLang() {
					return this.lang;
	}

	public void setLang(String lang) {
					this.lang = lang;
	}

	public String getTranslateResponse() {
					return this.translateResponse;
	}

	public void setTranslateResponse(String translateResponse) {
					this.translateResponse = translateResponse;
	}

	public String getDictionatyResponse() {
					return this.dictionatyResponse;
	}

	public void setDictionatyResponse(String dictionatyResponse) {
					this.dictionatyResponse = dictionatyResponse;
	}

	public boolean getIsFavorite() {
					return this.isFavorite;
	}

	public void setIsFavorite(boolean isFavorite) {
					this.isFavorite = isFavorite;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final History history = (History) o;

		if (!get_id().equals(history.get_id())) {
			return false;
		}
		if (!getText().equals(history.getText())) {
			return false;
		}
		if (!getLang().equals(history.getLang())) {
			return false;
		}
		if (getTranslateResponse() != null ? !getTranslateResponse().equals(history.getTranslateResponse())
			: history.getTranslateResponse() != null) {
			return false;
		}
		if (getDictionatyResponse() != null ? !getDictionatyResponse().equals(history.getDictionatyResponse())
			: history.getDictionatyResponse() != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = get_id().hashCode();
		result = 31 * result + getText().hashCode();
		result = 31 * result + getLang().hashCode();
		result = 31 * result + (getTranslateResponse() != null ? getTranslateResponse().hashCode() : 0);
		result = 31 * result + (getDictionatyResponse() != null ? getDictionatyResponse().hashCode() : 0);
		return result;
	}
}
