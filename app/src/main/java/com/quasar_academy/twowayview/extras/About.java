package com.quasar_academy.twowayview.extras;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.quasar_academy.twowayview.R;

public class About extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ad_container, new AdFragment())
                .commit();

    }
}
