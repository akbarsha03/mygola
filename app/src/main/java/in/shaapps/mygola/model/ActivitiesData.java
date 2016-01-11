package in.shaapps.mygola.model;

/**
 * My Gola
 * --
 * Created by Akbar Sha Ebrahim on 11/8/2015.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ActivitiesData {

    @SerializedName("activities")
    @Expose
    private List<Activity> activities = new ArrayList<Activity>();

    /**
     * @return The activities
     */
    public List<Activity> getActivities() {
        return activities;
    }

    /**
     * @param activities The activities
     */
    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

}