package info.jukov.yandextranslatetest.model.storage.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import info.jukov.yandextranslatetest.model.network.dict.LookupResponse;
import info.jukov.yandextranslatetest.util.JsonUtils;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;

/**
 * User: jukov
 * Date: 16.03.2017
 * Time: 21:30
 */
@SuppressWarnings("LocalCanBeFinal")
@Entity(nameInDb = "translation")
public final class Translation {

	@Id private Long _id;

	@NotNull private String text;
	@NotNull private String lang;

	@NonNull private String translateResponse;
	private String dictionatyResponse;

	@NonNull private boolean isFavorite;

	@Transient private LookupResponse lookupResponse;

	@Generated(hash = 857362517)
	public Translation(Long _id, @NotNull String text, @NotNull String lang,
			@NotNull String translateResponse, String dictionatyResponse,
			boolean isFavorite) {
		this._id = _id;
		this.text = text;
		this.lang = lang;
		this.translateResponse = translateResponse;
		this.dictionatyResponse = dictionatyResponse;
		this.isFavorite = isFavorite;
	}

	@Generated(hash = 321689573)
	public Translation() {
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

	@Nullable
	public LookupResponse getLookupResponse() {
		if (lookupResponse == null) {
			if (dictionatyResponse != null) {
				lookupResponse = JsonUtils.deserialize(LookupResponse.class, dictionatyResponse);
			}
		}

		return lookupResponse;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final Translation that = (Translation) o;

		if (!getText().equals(that.getText())) {
			return false;
		}
		if (!getLang().equals(that.getLang())) {
			return false;
		}
		if (!getTranslateResponse().equals(that.getTranslateResponse())) {
			return false;
		}
		if (getDictionatyResponse() != null ? !getDictionatyResponse().equals(that.getDictionatyResponse())
			: that.getDictionatyResponse() != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = getText().hashCode();
		result = 31 * result + getLang().hashCode();
		result = 31 * result + getTranslateResponse().hashCode();
		result = 31 * result + (getDictionatyResponse() != null ? getDictionatyResponse().hashCode() : 0);
		return result;
	}
}
