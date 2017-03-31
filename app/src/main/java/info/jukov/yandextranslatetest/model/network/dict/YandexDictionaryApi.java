package info.jukov.yandextranslatetest.model.network.dict;

import java.util.Set;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * User: jukov
 * Date: 16.03.2017
 * Time: 21:49
 */

interface YandexDictionaryApi {

	String GET_LANGS = "getLangs";
	String LOOKUP = "lookup";

	@GET(GET_LANGS)
	Call<Set<String>> getLangs(@Query("key") String key);

	@FormUrlEncoded
	@POST(LOOKUP)
	Call<LookupResponse> lookup(@Query("key") String key, @Query("lang") String lang,
								@Field("text") String text, @Query("ui") String ui,
								@Query("flags") String flags);

	//TODO: Family filter
	//TODO:Error code processing

}
