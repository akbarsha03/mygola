package in.shaapps.mygola.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.shaapps.mygola.R;
import in.shaapps.mygola.model.Activity;
import in.shaapps.mygola.model.DbHelper;

public class DetailInfoActivity extends AppCompatActivity {

    private String actualPrice;
    private String discount;
    private String rating;
    private String city;
    private String location;
    private String description;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_activiy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();

        DbHelper dbHelper = new DbHelper(this);

        String intentData = i.getStringExtra("NAME");
        Activity activity = dbHelper.getActivity(intentData);


        name = activity.getName();
        actualPrice = activity.getActualPrice();
        discount = activity.getDiscount();
        rating = activity.getRating();
        city = activity.getCity();
        location = activity.getLocation();
        description = activity.getDescription();
        String image = activity.getImage();

        TextView name = (TextView) findViewById(R.id.name);
        TextView actualPrice = (TextView) findViewById(R.id.actualPrice);
        TextView discount = (TextView) findViewById(R.id.discount);
        TextView rating = (TextView) findViewById(R.id.ratings);
        TextView city = (TextView) findViewById(R.id.cityPlace);
        TextView location = (TextView) findViewById(R.id.mlocation);
        TextView description = (TextView) findViewById(R.id.description);

        ImageView profileImage = (ImageView) findViewById(R.id.profileImage);

        name.setText("   " + this.name);
        actualPrice.setText("   " + this.actualPrice + " INR");
        discount.setText("   " + this.discount + " OFF");
        rating.setText("   " + this.rating + " RATING");
        city.setText("   " + this.city);
        location.setText("   " + this.location + " in " + this.city);
        description.setText(this.description);

        Picasso.with(this).load(image).into(profileImage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.second_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        DbHelper dbHelper = new DbHelper(this);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Name: " + name +
                    "\n" + "Actual Price     :" + " Rs " + actualPrice + "\n" + "Discount     :" + discount +
                    "\n" + "City     :" + city + "\n" + "Rating     :" + rating + " stars" +
                    "\n" + "Location     :" + location + "\n" + "Description     :" + description);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            return true;
        }

        if (id == R.id.action_bookmark) {

            dbHelper.addBookmark(name);
            Toast.makeText(this, "Bookmarked", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_unbookmark) {

            dbHelper.removeBookmark(name);
            Toast.makeText(this, "Bookmark removed", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_sms) {

            Intent pickerIntentOne = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            pickerIntentOne.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(pickerIntentOne, 1);

            return true;
        }

//        if (id == R.id.about) {
//
//            startActivity(new Intent(this, AboutMe.class));
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri urlOne = data.getData();
            Cursor cursorOne = getContentResolver().query(urlOne, null, null, null, null);
            if (cursorOne.moveToFirst()) {
                String s = cursorOne.getString(cursorOne.getColumnIndex(ContactsContract
                        .CommonDataKinds.Phone.NUMBER));

                String message = "Name: " + name +
                        "\n" + "Actual Price     :" + " Rs " + actualPrice + "\n" + "Discount     :" + discount +
                        "\n" + "City     :" + city + "\n" + "Rating     :" + rating + " stars" +
                        "\n" + "Location     :" + location + "\n" + "Description     :" + description;

                SmsManager smsManager = SmsManager.getDefault();
                ArrayList<String> parts = smsManager.divideMessage(message);
                smsManager.sendMultipartTextMessage(s, null, parts, null, null);

                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
            }
            cursorOne.close();
        }
    }

}
