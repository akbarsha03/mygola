package in.shaapps.mygola.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import in.shaapps.mygola.R;
import in.shaapps.mygola.adapter.RecyclerItemClickListener;
import in.shaapps.mygola.adapter.RecyclerViewAdapter;
import in.shaapps.mygola.model.ActivitiesData;
import in.shaapps.mygola.model.Activity;
import in.shaapps.mygola.model.ApiHitCount;
import in.shaapps.mygola.model.DbHelper;
import in.shaapps.mygola.model.MyGolaSharedPrefs;
import in.shaapps.mygola.service.MyGolaAPI;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainInfoActivity extends AppCompatActivity implements Callback<ActivitiesData> {

    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private ActivitiesData activitiesData;
    private RecyclerViewAdapter mAdapter;
    private MenuItem searchItem;
    private AutoCompleteTextView searchList;
    private MyGolaSharedPrefs sharedPrefs;
    private List<String> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Set icon to the toolbar
         */
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.primaryColor, null));
        } else {
            toolbar.setTitleTextColor(getResources().getColor(R.color.primaryColor));
        }


        sharedPrefs = MyGolaSharedPrefs.get(MainInfoActivity.this);

        searchList = (AutoCompleteTextView) findViewById(R.id.search);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Log.d("SHA", "position " + position);

                        Intent i = new Intent(MainInfoActivity.this, DetailInfoActivity.class);
                        Activity activity = activitiesData.getActivities().get(position);
                        i.putExtra("NAME", activity.getName());
                        startActivity(i);
                    }
                }));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyGolaAPI.END_POINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyGolaAPI huntAPI = retrofit.create(MyGolaAPI.class);

        Call<ActivitiesData> activitiesDataCall = huntAPI.queryActivities(MyGolaAPI.LIST_NEWS);

        activitiesDataCall.enqueue(this);

        Call<ApiHitCount> hitCount = huntAPI.queryCount(MyGolaAPI.API_HITS);

        hitCount.enqueue(new Callback<ApiHitCount>() {
            @Override
            public void onResponse(Response<ApiHitCount> response, Retrofit retrofit) {

                if (response.isSuccess()) {

                    TextView hitCount = (TextView) findViewById(R.id.apiHits);
                    hitCount.setText("API Hit: " + response.body().getApiHits());

                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        /**
         * Hide the keyboard for the first time when he opens the activity
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        progressDialog = ProgressDialog.show(this, "Loading", "Please wait", false, true);
        DbHelper dbHelper = new DbHelper(this);

        ActivitiesData activitiesData = new ActivitiesData();
        activitiesData.setActivities(dbHelper.getAllDetails());
        setUpList(activitiesData);

        Realm realm = Realm.getInstance(this);

    }

    @Override
    public void onResponse(Response<ActivitiesData> response, Retrofit retrofit) {

        Log.d("SHA", "Code: " + response.code());
        Log.d("SHA", "Message: " + response.message());

        if (response.isSuccess()) {

            progressDialog.dismiss();

            Log.d("SHA", "I've got something to see");

            /**
             * Set up the recycler view
             */
            setUpList(response.body());

            /**
             * Enable search
             */
            List<Activity> activities = response.body().getActivities();

            final List<String> searchItems = new ArrayList<>();
            cityList = new ArrayList<>();

            for (Activity m : activities) {
                searchItems.add(m.getName());
                if (!cityList.contains(m.getCity()))
                    cityList.add(m.getCity());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, searchItems);
            searchList.setAdapter(adapter);

            searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String selected = (String) parent.getItemAtPosition(position);
                    int pos = searchItems.indexOf(selected);

                    String selectedItem = searchItems.get(pos);
                    Log.d("SHA", "Selected " + selectedItem + " for position " + pos);

                    Intent i = new Intent(MainInfoActivity.this, DetailInfoActivity.class);
                    i.putExtra("NAME", selectedItem);
                    startActivity(i);
                }
            });
            new SaveDB().execute(activities);
        } else {

            Log.d("SHA", "Shit Happened! IN response");
        }
    }

    private void setUpList(ActivitiesData body) {

        activitiesData = body;

        progressDialog.dismiss();

        mAdapter = new RecyclerViewAdapter(body, this);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);

        TextView totalActivities = (TextView) findViewById(R.id.totalActivities);
        totalActivities.setText("Total Activities: " + activitiesData.getActivities().size());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFailure(Throwable t) {

        Log.d("SHA", "Shit Happened! IN Failure");

    }

    public class PriceComparator implements Comparator<Activity> {

        @Override
        public int compare(Activity lhs, Activity rhs) {
            return lhs.getActualPrice().compareTo(rhs.getActualPrice());
        }
    }

    public class RatingComparator implements Comparator<Activity> {

        @Override
        public int compare(Activity lhs, Activity rhs) {
            return lhs.getRating().compareTo(rhs.getRating());
        }
    }

    public class DiscountComparator implements Comparator<Activity> {

        @Override
        public int compare(Activity lhs, Activity rhs) {
            return lhs.getDiscount().compareTo(rhs.getDiscount());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        searchItem = menu.findItem(R.id.show_bookmark);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;

        if (searchItem != null) {
            Log.d("createOptionMenu", "search item not null");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            }
        }

        if (searchView != null) {
            Log.d("createOptionMenu", "search view not null");
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        } else {
            Log.e("createOptionMenu", "search view null");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.show_bookmark) {
//            MenuItemCompat.collapseActionView(searchItem);

            Intent intent = new Intent(this, BookmarkedActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.filter) {

            CharSequence[] items = new CharSequence[cityList.size()];

            for (int i = 0; i < cityList.size(); i++) {
                items[i] = cityList.get(i);
            }
            new MaterialDialog.Builder(this)
                    .title("Filter by")
                    .items(items)
                    .itemsCallbackSingleChoice(sharedPrefs.getSortBy(), new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            /**
                             * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                             * returning false here won't allow the newly selected radio button to actually be selected.
                             **/

                            DbHelper dbHelper = new DbHelper(MainInfoActivity.this);
                            List<Activity> activityList = dbHelper.getAllDetails();
                            List<Activity> filteredList = new ArrayList<>();

                            for (Activity a : activityList) {
                                if (a.getCity().equals(text)) {
                                    filteredList.add(a);
                                }
                            }

                            ActivitiesData activitiesData = new ActivitiesData();
                            activitiesData.setActivities(filteredList);
                            setUpList(activitiesData);

                            return true;
                        }
                    })
                    .positiveText("OK")
                    .show();
        }

        if (id == R.id.sort) {

            new MaterialDialog.Builder(this)
                    .title("Filter by")
                    .items(new CharSequence[]{"Price", "Rating", "Discount"})
                    .itemsCallbackSingleChoice(sharedPrefs.getSortBy(), new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            /**
                             * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                             * returning false here won't allow the newly selected radio button to actually be selected.
                             **/
                            sharedPrefs.putSortBy(which);

                            switch (which) {
                                case 0:

                                    Collections.sort(activitiesData.getActivities(), new PriceComparator());

                                    mAdapter = new RecyclerViewAdapter(activitiesData, MainInfoActivity.this);
                                    mAdapter.notifyDataSetChanged();
                                    mRecyclerView.setAdapter(mAdapter);

                                    Log.d("SHA", "Price");
                                    break;
                                case 1:

                                    Collections.sort(activitiesData.getActivities(), new RatingComparator());

                                    mAdapter = new RecyclerViewAdapter(activitiesData, MainInfoActivity.this);
                                    mAdapter.notifyDataSetChanged();
                                    mRecyclerView.setAdapter(mAdapter);
                                    Log.d("SHA", "Rating");
                                    break;
                                case 2:

                                    Collections.sort(activitiesData.getActivities(), new DiscountComparator());

                                    mAdapter = new RecyclerViewAdapter(activitiesData, MainInfoActivity.this);
                                    mAdapter.notifyDataSetChanged();
                                    mRecyclerView.setAdapter(mAdapter);
                                    Log.d("SHA", "Rating");
                                    break;
                            }
                            return true;
                        }
                    })
                    .positiveText("OK")
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    public class SaveDB extends AsyncTask<List<Activity>, Void, Void> {
        @Override
        protected Void doInBackground(List<Activity>... params) {

            List<Activity> activityList = params[0];

            DbHelper dbHelper = new DbHelper(MainInfoActivity.this);
            for (Activity m : activityList) {
                dbHelper.insertData(m);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(MainInfoActivity.this, "Database loaded - Offline enabled", Toast.LENGTH_SHORT).show();
        }
    }
}