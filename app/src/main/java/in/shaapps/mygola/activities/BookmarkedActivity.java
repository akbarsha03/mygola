package in.shaapps.mygola.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.List;

import in.shaapps.mygola.R;
import in.shaapps.mygola.adapter.RecyclerItemClickListener;
import in.shaapps.mygola.adapter.RecyclerViewAdapter;
import in.shaapps.mygola.model.ActivitiesData;
import in.shaapps.mygola.model.Activity;
import in.shaapps.mygola.model.DbHelper;

public class BookmarkedActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private ActivitiesData activitiesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarked);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DbHelper helper = new DbHelper(this);

        List<Activity> activityList = helper.getBookmarked();

        if (activityList.isEmpty()) {
            findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        }

        activitiesData = new ActivitiesData();
        activitiesData.setActivities(activityList);
        mAdapter = new RecyclerViewAdapter(activitiesData, this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Log.d("SHA", "position " + position);

                        Intent i = new Intent(BookmarkedActivity.this, DetailInfoActivity.class);
                        Activity activity = activitiesData.getActivities().get(position);
                        i.putExtra("NAME", activity.getName());
                        startActivity(i);
                    }
                }));
    }

}
