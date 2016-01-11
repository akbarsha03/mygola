package in.shaapps.mygola.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.shaapps.mygola.model.ActivitiesData;
import in.shaapps.mygola.model.Activity;
import in.shaapps.mygola.service.MyGolaAPI;
import in.shaapps.mygola.view.MainMvpView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * My Gola
 * --
 * Created by Akbar Sha Ebrahim on 11/8/2015.
 */
public class MainPresenter implements Presenter<MainMvpView>, Callback<ActivitiesData> {

    public static String TAG = "MainPresenter";

    private MainMvpView mainMvpView;
    private List<Activity> activityList;

    @Override
    public void attachView(MainMvpView view) {
        this.mainMvpView = view;
    }

    @Override
    public void detachView() {
        this.mainMvpView = null;
    }

    public void loadList(Context context) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyGolaAPI.END_POINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyGolaAPI huntAPI = retrofit.create(MyGolaAPI.class);

        Call<ActivitiesData> activitiesDataCall = huntAPI.queryActivities(MyGolaAPI.LIST_NEWS);

        activitiesDataCall.enqueue(this);
    }

    @Override
    public void onResponse(Response<ActivitiesData> response, Retrofit retrofit) {
        Log.d("SHA", "Code: " + response.code());
        Log.d("SHA", "Message: " + response.message());

        if (response.isSuccess()) {

//            progressDialog.dismiss();

            Log.d("SHA", "I've got something to see");

//            setUpList(response.body());

            List<Activity> activities = response.body().getActivities();

            final List<String> searchItems = new ArrayList<>();

            for (Activity m : activities) {

                searchItems.add(m.getName());
            }

            new SaveDB().execute(activities);
        } else {

            Log.d("SHA", "Shit Happened! IN response");
        }

    }

    @Override
    public void onFailure(Throwable t) {
        Log.d("SHA", "Shit Happened! IN Failure");
    }

    public class SaveDB extends AsyncTask<List<Activity>, Void, Void> {
        @Override
        protected Void doInBackground(List<Activity>... params) {

            List<Activity> activityList = params[0];

//            DbHelper dbHelper = new DbHelper(MainInfoActivity.this);
            for (Activity m : activityList) {
//                dbHelper.insertData(m);
            }
            return null;
        }
    }

}
