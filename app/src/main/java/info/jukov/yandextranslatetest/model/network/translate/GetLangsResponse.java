package info.jukov.yandextranslatetest.model.network.translate;

import java.util.Map;
import java.util.Set;

/**
 * User: jukov
 * Date: 18.03.2017
 * Time: 11:29
 */

public final class GetLangsResponse {

	Set<String> dirs;
	Map<String, String> langs;

	public Set<String> getDirs() {
		return dirs;
	}

	public void setDirs(final Set<String> dirs) {
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
		return "GetLangsResponse{" +
			"dirs=" + dirs +
			", langs=" + langs +
			'}';
	}
}
