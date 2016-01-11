package in.shaapps.mygola.service;

import in.shaapps.mygola.model.ActivitiesData;
import in.shaapps.mygola.model.ApiHitCount;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * My Gola
 * --
 * Created by Akbar Sha Ebrahim on 10/24/2015.
 */
public interface MyGolaAPI {

    String END_POINT = "https://mygola.0x10.info";
    String LIST_NEWS = "list_activity";
    String API_HITS = "api_hits";

    @GET("/api/mygola?type=json")
    Call<ActivitiesData> queryActivities(@Query("query") String queryString);

    @GET("/api/mygola?type=json")
    Call<ApiHitCount> queryCount(@Query("query") String queryString);
}
