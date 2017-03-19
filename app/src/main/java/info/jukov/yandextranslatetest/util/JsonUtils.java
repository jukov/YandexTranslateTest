package info.jukov.yandextranslatetest.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * User: jukov
 * Date: 19.03.2017
 * Time: 15:04
 */

public final class JsonUtils {

	private static final Log LOG = new Log(JsonUtils.class);

	private static ObjectMapper objectMapper;

	@Nullable
	public static <T> T deserialize(@NonNull final Class<T> clazz, @NonNull final String json) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		try {
			return objectMapper.readValue(json, clazz);
		} catch (final IOException e) {
			LOG.error(e.toString());
		}

		return null;
	}

	private JsonUtils() {
	}
}
