package in.shaapps.mygola.model;

/**
 * My Gola
 * --
 * Created by Akbar Sha Ebrahim on 10/24/2015.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiHitCount {

    @SerializedName("api_hits")
    @Expose
    private String apiHits;

    /**
     * @return The apiHits
     */
    public String getApiHits() {
        return apiHits;
    }

    /**
     * @param apiHits The api_hits
     */
    public void setApiHits(String apiHits) {
        this.apiHits = apiHits;
    }

}