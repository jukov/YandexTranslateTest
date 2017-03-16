package info.jukov.yandextranslatetest.model.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * User: jukov
 * Date: 16.03.2017
 * Time: 22:30
 */
@SuppressWarnings("LocalCanBeFinal")
@Entity(nameInDb = "history")
public class Favorites {

	@Id private Long _id;
	@NotNull private String text;
	@NotNull private String serializedDefinition;
	@Generated(hash = 785325821)
	public Favorites(Long _id, @NotNull String text,
									@NotNull String serializedDefinition) {
					this._id = _id;
					this.text = text;
					this.serializedDefinition = serializedDefinition;
	}
	@Generated(hash = 1752129379)
	public Favorites() {
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
	public String getSerializedDefinition() {
					return this.serializedDefinition;
	}
	public void setSerializedDefinition(String serializedDefinition) {
					this.serializedDefinition = serializedDefinition;
	}

}
