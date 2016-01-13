package in.shaapps.mygola;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class AboutMe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Feature 1
        // Feature 1
        // Feature 1
        // Feature 1
        // Feature 1
        // Feature 1

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
