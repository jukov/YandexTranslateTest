package info.jukov.yandextranslatetest.model.network.translate;

import java.util.List;
import java.util.Map;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 11:29
 */

public final class GetLangsResponce {

	List<String> dirs;
	Map<String, String> langs;

	public List<String> getDirs() {
		return dirs;
	}

	public void setDirs(final List<String> dirs) {
		this.dirs = dirs;
	}

	public Map<String, String> getLangs() {
		return langs;
	}

	public void setLangs(final Map<String, String> langs) {
		this.langs = langs;
	}

	@Override
	public String toString() {
		return "GetLangsResponce{" +
			"dirs=" + dirs +
			", langs=" + langs +
			'}';
	}
}
