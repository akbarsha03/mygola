package in.shaapps.mygola.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.shaapps.mygola.R;
import in.shaapps.mygola.model.ActivitiesData;
import in.shaapps.mygola.model.Activity;

/**
 * My Gola
 * --
 * Created by Akbar Sha Ebrahim on 10/24/2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final List<Activity> activityList;
    private final Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.status.setText(activityList.get(position).getName());
        holder.location.setText(activityList.get(position).getLocation());
        holder.rating.setText("  " + activityList.get(position).getRating() + " rating ");
        holder.price.setText("  " + activityList.get(position).getActualPrice() + " - Get "
                + activityList.get(position).getDiscount() + "  OFF");
        holder.city.setText(activityList.get(position).getCity());
        Picasso.with(mContext).load(activityList.get(position).getImage())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView status;
        public TextView location;
        public TextView rating;
        public TextView price;
        public TextView city;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            status = (TextView) v.findViewById(R.id.actualPrice);
            location = (TextView) v.findViewById(R.id.location);
            location.setTextColor(Color.parseColor("#FFD16806"));
            rating = (TextView) v.findViewById(R.id.rating);
            rating.setTextColor(Color.parseColor("#FF347FF9"));
            price = (TextView) v.findViewById(R.id.price);
            price.setTextColor(Color.parseColor("#FF20AF2A"));
            city = (TextView) v.findViewById(R.id.city);
            imageView = (ImageView) v.findViewById(R.id.imageView);
        }
    }

    public RecyclerViewAdapter(ActivitiesData activitiesData, Context context) {
        activityList = activitiesData.getActivities();
        mContext = context;
    }
}