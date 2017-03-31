package info.jukov.yandextranslatetest.model.network.translate;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * User: jukov
 * Date: 14.03.2017
 * Time: 22:03
 */

interface YandexTranslateApi {

	String GET_LANGS = "getLangs";
	String DETECT_LANG = "detect";
	String TRANSLATE = "translate";

	@FormUrlEncoded
	@POST(TRANSLATE)
	Call<TranslateResponse> translate(@Query("key") String key, @Query("lang") String lang,
									  @Field("text") String text);

	@GET(GET_LANGS)
	Call<GetLangsResponse> getLangs(@Query("key") String key, @Query("ui") String ui);
}
